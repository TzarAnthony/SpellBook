package com.tzaranthony.spellbook.core.entities.friendly;

import com.tzaranthony.spellbook.core.entities.ai.NecromancedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SummonedZombie extends SBSummonedEntity implements NecromancedEntity {
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_SPECIAL_TYPE_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.INT);

    public SummonedZombie(EntityType<? extends SummonedZombie> sZombie, Level level) {
        super(sZombie, level);
    }


    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new sZombieAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_BABY_ID, false);
        this.getEntityData().define(DATA_SPECIAL_TYPE_ID, 0);
    }

    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    public void setBaby(boolean p_34309_) {
        this.getEntityData().set(DATA_BABY_ID, p_34309_);
        if (this.level != null && !this.level.isClientSide) {
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_BABY);
            if (p_34309_) {
                attributeinstance.addTransientModifier(SPEED_MODIFIER_BABY);
            }
        }

    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_34307_) {
        if (DATA_BABY_ID.equals(p_34307_)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(p_34307_);
    }
    public void aiStep() {
        if (this.isAlive()) {
            boolean flag = this.isSunSensitive() && this.isSunBurnTick();
            if (flag) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.broadcastBreakEvent(EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }
                    flag = false;
                }
                if (flag) {
                    this.setSecondsOnFire(8);
                }
            }
        }

        super.aiStep();
    }

    protected boolean isSunSensitive() {
        return true;
    }

    public boolean doHurtTarget(Entity p_34276_) {
        boolean flag = super.doHurtTarget(p_34276_);
        if (flag) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                p_34276_.setSecondsOnFire(2 * (int)f);
            }
        }
        return flag;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_34327_) {
        return SoundEvents.ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }

    protected void playStepSound(BlockPos p_34316_, BlockState p_34317_) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance p_34286_) {
        super.populateDefaultEquipmentSlots(p_34286_);
        if (this.random.nextFloat() < (this.level.getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
            int i = this.random.nextInt(3);
            if (i == 0) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag p_34319_) {
        super.addAdditionalSaveData(p_34319_);
        p_34319_.putBoolean("IsBaby", this.isBaby());
    }

    public void readAdditionalSaveData(CompoundTag p_34305_) {
        super.readAdditionalSaveData(p_34305_);
        this.setBaby(p_34305_.getBoolean("IsBaby"));
    }

    protected float getStandingEyeHeight(Pose p_34313_, EntityDimensions p_34314_) {
        return this.isBaby() ? 0.93F : 1.74F;
    }

    public boolean canHoldItem(ItemStack p_34332_) {
        return p_34332_.is(Items.EGG) && this.isBaby() && this.isPassenger() ? false : super.canHoldItem(p_34332_);
    }

    public boolean wantsToPickUp(ItemStack p_182400_) {
        return p_182400_.is(Items.GLOW_INK_SAC) ? false : super.wantsToPickUp(p_182400_);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34297_, DifficultyInstance p_34298_, MobSpawnType p_34299_, @Nullable SpawnGroupData p_34300_, @Nullable CompoundTag p_34301_) {
        p_34300_ = super.finalizeSpawn(p_34297_, p_34298_, p_34299_, p_34300_, p_34301_);
        float f = p_34298_.getSpecialMultiplier();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * f);
        if (p_34300_ == null) {
            p_34300_ = new Zombie.ZombieGroupData(getSpawnAsBabyOdds(p_34297_.getRandom()), true);
        }
        if (p_34300_ instanceof Zombie.ZombieGroupData) {
            Zombie.ZombieGroupData zombie$zombiegroupdata = (Zombie.ZombieGroupData)p_34300_;
            if (zombie$zombiegroupdata.isBaby) {
                this.setBaby(true);
                if (zombie$zombiegroupdata.canSpawnJockey) {
                    if ((double)p_34297_.getRandom().nextFloat() < 0.05D) {
                        List<Chicken> list = p_34297_.getEntitiesOfClass(Chicken.class, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D), EntitySelector.ENTITY_NOT_BEING_RIDDEN);
                        if (!list.isEmpty()) {
                            Chicken chicken = list.get(0);
                            chicken.setChickenJockey(true);
                            this.startRiding(chicken);
                        }
                    } else if ((double)p_34297_.getRandom().nextFloat() < 0.05D) {
                        Chicken chicken1 = EntityType.CHICKEN.create(this.level);
                        chicken1.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        chicken1.finalizeSpawn(p_34297_, p_34298_, MobSpawnType.JOCKEY, (SpawnGroupData)null, (CompoundTag)null);
                        chicken1.setChickenJockey(true);
                        this.startRiding(chicken1);
                        p_34297_.addFreshEntity(chicken1);
                    }
                }
            }
            this.populateDefaultEquipmentSlots(p_34298_);
            this.populateDefaultEquipmentEnchantments(p_34298_);
        }
        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }
        this.handleAttributes(f);
        return p_34300_;
    }

    public static boolean getSpawnAsBabyOdds(Random p_34303_) {
        return p_34303_.nextFloat() < net.minecraftforge.common.ForgeConfig.SERVER.zombieBabyChance.get();
    }

    protected void handleAttributes(float p_34340_) {
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * (double)0.05F, AttributeModifier.Operation.ADDITION));
        double d0 = this.random.nextDouble() * 1.5D * (double)p_34340_;
        if (d0 > 1.0D) {
            this.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random zombie-spawn bonus", d0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (this.random.nextFloat() < p_34340_ * 0.05F) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 3.0D + 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0D : -0.45D;
    }

    public class sZombieAttackGoal extends MeleeAttackGoal {
        private final SummonedZombie zombie;
        private int raiseArmTicks;

        public sZombieAttackGoal(SummonedZombie p_26019_, double p_26020_, boolean p_26021_) {
            super(p_26019_, p_26020_, p_26021_);
            this.zombie = p_26019_;
        }

        public void start() {
            super.start();
            this.raiseArmTicks = 0;
        }

        public void stop() {
            super.stop();
            this.zombie.setAggressive(false);
        }

        public void tick() {
            super.tick();
            ++this.raiseArmTicks;
            if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
                this.zombie.setAggressive(true);
            } else {
                this.zombie.setAggressive(false);
            }

        }
    }
}