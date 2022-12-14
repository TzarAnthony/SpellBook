package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.boss.HigherVampireBat;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HigherVampireBatModel extends HierarchicalModel<HigherVampireBat> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart rightWingTip;
    private final ModelPart leftWingTip;

    public HigherVampireBatModel(ModelPart p_170427_) {
        this.root = p_170427_;
        this.head = p_170427_.getChild("head");
        this.body = p_170427_.getChild("body");
        this.rightWing = this.body.getChild("right_wing");
        this.rightWingTip = this.rightWing.getChild("right_wing_tip");
        this.leftWing = this.body.getChild("left_wing");
        this.leftWingTip = this.leftWing.getChild("left_wing_tip");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
        partdefinition1.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(24, 0).addBox(-4.0F, -6.0F, -2.0F, 3.0F, 4.0F, 1.0F), PartPose.ZERO);
        partdefinition1.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(1.0F, -6.0F, -2.0F, 3.0F, 4.0F, 1.0F), PartPose.ZERO);
        PartDefinition partdefinition2 = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 4.0F, -3.0F, 6.0F, 12.0F, 6.0F).texOffs(0, 34).addBox(-5.0F, 16.0F, 0.0F, 10.0F, 6.0F, 1.0F), PartPose.ZERO);
        PartDefinition partdefinition3 = partdefinition2.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(42, 0).addBox(-12.0F, 1.0F, 1.5F, 10.0F, 16.0F, 1.0F), PartPose.ZERO);
        partdefinition3.addOrReplaceChild("right_wing_tip", CubeListBuilder.create().texOffs(24, 16).addBox(-8.0F, 1.0F, 0.0F, 8.0F, 12.0F, 1.0F), PartPose.offset(-12.0F, 1.0F, 1.5F));
        PartDefinition partdefinition4 = partdefinition2.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(42, 0).mirror().addBox(2.0F, 1.0F, 1.5F, 10.0F, 16.0F, 1.0F), PartPose.ZERO);
        partdefinition4.addOrReplaceChild("left_wing_tip", CubeListBuilder.create().texOffs(24, 16).mirror().addBox(0.0F, 1.0F, 0.0F, 8.0F, 12.0F, 1.0F), PartPose.offset(12.0F, 1.0F, 1.5F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(HigherVampireBat p_102200_, float p_102201_, float p_102202_, float p_102203_, float p_102204_, float p_102205_) {
        this.head.xRot = p_102205_ * ((float)Math.PI / 180F);
        this.head.yRot = p_102204_ * ((float)Math.PI / 180F);
        this.head.zRot = 0.0F;
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.rightWing.setPos(0.0F, 0.0F, 0.0F);
        this.leftWing.setPos(0.0F, 0.0F, 0.0F);
        this.body.xRot = ((float)Math.PI / 4F) + Mth.cos(p_102203_ * 0.1F) * 0.15F;
        this.body.yRot = 0.0F;
        this.rightWing.yRot = Mth.cos(p_102203_ * 74.48451F * ((float)Math.PI / 180F)) * (float)Math.PI * 0.25F;
        this.leftWing.yRot = -this.rightWing.yRot;
        this.rightWingTip.yRot = this.rightWing.yRot * 0.5F;
        this.leftWingTip.yRot = -this.rightWing.yRot * 0.5F;
    }
}