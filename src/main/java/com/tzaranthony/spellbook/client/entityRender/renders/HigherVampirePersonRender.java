package com.tzaranthony.spellbook.client.entityRender.renders;

import com.tzaranthony.spellbook.client.entityRender.models.HigherVampirePersonModel;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.HigherVampirePhase1;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HigherVampirePersonRender extends HumanoidMobRenderer<HigherVampirePhase1, HigherVampirePersonModel<HigherVampirePhase1>> {
    private static final ResourceLocation VAMP_1 = new ResourceLocation("spellbook:textures/entity/vampire/higher/vampire1.png");
    private static final ResourceLocation VAMP_2 = new ResourceLocation("spellbook:textures/entity/vampire/higher/vampire2.png");
    private static final ResourceLocation VAMP_3 = new ResourceLocation("spellbook:textures/entity/vampire/higher/vampire3.png");
    private static final ResourceLocation VAMP_4 = new ResourceLocation("spellbook:textures/entity/vampire/higher/vampire4.png");

    public HigherVampirePersonRender(EntityRendererProvider.Context manager) {
        super(manager, new HigherVampirePersonModel(manager.bakeLayer(ModelLayers.ZOMBIE)), 0.55F);
        this.shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(HigherVampirePhase1 higherVampire) {
        switch (higherVampire.getVariant()) {
            case 1:
                return VAMP_1;
            case 2:
                return VAMP_2;
            case 3:
                return VAMP_3;
            default:
                return VAMP_4;
        }
    }
}