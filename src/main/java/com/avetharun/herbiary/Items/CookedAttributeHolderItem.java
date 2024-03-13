package com.avetharun.herbiary.Items;

import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;

public class CookedAttributeHolderItem extends Item {
    public CookedAttributeHolderItem(Settings settings) {
        super(settings);
    }
    public enum CookedAttribute{
        HEAT_RESIST("heat_resist", 0.20f, 20*45),
        COLD_RESIST("cold_resist", 0.10f, 20*120);

        CookedAttribute(String name, float value, int decayTime) {
            this.name = name;
            this.value = value;
            this.ticksUntilDecayed = decayTime;
        }
        public final String name;
        public final float value;
        public final int ticksUntilDecayed;

    }

}
