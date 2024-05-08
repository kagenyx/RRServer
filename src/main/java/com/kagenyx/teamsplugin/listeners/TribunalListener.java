package com.kagenyx.teamsplugin.listeners;

import com.kagenyx.teamsplugin.TeamRanksManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class TribunalListener implements Listener {
    private final String TEAM_NAME = "Tribunal";
    private TeamRanksManager trm;

    public TribunalListener(TeamRanksManager trm) {
        this.trm = trm;
    }

    //Dano de Picarreta
    @EventHandler
    public void onPickaxeHit(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity damagedEntity = event.getEntity();

        // Check if the attacker is a player
        //PODE DAR MERDA AQUI!! TESTAR TODO
        if (attacker instanceof Player && trm.getPlayerTeam(attacker.getUniqueId()) == TEAM_NAME) {
            Player player = (Player) attacker;

            // Check if the player is holding a pickaxe
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (weapon != null && isPickaxe(weapon.getType())) {
                switch (trm.getTeamRank(TEAM_NAME)){
                    case 1:
                        event.setDamage(event.getDamage()*1.5);
                        break;
                    case 2:
                        if(trm.getTeamLeader(TEAM_NAME).equals(attacker.getUniqueId())){
                            event.setDamage(event.getDamage()*2.5);
                            break;
                        }
                        event.setDamage(event.getDamage()*2);
                        break;
                    case 3:
                        event.setDamage(event.getDamage()*2.5);
                        break;
                    default:
                        break;
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
            if(!player.getActiveItem().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
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
                        if(player.getUniqueId().equals(trm.getTeamLeader("Tribunal"))) {
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

}
