package com.redmancometh.redenchants.abstraction;

import org.bukkit.entity.Player;

public interface SetEnchant
{
    public abstract int getSetPieceCount(Player p);

    public abstract String getSetName();

    public abstract String getSetPieceLore();

    public abstract void onTick(Player p);

}
