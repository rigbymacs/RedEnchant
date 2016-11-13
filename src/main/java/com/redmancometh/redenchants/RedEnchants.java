package com.redmancometh.redenchants;

import org.bukkit.plugin.java.JavaPlugin;

import com.redmancometh.redenchants.facade.EnchantManager;
import com.redmancometh.redenchants.listeners.EnchantListeners;
import com.redmancometh.redenchants.listeners.SignListeners;

public class RedEnchants extends JavaPlugin
{

    private EnchantManager enchantManager;
    private static RedEnchants instance;

    public void onEnable()
    {
        instance = this;
        saveDefaultConfig();
        Settings.loadConfig(getConfig());
        enchantManager = getManager();
        getServer().getPluginManager().registerEvents(new EnchantListeners(), this);
        getServer().getPluginManager().registerEvents(new SignListeners(), this);

    }

    public static RedEnchants getInstance()
    {
        return instance;
    }

    public EnchantManager getManager()
    {
        if (enchantManager == null)
        {
            enchantManager = new EnchantManager();
        }
        return enchantManager;
    }
}
