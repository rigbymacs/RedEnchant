package com.redmancometh.redenchants;

import org.bukkit.Material;

public class MaterialInfo
{
    private Material material;
    private int multiplier;

    public MaterialInfo(Material material, int multiplier)
    {
        this.material = material;
        this.multiplier = multiplier;
    }

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public int getMultiplier()
    {
        return multiplier;
    }

    public void setMultiplier(int multiplier)
    {
        this.multiplier = multiplier;
    }
}
