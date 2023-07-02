package com.avetharun.herbiary.hUtil;

import com.avetharun.herbiary.Herbiary;
import com.avetharun.herbiary.Items.UnlockableNamedItem;
import com.avetharun.herbiary.block.EdibleBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;


public class OnlyCreativePlaceItemUnlockable extends EdibleBlock {
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

    @Override
    public Text getName(ItemStack stack) {
        return UnlockableNamedItem.HandleName(stack, super.getName(stack));
    }
    @Override
    public boolean isFood() {
        return super.isFood();
    }

    public OnlyCreativePlaceItemUnlockable(Block block, Settings settings) {
        super(block, settings);
    }
}
