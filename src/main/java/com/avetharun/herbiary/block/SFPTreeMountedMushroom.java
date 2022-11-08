package com.avetharun.herbiary.block;

import com.avetharun.herbiary.hUtil.TreeMountedBlock;
// Summer-fall preferred tree mounted mushroom
public class SFPTreeMountedMushroom extends TreeMountedBlock {
    public SFPTreeMountedMushroom(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canGrowInSeason(int season) {
        return season == 2 || season == 3;
    }
}
