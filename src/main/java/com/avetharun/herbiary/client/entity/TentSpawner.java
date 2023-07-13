package com.avetharun.herbiary.client.entity;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.HerbiarySoundEvents;
import com.avetharun.herbiary.entity.ModEntityTypes;
import com.avetharun.herbiary.entity.TentEntity;
import com.avetharun.herbiary.hUtil.alib;
import net.minecraft.block.BedBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TentSpawner extends Item {
    public TentSpawner(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnBlock(net.minecraft.item.ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos().up(1);
        BlockPos[] surroundingPos = {blockPos.north(), blockPos.east(), blockPos.south(), blockPos.west()};
        // get the positions of the blocks surrounding the block above the clicked block
        if (alib.getEntitiesOfTypeInRange(world, blockPos, 10, ModEntityTypes.TENT_ENTITY_TYPE).stream().findAny().isPresent()) {
            SystemToast.show(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.TUTORIAL_HINT, Text.of("There is another tent too close to this!"), Text.of("Try to find an area that is clear.") );
            return ActionResult.PASS;
        }
        for (BlockPos pos : surroundingPos) {
            if (!world.getBlockState(pos.down()).isFullCube(world, pos.down())) {
                if (world.isClient) {
                    SystemToast.show(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.TUTORIAL_HINT, Text.of("The tent needs to be on the ground!"), Text.of("Try to find an area that is clear."));
                    world.addParticle(ParticleTypes.COMPOSTER, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0, 0);
                }
                return ActionResult.PASS;
            }
            BlockPos[] surroundingPos2 = {pos.up(1), pos.up(2)};
            for (BlockPos pos2 : surroundingPos2) {
                if (!world.getBlockState(pos).isIn(BlockTags.REPLACEABLE) && !world.getBlockState(pos).isAir()) {
                    if (world.isClient) {
                        SystemToast.show(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.TUTORIAL_HINT, Text.of("You cannot place this- there are blocks in the way!"), Text.of("Try to find an area that is clear."));
                        world.addParticle(ParticleTypes.COMPOSTER, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0, 0);
                    }
                    return ActionResult.PASS;
                }
            }
        }
        if (!world.isClient) {
            Direction d = context.getHorizontalPlayerFacing();
            context.getWorld().spawnEntity(new TentEntity(world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, d));
            ItemStack itemStack = context.getStack();
            itemStack.decrement(1);
        }
        world.playSound(context.getHitPos().x,context.getHitPos().y,context.getHitPos().z, Herbiary.UNZIP_TENT, SoundCategory.BLOCKS, 1f, 1f, true);
        return ActionResult.CONSUME;
    }
}
