package com.redmancometh.redenchants.enchantments.equip.armor;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.redmancometh.redenchants.RedEnchants;
import com.redmancometh.redenchants.abstraction.composites.HeldEnchant;

public class Haste extends HeldEnchant
{

    public Haste(int id, String name)
    {
        super(id, name);
    }

    @Override
    public void tickItem(Player p, ItemStack item)
    {
        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, item.getEnchantmentLevel(getEnchant()) - 1));
    }

    @Override
    public void removeEquipEffect(Player p)
    {
        p.removePotionEffect(PotionEffectType.FAST_DIGGING);
        p.removeMetadata(getMetaName(), RedEnchants.getInstance());
    }

    @Override
    public String getMetaName()
    {
        return "haste";
    }

    @Override
    public boolean isEffectApplied(Player p)
    {
        return p.hasPotionEffect(PotionEffectType.FAST_DIGGING);
    }

    @Override
    public Enchantment getEnchant()
    {
        return this;
    }

}
