package com.redmancometh.redenchants.enchantments.equip.armor;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.redmancometh.redenchants.abstraction.composites.EquipAnywhereEnchant;

public class Speed extends EquipAnywhereEnchant
{

    public Speed(int id, String name)
    {
        super(id, name);
    }

    @Override
    public void tickItem(Player p, ItemStack equipped)
    {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, equipped.getEnchantmentLevel(getEnchant()) - 1));
    }

    @Override
    public void removeEquipEffect(Player p)
    {
        p.removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public String getMetaName()
    {
        return "speed";
    }

    @Override
    public boolean isEffectApplied(Player p)
    {
        return p.hasPotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public Enchantment getEnchant()
    {
        return this;
    }

}
