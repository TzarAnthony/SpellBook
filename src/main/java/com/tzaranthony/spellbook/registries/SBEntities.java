package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedSkeleton;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedVex;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedWitherSkeleton;
import com.tzaranthony.spellbook.core.entities.friendly.SummonedZombie;
import com.tzaranthony.spellbook.core.entities.hostile.alchemical.NecroticSpider;
import com.tzaranthony.spellbook.core.entities.hostile.alchemical.SkeletonIllusion;
import com.tzaranthony.spellbook.core.entities.hostile.alchemical.ZombieIllusion;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.*;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.HigherVampire0;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.HigherVampirePerson;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.HigherVampireBat;
import com.tzaranthony.spellbook.core.entities.hostile.vampires.LesserVampire;
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

    public static final RegistryObject<EntityType<HigherVampire0>> HIGHVAMP0 = reg.register("higher_vampire_hidden", () ->
            EntityType.Builder.of(HigherVampire0::new, MobCategory.MONSTER).immuneTo(Blocks.POWDER_SNOW).sized(0.6F, 1.95F).clientTrackingRange(8).build("higher_vampire_hidden")
    );
    public static final RegistryObject<EntityType<HigherVampirePerson>> HIGHVAMP1 = reg.register("higher_vampire_human", () ->
            EntityType.Builder.of(HigherVampirePerson::new, MobCategory.MONSTER).immuneTo(Blocks.POWDER_SNOW).sized(0.6F, 1.95F).clientTrackingRange(8).build("higher_vampire_human")
    );
    public static final RegistryObject<EntityType<HigherVampireBat>> HIGHVAMP1BAT = reg.register("higher_vampire_bat", () ->
            EntityType.Builder.of(HigherVampireBat::new, MobCategory.MONSTER).immuneTo(Blocks.POWDER_SNOW).sized(0.5F, 0.9F).clientTrackingRange(8).build("higher_vampire_bat")
    );
    public static final RegistryObject<EntityType<LesserVampire>> LOWVAMP = reg.register("lesser_vampire", () ->
            EntityType.Builder.of(LesserVampire::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("lesser_vampire")
    );

    public static final RegistryObject<EntityType<ZombieIllusion>> FAKE_ZOMBIE = reg.register("fake_zombie", () ->
            EntityType.Builder.of(ZombieIllusion::new, MobCategory.MONSTER).fireImmune().immuneTo(Blocks.POWDER_SNOW).sized(0.6F, 1.95F).clientTrackingRange(8).build("fake_zombie")
    );
    public static final RegistryObject<EntityType<SkeletonIllusion>> FAKE_SKELLY = reg.register("fake_skelly", () ->
            EntityType.Builder.of(SkeletonIllusion::new, MobCategory.MONSTER).fireImmune().immuneTo(Blocks.POWDER_SNOW).sized(0.6F, 1.95F).clientTrackingRange(8).build("fake_skelly")
    );
    public static final RegistryObject<EntityType<NecroticSpider>> NECROTIC_SPIDER = reg.register("necrotic_spider", () ->
            EntityType.Builder.of(NecroticSpider::new, MobCategory.MONSTER).sized(2.0F, 1.3F).clientTrackingRange(8).build("necrotic_spider")
    );

    // Other
    public static final RegistryObject<EntityType<PhoenixAshesEntity>> PHOENIX_ASHES = reg.register("phoenix_ashes", () ->
            EntityType.Builder.of(PhoenixAshesEntity::new, MobCategory.MISC).fireImmune().sized(0.25F, 0.25F).build("phoenix_ashes")
    );
    public static final RegistryObject<EntityType<SilverArrow>> SILVER_ARROW = reg.register("silver_arrow", () ->
            EntityType.Builder.of(SilverArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("silver_arrow")
    );
    public static final RegistryObject<EntityType<GhostlyArrow>> GHOSTLY_ARROW = reg.register("ghostly_arrow", () ->
            EntityType.Builder.of(GhostlyArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("ghostly_arrow")
    );
    public static final RegistryObject<EntityType<CrystalArrow>> CRYSTAL_ARROW = reg.register("crystal_arrow", () ->
            EntityType.Builder.of(CrystalArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("crystal_arrow")
    );
    public static final RegistryObject<EntityType<EffectCarryingArrow>> FIRE_ARROW = reg.register("fire_arrow", () ->
            EntityType.Builder.of(EffectCarryingArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("fire_arrow")
    );
    public static final RegistryObject<EntityType<EffectCarryingArrow>> EARTH_ARROW = reg.register("earth_arrow", () ->
            EntityType.Builder.of(EffectCarryingArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("earth_arrow")
    );
    public static final RegistryObject<EntityType<EffectCarryingArrow>> WATER_ARROW = reg.register("water_arrow", () ->
            EntityType.Builder.of(EffectCarryingArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("water_arrow")
    );
    public static final RegistryObject<EntityType<EffectCarryingArrow>> AIR_ARROW = reg.register("air_arrow", () ->
            EntityType.Builder.of(EffectCarryingArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("air_arrow")
    );
    public static final RegistryObject<EntityType<MagicProjectile>> SMALL_MAGIC_PROJECTILE = reg.register("small_magic_projectile", () ->
            EntityType.Builder.of(MagicProjectile::new, MobCategory.MISC).fireImmune().sized(0.25F, 0.25F).build("small_magic_projectile")
    );
    public static final RegistryObject<EntityType<MagicProjectile>> MEDIUM_MAGIC_PROJECTILE = reg.register("medium_magic_projectile", () ->
            EntityType.Builder.of(MagicProjectile::new, MobCategory.MISC).fireImmune().sized(0.5F, 0.5F).build("medium_magic_projectile")
    );
    public static final RegistryObject<EntityType<MagicProjectile>> LARGE_MAGIC_PROJECTILE = reg.register("large_magic_projectile", () ->
            EntityType.Builder.of(MagicProjectile::new, MobCategory.MISC).fireImmune().sized(1.5F, 1.5F).build("large_magic_projectile")
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
        creationEvent.put(HIGHVAMP0.get(), HigherVampire0.createAttributes().build());
        creationEvent.put(HIGHVAMP1.get(), HigherVampirePerson.createAttributes().build());
        creationEvent.put(HIGHVAMP1BAT.get(), HigherVampirePerson.createAttributes().build());
        creationEvent.put(LOWVAMP.get(), LesserVampire.createAttributes().build());
        creationEvent.put(FAKE_ZOMBIE.get(), ZombieIllusion.createAttributes().build());
        creationEvent.put(FAKE_SKELLY.get(), ZombieIllusion.createAttributes().build());
        creationEvent.put(NECROTIC_SPIDER.get(), NecroticSpider.createAttributes().build());
    }
}