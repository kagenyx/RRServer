package com.kagenyx.teamsplugin.listeners;

import com.kagenyx.teamsplugin.TeamsPlugin;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ArcanaListener implements Listener {

    private TeamsPlugin plugin;

    public ArcanaListener(TeamsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEatEvent(PlayerItemConsumeEvent e) {
        if(plugin.getTrm().getPlayerTeam(e.getPlayer().getUniqueId()).equals("Arcana")) {
            if(checkMagicalFood(e.getItem().getType())) {
                plugin.setPlayerMagicFood(e.getPlayer());
            }
        }
    }

    private boolean checkMagicalFood(Material m){
        return m == Material.GOLDEN_APPLE
                || m == Material.GOLDEN_CARROT
                || m == Material.CHORUS_FRUIT
                || m == Material.GLOW_BERRIES
                || m == Material.ENCHANTED_GOLDEN_APPLE;
    }

    @EventHandler
    public void onPrepareItemEnchant(PrepareItemEnchantEvent event) {
        Player player = event.getEnchanter();
        int cost = 30;

        if (plugin.getTrm().getPlayerTeam(player.getUniqueId()).equals("Arcana")) {
            cost -= plugin.getTrm().getTeamRank("Arcana") * 5;

            if (player.getLevel() >= cost && player.getLevel() < 30) {
                event.getOffers()[2].setCost(cost);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if(plugin.getTrm().getPlayerTeam(player.getUniqueId()).equals("Arcana") && plugin.getTrm().getTeamLeader("Arcana").equals(player.getUniqueId())) {
                switch (plugin.getTrm().getTeamRank("Arcana")) {
                    case 1:
                        if (new Random().nextDouble() <= 0.25) { // 25% chance
                            if (event.getEntity() instanceof LivingEntity) { // Verifica se a entidade atingida é um jogador
                                LivingEntity target = (LivingEntity) event.getEntity();
                                target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0)); // Poison I por 2 segundos (40 ticks)
                            }
                        }
                        break;
                    case 2:
                        if (new Random().nextDouble() <= 0.35) {
                            if (event.getEntity() instanceof LivingEntity) { // Verifica se a entidade atingida é um jogador
                                LivingEntity target = (LivingEntity) event.getEntity();
                                target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0)); // Poison I por 2 segundos (40 ticks)
                            }
                        }
                        break;
                    case 3:
                        if (new Random().nextDouble() <= 0.5) {
                            if (event.getEntity() instanceof LivingEntity) { // Verifica se a entidade atingida é um jogador
                                LivingEntity target = (LivingEntity) event.getEntity();
                                target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 70, 2)); // Poison I por 2 segundos (40 ticks)
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
