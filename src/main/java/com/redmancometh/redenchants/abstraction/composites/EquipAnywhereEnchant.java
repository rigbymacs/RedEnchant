package com.redmancometh.redenchants.abstraction.composites;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class EquipAnywhereEnchant extends CustomEquipEnchant
{
    public EquipAnywhereEnchant(int id, String name)
    {
        super(id, name);
    }

    @Override
    public void tickItem(Player p, ItemStack equipped)
    {
        
    }

    @Override
    public List<Integer> getSlots(Player p)
    {
        List<Integer> armorSlots = super.getSlots(p);
        armorSlots.add(0);
        return armorSlots;
    }

}
