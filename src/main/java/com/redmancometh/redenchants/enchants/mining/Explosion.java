package com.redmancometh.redenchants.enchants.mining;

import me.clip.autosell.AutoSell;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.redmancometh.redenchants.MaterialInfo;
import com.redmancometh.redenchants.Settings;
import com.redmancometh.redenchants.abstraction.composites.CustomMiningEnchant;

import java.util.*;

public class Explosion extends CustomMiningEnchant
{

    private static Random random = new Random(); //Never initialize a non-static Random instance
    private Map<Material, MaterialInfo> invBlockMap = new HashMap();
    {
        invBlockMap.put(Material.DIAMOND, new MaterialInfo(Material.DIAMOND, 1));
        invBlockMap.put(Material.EMERALD, new MaterialInfo(Material.EMERALD, 1));
        invBlockMap.put(Material.GOLD_ORE, new MaterialInfo(Material.GOLD_INGOT, 1));
        invBlockMap.put(Material.IRON_ORE, new MaterialInfo(Material.IRON_INGOT, 1));
        invBlockMap.put(Material.LAPIS_ORE, new MaterialInfo(Material.INK_SACK, 1));
        invBlockMap.put(Material.REDSTONE, new MaterialInfo(Material.REDSTONE, 1));
        invBlockMap.put(Material.QUARTZ_ORE, new MaterialInfo(Material.QUARTZ, 1));
        invBlockMap.put(Material.COAL, new MaterialInfo(Material.COAL, 1));
        invBlockMap.put(Material.DIAMOND_BLOCK, new MaterialInfo(Material.DIAMOND_BLOCK, 1));
        invBlockMap.put(Material.EMERALD_BLOCK, new MaterialInfo(Material.EMERALD_BLOCK, 1));
        invBlockMap.put(Material.GOLD_BLOCK, new MaterialInfo(Material.GOLD_BLOCK, 1));
        invBlockMap.put(Material.IRON_BLOCK, new MaterialInfo(Material.IRON_BLOCK, 1));
        invBlockMap.put(Material.LAPIS_BLOCK, new MaterialInfo(Material.LAPIS_BLOCK, 1));
        invBlockMap.put(Material.REDSTONE_BLOCK, new MaterialInfo(Material.REDSTONE_BLOCK, 1));
        invBlockMap.put(Material.QUARTZ_BLOCK, new MaterialInfo(Material.QUARTZ_BLOCK, 1));
        invBlockMap.put(Material.COAL_BLOCK, new MaterialInfo(Material.COAL_BLOCK, 1));
        invBlockMap.put(Material.STONE, new MaterialInfo(Material.STONE, 1));
        invBlockMap.put(Material.COBBLESTONE, new MaterialInfo(Material.STONE, 1));
    }

    public Explosion(int id, String name)
    {
        super(id, name);
    }

    /**
     * 
     * @param e
     * @param breakAllRadius Everything in this radius will break, guaranteed
     * @param randomBreakRadius Stuff in this radius may or may not break
     * @return
     */
    private List<Block> getToBreak(BlockBreakEvent e, int breakAllRadius, int randomBreakRadius)
    {
        List<Block> toBreak = new ArrayList();
        Block originBlock = e.getBlock();

        Location originLoc = originBlock.getLocation();

        int breakAllSq = breakAllRadius * breakAllRadius;
        int randomSq = randomBreakRadius * randomBreakRadius;
        for (int dx = -randomBreakRadius; dx <= randomBreakRadius; dx++)
        {
            for (int dy = -randomBreakRadius; dy <= randomBreakRadius; dy++)
            {
                for (int dz = -randomBreakRadius; dz <= randomBreakRadius; dz++)
                {
                    Block b = originBlock.getWorld().getBlockAt(originBlock.getX() + dx, originBlock.getY() + dy, originBlock.getZ() + dz);
                    if (b.getTypeId() != 0)
                    {
                        int distanceSq = (int) originLoc.distanceSquared(b.getLocation());
                        if (distanceSq <= breakAllSq || (distanceSq <= randomSq && random.nextInt(2) == 0))
                        {
                            toBreak.add(b);
                        }
                    }
                }
            }
        }
        return toBreak;
    }

