package com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss;

import com.tzaranthony.spellbook.core.items.equipment.equipmentMain.SBGlaive;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class GhostKnight extends SBGhostCommander {
    private boolean aroundArcher = true;

    public GhostKnight(EntityType<? extends SBGhostCommander> type, Level level) {
        super(type, level);
        this.xpReward = 50;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DoNothingGoal());
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 1.0D);
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.aroundArcher = nbt.getBoolean("isAroundArcher");
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putBoolean("isAroundArcher", this.aroundArcher);
        super.addAdditionalSaveData(nbt);
    }

    protected void customServerAiStep() {
        List<GhostArcher> gaList = this.level.getEntitiesOfClass(GhostArcher.class, new AABB(this.blockPosition()).inflate(20));
        if (gaList.isEmpty()) {
            this.aroundArcher = false;
            if (this.isUsingItem()) {
                this.stopUsingItem();
            }
        } else {
            this.aroundArcher = true;
            if (!this.isUsingItem()) {
                this.startUsingItem(InteractionHand.OFF_HAND);
            }
        }
        super.customServerAiStep();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.aroundArcher) {
            return super.hurt(source, amount);
        }
        return false;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(SBItems.GHOST_GLAIVE.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.WITHER_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        spawnData = super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
        this.populateDefaultEquipmentSlots(difficulty);
        this.startUsingItem(InteractionHand.OFF_HAND);
        return spawnData;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (target instanceof Player player && this.getMainHandItem().is(SBItems.GHOST_GLAIVE.get())) {
            SBGlaive.maybeDisableShield(this, player);
        }
        return flag;
    }

    public boolean getAroundArcher() {
        return this.aroundArcher;
    }

    class DoNothingGoal extends Goal {
        public DoNothingGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return GhostKnight.this.getAroundArcher();
        }
    }
}