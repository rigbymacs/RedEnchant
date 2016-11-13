package com.redmancometh.redenchants.facade;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.redmancometh.redenchants.abstraction.CustomEnchant;
import com.redmancometh.redenchants.abstraction.EquipEnchant;
import com.redmancometh.redenchants.abstraction.composites.CustomEquipEnchant;
import com.redmancometh.redenchants.enchantments.equip.armor.Haste;
import com.redmancometh.redenchants.enchantments.equip.armor.Jump;
import com.redmancometh.redenchants.enchantments.equip.armor.Speed;
import com.redmancometh.redenchants.enchants.mining.Explosion;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EnchantManager implements Iterable<CustomEnchant> //Iterable for the forEach/lambda syntax sugar
{
    private List<CustomEnchant> enchantList = new CopyOnWriteArrayList();
    private Set<CustomEquipEnchant> equipEnchants;

    public EnchantManager()
    {
        loadEnchantments();
    }

    public List<CustomEnchant> getEnchants()
    {
        return enchantList;
    }

    public void setEquipEnchants()
    {
        forEachEquippable((ench) -> equipEnchants.add((CustomEquipEnchant) ench));
    }

    public void loadEnchantments()
    {
        // Allow New Enchantments
        try
        {
            try
            {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                enchantList.add(new Explosion(71, "BLASTING"));
                enchantList.add(new Haste(72, "HASTE"));
                enchantList.add(new Jump(73, "JUMP"));
                enchantList.add(new Speed(74, "SPEED"));
                enchantList.forEach((ench) -> Enchantment.registerEnchantment(ench));
                setEquipEnchants();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static int getEnchantmentLevel(Enchantment enchantment, ItemStack itemStack)
    {
        return itemStack.getEnchantments().containsKey(enchantment) ? itemStack.getEnchantments().get(enchantment) : 0;
    }

    public void forEachEquippable(Consumer<? super EquipEnchant> action)
    {
        // TODO Auto-generated method stub
        Iterable.super.forEach((ench) ->
        {
            if (ench instanceof EquipEnchant)
            {
                action.accept((EquipEnchant) ench);
            }
        });
    }

    @Override
    public Iterator<CustomEnchant> iterator()
    {
        return enchantList.iterator();
    }
}
