package com.redmancometh.redenchants.enchantments.equip.armor;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.redmancometh.redenchants.abstraction.composites.HeldEnchant;

public class Jump extends HeldEnchant
{

    public Jump(int id, String name)
    {
        super(id, name);
    }

    @Override
    public void tickItem(Player p, ItemStack equipped)
    {
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, equipped.getEnchantmentLevel(getEnchant()) - 1));
    }

    @Override
    public void removeEquipEffect(Player p)
    {
        p.removePotionEffect(PotionEffectType.JUMP);
    }

    @Override
    public String getMetaName()
    {
        return "jump";
    }

    @Override
    public boolean isEffectApplied(Player p)
    {
        return p.hasPotionEffect(PotionEffectType.JUMP);
    }

    @Override
    public Enchantment getEnchant()
    {
        return this;
    }

}
