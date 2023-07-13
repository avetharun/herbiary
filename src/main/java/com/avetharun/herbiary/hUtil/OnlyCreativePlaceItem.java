package com.avetharun.herbiary.hUtil;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;


public class OnlyCreativePlaceItem extends BlockItem {
    @Override

    public ActionResult useOnBlock(ItemUsageContext context) {
        assert context.getPlayer() != null;
        if (context.getPlayer().isCreative() || context.getWorld().getGameRules().getBoolean(Herbiary.ALLOW_HERB_PLACEMENTS)) {
            return this.place(new ItemPlacementContext(context));
        } else if (this.isFood()) {
            ActionResult actionResult2 = this.use(context.getWorld(), context.getPlayer(), context.getHand()).getResult();
            return actionResult2 == ActionResult.CONSUME ? ActionResult.CONSUME_PARTIAL : actionResult2;
        } else {
            return ActionResult.CONSUME;
        }
    }

    public OnlyCreativePlaceItem(Block block, Settings settings) {
        super(block, settings);
    }
}
