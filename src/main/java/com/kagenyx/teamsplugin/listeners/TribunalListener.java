package com.kagenyx.teamsplugin.listeners;

import com.kagenyx.teamsplugin.TeamRanksManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Wolf;
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

import java.util.*;

public class TribunalListener implements Listener {
    private final String TEAM_NAME = "Tribunal";
    private TeamRanksManager trm;
    private TeamsPlugin plugin;
    private final String stickIdentifier = "TribunalStick";
    private HashMap<UUID,Long> cooldowns;

    // Conjunto de minérios para verificação
    private static final Set<Material> ORES = EnumSet.of(
            Material.DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.GOLD_ORE,
            Material.IRON_ORE,
            Material.COAL_ORE,
            Material.LAPIS_ORE,
            Material.REDSTONE_ORE,
            Material.NETHER_QUARTZ_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.DEEPSLATE_REDSTONE_ORE,
            Material.NETHER_GOLD_ORE,
            Material.ANCIENT_DEBRIS
    );

    public TribunalListener(TeamsPlugin plugin, TeamRanksManager trm) {
        this.plugin = plugin;
        this.trm = trm;
        this.cooldowns = new HashMap<>();
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (trm.getPlayerTeam(player.getUniqueId()).equals(TEAM_NAME)) {
            ItemStack activeItem = player.getInventory().getItemInMainHand();
            if (!activeItem.containsEnchantment(Enchantment.FORTUNE) && !activeItem.containsEnchantment(Enchantment.SILK_TOUCH) && isPickaxe(activeItem.getType())) {
                Block block = event.getBlock();
                if (isOre(block.getType())) { // Verifica se é um minério
                    Random random = new Random();
                    int chance = random.nextInt(100) + 1;
                    Collection<ItemStack> drops = block.getDrops(activeItem);
                    switch (trm.getTeamRank(TEAM_NAME)) {
                        case 1:
                            if (chance < 26) {
                                for (ItemStack drop : drops) {
                                    player.getWorld().dropItemNaturally(block.getLocation(), drop);
                                }
                            }
                            break;
                        case 2:
                            if (chance < 31) {
                                for (ItemStack drop : drops) {
                                    player.getWorld().dropItemNaturally(block.getLocation(), drop);
                                }
                            }
                            break;
                        case 3:
                            if (player.getUniqueId().equals(trm.getTeamLeader(TEAM_NAME))) {
                                for (ItemStack drop : drops) {
                                    player.getWorld().dropItemNaturally(block.getLocation(), drop);
                                }
                                break;
                            }
                            if (chance < 51) {
                                for (ItemStack drop : drops) {
                                    player.getWorld().dropItemNaturally(block.getLocation(), drop);
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

    private boolean isPickaxe(Material material) {
        return material == Material.WOODEN_PICKAXE || material == Material.STONE_PICKAXE ||
                material == Material.IRON_PICKAXE || material == Material.GOLDEN_PICKAXE ||
                material == Material.DIAMOND_PICKAXE || material == Material.NETHERITE_PICKAXE;
    }

    private boolean isOre(Material material) {
        return ORES.contains(material);
    }

    //Stick
    @EventHandler
    public void onStickHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }

        Player hitter = (Player) e.getDamager();
        Player hitten = (Player) e.getEntity();

        ItemStack mainHandItem = hitter.getInventory().getItemInMainHand();
        if (isTribunalStick(mainHandItem)) {
            if (trm.getPlayerTeam(hitten.getUniqueId()).equals(trm.getPlayerTeam(hitter.getUniqueId()))) {
                int speedDuration = 1200; // Default duration for speed effect
                if (hitten.getPotionEffect(PotionEffectType.SPEED) != null) {
                    speedDuration = hitten.getPotionEffect(PotionEffectType.SPEED).getDuration() + 100;
                }
                hitten.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedDuration, 0));
            } else {
                UUID hitterId = hitter.getUniqueId();
                long currentTime = System.currentTimeMillis();

                // Verifica o cooldown
                if (cooldowns.containsKey(hitterId)) {
                    long lastUsedTime = cooldowns.get(hitterId);
                    long timeSinceLastUse = currentTime - lastUsedTime;
                    long cooldownTime = 10000; // 10 segundos de cooldown

                    if (timeSinceLastUse < cooldownTime) {
                        long timeLeft = (cooldownTime - timeSinceLastUse) / 1000; // Tempo restante em segundos
                        hitter.sendMessage(Component.text("Tens de esperar " + (timeLeft + 1) + " segundos antes de usares o Stick novamente!").color(NamedTextColor.RED));
                        e.setCancelled(true);
                        return;
                    }
                }

                // Atualiza o cooldown
                cooldowns.put(hitterId, currentTime);

                // Determina a ação a ser tomada
                Random random = new Random();
                int action = random.nextInt(4); // Gera um número aleatório entre 0 e 3

                // 0: nada, 1: invocar lobos, 2: invocar raio, 3: invocar ambos
                if (action == 1 || action == 3) {
                    for (int i = 0; i < 2; i++) {
                        Wolf wolf = hitten.getWorld().spawn(hitter.getLocation(), Wolf.class);
                        wolf.setOwner(hitter);
                        wolf.setTarget(hitten);
                        wolf.setHealth(4.0);
                    }
                }
                if (action == 2 || action == 3) {
                    hitten.getWorld().strikeLightning(hitten.getLocation());
                }
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
