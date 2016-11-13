package com.redmancometh.redenchants;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Settings
{

    private static FileConfiguration c;

    public static void loadConfig(FileConfiguration configuration)
    {
        c = configuration;
    }

    public enum General
    {

        SIGN_HEADER, ENCHANT_COLOR, TOKENS_COLOR;

        public String val()
        {
            return ChatColor.translateAlternateColorCodes('&', c.getString(name()));
        }

        public ChatColor toColor()
        {
            return ChatColor.valueOf(c.getString(name()));
        }
    }

    public enum Blasting
    {

        CHANCE_MIN, CHANCE_INCREMENT, CHANCE_MAX, DISABLED_WORLDS;

        public int val()
        {
            return c.getInt("blasting." + name());
        }

        public List<String> getList()
        {
            return c.getStringList("blasting." + name());
        }
    }

    public enum Messages
    {

        PREFIX, PURCHASE_SUCCESS, PURCHASE_FAILURE, PURCHASE_NO_ITEM, PURCHASE_MAX_ENCHANT, ITEM_CANT_ENCHANT;

        public String val()
        {
            return ChatColor.translateAlternateColorCodes('&', c.getString("messages." + name()));
        }

        public String fVal(Object... args)
        {
            return String.format(val(), args);
        }
    }
}
