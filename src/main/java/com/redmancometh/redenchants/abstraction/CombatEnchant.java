package com.redmancometh.redenchants.abstraction;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface CombatEnchant
{
    public abstract void strikeTarget(Player attacker, LivingEntity e);

    public abstract void strikePlayer(Player attacker, Player attacked);
}
