package com.avetharun.herbiary.mixin.Block;

import com.avetharun.herbiary.block.SiftableBlock;
import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.List;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {
    private static int lastUseTick = 0;
    private static final int GENERATION_DELAY_TICKS = 40;

    private boolean canGenerateLoot(BlockState state, World world, BlockPos pos) {
        int currentTick = world.getServer().getTicks();
        return currentTick >= lastUseTick + GENERATION_DELAY_TICKS && world.getRandom().nextBetween(0, 100) > 80;
    }
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        alib.setMixinField(player, "isSifting", true);
        if (!world.isClient) {
            if (canGenerateLoot(state,world,pos)) {
                LootTable lootTable = SiftableBlock.generateLootTable(world, (Block) (Object) this);
                lootTable.generateLoot(
                        new LootContext.Builder(new LootContextParameterSet.Builder((ServerWorld) world).add(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK)).build(null),
                        itemStack -> {
                            var _pos = pos.offset(hit.getSide()).toCenterPos();
                            world.spawnEntity(new ItemEntity(world, _pos.x, _pos.y, _pos.z, itemStack));
                        }
                );
                lastUseTick = world.getServer().getTicks();
            }
        }
        return ActionResult.CONSUME;
    }
}
