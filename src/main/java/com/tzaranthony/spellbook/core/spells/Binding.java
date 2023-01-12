package com.tzaranthony.spellbook.core.spells;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.tzaranthony.spellbook.client.SBRenderTypes;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.network.SoulBindS2CPacket;
import com.tzaranthony.spellbook.core.util.tags.SBEntityTags;
import com.tzaranthony.spellbook.registries.SBPackets;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Binding extends ProjectileSpell {
    public Binding(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.HEART);
        magic.setLifetime(15);
    }

    @Override
    public boolean perform_spell(Entity usr, Entity tgt) {
        if (tgt instanceof Mob target && !(tgt.getType().is(SBEntityTags.PACIFY_IMMUNE))) {
            if (!isBound(target) && !target.isLeashed()) {
                bindEntity(usr, target);
            }
            return true;
        }
        return false;
    }

    // TODO: create a knot like entity to bind to an alter or something
    public static void bindEntity(Entity binder, Mob bound) {
        if (bound.isPassenger()) {
            bound.stopRiding();
        }

        BindingUtils.addBinding(binder, bound);
    }

    public static boolean isBound(LivingEntity bound) {
        return BindingUtils.playersBoundTo(bound).isPresent();
    }

    public static Optional<Entity> isBoundBy(LivingEntity bound) {
        return BindingUtils.playersBoundTo(bound).stream().findFirst();
    }

    public static void unbindEntity(LivingEntity bound) {
        BindingUtils.removeBinding(bound);
    }

    public static void tickSoulBind(LivingEntity le) {
        if (le instanceof Mob bound) {
            Optional<Entity> maybeBinder = isBoundBy(bound);
            if (maybeBinder.isEmpty()) {
                return;
            }
            Entity binder = maybeBinder.get();
            bound.restrictTo(binder.blockPosition(), 5);
            float f = bound.distanceTo(binder);
            if (bound instanceof TamableAnimal && ((TamableAnimal) bound).isInSittingPose()) {
                if (f > 10.0F) {
                    unbindEntity(bound);
                }
                return;
            }

            if (f > 20.0F) {
                unbindEntity(bound);
                bound.goalSelector.disableControlFlag(Goal.Flag.MOVE);
                if (!le.level.isClientSide()) {
                    SBPackets.sendToAllPlayers(new SoulBindS2CPacket(le.getId(), binder.getUUID(), false));
                }
            } else if (f > 6.0F) {
                double d0 = (binder.getX() - bound.getX()) / (double) f;
                double d1 = (binder.getY() - bound.getY()) / (double) f;
                double d2 = (binder.getZ() - bound.getZ()) / (double) f;
                bound.setDeltaMovement(bound.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.4D, d0), Math.copySign(d1 * d1 * 0.4D, d1), Math.copySign(d2 * d2 * 0.4D, d2)));
            } else {
                bound.goalSelector.enableControlFlag(Goal.Flag.MOVE);
                Vec3 vec3 = (new Vec3(binder.getX() - bound.getX(), binder.getY() - bound.getY(), binder.getZ() - bound.getZ())).normalize().scale(Math.max(f - 2.0F, 0.0F));
                bound.getNavigation().moveTo(bound.getX() + vec3.x, bound.getY() + vec3.y, bound.getZ() + vec3.z, 1.0D);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderSoulBind(Mob bound, float pTick, PoseStack pose, MultiBufferSource buff, LivingEntity binder) {
        pose.pushPose();
        Vec3 vec3 = binder.getRopeHoldPosition(pTick);
        double d0 = (double) (Mth.lerp(pTick, bound.yBodyRot, bound.yBodyRotO) * ((float) Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = bound.getLeashOffset();
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp(pTick, bound.xo, bound.getX()) + d1;
        double d4 = Mth.lerp(pTick, bound.yo, bound.getY()) + vec31.y;
        double d5 = Mth.lerp(pTick, bound.zo, bound.getZ()) + d2;
        pose.translate(d3, d4, d5);
        float f = (float) (vec3.x - d3);
        float f1 = (float) (vec3.y - d4);
        float f2 = (float) (vec3.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = buff.getBuffer(SBRenderTypes.getTransparentLead());
        Matrix4f matrix4f = pose.last().pose();
        float f4 = Mth.fastInvSqrt(f * f + f2 * f2) * f3 / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = new BlockPos(bound.getEyePosition(pTick));
        BlockPos blockpos1 = new BlockPos(binder.getEyePosition(pTick));
        int i = getBlockLightLevel(bound, blockpos);
        int j = getBlockLightLevel(binder, blockpos1);
        int k = bound.level.getBrightness(LightLayer.SKY, blockpos);
        int l = bound.level.getBrightness(LightLayer.SKY, blockpos1);
        for (int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, f3, f3, f5, f6, i1, false);
        }
        for (int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, f3, 0.0F, f5, f6, j1, true);
        }
        pose.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    protected static int getBlockLightLevel(Entity entity, BlockPos pos) {
        return entity.isOnFire() ? 15 : entity.level.getBrightness(LightLayer.BLOCK, pos);
    }

    @OnlyIn(Dist.CLIENT)
    private static void addVertexPair(VertexConsumer consumer, Matrix4f m4, float x, float y, float z, int light, int light1, int light2, int light3, float xThicc, float yThicc, float dz, float dx, int segment, boolean colorSelector) {
        float f = (float) segment / 24.0F;
        int i = (int) Mth.lerp(f, (float) light, (float) light1);
        int j = (int) Mth.lerp(f, (float) light2, (float) light3);
        int k = LightTexture.pack(i, j);
        float f1 = segment % 2 == (colorSelector ? 1 : 0) ? 0.7F : 1.0F;
        float f2 = (180.0F / 255.0F) * f1;
        float f3 = (60.0F / 255.0F) * f1;
        float f4 = (200.0F / 255.0F) * f1;
        float f5 = x * f;
        float f6 = y > 0.0F ? y * f * f : y - y * (1.0F - f) * (1.0F - f);
        float f7 = z * f;
        consumer.vertex(m4, f5 - dz, f6 + yThicc, f7 + dx).color(f2, f3, f4, 0.5F).uv2(k).endVertex();
        consumer.vertex(m4, f5 + dz, f6 + xThicc - yThicc, f7 - dx).color(f2, f3, f4, 0.5F).uv2(k).endVertex();
    }

    /**
     * Binding storage for the {@link Binding} spell.
     */
    private static class BindingUtils {
        protected static final String BOUND_TAG_KEY = "SBUUIDSoulBoundTo";

        private static final Map<UUID, Entity> entities = new HashMap<>();

        /**
         * Adds a binding to the player {@link Entity} for the entity.
         *
         * @param user   the player to bind the entity.
         * @param target the entity to bind to the player.
         */
        public static void addBinding(Entity user, Entity target) {
            entities.put(user.getUUID(), user);
            entities.put(target.getUUID(), target);
            bindPlayerToEntity(user, target);
            bindEntityToPlayer(user, target);
        }

        private static void bindEntityToPlayer(Entity player, Entity entity) {
            CompoundTag tag = player.getPersistentData();
            tag.putBoolean(createBindedTag(entity.getUUID()), true);
        }

        private static String createBindedTag(UUID uuid) {
            return String.format("%s:%s", BOUND_TAG_KEY, uuid);
        }

        private static void bindPlayerToEntity(Entity player, Entity entity) {
            CompoundTag tag = entity.getPersistentData();
            tag.putUUID(BOUND_TAG_KEY, player.getUUID());
            if (!player.level.isClientSide()) {
                SBPackets.sendToAllPlayers(new SoulBindS2CPacket(entity.getId(), player.getUUID(), true));
            }
        }

        /**
         * Removes all bindings from all player {@link Entity Entities} for the specified entity.
         *
         * @param target the entity to remove the binding for.
         */
        public static void removeBinding(Entity target) {
            CompoundTag tag = target.getPersistentData();
            UUID userUUID = tag.getUUID(BOUND_TAG_KEY);
            Entity user = entities.get(userUUID);
            CompoundTag userData = user.getPersistentData();
            userData.remove(createBindedTag(target.getUUID()));
            tag.remove(BOUND_TAG_KEY);
            if (!target.level.isClientSide()) {
                SBPackets.sendToAllPlayers(new SoulBindS2CPacket(target.getId(), user.getUUID(), false));
            }
        }

        /**
         * Gets all the players bound to an entity.
         *
         * @param target the {@link Entity} to get the bound players.
         * @return a set of all player {@link Entity Entities} bound to the entity.
         */
        public static Optional<Entity> playersBoundTo(Entity target) {
            CompoundTag targetData = target.getPersistentData();
            if (!targetData.contains(BOUND_TAG_KEY)) {
                return Optional.empty();
            }
            UUID playerUUID = targetData.getUUID(BOUND_TAG_KEY);
            return Optional.ofNullable(entities.get(playerUUID));
        }
    }
}