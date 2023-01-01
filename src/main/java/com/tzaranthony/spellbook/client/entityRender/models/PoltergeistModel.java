package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Poltergeist;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PoltergeistModel extends AbstractGhostModel<Poltergeist> {
    public PoltergeistModel(ModelPart part) {
        super(part);
    }

    public void prepareMobModel(Poltergeist poltergeist, float p_102522_, float p_102523_, float p_102524_) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        ItemStack itemstack = poltergeist.getItemInHand(InteractionHand.MAIN_HAND);
//        if (itemstack.is(Items.TRIDENT) && poltergeist.isAggressive()) {
        if (poltergeist.isAggressive()) {
            if (poltergeist.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.THROW_SPEAR;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.THROW_SPEAR;
            }
        }
        super.prepareMobModel(poltergeist, p_102522_, p_102523_, p_102524_);
    }

    public void setupAnim(Poltergeist poltergeist, float p_102527_, float p_102528_, float p_102529_, float p_102530_, float p_102531_) {
        super.setupAnim(poltergeist, p_102527_, p_102528_, p_102529_, p_102530_, p_102531_);
//        if (this.leftArmPose == HumanoidModel.ArmPose.THROW_SPEAR) {
//            this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float)Math.PI;
//            this.leftArm.yRot = 0.0F;
//        }
//
//        if (this.rightArmPose == HumanoidModel.ArmPose.THROW_SPEAR) {
//            this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float)Math.PI;
//            this.rightArm.yRot = 0.0F;
//        }
    }

    //TODO: make this run in reverse
    protected void setupAttackAnimation(Poltergeist poltergeist, float p_102859_, boolean wait) {
        if (!(this.attackTime <= 0.0F)) {
            HumanoidArm humanoidarm = this.getAttackArm(poltergeist);
            ModelPart modelpart = this.getArm(humanoidarm);
            float f = this.attackTime;
            this.body.yRot = Mth.sin(Mth.sqrt(f) * ((float)Math.PI * 2F)) * 0.2F;
            if (humanoidarm == HumanoidArm.LEFT) {
                this.body.yRot *= -1.0F;
            }

            this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F;
            this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F;
            this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F;
            this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F;
            this.rightArm.yRot += this.body.yRot;
            this.leftArm.yRot += this.body.yRot;
            this.leftArm.xRot += this.body.yRot;
            f = 1.0F - this.attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float)Math.PI);
            float f2 = Mth.sin(this.attackTime * (float)Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            modelpart.xRot -= f1 * 1.2F + f2;
            modelpart.yRot += this.body.yRot * 2.0F;
            modelpart.zRot += Mth.sin(this.attackTime * (float)Math.PI) * -0.4F;
        }
    }

    private HumanoidArm getAttackArm(Poltergeist poltergeist) {
        HumanoidArm humanoidarm = poltergeist.getMainArm();
        return poltergeist.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
    }
}