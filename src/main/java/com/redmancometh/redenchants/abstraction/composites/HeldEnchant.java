package com.redmancometh.redenchants.abstraction.composites;

import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class HeldEnchant extends CustomEquipEnchant
{
    public HeldEnchant(int id, String name)
    {
        super(id, name);
    }

    @Override
    public List<ItemStack> getEquippedInSlot(Player p)
    {
        return Arrays.asList(new ItemStack[]
        { p.getItemInHand() });
    }
}
