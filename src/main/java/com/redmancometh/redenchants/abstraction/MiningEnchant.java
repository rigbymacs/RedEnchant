package com.redmancometh.redenchants.abstraction;

import org.bukkit.event.block.BlockBreakEvent;

public interface MiningEnchant
{
    public abstract void handleBlockBreak(BlockBreakEvent e);
}
