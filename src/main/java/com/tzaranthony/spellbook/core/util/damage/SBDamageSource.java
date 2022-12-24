package com.tzaranthony.spellbook.core.util.damage;

import com.tzaranthony.spellbook.core.entities.hostile.vampires.SBVampireEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class SBDamageSource extends DamageSource {
    // Effects
    public static final SBDamageSource INCINERATION = (SBDamageSource) (new SBDamageSource("incineration")).setMagic().setIsFire().bypassArmor();

    // Projectiles
    public static SBDamageSource scream(LivingEntity owner) {
        return (SBDamageSource) (new SBEntityDamageSource("scream", owner).setMagic()).bypassArmor().bypassMagic().setMagic();
    }
    public static SBDamageSource lifesteal(LivingEntity owner) {
        return (SBDamageSource) (new SBEntityDamageSource("life_steal", owner)).bypassArmor().bypassMagic().setMagic();
    }

    // Mobs
    // Vampire
    public static SBDamageSource bite(SBVampireEntity entity) {
        return (SBDamageSource) (new SBEntityDamageSource("bite", entity)).bypassArmor().bypassMagic();
    }

    public static final SBDamageSource BLEED = (SBDamageSource) (new SBDamageSource("bleed")).bypassArmor().bypassMagic();


    // Util
    public SBDamageSource(String damageTypeIn) {
        super(damageTypeIn);
    }
}