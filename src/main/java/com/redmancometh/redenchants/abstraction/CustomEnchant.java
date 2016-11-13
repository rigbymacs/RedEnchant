package com.redmancometh.redenchants.abstraction;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchant extends Enchantment
{

    private String name;

    public CustomEnchant(int id, String name)
    {
        super(id);
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public int getMaxLevel()
    {
        return 2;
    }

    @Override
    public boolean conflictsWith(Enchantment arg0)
    {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget()
    {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public int getStartLevel()
    {
        return 1;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack)
    {
        return true;
    }

    public boolean hasEnchant(ItemStack itemStack)
    {
        return itemStack != null && itemStack.getEnchantments() != null && itemStack.getEnchantments().containsKey(this);
    }

}
