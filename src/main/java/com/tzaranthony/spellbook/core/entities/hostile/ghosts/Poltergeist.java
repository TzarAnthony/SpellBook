package com.tzaranthony.spellbook.core.entities.hostile.ghosts;

import com.tzaranthony.spellbook.core.entities.ai.MoveToEntityGoal;
import com.tzaranthony.spellbook.core.entities.ai.NearestNightTimeTargetGoal;
import com.tzaranthony.spellbook.core.entities.arrows.ThrownTool;
import com.tzaranthony.spellbook.core.entities.other.CursedPainting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class Poltergeist extends SBGhostEntity implements RangedAttackMob {
    public Poltergeist(EntityType<? extends Poltergeist> type, Level level) {
        super(type, level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new MoveToEntityGoal(this, CursedPainting.class, 1.0D));
        this.goalSelector.addGoal(2, new ThrowItemGoal(this, 1.0D, 40, 10.0F));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, SBGhostEntity.class));
        this.targetSelector.addGoal(2, new NearestNightTimeTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag nbt) {
        this.setVariant(this.random.nextInt(4));
        this.selectRandomThrowableItem();
        return super.finalizeSpawn(accessor, difficulty, reason, spawnData, nbt);
    }

    protected void selectRandomThrowableItem() {
        ItemStack stack;
        switch (this.random.nextInt(6)) {
            case 0:
                stack = new ItemStack(Items.IRON_AXE);
                break;
            case 1:
                stack = new ItemStack(Items.IRON_SWORD);
                break;
            case 2:
                stack = new ItemStack(Items.IRON_PICKAXE);
                break;
            case 3:
                stack = new ItemStack(Items.IRON_HOE);
                break;
            case 4:
                stack = new ItemStack(Items.IRON_SHOVEL);
                break;
            default:
                stack = new ItemStack(Items.TRIDENT);
        }
        this.setItemInHand(InteractionHand.MAIN_HAND, stack);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distance) {
        if (this.getMainHandItem().is(Items.TRIDENT)) {
            ThrownTrident thrown = new ThrownTrident(this.level, this, new ItemStack(Items.TRIDENT));
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333D) - thrown.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            thrown.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
            this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(thrown);
        } else {
            ThrownTool thrown = new ThrownTool(this.level, this, this.getMainHandItem());
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333D) - thrown.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            thrown.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
            this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(thrown);
        }
        this.swing(InteractionHand.MAIN_HAND);
        this.selectRandomThrowableItem();
    }

    class ThrowItemGoal extends RangedAttackGoal {
        private final Poltergeist poltergeist;

        public ThrowItemGoal(RangedAttackMob poltergeist, double speedMod, int interval, float distance) {
            super(poltergeist, speedMod, interval, distance);
            this.poltergeist = (Poltergeist) poltergeist;
        }

        public boolean canUse() {
            return super.canUse();
        }

        public void start() {
            super.start();
            this.poltergeist.setAggressive(true);
            this.poltergeist.startUsingItem(InteractionHand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.poltergeist.stopUsingItem();
            this.poltergeist.setAggressive(false);
        }
    }
}