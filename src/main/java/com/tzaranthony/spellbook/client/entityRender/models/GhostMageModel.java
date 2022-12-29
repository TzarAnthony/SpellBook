package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostMage;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GhostMageModel extends AbstractGhostModel<GhostMage> {
    public GhostMageModel(ModelPart part) {
        super(part);
    }

    public boolean isAggressive(GhostMage ghost) {
        return ghost.isAggressive();
    }
}