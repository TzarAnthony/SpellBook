package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.LesserVampire;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LesserVampireModel extends HumanoidModel<LesserVampire> {
    public LesserVampireModel(ModelPart part) {
        super(part);
    }

    public boolean isAggressive(LesserVampire p_104155_) {
        return p_104155_.isAggressive();
    }
}