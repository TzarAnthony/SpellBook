package com.tzaranthony.spellbook.core.entities.other;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import com.tzaranthony.spellbook.core.spells.ProjectileSpell;
import com.tzaranthony.spellbook.registries.SBSpellRegistry;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class MagicProjectile extends Projectile {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Integer> DATA_SPELL = SynchedEntityData.defineId(MagicProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(MagicProjectile.class, EntityDataSerializers.PARTICLE);
    private int life;
    private int lifetime = 50 + this.random.nextInt(7);

    public MagicProjectile(EntityType<? extends MagicProjectile> projectile, Level level) {
        super(projectile, level);
    }

    public int getSpell() {
        return this.getEntityData().get(DATA_SPELL);
    }

    public void setSpell(int id) {
        this.getEntityData().set(DATA_SPELL, id);
    }

    public ParticleOptions getParticle() {
        return this.getEntityData().get(DATA_PARTICLE);
    }

    public void setParticle(ParticleOptions particle) {
        this.getEntityData().set(DATA_PARTICLE, particle);
    }

    protected void defineSynchedData() {
        this.getEntityData().define(DATA_SPELL, 0);
        this.getEntityData().define(DATA_PARTICLE, ParticleTypes.ENTITY_EFFECT);
    }

    public void initializeProjectile(Entity entity, double x, double y, double z) {
        this.setOwner(entity);
        this.setPos(x, y, z);
        this.setDeltaMovement(this.random.nextGaussian() * 0.001D, 0.05D, this.random.nextGaussian() * 0.001D);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dist) {
        return dist < 4096.0D;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return super.shouldRender(x, y, z);
    }

    @Override
    public void tick() {
        ProjectileSpell spell = getSpellById(getSpell());
        this.noPhysics = spell.spellIgnoresBlocks();

        super.tick();

        Vec3 vec33 = this.getDeltaMovement();
        this.move(MoverType.SELF, vec33);
        this.setDeltaMovement(vec33);

        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (!spell.spellIgnoresBlocksAndEntities()) {
            this.onHit(hitresult);
        }

        this.updateRotation();
        if (this.life == 0 && !this.isSilent()) {
            spell.playCustomSound(this.level, this.getX(), this.getY(), this.getZ());
        }

        ++this.life;
        if (this.level.isClientSide && this.life % 2 < 2) {
            this.level.addParticle(getParticle(), this.getX(), this.getY() + this.getBbHeight()/2.0D, this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
        }

        if (!this.level.isClientSide && this.life > this.lifetime) {
            this.fizzle();
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        Entity user = this.getOwner();
        getSpellById(getSpell()).perform_spell(user, target);
        if (!this.noPhysics) {
            this.level.broadcastEntityEvent(this, (byte)17);
            this.discard();
        }
    }

    protected void onHitBlock(BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        super.onHitBlock(result);
        getSpellById(getSpell()).perform_spell(this.getOwner(), this.level, pos);
        if (!this.noPhysics) {
            this.level.broadcastEntityEvent(this, (byte)17);
            this.discard();
        }
    }

    protected ProjectileSpell getSpellById(int id) {
        if (id == 9) {
            return (ProjectileSpell) SBSpellRegistry.FIREWALL;
        } else if (id == 10) {
            return (ProjectileSpell) SBSpellRegistry.FROST_WAVE;
        } else if (id == 12) {
            return (ProjectileSpell) SBSpellRegistry.OUTWARD_WINDS;
        } else if (id == 15) {
            return (ProjectileSpell) SBSpellRegistry.EXPLOSION;
        } else if (id == 20) {
            return (ProjectileSpell) SBSpellRegistry.INSATIABLE_LETHARGY;
        } else if (id == 21) {
            return (ProjectileSpell) SBSpellRegistry.SCREAM;
        } else if (id == 24) {
            return (ProjectileSpell) SBSpellRegistry.DARK_SNARE;
        } else if (id == 25) {
            return (ProjectileSpell) SBSpellRegistry.LIFE_STEAL;
        } else if (id == 26) {
            return (ProjectileSpell) SBSpellRegistry.SOULBIND;
        } else if (id == 28) {
            return (ProjectileSpell) SBSpellRegistry.RIFT_OF_DARKNESS;
        } else if (id == 34) {
            return (ProjectileSpell) SBSpellRegistry.IGNITE;
        } else {
            return (ProjectileSpell) SBSpellRegistry.TIME_SPELL;
        }
    }

    private void fizzle() {
        getSpellById(getSpell()).finishSpell(this.getOwner(), this.level, this.blockPosition());
        this.level.broadcastEntityEvent(this, (byte)17);
        this.discard();
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        this.life = tag.getInt("Life");
        this.lifetime = tag.getInt("MaxLife");
        if (tag.contains("Spell", 90)) {
            this.setSpell(tag.getInt("Spell"));
        }
        if (tag.contains("Particle", 8)) {
            try {
                this.setParticle(ParticleArgument.readParticle(new StringReader(tag.getString("Particle"))));
            } catch (CommandSyntaxException commandsyntaxexception) {
                LOGGER.warn("Couldn't load custom particle {}", tag.getString("Particle"), commandsyntaxexception);
            }
        }
        if (tag.hasUUID("Owner")) {
            this.setOwner(this.level.getPlayerByUUID(tag.getUUID("Owner")));
        }
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        tag.putInt("Life", this.life);
        tag.putInt("MaxLife", this.lifetime);
        tag.putInt("Spell", this.getSpell());
        tag.putString("Particle", this.getParticle().writeToString());
        if (this.getOwner() != null) {
            tag.putUUID("Owner", this.getOwner().getUUID());
        }
    }

    public boolean isAttackable() {
        return false;
    }
}