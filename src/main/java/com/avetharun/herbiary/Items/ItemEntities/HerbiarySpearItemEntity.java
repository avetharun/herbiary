package com.avetharun.herbiary.Items.ItemEntities;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.HerbiarySoundEvents;
import com.avetharun.herbiary.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.InstancedAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class HerbiarySpearItemEntity extends PersistentProjectileEntity implements GeoAnimatable {

    public  ItemStack tridentStack;
    private Entity owner;
    public HerbiarySpearItemEntity(EntityType<HerbiarySpearItemEntity> type, World world) {
        super (type, world, ModItems.SPEAR_ITEM.getDefaultStack());
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        entityHitResult.getEntity().damage(this.getDamageSources().trident(this, owner), 4);
        entityHitResult.getEntity().getWorld().playSound(getX(), getY(), getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE, SoundCategory.MASTER, 1, 0.1f, false);
    }

    public static EntityType<HerbiarySpearItemEntity> getEntityType() {
        return  FabricEntityTypeBuilder.<HerbiarySpearItemEntity>create(SpawnGroup.MISC, HerbiarySpearItemEntity::new).
                dimensions(EntityDimensions.fixed(0.35f,0.35f))
                .build();
    }

    protected ItemStack asItemStack() {
        if (this.tridentStack == null) {return ModItems.SPEAR_ITEM.getDefaultStack();}
        return this.tridentStack.copy();
    }
    @Override
    protected SoundEvent getHitSound() {
        return Herbiary.SPEAR_LAND;
    }


    public HerbiarySpearItemEntity(World world, LivingEntity _owner, ItemStack stack) {
        super(ModItems.SPEAR_ENTITY_TYPE, world, stack);

        this.tridentStack = stack;
        owner = _owner;
        world.playSound(null, _owner.getBlockPos(), Herbiary.SPEAR_THROWN, SoundCategory.PLAYERS, 1, 1);
    }
    @Override
    // Never age
    public void age() {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
    AnimatableInstanceCache cache = new InstancedAnimatableInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }
}
