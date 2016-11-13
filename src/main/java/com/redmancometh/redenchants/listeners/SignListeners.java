package com.redmancometh.redenchants.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.CaseFormat;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.redmancometh.redenchants.RedEnchants;
import com.redmancometh.redenchants.Settings;
import com.redmancometh.redenchants.abstraction.CustomEnchant;
import com.redmancometh.redenchants.facade.EnchantManager;

public class SignListeners implements Listener
{

    LoadingCache<UUID, Boolean> cooldowns = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).build(new CacheLoader<UUID, Boolean>()
    {
        @Override
        public Boolean load(UUID uuid)
        {
            return false;
        }
    });

    private void msg(Player player, String message)
    {
        player.sendMessage(Settings.Messages.PREFIX.val() + " " + ChatColor.translateAlternateColorCodes('&', message));
    }

    private Enchantment getEnchantFromName(String name)
    {
        switch (name.toUpperCase())
        {
            case "EFFICIENCY":
                return Enchantment.DIG_SPEED;
            case "FORTUNE":
                return Enchantment.LOOT_BONUS_BLOCKS;
            case "LOOTING":
                return Enchantment.LOOT_BONUS_MOBS;
            default:
                return Enchantment.getByName(name);
        }
    }

    private void addCustomLore(ItemStack item, Enchantment enchantment, int level)
    {
        if (!(enchantment instanceof CustomEnchant))
        {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore() == null ? new ArrayList<String>() : meta.getLore();
        String newEnchant = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, enchantment.getName());
        if (lore.contains(ChatColor.GRAY + newEnchant + " " + (level - 1)))
        {
            lore.remove(ChatColor.GRAY + newEnchant + " " + (level - 1));
        }
        lore.add(0, ChatColor.GRAY + newEnchant + " " + level);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSignClick(PlayerInteractEvent e)
    {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }
        if (!(e.getClickedBlock().getState() instanceof Sign))
        {
            return;
        }
        Sign sign = (Sign) e.getClickedBlock().getState();
        if (!sign.getLine(0).equals(Settings.General.SIGN_HEADER.val()))
        {
            return;
        }
        Enchantment enchantment = getEnchantFromName(ChatColor.stripColor(sign.getLine(1)));
        e.setCancelled(true);
        //TODO: null checks
        int cost = Integer.parseInt(ChatColor.stripColor(sign.getLine(2).split(" ")[0]));
        /*if (PrisonTokens.getBothTokens(e.getPlayer()) < cost)
        {
            msg(e.getPlayer(), Settings.Messages.PURCHASE_FAILURE.val());
            return;
        }*/
        if (e.getItem() == null || e.getItem().getType() == Material.AIR)
        {
            msg(e.getPlayer(), Settings.Messages.PURCHASE_NO_ITEM.val());
            return;
        }
        try
        {
            if (!cooldowns.get(e.getPlayer().getUniqueId()))
            {
                int newLevel = EnchantManager.getEnchantmentLevel(enchantment, e.getItem()) + 1;
                int max = RedEnchants.getInstance().getConfig().getInt("max." + enchantment.getName(), -1);
                List items = RedEnchants.getInstance().getConfig().getStringList("items." + enchantment.getName());
                if (max > 0 && newLevel > max)
                {
                    msg(e.getPlayer(), Settings.Messages.PURCHASE_MAX_ENCHANT.val());
                    return;
                }
                else if (items != null && !items.isEmpty() && !items.contains(e.getItem().getType().toString()))
                {
                    msg(e.getPlayer(), Settings.Messages.ITEM_CANT_ENCHANT.val());
                    return;
                }
                e.getPlayer().getItemInHand().addUnsafeEnchantment(enchantment, newLevel);
                addCustomLore(e.getItem(), enchantment, newLevel);
                //PrisonTokens.takeBothTokens(e.getPlayer(), cost);
                msg(e.getPlayer(), Settings.Messages.PURCHASE_SUCCESS.fVal(enchantment.getName(), cost));
                cooldowns.put(e.getPlayer().getUniqueId(), true);
            }
        }
        catch (ExecutionException e1)
        {
            e1.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSignChange(SignChangeEvent e)
    {
        if (!e.getLine(0).equals("[Enchants]"))
        {
            return;
        }
        if (!e.getPlayer().isOp() && !e.getPlayer().hasPermission("enchants.admin"))
        {
            e.getBlock().breakNaturally();
            e.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to make this sign!");
            return;
        }
        Enchantment enchantment = getEnchantFromName(e.getLine(1));
        if (enchantment == null)
        {
            e.getBlock().breakNaturally();
            e.getPlayer().sendMessage(ChatColor.RED + "Error! Invalid Enchantment on Line 2!");
            return;
        }
        try
        {
            int cost = Integer.parseInt(e.getLine(2));
            e.setLine(0, Settings.General.SIGN_HEADER.val());
            e.setLine(1, Settings.General.ENCHANT_COLOR.toColor() + e.getLine(1).toUpperCase());
            e.setLine(2, Settings.General.TOKENS_COLOR.toColor() + "" + cost + " Tokens");
        }
        catch (Exception exception)
        {
            e.getBlock().breakNaturally();
            e.getPlayer().sendMessage(ChatColor.RED + "Error! Invalid Token Cost on Line 3!");
        }
    }

}
