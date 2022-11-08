package com.tzaranthony.spellbook.core.util.tags;

import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SBBlockTags {
    public static final TagKey<Block> ALTER_BLOCKS_1 = registerBlockTag("alter_1_blocks");
    public static final TagKey<Block> ALTER_SLABS_1 = registerBlockTag("alter_1_slabs");
    public static final TagKey<Block> ALTER_VALUABLES_2 = registerBlockTag("alter_2_valuables");
    public static final TagKey<Block> ALTER_VALUABLES_3 = registerBlockTag("alter_3_valuables");

    public static final TagKey<Block> GROWTH_SPELL_GROWABLES = registerBlockTag("growth_spell_growables");
    public static final TagKey<Block> EARTH_BERRIES_BASE_BLOCKS = registerBlockTag("earth_berries_base_blocks");
    public static final TagKey<Block> LAVA_BERRIES_BASE_BLOCKS = registerBlockTag("lava_berries_base_blocks");

    private static TagKey<Block> registerBlockTag(String name) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(SpellBook.MOD_ID, name));
    }
}