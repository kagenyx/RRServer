package com.kagenyx.teamsplugin.listeners;

import com.kagenyx.teamsplugin.TeamsPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class RightClickListener implements Listener {

    private TeamsPlugin plugin;
    private HashMap<UUID, Calendar> lastTimeShield;

    public RightClickListener(TeamsPlugin plugin) {
        this.plugin = plugin;
        this.lastTimeShield = new HashMap<>();
    }

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        Player p = e.getPlayer();

        Material clickedBlock = e.getClickedBlock().getType();
        if (e.getAction().name().contains("RIGHT_CLICK_BLOCK") && clickedBlock == Material.WHITE_BANNER && e.getHand() == EquipmentSlot.HAND) {
            if (isOnCooldown(p)) {
                long cooldownTimeLeft = getCooldownTimeLeft(p);
                String remainingTime = formatTime(cooldownTimeLeft);
                p.sendMessage("You are on cooldown. Please wait " + remainingTime + " before using this again.");
                return;
            }

            putOnCooldown(p);

            ItemStack is = new ItemStack(Material.SHIELD);
            ItemMeta im = is.getItemMeta();
            im.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE,new AttributeModifier(UUID.randomUUID(),
                    "generic.knockback_resistance",2,
                    AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.OFF_HAND));
            is.setItemMeta(im);

            p.getInventory().addItem(is);

            spawnSmokeEffect(e.getClickedBlock().getLocation(),Particle.SMOKE_NORMAL);
            p.playSound(p.getLocation(), Sound.ENTITY_EVOKER_PREPARE_WOLOLO,1.0F,1.0F);

        }

        if (e.getAction().name().contains("RIGHT_CLICK_BLOCK") && clickedBlock == Material.CRYING_OBSIDIAN && e.getHand() == EquipmentSlot.HAND) {
            if(p.getPotionEffect(PotionEffectType.NIGHT_VISION) == null) {
                p.sendMessage("Night Vision on!");
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,999999999,0));
            } else {
                p.sendMessage("Night Vision off!");
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
            spawnSmokeEffect(e.getClickedBlock().getLocation(),Particle.DRAGON_BREATH);
            p.playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS,1.0F,1.0F);
        }
    }

    private boolean isOnCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (lastTimeShield.containsKey(playerId)) {
            Calendar lastTime = lastTimeShield.get(playerId);
            Calendar currentTime = Calendar.getInstance();
            currentTime.add(Calendar.HOUR, -1); // Subtract 1 hour from the current time
            return lastTime.after(currentTime);
        }
        return false; // Not on cooldown if no previous time is recorded
    }

    private long getCooldownTimeLeft(Player player) {
        UUID playerId = player.getUniqueId();
        if (lastTimeShield.containsKey(playerId)) {
            Calendar lastTime = lastTimeShield.get(playerId);
            Calendar currentTime = Calendar.getInstance();
            currentTime.add(Calendar.HOUR, -1); // Subtract 1 hour from the current time
            long cooldownTimeLeft = lastTime.getTimeInMillis() - currentTime.getTimeInMillis();
            return cooldownTimeLeft > 0 ? cooldownTimeLeft : 0; // Ensure non-negative value
        }
        return 0; // Assume no cooldown if no previous time is recorded
    }

    private void putOnCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        lastTimeShield.put(playerId, Calendar.getInstance());
    }

    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return minutes + "m" + seconds + "s";
    }

    private void spawnSmokeEffect(Location location, Particle p) {
        location.setX(location.getX()+0.5);
        location.setZ(location.getZ()+0.5);
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                // Spawn smoke particles every tick for a certain duration
                location.getWorld().spawnParticle(p, location, 35, 0.5, 0.5, 0.5, 0.1);
                ticks++;

                // Stop spawning particles after 20 ticks (1 second)
                if (ticks >= 20) {
                    cancel();
                }
            }
        }.runTaskTimer(this.plugin.getPlugin(TeamsPlugin.class), 0L, 1L); // Run every tick
    }

}
