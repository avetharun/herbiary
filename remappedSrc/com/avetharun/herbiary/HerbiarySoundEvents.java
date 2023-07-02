package com.avetharun.herbiary;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class HerbiarySoundEvents {
    public static final Identifier SPEAR_THROWN_ID = new Identifier("al_herbiary:spear_thrown");
    public static final SoundEvent SPEAR_THROWN = SoundEvent.of(SPEAR_THROWN_ID);
    public static final Identifier SPEAR_LAND_ID = new Identifier("al_herbiary:spear_land");
    public static final SoundEvent SPEAR_LAND = SoundEvent.of(SPEAR_LAND_ID);
    public static final Identifier FLINT_FAIL_ID = new Identifier("al_herbiary:flint_fail");
    public static final SoundEvent FLINT_FAIL = SoundEvent.of(FLINT_FAIL_ID);
    public static final Identifier FLINT_SUCCEED_ID = new Identifier("al_herbiary:flint_succeed");
    public static final SoundEvent FLINT_SUCCEED = SoundEvent.of(FLINT_SUCCEED_ID);
    public static final Identifier UNZIP_TENT_ID = new Identifier("al_herbiary:unzip_tent");
    public static final SoundEvent UNZIP_TENT = SoundEvent.of(UNZIP_TENT_ID);
    public static final Identifier ROCK_STRIKE_ID = new Identifier("al_herbiary:rock_strike");
    public static final SoundEvent ROCK_STRIKE = SoundEvent.of(ROCK_STRIKE_ID);
}
