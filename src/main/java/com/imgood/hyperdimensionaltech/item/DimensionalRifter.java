package com.imgood.hyperdimensionaltech.item;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.entity.HT_EntityDimensionalRiftBlade;
import com.imgood.hyperdimensionaltech.network.EnergyUpdatePacket;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import software.bernie.geckolib3.core.easing.EasingType;

/**
 * @author Imgood
 */
public class DimensionalRifter extends Item implements IAnimatable {
    public static final int MAX_ENERGY = 100;
    private int energy = 100;
    private long lastRightClickTime = 0;
    private long lastAnimationTime = 0; // 新增属性
    private static final long ANIMATION_DELAY = 500; // 延迟时间（毫秒）

    private long lastLeftClickTime = 0; // 新增属性：左键点击时间
    private static final long LEFT_CLICK_COOLDOWN = 500; // 左键点击冷却时间（毫秒）

    public AnimationFactory factory = new AnimationFactory(this);
    private String controllerName = "EnergyWeaponController";

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<DimensionalRifter> controller = new AnimationController(this, this.controllerName, 1F, this::predicate);
        controller.registerSoundListener(this::soundListener);
        data.addAnimationController(controller);
    }

    private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
    }

    public DimensionalRifter() {
        super();
        this.setUnlocalizedName("energyWeapon");
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabCombat);
    }

    public void addEnergy(int amount) {
        this.energy = Math.min(MAX_ENERGY, this.energy + amount);
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void useEnergy(int amount) {
        this.energy -= amount;
        System.out.println("Energy: " + amount);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemStackIn, EntityPlayer playerIn, Entity entity) {
        long currentTime = System.currentTimeMillis();

        // 检查冷却时间是否已过
        if (currentTime - lastLeftClickTime < LEFT_CLICK_COOLDOWN) {
            return false; // 冷却时间未过，取消攻击
        }

        lastLeftClickTime = currentTime; // 更新上次左键点击时间

        AnimationController<?> controller = GeckoLibUtil.getControllerForStack(this.factory, itemStackIn, this.controllerName);
        if (controller.getAnimationState() == AnimationState.Stopped) {
            controller.markNeedsReload();
            controller.easingType = EasingType.EaseInOutExpo;
            controller.setAnimation((new AnimationBuilder()).addAnimation("animation.model.new2", false));
        }

        World worldIn = playerIn.worldObj;
        Vec3 look = playerIn.getLookVec();
        // 定义一个计数器来延迟生成实体
            HT_EntityDimensionalRiftBlade energyBlade = new HT_EntityDimensionalRiftBlade(worldIn, playerIn, look.xCoord * 4, look.yCoord * 4, look.zCoord * 4, 4);
            worldIn.spawnEntityInWorld(energyBlade);

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        // 获取当前系统时间
        long currentTime = System.currentTimeMillis();

        if (energy <= MAX_ENERGY && energy >= 30) {
            // 播放动画的条件

        }

        // 判断是否冷却时间已过（1秒）
        if (currentTime - lastRightClickTime >= 1000) {
            // 更新上次右键时间
            lastRightClickTime = currentTime;
            Vec3 look = playerIn.getLookVec();
            // 确保只在服务器端进行处理
            if (energy <= MAX_ENERGY && energy >= 30) {
                if (currentTime - lastAnimationTime >= ANIMATION_DELAY) {
                    AnimationController<?> controller = GeckoLibUtil.getControllerForStack(this.factory, itemStackIn, this.controllerName);
                    if (controller.getAnimationState() == AnimationState.Stopped) {
                        controller.markNeedsReload();
                        controller.easingType = EasingType.EaseInOutExpo;
                        controller.tickOffset = 10;
                        controller.animationSpeed = 1;
                        controller.setAnimation((new AnimationBuilder()).addAnimation("animation.model.new", false));
                        lastAnimationTime = currentTime;
                    }
                }
                playerIn.addVelocity(0, 0.5, 0);
                playerIn.addVelocity(look.xCoord * 5.5, look.yCoord * 5.4, look.zCoord * 5.5);
                useEnergy(30);

                HT_EntityDimensionalRiftBlade energyBlade = new HT_EntityDimensionalRiftBlade(worldIn, playerIn, look.xCoord * 3, look.yCoord * 3, look.zCoord * 3);
                worldIn.spawnEntityInWorld(energyBlade);

                // 添加抗性5和摔落保护的Buff，持续3秒（60 ticks） 没同步到服务端 以后再说
                playerIn.addPotionEffect(new PotionEffect(Potion.resistance.id, 100, 10));
                playerIn.fallDistance = 10000000;
            }
        }

        return itemStackIn;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        // 可以根据需要在这里添加逻辑
        System.out.println("Energy Weapon used");
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof EntityPlayer && isSelected) {
            EntityPlayer player = (EntityPlayer) entity;
            // 检查是否为主手物品
            if (player.getHeldItem() == stack) {
                player.swingProgress = -1;
                player.swingProgressInt = -1;
            }
        }
        if (!world.isRemote && isSelected && entity instanceof EntityPlayer) {
            addEnergy(1);
            if (world.getTotalWorldTime() % 20 == 0) {
                HyperdimensionalTech.network.sendTo(new EnergyUpdatePacket(energy), (EntityPlayerMP) entity);
            }
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


}
