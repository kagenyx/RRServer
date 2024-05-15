package com.kagenyx.teamsplugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KeepItemListener implements Listener {

    @EventHandler
    public void onStickDrop (PlayerDropItemEvent e) {
        String locName = e.getItemDrop().getItemStack().getItemMeta().getLocalizedName();
        if(locName != null && locName.equals("TribunalStick") || locName.equals("KarmaShard") || locName.equals("ArcanaBow")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeathEvent (PlayerDeathEvent e) {
        ItemStack is = new ItemStack(Material.STICK);
        ItemMeta im = is.getItemMeta();
        im.setLocalizedName("TribunalStick");
        is.setItemMeta(im);
        if (e.getDrops().contains(is)) {
            e.getDrops().remove(is);
        }
        e.getItemsToKeep().add(is);
    }

    @EventHandler
    public void onCraftEvent (CraftItemEvent e) {
        CraftingInventory inv = e.getInventory();

        for (ItemStack is : inv.getMatrix()) {

            try{
                String locName = is.getItemMeta().getLocalizedName();
                if(locName != null && locName.equals("TribunalStick") || locName.equals("KarmaShard") || locName.equals("ArcanaBow")) {
                    e.setCancelled(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    @EventHandler
    public void onInvMove (InventoryClickEvent e) {
        try {
            if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                try{
                    String locName = e.getCurrentItem().getItemMeta().getLocalizedName();
                    if(locName != null && locName.equals("TribunalStick") || locName.equals("KarmaShard") || locName.equals("ArcanaBow")) {
                        e.setCancelled(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if(!e.getClickedInventory().getHolder().equals(e.getInventory().getHolder())) {
                String locName = e.getCurrentItem().getItemMeta().getLocalizedName();
                if(locName != null && locName.equals("TribunalStick") || locName.equals("KarmaShard") || locName.equals("ArcanaBow")) {
                    e.setCancelled(true);
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }
}
