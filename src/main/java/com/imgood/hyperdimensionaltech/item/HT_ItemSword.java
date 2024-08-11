package com.imgood.hyperdimensionaltech.item;

import com.imgood.hyperdimensionaltech.entity.EntitySwordBeam;
import com.imgood.hyperdimensionaltech.network.NetworkHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import static com.imgood.hyperdimensionaltech.utils.Enums.Names.MOD_ID;

public class HT_ItemSword extends ItemSword {
    private int energy = 0;
    private static final int MAX_ENERGY = 100;

    public HT_ItemSword(ToolMaterial material) {
        super(material);
        this.setUnlocalizedName("hyperdimensionalSword");
        this.setTextureName(MOD_ID+"/item/HyperdimensionalSword.png");
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isRemote && isSelected && entity instanceof EntityPlayer) {
            if (energy < MAX_ENERGY) {
                energy++;
                NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
                nbt.setInteger("energy", energy);
                stack.setTagCompound(nbt);
            }
        }
    }

    public int getEnergy(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("energy")) {
            return stack.getTagCompound().getInteger("energy");
        }
        return 0;
    }

    public void useEnergy(ItemStack stack, int amount) {
        int currentEnergy = getEnergy(stack);
        NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        nbt.setInteger("energy", Math.max(0, currentEnergy - amount));
        stack.setTagCompound(nbt);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.isSneaking() && getEnergy(itemStack) >= 25) {
            if (!world.isRemote) {
                EntitySwordBeam swordBeam = new EntitySwordBeam(world, player);
                world.spawnEntityInWorld(swordBeam);
                NetworkHandler.INSTANCE.sendToAllAround(new NetworkHandler.MessageSwordBeamSpawn(swordBeam),
                    new NetworkRegistry.TargetPoint(world.provider.dimensionId, player.posX, player.posY, player.posZ, 64));
                useEnergy(itemStack, 50);
            }
            // 实现冲锋
            player.motionX += player.getLookVec().xCoord * 0.5;
            player.motionZ += player.getLookVec().zCoord * 0.5;
        }
        return itemStack;
    }
}
