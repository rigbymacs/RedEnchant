package com.redmancometh.redenchants.listeners;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.redmancometh.redenchants.RedEnchants;
import com.redmancometh.redenchants.abstraction.CustomEnchant;
import com.redmancometh.redenchants.abstraction.MiningEnchant;
import com.redmancometh.redenchants.abstraction.composites.CustomEquipEnchant;

import javafx.util.Pair;

public class EnchantListeners implements Listener
{
    private int taskId;
    {
        scheduleTickTask();
    }

    public int getTaskId()
    {
        return taskId;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void handleMiningEnchants(BlockBreakEvent e)
    {
        if (e.getPlayer().getItemInHand() == null || e.getPlayer().getItemInHand().getEnchantments() == null)
        {
            return;
        }
        for (Enchantment enchantment : e.getPlayer().getItemInHand().getEnchantments().keySet())
        {
            if (enchantment instanceof MiningEnchant)
            {
                ((MiningEnchant) enchantment).handleBlockBreak(e);
            }
        }
    }

    private void scheduleTickTask()
    {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RedEnchants.getInstance(), () -> tickPlayers(), 20, 20);
    }

    private int equipTicker;
    {
        equipTicker = Bukkit.getScheduler().scheduleSyncRepeatingTask(RedEnchants.getInstance(), () -> tickPlayers(), 20, 20);
    }

    public int getEquipTaskId()
    {
        return equipTicker;
    }

    @EventHandler
    public void handleCombatEnchants(EntityDamageByEntityEvent e)
    {

    }

    public void handleEquipEnchants(Player p)
    {

    }

    public void tickPlayers()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            for (CustomEnchant ench : RedEnchants.getInstance().getManager().getEnchants())
            {
                if (ench instanceof CustomEquipEnchant)
                {
                    applyToPlayer(player, (CustomEquipEnchant) ench);
                }
            }
        }
    }

    public static void applyToPlayer(Player player, CustomEquipEnchant equipEnchant)
    {
        Pair<Boolean, ItemStack> equipResult = equipEnchant.isEquipped(player);
        if (equipResult.getKey())
        {
            if (equipEnchant.isTickApplied(player))
            {
                return;
            }
            equipEnchant.onTickApply(player, equipResult.getValue());
            return;
        }
        if (!equipEnchant.isTickApplied(player))
        {
            return;
        }
        equipEnchant.removeEffects(player);
    }

}
