package com.redmancometh.redenchants.abstraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.redmancometh.redenchants.RedEnchants;

import javafx.util.Pair;

public interface EquipEnchant
{
    public abstract String getMetaName();

    public default List<Integer> getSlots(Player p)
    {
        //don't force implementation
        //use getEquippedInSlot which calls this by default
        //leave this as default and override getEquippedInSlot 
        //this is an armor enchant as default
        return new ArrayList(Arrays.asList(36, 37, 38, 39));
    }

    public abstract Enchantment getEnchant();

    public default List<ItemStack> getEquippedInSlot(Player p)
    {
        List<ItemStack> itemList = new ArrayList();
        getSlots(p).forEach((slot) -> itemList.add(p.getInventory().getItem((slot))));
        return itemList;
    }

    public default Pair<Boolean, ItemStack> isEquipped(Player p)
    {
        for (ItemStack item : getEquippedInSlot(p))
        {
            
            if (item != null)
            {
                if (item.containsEnchantment(getEnchant()))
                {
                    return new Pair(true, item);
                }
            }
        }
        return new Pair(false, null);
    }

    public abstract void tickItem(Player p, ItemStack equipped);

    public default void applyItemEnchants(Player player, ItemStack item)
    {
        for (CustomEnchant customEnchant : RedEnchants.getInstance().getManager().getEnchants())
        {
            if (!(customEnchant instanceof EquipEnchant))
            {
                continue;
            }
            EquipEnchant equipEnchant = (EquipEnchant) customEnchant;
            Pair<Boolean, ItemStack> equipResult = equipEnchant.isEquipped(player);
            if (equipResult.getKey())
            {
                if (equipEnchant.isEffectApplied(player))
                {
                    equipEnchant.tickItem(player, equipResult.getValue());
                    continue;
                }
                equipEnchant.onTickApply(player, equipResult.getValue());
                continue;
            }
            if (equipEnchant.isTickApplied(player))
            {
                equipEnchant.removeEffects(player);
            }
        }
    }

    public default void onTickApply(Player p, ItemStack equipped)
    {
        setEquipMeta(p);
        tickItem(p, equipped);
    }

    public abstract boolean isEffectApplied(Player p);

    public default boolean isTickApplied(Player p)
    {
        return p.hasMetadata(getMetaName());
    }

    public default void removeEffects(Player p)
    {
        p.removeMetadata(getMetaName(), RedEnchants.getInstance());
        removeEquipEffect(p);
    }

    public abstract void removeEquipEffect(Player p);

    public default void setEquipMeta(Player p)
    {
        p.setMetadata(getMetaName(), new FixedMetadataValue(RedEnchants.getInstance(), getMetaName()));
    }
}
