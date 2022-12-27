package com.tzaranthony.spellbook.core.entities.other;

import com.google.common.collect.Lists;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.SBGhostEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.level.Level;

import java.util.Iterator;
import java.util.List;

public class CursedPainting extends Painting {
    public CursedPainting(EntityType<? extends CursedPainting> entityType, Level level) {
        super(entityType, level);
    }

    public void initPainting(Level p_31907_, BlockPos p_31908_, Direction p_31909_) {
        this.pos = p_31908_;
        List<Motive> list = Lists.newArrayList();
        int i = 0;

        for(Motive motive : Registry.MOTIVE) {
            this.motive = motive;
            this.setDirection(p_31909_);
            if (this.survives()) {
                list.add(motive);
                int j = motive.getWidth() * motive.getHeight();
                if (j > i) {
                    i = j;
                }
            }
        }

        if (!list.isEmpty()) {
            Iterator<Motive> iterator = list.iterator();

            while(iterator.hasNext()) {
                Motive motive1 = iterator.next();
                if (motive1.getWidth() * motive1.getHeight() < i) {
                    iterator.remove();
                }
            }

            this.motive = list.get(this.random.nextInt(list.size()));
        }

        this.setDirection(p_31909_);
    }

    @Override
    public boolean hurt(DamageSource source, float amt) {
        if (!(source.getEntity() instanceof SBGhostEntity)) {
            return super.hurt(source, amt);
        }
        return false;
    }
}