package com.tzaranthony.spellbook.client.entityRender.models;


import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Shade;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShadeModel extends AbstractGhostModel<Shade> {
    public ShadeModel(ModelPart part) {
        super(part);
    }

    public void setupAnim(Shade shade, float p_102002_, float p_102003_, float p_102004_, float p_102005_, float p_102006_) {
        super.setupAnim(shade, p_102002_, p_102003_, p_102004_, p_102005_, p_102006_);
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(shade), this.attackTime, p_102004_);
    }

    public boolean isAggressive(Shade shade) {
        return shade.isAggressive();
    }
}