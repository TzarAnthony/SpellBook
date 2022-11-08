package com.tzaranthony.spellbook.client.entityRender.renders;

import com.tzaranthony.spellbook.client.entityRender.models.LesserVampireModel;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.LesserVampire;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LesserVampireRender extends HumanoidMobRenderer<LesserVampire, LesserVampireModel> {
    private static final ResourceLocation VAMP_1 = new ResourceLocation("spellbook:textures/entity/vampire/lower/vampire1.png");
    private static final ResourceLocation VAMP_2 = new ResourceLocation("spellbook:textures/entity/vampire/lower/vampire2.png");
    private static final ResourceLocation VAMP_3 = new ResourceLocation("spellbook:textures/entity/vampire/lower/vampire3.png");
    private static final ResourceLocation VAMP_4 = new ResourceLocation("spellbook:textures/entity/vampire/lower/vampire4.png");

    public LesserVampireRender(EntityRendererProvider.Context manager) {
        super(manager, new LesserVampireModel(manager.bakeLayer(ModelLayers.ZOMBIE)), 0.55F);
        this.shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(LesserVampire lesserVampire) {
        switch (lesserVampire.getVariant()) {
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