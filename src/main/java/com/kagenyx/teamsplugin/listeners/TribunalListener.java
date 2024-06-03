package com.kagenyx.teamsplugin.listeners;

import com.kagenyx.teamsplugin.TeamRanksManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import com.kagenyx.teamsplugin.TeamsPlugin;

import java.util.Random;

public class TribunalListener implements Listener {
    private final String TEAM_NAME = "Tribunal";
    private TeamRanksManager trm;
    private TeamsPlugin plugin;
    private final String stickIdentifier = "tribunal_stick";

    public TribunalListener(TeamsPlugin plugin, TeamRanksManager trm) {
        this.plugin = plugin;
        this.trm = trm;
    }

    //Dano de Picarreta
    @EventHandler
    public void onPickaxeHit(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity damagedEntity = event.getEntity();

        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            String playerTeam = trm.getPlayerTeam(player.getUniqueId());

            if (TEAM_NAME.equals(playerTeam)) {
                ItemStack weapon = player.getInventory().getItemInMainHand();
                if (weapon != null && isPickaxe(weapon.getType())) {
                    switch (trm.getTeamRank(TEAM_NAME)) {
                        case 1:
                            event.setDamage(event.getDamage() * 1.5);
                            break;
                        case 2:
                            if (trm.getTeamLeader(TEAM_NAME).equals(player.getUniqueId())) {
                                event.setDamage(event.getDamage() * 2.5);
                                break;
                            }
                            event.setDamage(event.getDamage() * 2);
                            break;
                        case 3:
                            event.setDamage(event.getDamage() * 2.5);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private boolean isPickaxe(Material material) {
        return material == Material.WOODEN_PICKAXE ||
                material == Material.STONE_PICKAXE ||
                material == Material.IRON_PICKAXE ||
                material == Material.GOLDEN_PICKAXE ||
                material == Material.DIAMOND_PICKAXE ||
                material == Material.NETHERITE_PICKAXE;
    }

    //Double Drops
    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(trm.getPlayerTeam(player.getUniqueId()) == TEAM_NAME) {
            if(!player.getActiveItem().containsEnchantment(Enchantment.FORTUNE)) {
                Random random = new Random();
                int chance = random.nextInt(100)+1;
                Block block = event.getBlock();
                ItemStack is = new ItemStack(block.getType());
                switch (trm.getTeamRank(TEAM_NAME)){
                    case 1:
                        if(chance < 26) {
                            player.getWorld().dropItemNaturally(block.getLocation(),is);
                        }
                        break;
                    case 2:
                        if(chance < 31) {
                            player.getWorld().dropItemNaturally(block.getLocation(),is);
                        }
                        break;
                    case 3:
                        if(player.getUniqueId().equals(trm.getTeamLeader(TEAM_NAME))) {
                            player.getWorld().dropItemNaturally(block.getLocation(),is);
                            break;
                        }
                        if(chance < 51) {
                            player.getWorld().dropItemNaturally(block.getLocation(),is);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //Stick
    @EventHandler
    public void onStickHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }

        Player hit = (Player) e.getEntity();
        Player hitten = (Player) e.getDamager();

        ItemStack mainHandItem = hitten.getInventory().getItemInMainHand();
        if (isTribunalStick(mainHandItem)) {
            if (trm.getPlayerTeam(hitten.getUniqueId()).equals(trm.getPlayerTeam(hit.getUniqueId()))) {
                int speedDuration = 200; // Default duration for speed effect
                if (hitten.getPotionEffect(PotionEffectType.SPEED) != null) {
                    speedDuration = hitten.getPotionEffect(PotionEffectType.SPEED).getDuration() + 100;
                }
                hitten.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedDuration, 0));
            }
        }
    }

    private boolean isTribunalStick(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() != Material.STICK) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        String identifier = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "custom_item_tag"), PersistentDataType.STRING);
        return identifier != null && identifier.equals(stickIdentifier);
    }


}
