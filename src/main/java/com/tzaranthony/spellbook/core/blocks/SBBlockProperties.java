package com.tzaranthony.spellbook.core.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class SBBlockProperties {
    public static BlockBehaviour.Properties Block(Material material, SoundType sound, float hardness, float resistance) {
        return BlockBehaviour.Properties.of(material)
                .sound(sound)
                .strength(hardness, resistance);
    }

    public static BlockBehaviour.Properties ColoredBlock(Material material, MaterialColor color, SoundType sound, float hardness, float resistance) {
        return BlockBehaviour.Properties.of(material, color)
                .sound(sound)
                .strength(hardness, resistance);
    }

    public static BlockBehaviour.Properties ClothBlock(MaterialColor color) {
        return ColoredBlock(Material.WOOL, color, SoundType.WOOL, 0.8F, 0.8F);
    }

    // metal blocks
    public static BlockBehaviour.Properties NetheriteBasedBlock(float hardness, float resistance) {
        return Block(Material.HEAVY_METAL, SoundType.NETHERITE_BLOCK, hardness, resistance);
//                .requiresCorrectToolForDrops();
    }

    public static BlockBehaviour.Properties NetheriteBasedBlock(float resistance) {
        return NetheriteBasedBlock(30.0F, resistance);
    }

    public static BlockBehaviour.Properties LightyNetheriteBasedBlock(float resistance, int light) {
        return NetheriteBasedBlock(resistance)
                .lightLevel((lightVar) -> light);
    }

    public static BlockBehaviour.Properties StandardMetal(SoundType sound, float hardness, float resistance) {
        return Block(Material.METAL, sound, hardness, resistance);
//                .requiresCorrectToolForDrops();
    }

    public static BlockBehaviour.Properties StandardMetal(float hardness, float resistance) {
        return StandardMetal(SoundType.METAL, hardness, resistance);
    }

    public static BlockBehaviour.Properties StandardMetal() {
        return StandardMetal(5.0F, 6.0F);
    }


    // rocks
    public static BlockBehaviour.Properties StandardRock(SoundType sound, float hardness, float resistance) {
        return Block(Material.STONE, sound, hardness, resistance);
//                .requiresCorrectToolForDrops();
    }

    public static BlockBehaviour.Properties StandardRock(float hardness, float resistance) {
        return StandardRock(SoundType.STONE, hardness, resistance);
    }

    public static BlockBehaviour.Properties StandardRock(SoundType sound) {
        return StandardRock(sound,3.0F, 3.0F);
    }

    public static BlockBehaviour.Properties StandardRock() {
        return StandardRock(SoundType.STONE, 3.0F, 3.0F);
    }

    public static BlockBehaviour.Properties StandardDeepslate() {
        return StandardRock(SoundType.DEEPSLATE, 4.5F, 3.0F)
                .color(MaterialColor.DEEPSLATE);
    }

    // light blocks
    public static BlockBehaviour.Properties StandardLight(Material material, SoundType sound, float hardness, float resistance, int light) {
        return Block(material, sound, hardness, resistance)
                .lightLevel((lightVar) -> light);
    }

    public static BlockBehaviour.Properties ColoredLight(Material material, MaterialColor color, SoundType sound, float hardness, float resistance, int light) {
        return ColoredBlock(material, color, sound, hardness, resistance)
                .lightLevel((lightVar) -> light);
    }

    public static BlockBehaviour.Properties Crystal(SoundType sound, int light) {
        return BlockBehaviour.Properties
                .of(Material.AMETHYST)
//                .requiresCorrectToolForDrops()
                .noOcclusion()
                .randomTicks()
                .sound(sound)
                .strength(1.5F)
                .lightLevel((lightSupplier) -> {
            return light;
        });
    }

    public static BlockBehaviour.Properties Lantern(int light) {
        return StandardLight(Material.METAL, SoundType.LANTERN, 3.5F, 3.5F, light)
//                .requiresCorrectToolForDrops()
                .noOcclusion();
    }

    public static BlockBehaviour.Properties Torch(int light) {
        return StandardLight(Material.DECORATION, SoundType.WOOD, 0.0F, 0.0F, light)
                .noCollission();
    }

    public static BlockBehaviour.Properties WallTorch(int light, Block baseBlock) {
        return Torch(light)
                .dropsLike(baseBlock);
    }

    public static BlockBehaviour.Properties Campfire(int light) {
        return ColoredLight(Material.WOOD, MaterialColor.PODZOL, SoundType.WOOD, 2.0F, 2.0F, light)
                .noOcclusion();
    }

    public static BlockBehaviour.Properties Fire(MaterialColor color, int light) {
        return ColoredLight(Material.FIRE, color, SoundType.WOOL, 0.0F, 0.0F, light)
                .noCollission();
    }

    // plant block
    public static BlockBehaviour.Properties StandardPlant(Material material, SoundType sound) {
        return Block(material, sound, 0.0F, 0.0F)
                .noCollission();
    }

    public static BlockBehaviour.Properties TickingPlant(Material material, SoundType sound) {
        return StandardPlant(material, sound)
                .randomTicks();
    }

    public static BlockBehaviour.Properties TickingLavaPlant(Material material, SoundType sound) {
        return StandardPlant(material, sound)
                .randomTicks()
                .lightLevel((lightSupplier) -> {
                    return 15;
                });
    }

    // dirt like
    public static BlockBehaviour.Properties DirtBlock(Material material, MaterialColor color, SoundType sound, float hardness, float resistance) {
        return ColoredBlock(material, color, sound, hardness, resistance);
    }

    // glass
    public static BlockBehaviour.Properties StandardGlass(float hardness, float resistance) {
        return Block(Material.GLASS, SoundType.GLASS, hardness, resistance)
                .noOcclusion()
                .isValidSpawn(SBBlockProperties::never)
                .isRedstoneConductor(SBBlockProperties::never)
                .isSuffocating(SBBlockProperties::never)
                .isViewBlocking(SBBlockProperties::never);
    }

    public static BlockBehaviour.Properties StandardGlass() {
        return StandardGlass(0.3F, 0.3F);
    }

    public static BlockBehaviour.Properties PottedPlant() {
        return BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion();
    }

    public static BlockBehaviour.Properties MagicBlock() {
        return BlockBehaviour.Properties.of(Material.EXPLOSIVE)
                .strength(-1.0F, 3600000.0F)
                .noDrops()
                .isValidSpawn(SBBlockProperties::never)
                .randomTicks();
    }

    private static Boolean never(BlockState p_235427_0_, BlockGetter p_235427_1_, BlockPos p_235427_2_, EntityType<?> p_235427_3_) {
        return (boolean) false;
    }

    private static boolean never(BlockState p_235436_0_, BlockGetter p_235436_1_, BlockPos p_235436_2_) {
        return false;
    }
}