package com.avetharun.herbiary.block;

import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class SiftableBlock extends Block{
    static int lastUseTick = 0;
    static int GENERATION_DELAY_TICKS = 40;

    public SiftableBlock(Settings settings) {
        super(settings);
    }

    static String rearrangeSiftBlockIdentifier(String blockIdentifier) {
        String[] parts = blockIdentifier.split("/");
        if (parts.length == 2) {
            return parts[0] + "/sift" + parts[1];
        } else {
            // Handle invalid input
            return blockIdentifier;
        }
    }
    public static LootTable generateLootTable(WorldAccess worldAccess, Block source) {
        Identifier id = source.getLootTableId();
        String p = rearrangeSiftBlockIdentifier(id.getPath());
        Identifier lootTableIdentifier = new Identifier(id.getNamespace(), p);
        assert  worldAccess.getServer() != null;
        LootManager lootManager = worldAccess.getServer().getLootManager();
        return lootManager.getLootTable(lootTableIdentifier);
    }

    public static boolean canGenerateLoot(BlockState state, World world, BlockPos pos) {
        int currentTick = world.getServer().getTicks();
        return currentTick >= lastUseTick + GENERATION_DELAY_TICKS && world.getRandom().nextBetween(0, 100) > 80;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return onUseExt(state, world, pos, player, hand, hit);
    }

    public static ActionResult onUseExt(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        alib.setMixinField(player, "isSifting", true);
        if (!world.isClient) {
            if (canGenerateLoot(state,world,pos)) {
                LootTable lootTable = SiftableBlock.generateLootTable(world, state.getBlock());
                lootTable.generateLoot(
                        new LootContext.Builder(new LootContextParameterSet.Builder((ServerWorld) world).add(LootContextParameters.BLOCK_STATE, state).add(LootContextParameters.TOOL, player.getStackInHand(hand)).add(LootContextParameters.ORIGIN, hit.getPos()).build(LootContextTypes.BLOCK)).build(null),
                        itemStack -> {
                            var _pos = pos.toCenterPos().offset(hit.getSide(),1);
                            world.spawnEntity(new ItemEntity(world, _pos.x, _pos.y, _pos.z, itemStack));
                        }
                );
                lastUseTick = world.getServer().getTicks();
            }
        }
        return ActionResult.CONSUME_PARTIAL;
    }
}
