package com.tzaranthony.spellbook.registries;

import com.google.common.collect.ImmutableList;
import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.blocks.block.PotionFluidBlock;
import com.tzaranthony.spellbook.core.items.items.SBBucket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.util.List;

public class SBFluids {
    // bases
    public static final PotionFluidRegistry MUNDANE = new PotionFluidRegistry("mundane", Potions.MUNDANE, 500, 400, 0, 0);

    public static final PotionFluidRegistry THICK = new PotionFluidRegistry("thick", Potions.THICK, 500, 1600, 0, 0);

    public static final PotionFluidRegistry AWKWARD = new PotionFluidRegistry("awkward", Potions.AWKWARD, 500, 400, 0, 0);


    // visibility
    public static final PotionFluidRegistry NIGHT_VISION = new PotionFluidRegistry("night_vision", Potions.NIGHT_VISION,
            500, 400, 0, 8, new MobEffectInstance(MobEffects.NIGHT_VISION, 3 * 3600));

    public static final PotionFluidRegistry LONG_NIGHT_VISION = new PotionFluidRegistry("long_night_vision", Potions.LONG_NIGHT_VISION,
            500, 400, 0, 8, new MobEffectInstance(MobEffects.NIGHT_VISION, 3 * 9600));

    public static final PotionFluidRegistry INVISIBILITY = new PotionFluidRegistry("invisibility", Potions.INVISIBILITY,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.INVISIBILITY, 3 * 3600));

    public static final PotionFluidRegistry LONG_INVISIBILITY = new PotionFluidRegistry("long_invisibility", Potions.LONG_INVISIBILITY,
            -200, 0, 0, 0, new MobEffectInstance(MobEffects.INVISIBILITY, 3 * 9600));


    // leaping
    public static final PotionFluidRegistry LEAPING = new PotionFluidRegistry("leaping", Potions.LEAPING,
            -200, 400, 0, 0, new MobEffectInstance(MobEffects.JUMP, 3 * 3600));

    public static final PotionFluidRegistry LONG_LEAPING = new PotionFluidRegistry("long_leaping", Potions.LONG_LEAPING,
            -200, 400, 0, 0, new MobEffectInstance(MobEffects.JUMP, 3 * 9600));

    public static final PotionFluidRegistry STRONG_LEAPING = new PotionFluidRegistry("strong_leaping", Potions.STRONG_LEAPING,
            -400, -50, 0, 0, new MobEffectInstance(MobEffects.JUMP, 3 * 1800, 2 * 1));


    // resistance
    public static final PotionFluidRegistry FIRE_RESISTANCE = new PotionFluidRegistry("fire_resistance", Potions.FIRE_RESISTANCE,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3 * 3600));

    public static final PotionFluidRegistry LONG_FIRE_RESISTANCE = new PotionFluidRegistry("long_fire_resistance", Potions.LONG_FIRE_RESISTANCE,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3 * 9600));


    // speed
    public static final PotionFluidRegistry SWIFTNESS = new PotionFluidRegistry("swiftness", Potions.SWIFTNESS,
            500, -500, 0, 0, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3 * 3600));

    public static final PotionFluidRegistry LONG_SWIFTNESS = new PotionFluidRegistry("long_swiftness", Potions.LONG_SWIFTNESS,
            500, -500, 0, 0, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3 * 9600));

    public static final PotionFluidRegistry STRONG_SWIFTNESS = new PotionFluidRegistry("strong_swiftness", Potions.STRONG_SWIFTNESS,
            500, -800, 0, 0, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3 * 1800, 2 * 1));

    public static final PotionFluidRegistry SLOWNESS = new PotionFluidRegistry("slowness", Potions.SLOWNESS,
            500, 800, 0, 0, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3 * 1800));

    public static final PotionFluidRegistry LONG_SLOWNESS = new PotionFluidRegistry("long_slowness", Potions.LONG_SLOWNESS,
            500, 800, 0, 0, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3 * 4800));

    public static final PotionFluidRegistry STRONG_SLOWNESS = new PotionFluidRegistry("strong_slowness", Potions.STRONG_SLOWNESS,
            500, 1100, 0, 0, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3 * 400, 2 * 3));


    // turtle
    public static final PotionFluidRegistry TURTLE_MASTER = new PotionFluidRegistry("turtle_master", Potions.TURTLE_MASTER,
            500, 1100, 0, 0, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3 * 400, 2 * 3),
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3 * 400, 2 * 2));

    public static final PotionFluidRegistry LONG_TURTLE_MASTER = new PotionFluidRegistry("long_turtle_master", Potions.LONG_TURTLE_MASTER,
            500, 1100, 0, 0, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3 * 800, 2 * 3),
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3 * 800, 2 * 2));

    public static final PotionFluidRegistry STRONG_TURTLE_MASTER = new PotionFluidRegistry("strong_turtle_master", Potions.STRONG_TURTLE_MASTER,
            500, 2000, 0, 0, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3 * 400, 2 * 5),
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3 * 400, 2 * 3));


    // water
    public static final PotionFluidRegistry WATER_BREATHING = new PotionFluidRegistry("water_breathing", Potions.WATER_BREATHING,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.WATER_BREATHING, 3 * 3600));

    public static final PotionFluidRegistry LONG_WATER_BREATHING = new PotionFluidRegistry("long_water_breathing", Potions.LONG_WATER_BREATHING,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.WATER_BREATHING, 3 * 9600));


    // health
    public static final PotionFluidRegistry HEALING = new PotionFluidRegistry("healing", Potions.HEALING,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.HEAL, 1, 1));

    public static final PotionFluidRegistry STRONG_HEALING = new PotionFluidRegistry("strong_healing", Potions.STRONG_HEALING,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.HEAL, 1, 3 * 1));

    public static final PotionFluidRegistry HARMING = new PotionFluidRegistry("harming", Potions.HARMING,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.HARM, 1, 1));

    public static final PotionFluidRegistry STRONG_HARMING = new PotionFluidRegistry("strong_harming", Potions.STRONG_HARMING,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.HARM, 1, 3 * 1));

    public static final PotionFluidRegistry POISON = new PotionFluidRegistry("poison", Potions.POISON,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.POISON, 3 * 900));

    public static final PotionFluidRegistry LONG_POISON = new PotionFluidRegistry("long_poison", Potions.LONG_POISON,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.POISON, 3 * 1800));

    public static final PotionFluidRegistry STRONG_POISON = new PotionFluidRegistry("strong_poison", Potions.STRONG_POISON,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.POISON, 3 * 432, 2 * 1));

    public static final PotionFluidRegistry REGENERATION = new PotionFluidRegistry("regeneration", Potions.REGENERATION,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.REGENERATION, 3 * 900));

    public static final PotionFluidRegistry LONG_REGENERATION = new PotionFluidRegistry("long_regeneration", Potions.LONG_REGENERATION,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.REGENERATION, 3 * 1800));

    public static final PotionFluidRegistry STRONG_REGENERATION = new PotionFluidRegistry("strong_regeneration", Potions.STRONG_REGENERATION,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.REGENERATION, 3 * 450, 2 * 1));


    // strength
    public static final PotionFluidRegistry STRENGTH = new PotionFluidRegistry("strength", Potions.STRENGTH,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3 * 3600));

    public static final PotionFluidRegistry LONG_STRENGTH = new PotionFluidRegistry("long_strength", Potions.LONG_STRENGTH,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3 * 9600));

    public static final PotionFluidRegistry STRONG_STRENGTH = new PotionFluidRegistry("strong_strength", Potions.STRONG_STRENGTH,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3 * 1800, 2 * 1));

    public static final PotionFluidRegistry WEAKNESS = new PotionFluidRegistry("weakness", Potions.WEAKNESS,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.WEAKNESS, 3 * 1800));

    public static final PotionFluidRegistry LONG_WEAKNESS = new PotionFluidRegistry("long_weakness", Potions.LONG_WEAKNESS,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.WEAKNESS, 3 * 4800));


    // luck
    public static final PotionFluidRegistry LUCK = new PotionFluidRegistry("luck", Potions.LUCK,
            500, 400, 0, 0, new MobEffectInstance(MobEffects.LUCK, 3 * 6000));


    // feather falling
    public static final PotionFluidRegistry SLOW_FALLING = new PotionFluidRegistry("slow_falling", Potions.SLOW_FALLING, -500,
            0, 0, 0, new MobEffectInstance(MobEffects.SLOW_FALLING, 3 * 1800));

    public static final PotionFluidRegistry LONG_SLOW_FALLING = new PotionFluidRegistry("long_slow_falling", Potions.LONG_SLOW_FALLING,
            -500, 0, 0, 0, new MobEffectInstance(MobEffects.SLOW_FALLING, 3 * 4800));


    public static void registerFluids() {
        try {
            for (Field f : SBFluids.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof PotionFluidRegistry) {
                    ((PotionFluidRegistry) obj).BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
                    ((PotionFluidRegistry) obj).ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
                    ((PotionFluidRegistry) obj).FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static class PotionFluidRegistry {
        public static final String MODID = SpellBook.MOD_ID;

        public static final ResourceLocation FLUID_STILL = new ResourceLocation("spellbook:block/potion_still");
        public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("spellbook:block/potion_flow");
        public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation("spellbook:block/potion_overlay");

        public DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        public DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        public DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);

        private ForgeFlowingFluid.Properties makeProperties(int color, int DenseAdd, int ViscAdd, int TempAdd, int LghtAdd) {
            return new ForgeFlowingFluid.Properties(fluid, fluid_flowing,
                    FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING)
                            .overlay(FLUID_OVERLAY)
                            .color(color)
                            .density(1000 + DenseAdd)
                            .viscosity(1000 + ViscAdd)
                            .temperature(300 + TempAdd)
                            .luminosity(0 + LghtAdd))
                    .levelDecreasePerBlock(2)
                    .bucket(fluid_bucket)
                    .block(fluid_block);
        }

        public RegistryObject<FlowingFluid> fluid;

        public RegistryObject<FlowingFluid> fluid_flowing;

        public RegistryObject<LiquidBlock> fluid_block;

        public RegistryObject<Item> fluid_bucket;

        public PotionFluidRegistry(String name, Potion potion, int DenseAdd, int ViscAdd, int TempAdd, int LghtAdd, MobEffectInstance ... effectsIn) {
            name = name + "_potion";
            int color = PotionUtils.getColor(potion);
            List<MobEffectInstance> effects = ImmutableList.copyOf(effectsIn);

            this.fluid = FLUIDS.register(name, () ->
                    new ForgeFlowingFluid.Source(makeProperties(color, DenseAdd, ViscAdd, TempAdd, LghtAdd))
            );

            this.fluid_flowing = FLUIDS.register(name + "_flowing", () ->
                    new ForgeFlowingFluid.Flowing(makeProperties(color, DenseAdd, ViscAdd, TempAdd, LghtAdd))
            );

            this.fluid_block = BLOCKS.register(name + "_block", () ->
                    new PotionFluidBlock(fluid, effects)
            );

            this.fluid_bucket = ITEMS.register(name + "_bucket", () ->
                    new SBBucket(fluid, color)
            );
        }
    }
}