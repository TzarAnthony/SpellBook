package com.tzaranthony.spellbook.client.entityRender.models;

import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Yurei;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class YureiModel extends AbstractGhostModel<Yurei> {
    public YureiModel(ModelPart part) {
        super(part);
    }

    public boolean isAggressive(Yurei p_104155_) {
        return p_104155_.isAggressive();
    }
}