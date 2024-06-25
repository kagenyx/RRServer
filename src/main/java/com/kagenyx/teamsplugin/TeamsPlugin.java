package com.kagenyx.teamsplugin;

import com.kagenyx.teamsplugin.commands.*;
import com.kagenyx.teamsplugin.listeners.ArcanaListener;
import net.ess3.api.IEssentials;
import net.milkbowl.vault.economy.Economy;
import com.kagenyx.teamsplugin.listeners.KeepItemListener;
import com.kagenyx.teamsplugin.listeners.RightClickListener;
import com.kagenyx.teamsplugin.listeners.TribunalListener;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public final class TeamsPlugin extends JavaPlugin {

    private TeamRanksManager trm;
    private Calendar lastTimeTribunalDeath;
    private Economy economy;
    private IEssentials essentials;
    private Map<UUID,Integer> lastTimeArcanaMagic;
    private NamespacedKey key;

    @Override
    public void onEnable() {
        this.key = new NamespacedKey(this, "custom_item_tag");
        getCommand("rankup").setExecutor(new RankCommand(this));
        getCommand("rankupcost").setExecutor(new RankCostCommand(this));
        getCommand("jointeam").setExecutor(new TeamJoinCommand(this));
        getCommand("checklevel").setExecutor(new CheckLevelCommand(this));
        getCommand("testStick").setExecutor(new StickTestCommand(this.trm,this));
        trm = new TeamRanksManager(this);
        Bukkit.getPluginManager().registerEvents(new TribunalListener(this,trm), this);
        Bukkit.getPluginManager().registerEvents(new KeepItemListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RightClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ArcanaListener(this),this);
        lastTimeArcanaMagic = new HashMap<>();

        setupEconomy();
        essentials = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            // Tribunal
            LinkedList<UUID> tribunal_uuids = trm.extractUUIDs("Tribunal");
            LinkedList<Player> tribunal_players = new LinkedList<>();
            for (UUID uuid : tribunal_uuids) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    // Check if the player is online
                    tribunal_players.add(player);
                    if (!trm.getTeamLeader("Tribunal").equals(uuid)) {
                        getHasteAmp(player.getLocation().getY(), player);
                    } else {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 43, -1 + trm.getTeamRank("Tribunal")));
                    }
                    if(trm.getTeamRank("Tribunal")>=2) {
                        if(lastTimeTribunalDeath != null) {
                            Calendar currentTime = Calendar.getInstance();
                            long timeDifferenceMillis = currentTime.getTimeInMillis() - lastTimeTribunalDeath.getTimeInMillis();
                            long timeDifferenceHours = timeDifferenceMillis / (1000 * 60 * 60);
                            if (timeDifferenceHours < 2 && lastTimeTribunalDeath != null) {
                                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(16.0);

                            } else {
                                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                            }
                        } else {
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                        }
                    }
                }
            }

            // Check if Tribunal players are together
            if(trm.getTeamRank("Tribunal")>=2){
                for (Player player : tribunal_players) {
                    boolean together = tribunal_players.stream().anyMatch(otherPlayer -> !otherPlayer.equals(player) && otherPlayer.getLocation().distance(player.getLocation()) <= 15);
                    if (together) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 43, 0, true, false, true));
                    }
                }

            }




            //ARCANA
            LinkedList<UUID> arcana_uuids = trm.extractUUIDs("Arcana");
            LinkedList<Player> arcana_players = new LinkedList<>();
            System.out.println("teste");
            for (UUID uuid : arcana_uuids) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    arcana_players.add(player);
                    if(trm.getTeamRank("Arcana")>=2) {
                        if(!lastTimeArcanaMagic.containsKey(player.getUniqueId()) || lastTimeArcanaMagic.get(player.getUniqueId()) > 2400) {
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(18.0);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 43, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 43, 0));
                        }
                        else {
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                            player.removePotionEffect(PotionEffectType.WEAKNESS);
                            player.removePotionEffect(PotionEffectType.UNLUCK);
                        }
                    }
                }
            }
            for (Player uuid : arcana_players) {
                System.out.println(uuid.getName());
                if(trm.getTeamRank("Arcana")>=2) {
                    if(lastTimeArcanaMagic.containsKey(uuid.getUniqueId())) {
                        lastTimeArcanaMagic.replace(uuid.getUniqueId(), lastTimeArcanaMagic.get(uuid.getUniqueId()) + 2);
                    }
                }
            }
            // Check if Arcana players are together
            for (Player player : arcana_players) {
                if(trm.getTeamRank("Arcana")>=2) {
                    boolean together = arcana_players.stream().anyMatch(otherPlayer -> !otherPlayer.equals(player) && otherPlayer.getLocation().distance(player.getLocation()) <= 15);
                    if (together) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 43, 0, true, false, true));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 43, 1, true, false, true));
                    }
                }
            }

            System.out.println(lastTimeArcanaMagic);


        }, 20, 40);
    }

    public TeamRanksManager getTrm() {
        return this.trm;
    }

    private void getHasteAmp(Double y, Player p) {
        switch (trm.getTeamRank("Tribunal")) {
            case 1:
                if (y.intValue() >= -20 && y.intValue() <= 20) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 43, 0));
                }
                break;
            case 2:
                if (y.intValue() >= -40 && y.intValue() <= 40) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 43, 0));
                }
                break;
            case 3:
                p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 43, 1));
                break;
            default:
                break;
        }
    }

    public Calendar getLastTimeTribunalDeath() {
        return lastTimeTribunalDeath;
    }

    public void setLastTimeTribunalDeath(Calendar lastTimeTribunalDeath) {
        this.lastTimeTribunalDeath = lastTimeTribunalDeath;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    public IEssentials getEssentials() {
        return essentials;
    }

    public void setPlayerMagicFood(Player p) {
        if(this.lastTimeArcanaMagic.containsKey(p.getUniqueId())) {
            this.lastTimeArcanaMagic.remove(p.getUniqueId());
            this.lastTimeArcanaMagic.put(p.getUniqueId(),0);
        } else {
            this.lastTimeArcanaMagic.put(p.getUniqueId(),0);
        }
    }

    public NamespacedKey getCustomKey() {
        return key;
    }

}
