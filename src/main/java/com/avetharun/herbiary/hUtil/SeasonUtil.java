package com.avetharun.herbiary.hUtil;

import com.avetharun.herbiary.Herbiary;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Optional;

public class SeasonUtil {
    public static class GrassColors {
        public static int SPRING = 0x52855f;
        public static int SUMMER = 0x5a8552;
        public static int FALL =   0x789146;
        public static int WINTER = 0xffffff;

        public static int getColor(Season season) {
            return switch (season) {
                case SPRING -> SPRING;
                case SUMMER -> SUMMER;
                case FALL -> FALL;
                case WINTER -> WINTER;
            };
        }
    };
    public static class FoliageColors {
        public static int SPRING = 0x52855f;
        public static int SUMMER = 0x5a8552;
        public static int FALL =   0x967142;
        public static int WINTER = 0xffffff;
        public static int getColor(Season season) {
            return switch (season) {
                case SPRING -> SPRING;
                case SUMMER -> SUMMER;
                case FALL -> FALL;
                case WINTER -> WINTER;
            };
        }
    };
}
