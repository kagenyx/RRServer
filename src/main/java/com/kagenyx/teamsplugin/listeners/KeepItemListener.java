package com.kagenyx.teamsplugin.listeners;

import com.kagenyx.teamsplugin.TeamsPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Calendar;

public class KeepItemListener implements Listener {

    private final TeamsPlugin plugin;
    private final NamespacedKey key;

    public KeepItemListener(TeamsPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "custom_item_tag");
    }

    @EventHandler
    public void onStickDrop(PlayerDropItemEvent e) {
        ItemStack itemStack = e.getItemDrop().getItemStack();
        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                if (data.has(key, PersistentDataType.STRING)) {
                    String value = data.get(key, PersistentDataType.STRING);
                    if ("TribunalStick".equals(value) || "ArcanaBow".equals(value)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeathEvent(PlayerDeathEvent e) {
        if(plugin.getTrm().getPlayerTeam(e.getPlayer().getUniqueId()).equals("Tribunal")){
            if(plugin.getTrm().getTeamRank("Tribunal") >= 2) {
                this.plugin.setLastTimeTribunalDeath(Calendar.getInstance());
                ItemStack is = new ItemStack(Material.STICK);
                ItemMeta im = is.getItemMeta();
                if (im != null) {
                    PersistentDataContainer data = im.getPersistentDataContainer();
                    data.set(key, PersistentDataType.STRING, "TribunalStick");
                    is.setItemMeta(im);
                    e.getItemsToKeep().add(is);
                }
            }
        }
        if(plugin.getTrm().getPlayerTeam(e.getPlayer().getUniqueId()).equals("Arcana")){
            if(plugin.getTrm().getTeamRank("Arcana") >= 2) {
                ItemStack is = new ItemStack(Material.BOW);
                ItemMeta im = is.getItemMeta();
                if (im != null) {
                    PersistentDataContainer data = im.getPersistentDataContainer();
                    data.set(key, PersistentDataType.STRING, "ArcanaBow");
                    is.setItemMeta(im);
                    e.getItemsToKeep().add(is);
                }
            }
        }
    }

    @EventHandler
    public void onCraftEvent(CraftItemEvent e) {
        CraftingInventory inv = e.getInventory();

        for (ItemStack is : inv.getMatrix()) {
            if (is != null) {
                ItemMeta itemMeta = is.getItemMeta();
                if (itemMeta != null) {
                    PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                    if (data.has(key, PersistentDataType.STRING)) {
                        String value = data.get(key, PersistentDataType.STRING);
                        if ("TribunalStick".equals(value) ||  "ArcanaBow".equals(value)) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInvMove(InventoryClickEvent e) {
        try {
            if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                ItemStack currentItem = e.getCurrentItem();
                if (currentItem != null) {
                    ItemMeta itemMeta = currentItem.getItemMeta();
                    if (itemMeta != null) {
                        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                        if (data.has(key, PersistentDataType.STRING)) {
                            String value = data.get(key, PersistentDataType.STRING);
                            if ("TribunalStick".equals(value) || "ArcanaBow".equals(value)) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
            if (!e.getClickedInventory().getHolder().equals(e.getInventory().getHolder())) {
                ItemStack currentItem = e.getCurrentItem();
                if (currentItem != null) {
                    ItemMeta itemMeta = currentItem.getItemMeta();
                    if (itemMeta != null) {
                        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                        if (data.has(key, PersistentDataType.STRING)) {
                            String value = data.get(key, PersistentDataType.STRING);
                            if ("TribunalStick".equals(value) ||  "ArcanaBow".equals(value)) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
