package com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss;

import com.tzaranthony.spellbook.core.entities.ai.MagicMultiAttackGoal;
import com.tzaranthony.spellbook.core.spells.Spell;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class GhostMage extends SBGhostCommander {
    public GhostMage(EntityType<? extends SBGhostCommander> type, Level level) {
        super(type, level);
        this.xpReward = 50;
    }

    protected void registerGoals() {
        //TODO: replace scream with a new spell?
        NonNullList<Spell> spells = NonNullList.of(SBSpellRegistry.LIFE_STEAL, SBSpellRegistry.STONE_REINFORCEMENT, SBSpellRegistry.SCREAM, SBSpellRegistry.IGNITE, SBSpellRegistry.FROST_WAVE);
        NonNullList<Integer> cooldowns = NonNullList.of(200, 140, 160, 120, 100);
        NonNullList<Integer> probabilities = NonNullList.of(100, 40, 50, 50, 80);

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MagicMultiAttackGoal(this, spells, cooldowns, probabilities));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.ATTACK_DAMAGE, 8+.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        //TODO: create texture
//        super.populateDefaultEquipmentSlots(difficulty);
//        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(SBItems.GHOST_SCEPTER.get()));
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ZOMBIE_HORSE_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        spawnData = super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
        this.populateDefaultEquipmentSlots(difficulty);
        return spawnData;
    }
}