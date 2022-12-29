package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.client.entityRender.renders.alchemical.*;
import com.tzaranthony.spellbook.client.entityRender.renders.arrows.*;
import com.tzaranthony.spellbook.client.entityRender.renders.ghosts.*;
import com.tzaranthony.spellbook.client.entityRender.renders.ghosts.boss.GhostArcherRender;
import com.tzaranthony.spellbook.client.entityRender.renders.ghosts.boss.GhostKnightRender;
import com.tzaranthony.spellbook.client.entityRender.renders.ghosts.boss.GhostMageRender;
import com.tzaranthony.spellbook.client.entityRender.renders.other.CursedPaintingRender;
import com.tzaranthony.spellbook.client.entityRender.renders.other.FrostWaveRender;
import com.tzaranthony.spellbook.client.entityRender.renders.other.MagicProjectileRender;
import com.tzaranthony.spellbook.client.entityRender.renders.other.ShatteringCrystalRender;
import com.tzaranthony.spellbook.client.entityRender.renders.vampire.boss.HigherVampireBatRender;
import com.tzaranthony.spellbook.client.entityRender.renders.vampire.boss.HigherVampirePersonRender;
import com.tzaranthony.spellbook.client.entityRender.renders.vampire.LesserVampireRender;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SBEntityRender {
    public static void renderEntities() {
        // Friendly
        EntityRenderers.register(SBEntities.SUMMONED_VEX.get(), SummonedVexRender::new);
        EntityRenderers.register(SBEntities.SUMMONED_ZOMBIE.get(), SummonedZombieRender::new);
        EntityRenderers.register(SBEntities.SUMMONED_SKELLY.get(), SummonedSkeletonRender::new);
        EntityRenderers.register(SBEntities.SUMMONED_WITHER_SKELLY.get(), SummonedWitherSkeletonRender::new);
        // Hostile
        EntityRenderers.register(SBEntities.SHADE.get(), ShadeRender::new);
        EntityRenderers.register(SBEntities.POLTERGEIST.get(), PoltergeistRender::new);
        EntityRenderers.register(SBEntities.BANSHEE.get(), BansheeRender::new);
        EntityRenderers.register(SBEntities.WRAITH.get(), WraithRender::new);
        EntityRenderers.register(SBEntities.YUREI.get(), YureiRender::new);
        EntityRenderers.register(SBEntities.GHOST_HORSE.get(), GhostHorseRender::new);
        //TODO: create models for Ghost Commanders
        EntityRenderers.register(SBEntities.GHOST_MAGE.get(), GhostMageRender::new);
        EntityRenderers.register(SBEntities.GHOST_ARCHER.get(), GhostArcherRender::new);
        EntityRenderers.register(SBEntities.GHOST_KNIGHT.get(), GhostKnightRender::new);
        EntityRenderers.register(SBEntities.HIGHVAMP1.get(), HigherVampirePersonRender::new);
        EntityRenderers.register(SBEntities.HIGHVAMP1BAT.get(), HigherVampireBatRender::new);
        EntityRenderers.register(SBEntities.LOWVAMP.get(), LesserVampireRender::new);
        EntityRenderers.register(SBEntities.FAKE_ZOMBIE.get(), ZombieIllusionRender::new);
        EntityRenderers.register(SBEntities.FAKE_SKELLY.get(), SkeletonIllusionRender::new);
        EntityRenderers.register(SBEntities.NECROTIC_SPIDER.get(), NecroticSpiderRender::new);
        // Arrows
        EntityRenderers.register(SBEntities.SILVER_ARROW.get(), SilverArrowRender::new);
        EntityRenderers.register(SBEntities.GHOSTLY_ARROW.get(), GhostlyArrowRender::new);
        EntityRenderers.register(SBEntities.CRYSTAL_ARROW.get(), CrystalArrowRender::new);
        EntityRenderers.register(SBEntities.SHATTER_CRYSTAL.get(), ShatteringCrystalRender::new);
        EntityRenderers.register(SBEntities.CRYSTAL_SHARD.get(), CrystalShardRender::new);
        EntityRenderers.register(SBEntities.FIRE_ARROW.get(), FireArrowRender::new);
        EntityRenderers.register(SBEntities.WATER_ARROW.get(), WaterArrowRender::new);
        EntityRenderers.register(SBEntities.AIR_ARROW.get(), AirArrowRender::new);
        EntityRenderers.register(SBEntities.EARTH_ARROW.get(), EarthArrowRender::new);
        // Magic
        EntityRenderers.register(SBEntities.SMALL_MAGIC_PROJECTILE.get(), MagicProjectileRender::new);
        EntityRenderers.register(SBEntities.MEDIUM_MAGIC_PROJECTILE.get(), MagicProjectileRender::new);
        EntityRenderers.register(SBEntities.LARGE_MAGIC_PROJECTILE.get(), MagicProjectileRender::new);
        EntityRenderers.register(SBEntities.FROST_WAVE_PROJECTILE.get(), FrostWaveRender::new);
        // Other
        EntityRenderers.register(SBEntities.FIRE_WALL.get(), NoopRenderer::new);
        EntityRenderers.register(SBEntities.CURSED_PAINTING.get(), CursedPaintingRender::new);
        EntityRenderers.register(SBEntities.PHOENIX_ASHES.get(), ItemEntityRenderer::new);
    }
}