package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.core.util.tags.SBBlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class AlterTiers {
    public static String[][] tier1_fmt = {
            {
                    "##X#X##",
                    "#######",
                    "X#YYY#X",
                    "##Y#Y##",
                    "X#YYY#X",
                    "#######",
                    "##X#X##"
            },
            {
                    "##X#X##",
                    "#######",
                    "X#####X",
                    "#######",
                    "X#####X",
                    "#######",
                    "##X#X##"
            },
            {
                    "##YYY##",
                    "#######",
                    "Y#####Y",
                    "Y#####Y",
                    "Y#####Y",
                    "#######",
                    "##YYY##"
            }
    };
    public static Map<String, TagKey<Block>> tier1_map = Map.of(
        "X", SBBlockTags.ALTER_BLOCKS_1,
        "Y", SBBlockTags.ALTER_SLABS_1
    );
    public static String[][] tier2_fmt = {
            {
                    "##ZZZZZ##",
                    "#ZZZZZZZ#",
                    "ZZZZZZZZZ",
                    "ZZZZZZZZZ",
                    "ZZZZ#ZZZZ",
                    "ZZZZZZZZZ",
                    "ZZZZZZZZZ",
                    "#ZZZZZZZ#",
                    "##ZZZZZ##",
            },
            {
                    "##Z###Z##",
                    "#Z#####Z#",
                    "Z#######Z",
                    "#########",
                    "#########",
                    "#########",
                    "Z#######Z",
                    "#Z#####Z#",
                    "##Z###Z##"
            },
            {
                    "#########",
                    "#Z#####Z#",
                    "#########",
                    "#########",
                    "#########",
                    "#########",
                    "#########",
                    "#Z#####Z#",
                    "#########"
            },
            {
                    "#########",
                    "#Y#####Y#",
                    "#########",
                    "#########",
                    "#########",
                    "#########",
                    "#########",
                    "#Y#####Y#",
                    "#########"
            }
    };
    public static Map<String, TagKey<Block>> tier2_map = Map.of(
            "Z", SBBlockTags.ALTER_VALUABLES_2,
            "Y", SBBlockTags.ALTER_VALUABLES_2
    );
    public static String[][] tier3_fmt = {
            {
                    "#####Z#####",
                    "###ZZZZZ###",
                    "##ZZZZZZZ##",
                    "#ZZZZZZZZZ#",
                    "#ZZZZYZZZZ#",
                    "ZZZZY#YZZZZ",
                    "#ZZZZYZZZZ#",
                    "#ZZZZZZZZZ#",
                    "##ZZZZZZZ##",
                    "###ZZZZZ###",
                    "#####Z#####",
            },
            {
                    "#####Z#####",
                    "##Z#####Z##",
                    "#Z#######Z#",
                    "###########",
                    "###########",
                    "Z#########Z",
                    "###########",
                    "###########",
                    "#Z#######Z#",
                    "##Z#####Z##",
                    "#####Z#####"
            },
            {
                    "#####Z#####",
                    "#Z#######Z#",
                    "###########",
                    "###########",
                    "###########",
                    "Z#########Z",
                    "###########",
                    "###########",
                    "###########",
                    "#Z#######Z#",
                    "#####Z#####"
            },
            {
                    "#####Z#####",
                    "#Y#######Y#",
                    "###########",
                    "###########",
                    "###########",
                    "Z#########Z",
                    "###########",
                    "###########",
                    "###########",
                    "#Y#######Y#",
                    "#####Z#####"
            },
            {
                    "#####Y#####",
                    "###########",
                    "###########",
                    "###########",
                    "###########",
                    "Y#########Y",
                    "###########",
                    "###########",
                    "###########",
                    "###########",
                    "#####Y#####"
            }
    };
    public static Map<String, TagKey<Block>> tier3_map = Map.of(
            "Z", SBBlockTags.ALTER_VALUABLES_3,
            "Y", SBBlockTags.ALTER_VALUABLES_3
    );
    public static String[][] tier4_fmt = {
            {
                    "###ZZZZZZZ###",
                    "#ZZZZZZZZZZZ#",
                    "#ZZZ#####ZZZ#",
                    "ZZZ#######ZZZ",
                    "ZZ#########ZZ",
                    "ZZ#########ZZ",
                    "ZZ#########ZZ",
                    "ZZ#########ZZ",
                    "ZZ#########ZZ",
                    "ZZZ#######ZZZ",
                    "#ZZZ#####ZZZ#",
                    "#ZZZZZZZZZZZ#",
                    "###ZZZZZZZ###"
            },
            {
                    "###Z#####Z###",
                    "#Z#########Z#",
                    "####ZZZZZ####",
                    "Z##ZZZZZZZ##Z",
                    "##ZZZZZZZZZ##",
                    "##ZZZZZZZZZ##",
                    "##ZZZZ#ZZZZ##",
                    "##ZZZZZZZZZ##",
                    "##ZZZZZZZZZ##",
                    "Z##ZZZZZZZ##Z",
                    "####ZZZZZ####",
                    "#Z#########Z#",
                    "###Z#####Z###"
            },
            {
                    "###Z#####Z###",
                    "#Z#########Z#",
                    "#############",
                    "Z###########Z",
                    "#############",
                    "#############",
                    "#############",
                    "#############",
                    "#############",
                    "Z###########Z",
                    "#############",
                    "#Z#########Z#",
                    "###Z#####Z###"
            },
            {
                    "###Z#####Z###",
                    "#Y#########Y#",
                    "#############",
                    "Z###########Z",
                    "#############",
                    "#############",
                    "#############",
                    "#############",
                    "#############",
                    "Z###########Z",
                    "#############",
                    "#Y#########Y#",
                    "###Z#####Z###"
            },
            {
                    "###Z#####Z###",
                    "#############",
                    "#############",
                    "Z###########Z",
                    "#############",
                    "#############",
                    "#############",
                    "#############",
                    "#############",
                    "Z###########Z",
                    "#############",
                    "#############",
                    "###Z#####Z###"
            },
            {
                    "###Y#####Y###",
                    "#############",
                    "#############",
                    "Y###########Y",
                    "#############",
                    "#############",
                    "#############",
                    "#############",
                    "#############",
                    "Y###########Y",
                    "#############",
                    "#############",
                    "###Y#####Y###"
            },
    };
    public static Map<String, TagKey<Block>> tier4_map = Map.of(
            "Z", SBBlockTags.ALTER_VALUABLES_4,
            "Y", SBBlockTags.ALTER_VALUABLES_4
    );
}