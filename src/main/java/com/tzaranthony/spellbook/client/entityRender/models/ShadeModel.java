package com.tzaranthony.spellbook.client.entityRender.models;


import com.tzaranthony.spellbook.core.entities.hostile.ghosts.Shade;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShadeModel extends AbstractGhostModel<Shade> {
    public ShadeModel(ModelPart part) {
        super(part);
    }

    public boolean isAggressive(Shade p_104155_) {
        return p_104155_.isAggressive();
    }
}