package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Poltergeist;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PoltergeistModel extends AbstractGhostModel<Poltergeist> {
    public PoltergeistModel(ModelPart part) {
        super(part);
    }

    public boolean isAggressive(Poltergeist p_104155_) {
        return p_104155_.isAggressive();
    }
}