package com.redmancometh.redenchants.abstraction;

import org.bukkit.entity.Player;

public interface StealthEnchant extends CombatEnchant
{
    public abstract void enterStealth(Player p);

    public abstract void exitStealth(Player p);

    public abstract void attackInStealth(Player p);

}
