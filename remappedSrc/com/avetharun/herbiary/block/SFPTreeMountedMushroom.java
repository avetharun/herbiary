package com.avetharun.herbiary.block;

import com.avetharun.herbiary.hUtil.Season;

// Summer-fall preferred tree mounted mushroom
public class SFPTreeMountedMushroom extends TreeMountedBlock {
    public SFPTreeMountedMushroom(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canGrowInSeason(int season) {
        return season == Season.FALL.ordinal() || season == Season.SUMMER.ordinal();
    }

    @Override
    public boolean growMoreInRain() {
        return true;
    }
}
