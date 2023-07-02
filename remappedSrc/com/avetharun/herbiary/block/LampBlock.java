package com.avetharun.herbiary.block;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.object.Axis;

import java.util.Optional;
import java.util.function.ToIntFunction;

import static com.avetharun.herbiary.block.PartiallySubmergedBlock.WATERLOGGED;

public class LampBlock extends Block {
    public static final BooleanProperty LIT, HANGING;
    public LampBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }
    public static boolean canBeLit(BlockState state) {
        return state.isIn(Herbiary.LAMPS, (statex) -> {
            return statex.contains(LIT);
        }) && !state.get(LIT);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction[] var3 = ctx.getPlacementDirections();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Direction direction = var3[var5];
            if (direction.getAxis() == Direction.Axis.Y) {
                BlockState blockState = (BlockState)this.getDefaultState().with(HANGING, direction == Direction.UP);
                if (blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
                    return blockState;
                }
            }
        }

        return null;
    }
    public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE;

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, HANGING);
    }
    VoxelShape HANGING_SHAPE = VoxelShapes.cuboid(0.375, 0.25, 0.375, 0.625, 1, 0.625);
    VoxelShape GROUNDED_STATE = VoxelShapes.cuboid(0.375, 0, 0.375, 0.625, 0.5625, 0.625);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HANGING) ? HANGING_SHAPE : GROUNDED_STATE;
    }
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(LIT) && !player.getStackInHand(hand).isOf(Items.FLINT_AND_STEEL)) {
            world.setBlockState(pos, state.with(LIT,false));
            world.playSound(null, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.PLAYERS, 1, 1);
        }
        return ActionResult.PASS;
    }
    static {
        LIT = Properties.LIT;
        HANGING = Properties.HANGING;
        STATE_TO_LUMINANCE = (state) -> (Boolean)state.get(LIT) ? 12 : 0;
    }
}
