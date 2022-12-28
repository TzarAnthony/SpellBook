package com.tzaranthony.spellbook.core.spells;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.core.network.SoulBindS2CPacket;
import com.tzaranthony.spellbook.core.util.tags.SBEntityTags;
import com.tzaranthony.spellbook.registries.SBPackets;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

import static java.util.stream.Collectors.toSet;

// TODO: Remove references to the tags in favor of the BindingUtil.
public class Binding extends ProjectileSpell {
    protected static String BOUND_TO_ID = "SBEIDSoulBoundTo:";
    protected static String BOUND_TO_UUID = "SBUUIDSoulBoundTo:";
    protected static String IS_BOUND = "SBIsSoulBound";

    public Binding(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    @Override
    public void addSpellDataToProjectile(MagicProjectile magic) {
        magic.setSpell(this.getId());
        magic.setParticle(ParticleTypes.HEART);
    }

    @Override
    public boolean perform_spell(Entity usr, Entity tgt) {
        if (tgt instanceof Mob target && !(tgt.getType().is(SBEntityTags.PACIFY_IMMUNE)) && !(tgt instanceof Player)) {
            if (!isBound(target) && !target.isLeashed()) {
                bindEntity(usr, target);
            }
//            target.setLeashedTo(user, true);
            return true;
        }
        return false;
    }

    @Override
    public void playCustomSound(Level level, double x, double y, double z) {
        level.playSound((Player) null, x, y, z, SoundEvents.GLOW_INK_SAC_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    //TODO: create a knot like entity to bind to an alter
    public static void bindEntity(Entity binder, Mob bound) {
        if (bound.isPassenger()) {
            bound.stopRiding();
        }

        if (!bound.level.isClientSide()) {
            // TODO(TzarAnthony): Not sure what this does, but it probably needs to change now.
            SBPackets.sendToAllPlayers(new SoulBindS2CPacket(bound.getId(), IS_BOUND, BOUND_TO_UUID + binder.getStringUUID(), BOUND_TO_ID + binder.getId()));
        }

        bindEntity(binder, bound);
    }

    public static boolean isBound(LivingEntity bound) {
        return !BindingStore.boundEntities(bound).isEmpty();
    }

    public static Optional<Entity> isBoundBy(LivingEntity bound) {
        return BindingStore.boundPlayers(bound).stream().findFirst();
    }

    public static void unbindEntity(LivingEntity bound) {
        BindingStore.removeBinding(bound);
    }

    public static void tickSoulBind(LivingEntity le) {
        if (le instanceof Mob bound) {
            Optional<Entity> maybeBinder = isBoundBy(bound);
            if (maybeBinder.isEmpty()) {
                return;
            }
            Entity binder = maybeBinder.get();

            if (!bound.level.isClientSide()) {
                // TODO(TzarAnthony): Not sure what this does, but it probably needs to change now.
                SBPackets.sendToAllPlayers(new SoulBindS2CPacket(bound.getId(), IS_BOUND, BOUND_TO_UUID + binder.getStringUUID(), BOUND_TO_ID + binder.getId()));
            }

            if (binder.level == bound.level) {
                bound.restrictTo(binder.blockPosition(), 5);
                float f = bound.distanceTo(binder);
                if (bound instanceof TamableAnimal && ((TamableAnimal) bound).isInSittingPose()) {
                    if (f > 10.0F) {
                        unbindEntity(bound);
                    }
                    return;
                }

                if (f > 15.0F) {
                    unbindEntity(bound);
                    bound.goalSelector.disableControlFlag(Goal.Flag.MOVE);
                } else if (f > 6.0F) {
                    double d0 = (binder.getX() - bound.getX()) / (double) f;
                    double d1 = (binder.getY() - bound.getY()) / (double) f;
                    double d2 = (binder.getZ() - bound.getZ()) / (double) f;
                    bound.setDeltaMovement(bound.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.4D, d0), Math.copySign(d1 * d1 * 0.4D, d1), Math.copySign(d2 * d2 * 0.4D, d2)));
                } else {
                    bound.goalSelector.enableControlFlag(Goal.Flag.MOVE);
                    float f1 = 2.0F;
                    Vec3 vec3 = (new Vec3(binder.getX() - bound.getX(), binder.getY() - bound.getY(), binder.getZ() - bound.getZ())).normalize().scale((double) Math.max(f - 2.0F, 0.0F));
                    bound.getNavigation().moveTo(bound.getX() + vec3.x, bound.getY() + vec3.y, bound.getZ() + vec3.z, 1.0D);
                }
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
        double d3 = Mth.lerp((double) pTick, bound.xo, bound.getX()) + d1;
        double d4 = Mth.lerp((double) pTick, bound.yo, bound.getY()) + vec31.y;
        double d5 = Mth.lerp((double) pTick, bound.zo, bound.getZ()) + d2;
        pose.translate(d1, vec31.y, d2);
        float f = (float) (vec3.x - d3);
        float f1 = (float) (vec3.y - d4);
        float f2 = (float) (vec3.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = buff.getBuffer(RenderType.leash());
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
    private static void addVertexPair(VertexConsumer consumer, Matrix4f m4, float x, float y, float z, int light, int light1, int light2, int light3, float xthicc, float ythicc, float dz, float dx, int segment, boolean colorSelector) {
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
        consumer.vertex(m4, f5 - dz, f6 + ythicc, f7 + dx).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        consumer.vertex(m4, f5 + dz, f6 + xthicc - ythicc, f7 - dx).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    /**
     * Binding storage for the {@link Binding} spell.
     */
    private static class BindingStore {
        private static Map<UUID, Set<UUID>> playerBindings = new HashMap<>();
        private static Map<UUID, Set<UUID>> entityBindings = new HashMap<>();
        private static Map<UUID, Entity> entities = new HashMap<>();

        /**
         * Adds a binding to the player {@link Entity} for the entity.
         *
         * @param player the player to bind the entity.
         * @param entity the entity to bind to the player.
         */
        public static void addBinding(Entity player, Entity entity) {
            if (isBindable(entity)) {
                entities.put(player.getUUID(), player);
                entities.put(entity.getUUID(), entity);
                bindPlayerToEntity(player.getUUID(), entity.getUUID());
                bindEntityToPlayer(player.getUUID(), entity.getUUID());
            }
        }

        private static boolean isBindable(Entity entity) {
            // Players should probably not be bindable.
            if (entity instanceof Player) {
                return false;
            }
            return entity instanceof LivingEntity;
        }

        private static void bindEntityToPlayer(UUID playerUUID, UUID entityUUID) {
            if (playerBindings.containsKey(playerUUID)) {
                playerBindings.get(playerUUID).add(entityUUID);
                return;
            }
            playerBindings.put(playerUUID, new HashSet<>(List.of(new UUID[]{entityUUID})));
        }

        private static void bindPlayerToEntity(UUID playerUUID, UUID entityUUID) {
            if (entityBindings.containsKey(entityUUID)) {
                entityBindings.get(entityUUID).add(playerUUID);
                return;
            }
            playerBindings.put(entityUUID, new HashSet<>(List.of(new UUID[]{playerUUID})));
        }

        /**
         * Removes a binding from the player {@link Entity} for the entity.
         *
         * @param entity the entity to remove the binding for.
         */
        public static void removeBinding(Entity entity) {
            UUID entityUUID = entity.getUUID();
            Set<UUID> bindedPlayers = playerBindings.get(entityUUID);
        }

        /**
         * Gets all the players bound to an entity.
         *
         * @param entity the {@link Entity} to get the bound players.
         * @return a set of all player {@link Entity Entities} bound to the entity.
         */
        public static Set<Entity> boundPlayers(Entity entity) {
            UUID entityUUID = entity.getUUID();
            if (playerBindings.containsKey(entityUUID)) {
                return entityBindings.get(entityUUID).stream().map(boundUUID -> entities.get(boundUUID)).collect(toSet());
            }
            return new HashSet<>();
        }

        /**
         * Gets all the bound {@link Entity Player} to the player.
         *
         * @param player the player {@link Entity} to get the entities that are bound to it.
         * @return the set of all {@link Entity Entities} bound to the player
         */
        public static Set<Entity> boundEntities(Entity player) {
            UUID playerUUID = player.getUUID();
            if (playerBindings.containsKey(playerUUID)) {
                return entityBindings.get(playerUUID).stream().map(boundUUID -> entities.get(boundUUID)).collect(toSet());
            }
            return new HashSet<>();
        }
    }
}