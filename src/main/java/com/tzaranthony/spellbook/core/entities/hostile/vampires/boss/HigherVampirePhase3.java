package com.tzaranthony.spellbook.core.entities.hostile.vampires.boss;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.List;

//TODO: should i make this purely a cloud or like an illusioner, but instead of illusions it creates a cloud that follows the vampire??
public class HigherVampirePhase3 extends HigherVampire {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(HigherVampirePhase3.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(HigherVampirePhase3.class, EntityDataSerializers.PARTICLE);
    private Potion potion = Potions.EMPTY;
    private final List<MobEffectInstance> effects = Lists.newArrayList();
    private int reapplicationDelay = 20;

    public HigherVampirePhase3(EntityType<? extends HigherVampirePhase3> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_COLOR, 0);
        this.getEntityData().define(DATA_PARTICLE, ParticleTypes.ENTITY_EFFECT);
    }

    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.reapplicationDelay = tag.getInt("ReapplicationDelay");
        if (tag.contains("Particle", 8)) {
            try {
                this.setParticle(ParticleArgument.readParticle(new StringReader(tag.getString("Particle"))));
            } catch (CommandSyntaxException commandsyntaxexception) {
                LOGGER.warn("Couldn't load custom particle {}", tag.getString("Particle"), commandsyntaxexception);
            }
        }
        if (tag.contains("Potion", 8)) {
            this.setPotion(PotionUtils.getPotion(tag));
        }
        if (tag.contains("Effects", 9)) {
            ListTag listtag = tag.getList("Effects", 10);
            this.effects.clear();

            for(int i = 0; i < listtag.size(); ++i) {
                MobEffectInstance mobeffectinstance = MobEffectInstance.load(listtag.getCompound(i));
                if (mobeffectinstance != null) {
                    this.addEffect(mobeffectinstance);
                }
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("ReapplicationDelay", this.reapplicationDelay);
        tag.putString("Particle", this.getParticle().writeToString());
        if (this.potion != Potions.EMPTY) {
            tag.putString("Potion", Registry.POTION.getKey(this.potion).toString());
        }

        if (!this.effects.isEmpty()) {
            ListTag listtag = new ListTag();

            for(MobEffectInstance mobeffectinstance : this.effects) {
                listtag.add(mobeffectinstance.save(new CompoundTag()));
            }

            tag.put("Effects", listtag);
        }
        super.addAdditionalSaveData(tag);
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }

    private void updateColor() {
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.getEntityData().set(DATA_COLOR, 0);
        } else {
            this.getEntityData().set(DATA_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
        }
    }

    public int getColor() {
        return this.getEntityData().get(DATA_COLOR);
    }

    public void setFixedColor(int color) {
        this.getEntityData().set(DATA_COLOR, color);
    }

    public ParticleOptions getParticle() {
        return this.getEntityData().get(DATA_PARTICLE);
    }

    public void setParticle(ParticleOptions particle) {
        this.getEntityData().set(DATA_PARTICLE, particle);
    }
}