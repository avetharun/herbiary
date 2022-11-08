package com.avetharun.herbiary.hUtil;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Pair;

public class ModBlockItem extends Pair<Block, Item> {
    Block block;
    Item item;
    public ModBlockItem(Block left, Item right) {
        super(left, right);
    }
};
