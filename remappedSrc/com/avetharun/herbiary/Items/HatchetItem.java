package com.avetharun.herbiary.Items;

import com.avetharun.herbiary.ModItems;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public class HatchetItem extends AxeItem {
    public HatchetItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
    private Optional<BlockState> getStrippedState(BlockState state) {
        return Optional.ofNullable((Block)STRIPPED_BLOCKS.get(state.getBlock())).map((block) -> {
            return (BlockState)block.getDefaultState().with(PillarBlock.AXIS, (Direction.Axis)state.get(PillarBlock.AXIS));
        });
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        System.out.println("hit!");
        return super.postHit(stack, target, attacker);
    }
    public void SpawnBark(ItemStack stack, PlayerEntity user, BlockPos woodPos) {
        Vec3d p = Vec3d.ofCenter(woodPos).subtract(user.getRotationVec(0).multiply(1.2f));
        user.method_48926().spawnEntity(new ItemEntity(user.method_48926(), p.getX(), p.getY(), p.getZ(), stack));
    }
    
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        Optional<BlockState> optional = this.getStrippedState(blockState);
        ItemStack itemStack = context.getStack();
        if (optional.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            assert playerEntity != null;
            if (blockState.isOf(Blocks.BIRCH_LOG)) {
                SpawnBark(ModItems.BIRCH_BARK.getDefaultStack(), playerEntity, blockPos);
            }
            if (blockState.isOf(Blocks.OAK_LOG)) {
                SpawnBark(ModItems.OAK_BARK.getDefaultStack(), playerEntity, blockPos);
            }
            if (blockState.isOf(Blocks.SPRUCE_LOG)) {
                SpawnBark(ModItems.SPRUCE_BARK.getDefaultStack(), playerEntity, blockPos);
            }
            world.setBlockState(blockPos, optional.get(), 0);
        }

        return ActionResult.PASS;
    }


}
