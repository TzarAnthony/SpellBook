package com.tzaranthony.spellbook.client.entityRender.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Wraith;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WraithModel<T extends Wraith> extends HumanoidModel<T> {
    public WraithModel(ModelPart model) {
        super(model, RenderType::entityTranslucent);
    }

    public void prepareMobModel(T p_103793_, float p_103794_, float p_103795_, float p_103796_) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        ItemStack itemstack = p_103793_.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemstack.is(Items.BOW) && p_103793_.isAggressive()) {
            if (p_103793_.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        }

        super.prepareMobModel(p_103793_, p_103794_, p_103795_, p_103796_);
    }

    public void setupAnim(T p_103798_, float p_103799_, float p_103800_, float p_103801_, float p_103802_, float p_103803_) {
        super.setupAnim(p_103798_, p_103799_, p_103800_, p_103801_, p_103802_, p_103803_);
        ItemStack itemstack = p_103798_.getMainHandItem();
        if (p_103798_.isAggressive() && (itemstack.isEmpty() || !itemstack.is(Items.BOW))) {
            float f = Mth.sin(this.attackTime * (float)Math.PI);
            float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f * 0.6F);
            this.leftArm.yRot = 0.1F - f * 0.6F;
            this.rightArm.xRot = (-(float)Math.PI / 2F);
            this.leftArm.xRot = (-(float)Math.PI / 2F);
            this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
            this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
            AnimationUtils.bobArms(this.rightArm, this.leftArm, p_103801_);
        }

    }

    public void translateToHand(HumanoidArm p_103778_, PoseStack p_103779_) {
        float f = p_103778_ == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        ModelPart modelpart = this.getArm(p_103778_);
        modelpart.x += f;
        modelpart.translateAndRotate(p_103779_);
        modelpart.x -= f;
    }
}