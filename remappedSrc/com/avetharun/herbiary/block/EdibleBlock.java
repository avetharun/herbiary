package com.avetharun.herbiary.block;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.image.PackedColorModel;


public class EdibleBlock extends BlockItem {

    public ActionResult useOnBlock(ItemUsageContext context) {
        assert context.getPlayer() != null;
        if (context.getPlayer().isCreative()) {
            return this.place(new ItemPlacementContext(context));
        } else if (this.isFood()) {
            ActionResult actionResult2 = this.use(context.getWorld(), context.getPlayer(), context.getHand()).getResult();
            return actionResult2 == ActionResult.CONSUME ? ActionResult.CONSUME_PARTIAL : actionResult2;
        } else {
            return ActionResult.CONSUME;
        }
    }

    @Override
    public boolean isFood() {
        return super.isFood();
    }

    public EdibleBlock(Block block, Settings settings) {
        super(block, settings);
    }
}
