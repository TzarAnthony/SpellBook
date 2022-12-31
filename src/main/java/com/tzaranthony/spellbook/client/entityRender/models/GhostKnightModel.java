package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostKnight;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GhostKnightModel extends AbstractGhostModel<GhostKnight> {
    public GhostKnightModel(ModelPart part) {
        super(part);
    }

    public boolean isAggressive(GhostKnight ghost) {
        return ghost.isAggressive();
    }

    public void prepareMobModel(GhostKnight knight, float p_102522_, float p_102523_, float p_102524_) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        ItemStack itemstack = knight.getItemInHand(InteractionHand.OFF_HAND);
        if (itemstack.is(Items.SHIELD) && knight.isUsingItem() && knight.getUseItem().is(Items.SHIELD)) {
            this.leftArmPose = ArmPose.BLOCK;
        }
        super.prepareMobModel(knight, p_102522_, p_102523_, p_102524_);
    }

    public void setupAnim(GhostKnight knight, float p_103799_, float p_103800_, float p_103801_, float p_103802_, float p_103803_) {
        super.setupAnim(knight, p_103799_, p_103800_, p_103801_, p_103802_, p_103803_);
        if (this.leftArmPose == HumanoidModel.ArmPose.BLOCK && !knight.isPassenger()) {
            this.leftArm.yRot = ((float)Math.PI / 6F);
        }
    }
}