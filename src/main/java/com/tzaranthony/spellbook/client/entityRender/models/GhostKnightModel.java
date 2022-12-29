package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostKnight;
import net.minecraft.client.model.geom.ModelPart;
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
}