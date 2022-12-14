package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.entities.arrows.*;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedSkeleton;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedVex;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedWitherSkeleton;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedZombie;
import com.tzaranthony.spellbook.core.entities.hostile.alchemical.NecroticSpider;
import com.tzaranthony.spellbook.core.entities.hostile.alchemical.SkeletonIllusion;
import com.tzaranthony.spellbook.core.entities.hostile.alchemical.ZombieIllusion;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.*;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostArcher;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostHorse;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostKnight;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.boss.GhostMage;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.LesserVampire;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.boss.HigherVampireBat;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.boss.HigherVampirePerson;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.boss.HigherVampirePhase2;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.boss.HigherVampirePhase3;
import com.tzaranthony.spellbook.core.entities.neutral.Phoenix;
import com.tzaranthony.spellbook.core.entities.other.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = SpellBook.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SBEntities {
    public static final DeferredRegister<EntityType<?>> reg = DeferredRegister.create(ForgeRegistries.ENTITIES, SpellBook.MOD_ID);

    // Friendly
    public static final RegistryObject<EntityType<SummonedVex>> SUMMONED_VEX = reg.register("summoned_vex", () ->
            EntityType.Builder.of(SummonedVex::new, MobCategory.CREATURE).fireImmune().sized(0.4F, 0.8F).clientTrackingRange(8).build("summoned_vex")
    );
    public static final RegistryObject<EntityType<SummonedZombie>> SUMMONED_ZOMBIE = reg.register("summoned_zombie", () ->
            EntityType.Builder.of(SummonedZombie::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(8).build("summoned_zombie")
    );
    public static final RegistryObject<EntityType<SummonedSkeleton>> SUMMONED_SKELLY = reg.register("summoned_skelly", () ->
            EntityType.Builder.of(SummonedSkeleton::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(8).build("summoned_skelly")
    );
    public static final RegistryObject<EntityType<SummonedWitherSkeleton>> SUMMONED_WITHER_SKELLY = reg.register("summoned_wither_skelly", () ->
            EntityType.Builder.of(SummonedWitherSkeleton::new, MobCategory.CREATURE).fireImmune().immuneTo(Blocks.WITHER_ROSE).sized(0.7F, 2.4F).clientTrackingRange(8).build("summoned_wither_skelly")
    );

    //Hostile
    public static final RegistryObject<EntityType<NecroticSpider>> NECROTIC_SPIDER = reg.register("necrotic_spider", () ->
            EntityType.Builder.of(NecroticSpider::new, MobCategory.MONSTER).sized(2.0F, 1.3F).clientTrackingRange(8).build("necrotic_spider")
    );

    public static final RegistryObject<EntityType<Shade>> SHADE = reg.register("shade", () ->
            EntityType.Builder.of(Shade::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("shade")
    );
    public static final RegistryObject<EntityType<Poltergeist>> POLTERGEIST = reg.register("poltergeist", () ->
            EntityType.Builder.of(Poltergeist::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("poltergeist")
    );
    public static final RegistryObject<EntityType<Wraith>> WRAITH = reg.register("wraith", () ->
            EntityType.Builder.of(Wraith::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.95F).clientTrackingRange(8).build("wraith")
    );
    public static final RegistryObject<EntityType<Banshee>> BANSHEE = reg.register("banshee", () ->
            EntityType.Builder.of(Banshee::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("banshee")
    );
    public static final RegistryObject<EntityType<Yurei>> YUREI = reg.register("yurei", () ->
            EntityType.Builder.of(Yurei::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("yurei")
    );
    public static final RegistryObject<EntityType<GhostHorse>> GHOST_HORSE = reg.register("ghost_horse", () ->
            EntityType.Builder.of(GhostHorse::new, MobCategory.CREATURE).fireImmune().sized(1.3964844F, 1.6F).clientTrackingRange(10).build("ghost_horse")
    );
    public static final RegistryObject<EntityType<GhostMage>> GHOST_MAGE = reg.register("ghost_mage", () ->
            EntityType.Builder.of(GhostMage::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(10).build("ghost_mage")
    );
    public static final RegistryObject<EntityType<GhostArcher>> GHOST_ARCHER = reg.register("ghost_archer", () ->
            EntityType.Builder.of(GhostArcher::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(10).build("ghost_archer")
    );
    public static final RegistryObject<EntityType<GhostKnight>> GHOST_KNIGHT = reg.register("ghost_knight", () ->
            EntityType.Builder.of(GhostKnight::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(10).build("ghost_knight")
    );

    public static final RegistryObject<EntityType<LesserVampire>> LOW_VAMP = reg.register("lesser_vampire", () ->
            EntityType.Builder.of(LesserVampire::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("lesser_vampire")
    );
    public static final RegistryObject<EntityType<HigherVampirePerson>> HIGH_VAMP1 = reg.register("higher_vampire_human", () ->
            EntityType.Builder.of(HigherVampirePerson::new, MobCategory.MONSTER).immuneTo(Blocks.POWDER_SNOW).sized(0.6F, 1.95F).clientTrackingRange(8).build("higher_vampire_human")
    );
    public static final RegistryObject<EntityType<HigherVampireBat>> HIGH_VAMP1_BAT = reg.register("higher_vampire_bat", () ->
            EntityType.Builder.of(HigherVampireBat::new, MobCategory.MONSTER).immuneTo(Blocks.POWDER_SNOW).sized(0.5F, 0.9F).clientTrackingRange(8).build("higher_vampire_bat")
    );
    public static final RegistryObject<EntityType<HigherVampirePhase2>> HIGH_VAMP2 = reg.register("higher_vampire_monster", () ->
            EntityType.Builder.of(HigherVampirePhase2::new, MobCategory.MONSTER).immuneTo(Blocks.POWDER_SNOW).sized(1.4F, 2.7F).clientTrackingRange(8).build("higher_vampire_monster")
    );
    public static final RegistryObject<EntityType<HigherVampirePhase3>> HIGH_VAMP3 = reg.register("higher_vampire_mist", () ->
            EntityType.Builder.of(HigherVampirePhase3::new, MobCategory.MONSTER).immuneTo(Blocks.POWDER_SNOW).sized(6.0F, 2.0F).clientTrackingRange(8).build("higher_vampire_mist")
    );

    public static final RegistryObject<EntityType<ZombieIllusion>> FAKE_ZOMBIE = reg.register("fake_zombie", () ->
            EntityType.Builder.of(ZombieIllusion::new, MobCategory.MONSTER).fireImmune().immuneTo(Blocks.POWDER_SNOW).sized(0.6F, 1.95F).clientTrackingRange(8).build("fake_zombie")
    );
    public static final RegistryObject<EntityType<SkeletonIllusion>> FAKE_SKELLY = reg.register("fake_skelly", () ->
            EntityType.Builder.of(SkeletonIllusion::new, MobCategory.MONSTER).fireImmune().immuneTo(Blocks.POWDER_SNOW).sized(0.6F, 1.95F).clientTrackingRange(8).build("fake_skelly")
    );

    // Arrows
    public static final RegistryObject<EntityType<SilverArrow>> SILVER_ARROW = reg.register("silver_arrow", () ->
            EntityType.Builder.<SilverArrow>of(SilverArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("silver_arrow")
    );
    public static final RegistryObject<EntityType<GhostlyArrow>> GHOSTLY_ARROW = reg.register("ghostly_arrow", () ->
            EntityType.Builder.<GhostlyArrow>of(GhostlyArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("ghostly_arrow")
    );
    public static final RegistryObject<EntityType<CrystalArrow>> CRYSTAL_ARROW = reg.register("crystal_arrow", () ->
            EntityType.Builder.<CrystalArrow>of(CrystalArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("crystal_arrow")
    );
    public static final RegistryObject<EntityType<ShatteringCrystal>> SHATTER_CRYSTAL = reg.register("crystal_shatter", () ->
            EntityType.Builder.of(ShatteringCrystal::new, MobCategory.MISC).sized(0.8F, 1.6F).clientTrackingRange(6).updateInterval(2).build("crystal_shatter")
    );
    public static final RegistryObject<EntityType<CrystalShard>> CRYSTAL_SHARD = reg.register("crystal_shard", () ->
            EntityType.Builder.of(CrystalShard::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("crystal_shard")
    );
    public static final RegistryObject<EntityType<EffectCarryingArrow>> FIRE_ARROW = reg.register("fire_arrow", () ->
            EntityType.Builder.<EffectCarryingArrow>of(EffectCarryingArrow::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("fire_arrow")
    );
    public static final RegistryObject<EntityType<EffectCarryingArrow>> EARTH_ARROW = reg.register("earth_arrow", () ->
            EntityType.Builder.<EffectCarryingArrow>of(EffectCarryingArrow::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("earth_arrow")
    );
    public static final RegistryObject<EntityType<EffectCarryingArrow>> WATER_ARROW = reg.register("water_arrow", () ->
            EntityType.Builder.<EffectCarryingArrow>of(EffectCarryingArrow::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("water_arrow")
    );
    public static final RegistryObject<EntityType<EffectCarryingArrow>> AIR_ARROW = reg.register("air_arrow", () ->
            EntityType.Builder.<EffectCarryingArrow>of(EffectCarryingArrow::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("air_arrow")
    );
    public static final RegistryObject<EntityType<ThrownTool>> THROWN_TOOL = reg.register("thrown_tool", () ->
            EntityType.Builder.<ThrownTool>of(ThrownTool::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("thrown_tool")
    );

    // Magic
    public static final RegistryObject<EntityType<MagicProjectile>> SMALL_MAGIC_PROJECTILE = reg.register("small_magic_projectile", () ->
            EntityType.Builder.of(MagicProjectile::new, MobCategory.MISC).fireImmune().sized(0.25F, 0.25F).build("small_magic_projectile")
    );
    public static final RegistryObject<EntityType<MagicProjectile>> MEDIUM_MAGIC_PROJECTILE = reg.register("medium_magic_projectile", () ->
            EntityType.Builder.of(MagicProjectile::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F).build("medium_magic_projectile")
    );
    public static final RegistryObject<EntityType<MagicProjectile>> LARGE_MAGIC_PROJECTILE = reg.register("large_magic_projectile", () ->
            EntityType.Builder.of(MagicProjectile::new, MobCategory.MISC).fireImmune().sized(1.5F, 1.5F).build("large_magic_projectile")
    );
    public static final RegistryObject<EntityType<MagicProjectile>> FROST_WAVE_PROJECTILE = reg.register("frost_wave_projectile", () ->
            EntityType.Builder.of(MagicProjectile::new, MobCategory.MISC).fireImmune().sized(2.0F, 0.2F).build("frost_wave_projectile")
    );

    // Other
    public static final RegistryObject<EntityType<AreaFireCloud>> FIRE_WALL = reg.register("fire_wall", () ->
            EntityType.Builder.of(AreaFireCloud::new, MobCategory.MISC).fireImmune().sized(0.5F, 6.0F).updateInterval(Integer.MAX_VALUE).build("fire_wall")
    );
    public static final RegistryObject<EntityType<CursedPainting>> CURSED_PAINTING = reg.register("cursed_painting", () ->
            EntityType.Builder.of(CursedPainting::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).build("cursed_painting")
    );
    public static final RegistryObject<EntityType<PhoenixAshesEntity>> PHOENIX_ASHES = reg.register("phoenix_ashes", () ->
            EntityType.Builder.<PhoenixAshesEntity>of(PhoenixAshesEntity::new, MobCategory.MISC).fireImmune().sized(0.25F, 0.25F).build("phoenix_ashes")
    );
    public static final RegistryObject<EntityType<Phoenix>> PHOENIX = reg.register("phoenix", () ->
            EntityType.Builder.of(Phoenix::new, MobCategory.CREATURE).fireImmune().sized(0.25F, 0.25F).build("phoenix")
    );


    @SubscribeEvent
    public static void bakeAttributes(EntityAttributeCreationEvent creationEvent) {
        creationEvent.put(SUMMONED_VEX.get(), SummonedVex.createAttributes().build());
        creationEvent.put(SUMMONED_ZOMBIE.get(), SummonedSkeleton.createAttributes().build());
        creationEvent.put(SUMMONED_SKELLY.get(), SummonedSkeleton.createAttributes().build());
        creationEvent.put(SUMMONED_WITHER_SKELLY.get(), SummonedWitherSkeleton.createAttributes().build());
        creationEvent.put(SHADE.get(), Shade.createAttributes().build());
        creationEvent.put(POLTERGEIST.get(), Poltergeist.createAttributes().build());
        creationEvent.put(WRAITH.get(), Wraith.createAttributes().build());
        creationEvent.put(BANSHEE.get(), Banshee.createAttributes().build());
        creationEvent.put(YUREI.get(), Yurei.createAttributes().build());
        creationEvent.put(GHOST_HORSE.get(), GhostHorse.createAttributes().build());
        creationEvent.put(GHOST_MAGE.get(), GhostMage.createAttributes().build());
        creationEvent.put(GHOST_ARCHER.get(), GhostArcher.createAttributes().build());
        creationEvent.put(GHOST_KNIGHT.get(), GhostKnight.createAttributes().build());
        creationEvent.put(LOW_VAMP.get(), LesserVampire.createAttributes().build());
        creationEvent.put(HIGH_VAMP1.get(), HigherVampirePerson.createAttributes().build());
        creationEvent.put(HIGH_VAMP1_BAT.get(), HigherVampireBat.createAttributes().build());
        creationEvent.put(HIGH_VAMP2.get(), HigherVampirePhase2.createAttributes().build());
        creationEvent.put(HIGH_VAMP3.get(), HigherVampirePhase3.createAttributes().build());
        creationEvent.put(FAKE_ZOMBIE.get(), ZombieIllusion.createAttributes().build());
        creationEvent.put(FAKE_SKELLY.get(), ZombieIllusion.createAttributes().build());
        creationEvent.put(NECROTIC_SPIDER.get(), NecroticSpider.createAttributes().build());
        creationEvent.put(PHOENIX.get(), Phoenix.createLivingAttributes().build());
    }
}