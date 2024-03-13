package com.avetharun.herbiary.client.entity.model;

import com.avetharun.herbiary.entity.OwlEntity;
import com.avetharun.herbiary.entity.TentEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TentEntityModel extends GeoModel<TentEntity> {
    private static final Identifier MODEL_LOCATION = new Identifier("al_herbiary", "geo/entity/tent.geo.json");

    private static final Identifier TEXTURE_LOCATION = new Identifier("al_herbiary", "textures/entity/tent.png");

    @Override
    public Identifier getModelResource(TentEntity object) {
        return MODEL_LOCATION;
    }

    @Override
    public Identifier getTextureResource(TentEntity object) {
        return TEXTURE_LOCATION;
    }

    @Override
    public Identifier getAnimationResource(TentEntity owl) {
        return null;
    }

    @Override
    public void setCustomAnimations(TentEntity tent, long instanceId, AnimationState<TentEntity> animationState) {
        super.setCustomAnimations(tent, instanceId, animationState);
        CoreGeoBone bed = this.getAnimationProcessor().getBone("bed");
        CoreGeoBone root = this.getAnimationProcessor().getBone("root");
        CoreGeoBone storage = this.getAnimationProcessor().getBone("storage");
        CoreGeoBone stand = this.getAnimationProcessor().getBone("stand");

        bed.setHidden(!tent.hasSleepingBag());
        storage.setHidden(true);
        stand.setHidden(!tent.hasStand());
        root.setRotY(tent.getFacing().asRotation());
    }
}
