package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.client.entityRender.renders.*;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
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
        EntityRenderers.register(SBEntities.HIGHVAMP1.get(), HigherVampirePersonRender::new);
        EntityRenderers.register(SBEntities.HIGHVAMP1BAT.get(), HigherVampireBatRender::new);
        EntityRenderers.register(SBEntities.LOWVAMP.get(), LesserVampireRender::new);
        EntityRenderers.register(SBEntities.FAKE_ZOMBIE.get(), ZombieIllusionRender::new);
        EntityRenderers.register(SBEntities.FAKE_SKELLY.get(), SkeletonIllusionRender::new);
        EntityRenderers.register(SBEntities.NECROTIC_SPIDER.get(), NecroticSpiderRender::new);
        // Other
        EntityRenderers.register(SBEntities.PHOENIX_ASHES.get(), ItemEntityRenderer::new);
        EntityRenderers.register(SBEntities.SILVER_ARROW.get(), SilverArrowRender::new);
        EntityRenderers.register(SBEntities.GHOSTLY_ARROW.get(), GhostlyArrowRender::new);
        EntityRenderers.register(SBEntities.CRYSTAL_ARROW.get(), CrystalArrowRender::new);
        EntityRenderers.register(SBEntities.SHATTER_CRYSTAL.get(), ShatteringCrystalRender::new);
        EntityRenderers.register(SBEntities.CRYSTAL_SHARD.get(), CrystalShardRender::new);
        EntityRenderers.register(SBEntities.FIRE_ARROW.get(), FireArrowRender::new);
        EntityRenderers.register(SBEntities.WATER_ARROW.get(), WaterArrowRender::new);
        EntityRenderers.register(SBEntities.AIR_ARROW.get(), AirArrowRender::new);
        EntityRenderers.register(SBEntities.EARTH_ARROW.get(), EarthArrowRender::new);
        EntityRenderers.register(SBEntities.SMALL_MAGIC_PROJECTILE.get(), MagicProjectileRender::new);
        EntityRenderers.register(SBEntities.MEDIUM_MAGIC_PROJECTILE.get(), MagicProjectileRender::new);
        EntityRenderers.register(SBEntities.LARGE_MAGIC_PROJECTILE.get(), MagicProjectileRender::new);
    }
}