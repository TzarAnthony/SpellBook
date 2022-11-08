package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.HigherVampirePhase1;
import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HigherVampirePersonModel<T extends HigherVampirePhase1> extends AbstractZombieModel<T> {
    public HigherVampirePersonModel(ModelPart part) {
        super(part);
    }

    public boolean isAggressive(T p_104155_) {
        return p_104155_.isAggressive();
    }
}