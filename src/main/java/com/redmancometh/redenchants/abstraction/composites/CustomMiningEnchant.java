package com.redmancometh.redenchants.abstraction.composites;

import com.redmancometh.redenchants.abstraction.CustomEnchant;
import com.redmancometh.redenchants.abstraction.MiningEnchant;

public abstract class CustomMiningEnchant extends CustomEnchant implements MiningEnchant
{
    public CustomMiningEnchant(int id, String name)
    {
        super(id, name);
    }

}
