package com.avetharun.herbiary.client;

import net.minecraft.client.texture.Sprite;
import net.minecraft.resource.Resource;

public class PlayerStatus {
    public float requiredPercentageToDisplay = 10;
    public float percentage;
    public String title;
    public Sprite sprite;
    public PlayerStatus(String title, float requiredPercentage) {
        this.title = title;
        this.requiredPercentageToDisplay = requiredPercentage;
    }
}
