package com.tzaranthony.spellbook.core.spells;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.tzaranthony.spellbook.core.entities.other.MagicProjectile;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ProjectileSpell extends Spell {
    public ProjectileSpell(int id, String name, SpellTier tier) {
        super(id, name, tier);
    }

    public boolean perform_spell(Level level, Player player, InteractionHand hand, BlockPos blockPos) {
        return this.perform_spell(level, player, hand, blockPos, 0.0F);
    }

    public boolean perform_spell(Level level, Player player, InteractionHand hand, BlockPos blockPos, float spread) {
        MagicProjectile magic = new MagicProjectile(getMagicProjectile(), level);
        magic.initializeProjectile(player, player.getX(), player.getEyeY() - (double)0.15F, player.getZ());
        addSpellDataToProjectile(magic);

        Vec3 vec31 = player.getUpVector(1.0F);
        Quaternion quaternion = new Quaternion(new Vector3f(vec31), spread, true);
        Vec3 vec3 = player.getViewVector(1.0F);
        Vector3f vector3f = new Vector3f(vec3);
        vector3f.transform(quaternion);
        magic.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), 1.6F, 1.0F);

        level.addFreshEntity(magic);
        return true;
    }

    public EntityType<MagicProjectile> getMagicProjectile() {
        return SBEntities.SMALL_MAGIC_PROJECTILE.get();
    }

    public void finishSpell(Entity user, Level level, BlockPos pos) {

    }

    public void addSpellDataToProjectile(MagicProjectile magic) {
        return;
    }

    public void playCustomSound(Level level, double x, double y, double z) {
        return;
    }

    public boolean spellIgnoresBlocks() {
        return false;
    }

    public boolean spellIgnoresBlocksAndEntities() {
        return false;
    }
}