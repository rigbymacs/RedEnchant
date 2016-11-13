package com.redmancometh.redenchants.abstraction.composites;

import com.redmancometh.redenchants.abstraction.CustomEnchant;
import com.redmancometh.redenchants.abstraction.EquipEnchant;

public abstract class CustomEquipEnchant extends CustomEnchant implements EquipEnchant
{
    public CustomEquipEnchant(int id, String name)
    {
        super(id, name);
    }

}
