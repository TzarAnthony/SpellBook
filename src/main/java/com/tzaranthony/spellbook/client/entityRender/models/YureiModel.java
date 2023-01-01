package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Yurei;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class YureiModel extends AbstractGhostModel<Yurei> {
    public YureiModel(ModelPart part) {
        super(part);
    }

    public void setupAnim(Yurei yurei, float p_102002_, float p_102003_, float p_102004_, float p_102005_, float p_102006_) {
        super.setupAnim(yurei, p_102002_, p_102003_, p_102004_, p_102005_, p_102006_);
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(yurei), this.attackTime, p_102004_);
    }

    public boolean isAggressive(Yurei yurei) {
        return yurei.isAggressive();
    }
}