    private void handleDrops(Player p, Block b)
    {
        for (ItemStack drop : b.getDrops(p.getItemInHand()))
        {
            if (drop == null)
            {
                continue;
            }
            if (invBlockMap.containsKey(drop.getType()) && !AutoSell.getInstance().getOptions().autoSmeltDisabledWorlds().contains(p.getWorld().getName()))
            {
                MaterialInfo info = invBlockMap.get(drop.getType());
                drop.setType(info.getMaterial());
                drop.setAmount(drop.getAmount() * info.getMultiplier());
                if (p.getItemInHand().getEnchantmentLevel(LOOT_BONUS_BLOCKS) > 0 && !AutoSell.getInstance().getOptions().getFortuneDisabledWorlds().contains(p.getWorld().getName()))
                {
                    drop.setAmount(getFortuneAmount(p.getItemInHand().getEnchantmentLevel(LOOT_BONUS_BLOCKS)));
                }
                p.getInventory().addItem(drop);
                //tokenIncrement(p, b);
                b.setType(Material.AIR);
            }
        }
    }

    private int getFortuneAmount(int var1)
    {
        boolean var2 = AutoSell.getInstance().getOptions().isFortuneRandom();
        double var3 = AutoSell.getInstance().getOptions().getFortuneMultiplier();
        int var5 = AutoSell.getInstance().getOptions().getFortuneMinDrops();
        int var6 = AutoSell.getInstance().getOptions().getFortuneMaxDrops();
        int var7 = AutoSell.getInstance().getOptions().getFortuneModifier();
        int var8 = (int) (Math.floor((double) var1 * var3) + 1.0D);
        if (var8 > var6)
        {
            if (var2)
            {
                var8 = random.nextInt(var6) + var5;
            }
            else
            {
                var8 = var6 + var5;
            }
        }
        else if (var2)
        {
            var8 = random.nextInt(var8) + var5;
        }
        else
        {
            var8 += var5;
        }

        if (var7 > 0)
        {
            var7 = random.nextInt(var7);
        }

        return var7 <= 0 ? var8 : (random.nextBoolean() ? var8 + var7 : (var8 - var7 > 1 ? var8 - var7 : 1));
    }

    private boolean isSkipped(Block b)
    {
        for (ItemStack drop : b.getDrops())
        {
            if (invBlockMap.containsKey(drop.getType()))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void handleBlockBreak(BlockBreakEvent e)
    {
        if (Settings.Blasting.DISABLED_WORLDS.getList().contains(e.getBlock().getWorld().getName()))
        {
            return;
        }
        if (isSkipped(e.getBlock()))
        {
            return;
        }
        e.setCancelled(true);
        int level = e.getPlayer().getItemInHand().getEnchantments().get(this);
        int chance = Settings.Blasting.CHANCE_MIN.val() + (Settings.Blasting.CHANCE_INCREMENT.val() * (level - 1));
        if (chance > Settings.Blasting.CHANCE_MAX.val()) chance = Settings.Blasting.CHANCE_MAX.val();
        boolean doDamage = random.nextInt(100) <= chance;
        Location l = e.getBlock().getLocation();
        e.getPlayer().playEffect(l, Effect.EXPLOSION_LARGE, 1);
        handleDrops(e.getPlayer(), e.getBlock());
        if (!doDamage)
        {
            return;
        }
        List<Block> toBreak = getToBreak(e, Math.max(1, level / 12), (int) Math.max(1, level / 9));
        for (Block block : toBreak)
        {
            if (block.getType() == Material.AIR || block.getType() == Material.BEDROCK)
            {
                continue;
            }
            handleDrops(e.getPlayer(), block);
        }
    }

}
