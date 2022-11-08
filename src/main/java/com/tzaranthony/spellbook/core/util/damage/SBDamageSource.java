package com.tzaranthony.spellbook.core.util.damage;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class SBDamageSource extends DamageSource {
    private boolean isFireSource;
    private boolean isWaterSource;
    private boolean isEarthSource;
    private boolean isAirSource;
    private boolean isVoidSource;
    private boolean isPsychicSource;
    private boolean isSoulSource;

    // Magic Core
    public static final SBDamageSource FIRE_MAGIC = (SBDamageSource) (new SBDamageSource("fire_magic").setIsFireSource()).setMagic();
    public static final SBDamageSource WATER_MAGIC = (SBDamageSource) (new SBDamageSource("water_magic").setIsWater()).setMagic();
    public static final SBDamageSource EARTH_MAGIC = (SBDamageSource) (new SBDamageSource("earth_magic").setIsEarth()).setMagic();
    public static final SBDamageSource AIR_MAGIC = (SBDamageSource) (new SBDamageSource("air_magic").setIsAir()).setMagic();
    public static final SBDamageSource VOID_MAGIC = (SBDamageSource) (new SBDamageSource("void_magic").setIsVoid()).setMagic().bypassArmor().bypassMagic();
    public static final SBDamageSource PSYCHIC_MAGIC = (SBDamageSource) (new SBDamageSource("psychic_magic").setIsPsychic()).setMagic().bypassArmor().bypassMagic();
    public static final SBDamageSource SOUL_MAGIC = (SBDamageSource) (new SBDamageSource("psychic_magic").setIsSoul()).setMagic().bypassArmor().bypassMagic();
    // Inflicted
    public static final SBDamageSource CURSE = (SBDamageSource) (new SBDamageSource("curse").setIsSoul()).setMagic().bypassArmor();
    public static final SBDamageSource LASER = (SBDamageSource) (new SBDamageSource("laser").setIsVoid()).setMagic().bypassArmor().bypassMagic();
    public static final SBDamageSource FRACTURING = (SBDamageSource) (new SBDamageSource("fracturing").setIsVoid()).setExplosion().bypassMagic();
    // Effects
    public static final SBDamageSource INCINERATION = (SBDamageSource) (new SBDamageSource("incineration").setIsFireSource()).setMagic().setIsFire().bypassArmor();

    // Entities
    public static SBDamageSource scream(LivingEntity owner) {
        return (SBDamageSource) (new SBEntityDamageSource("scream", owner).setIsAir()).bypassArmor().bypassMagic().setMagic();
    }
    public static SBDamageSource lifesteal(LivingEntity owner) {
        return (SBDamageSource) (new SBEntityDamageSource("life_steal", owner).setIsSoul()).bypassArmor().bypassMagic().setMagic();
    }

    // Vampire
    public static SBDamageSource bite(SBVampireEntity entity) {
        return (SBDamageSource) (new SBEntityDamageSource("bite", entity).setIsSoul()).bypassArmor().bypassMagic();
    }

    public static final SBDamageSource BLEED = (SBDamageSource) (new SBDamageSource("bleed")).bypassArmor().bypassMagic();


    public SBDamageSource(String damageTypeIn) {
        super(damageTypeIn);
    }

    public DamageSource setIsFireSource() {
        this.isFireSource = true;
        return this;
    }

    public DamageSource setIsWater() {
        this.isWaterSource = true;
        return this;
    }

    public DamageSource setIsEarth() {
        this.isEarthSource = true;
        return this;
    }

    public DamageSource setIsAir() {
        this.isAirSource = true;
        return this;
    }

    public DamageSource setIsVoid() {
        this.isVoidSource = true;
        return this;
    }

    public DamageSource setIsPsychic() {
        this.isPsychicSource = true;
        return this;
    }

    public DamageSource setIsSoul() {
        this.isSoulSource = true;
        return this;
    }

    public boolean isWaterDmg() {
        return this.isWaterSource;
    }

    public boolean isEarthDmg() {
        return this.isEarthSource;
    }

    public boolean isAirDmg() {
        return this.isAirSource;
    }

    public boolean isVoidDmg() {
        return this.isVoidSource;
    }

    public boolean isPsychicDmg() {
        return this.isPsychicSource;
    }
}