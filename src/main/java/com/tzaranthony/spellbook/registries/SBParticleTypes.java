package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SBParticleTypes {
    public static final DeferredRegister<ParticleType<?>> reg = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SpellBook.MOD_ID);

    public static final RegistryObject<SimpleParticleType> END_FIRE_FLAME = reg.register("end_fire_flame", ()-> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_DRIP = reg.register("dripping_blood", ()-> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_FALL = reg.register("falling_blood", ()-> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BLOOD_LAND = reg.register("landing_blood", ()-> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SCREAM = reg.register("scream", () -> new SimpleParticleType(true));
}