package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostArcher;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GhostArcherModel extends AbstractGhostModel<GhostArcher> {
    public GhostArcherModel(ModelPart part) {
        super(part);
    }

    public boolean isAggressive(GhostArcher ghost) {
        return ghost.isAggressive();
    }
}