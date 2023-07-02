package com.avetharun.herbiary.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;


@SuppressWarnings("rawtypes")
public class CampfirePlaceableBlock extends HorizontalFacingBlock {
    public static Property[] props;
    public CampfirePlaceableBlock(Settings settings, Property... props) {
        super(settings);
        CampfirePlaceableBlock.props = props;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        if (props != null && props.length > 0) {
            for (Property prop : props) {
                builder = builder.add(prop);
                System.out.println("addded prop");
            }
        }
        super.appendProperties(builder);
    }
}
