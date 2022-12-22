package com.tzaranthony.spellbook.core.items.equipment.equipmentOther;

import com.tzaranthony.spellbook.core.entities.friendly.SummonedVex;
import com.tzaranthony.spellbook.core.entities.hostile.ghosts.SBGhostEntity;
import com.tzaranthony.spellbook.core.items.SBItemProperties;
import com.tzaranthony.spellbook.registries.SBEffects;
import com.tzaranthony.spellbook.registries.SBEntities;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TarotCard extends Item {
    public static final String CARD = "CardDraw";
    public static final String DISPLAY_TIME = "DisplayTime";

    public TarotCard() {
        super(SBItemProperties.Standard(Rarity.RARE, 8));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.getCooldowns().addCooldown(this, 20);
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            cardSelector(level, player, stack);
            return InteractionResultHolder.consume(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int size, boolean selected) {
        if (hasCard(stack)) {
            CompoundTag tag = stack.getTag();
            tag.putInt(DISPLAY_TIME, tag.getInt(DISPLAY_TIME) - 1);
            if (tag.getInt(DISPLAY_TIME) <= 0) {
                setCard(stack, 0);
                resetCardDisplayDuration(stack);
            }
        }
    }

    public void cardSelector(Level level, Player player, ItemStack stack) {
        // https://en.wikipedia.org/wiki/Tarot_card_reading#The_Hermetic_Order_of_the_Golden_Dawn_and_its_heirs
        double luck = player.getAttributeValue(Attributes.LUCK);
        double prob = Math.random() + (luck * 0.03);
        boolean isGood;
        BlockPos pos = player.blockPosition();
        if ((Math.random() + (luck * 0.1)) > 0.5D) {
            isGood = true;
        } else {
            isGood = false;
        }
        resetCardDisplayDuration(stack);

        if (prob < 0.0455D) {
            System.out.print("Hanged Man\n");
            // The Hanged Man - Wisdom, discernment, sacrifice, divination, prophecy - Selfishness - Kills all living entities around the user?
            setCard(stack, 13);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,
                    new AABB((double) (pos.getX() - 10), (double) (pos.getY() - 10), (double) (pos.getZ() - 10), (double) (pos.getX() + 10), (double) (pos.getY() + 10), (double) (pos.getZ() + 10)));

            if (!entities.isEmpty()) {
                for (LivingEntity mob : entities) {
                    mob.hurt(DamageSource.OUT_OF_WORLD, 10.0F);
                }
            }
            return;
        } else if (prob < 0.091D) {
            System.out.print("Emperor\n");
            // The Emperor - Stability, power, protection, a great person, authority, will - Benevolence, compassion, obstruction to enemies - Summons a boss?
            setCard(stack, 5);
            if (isGood) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60*20*2, 4));
            } else {
                EntityType.WITHER.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
            }
        } else if (prob < 0.1365D) {
            System.out.print("Tower\n");
            // The Tower - Misery, distress, calamity - Negligence, carelessness, apathy - Summons all the bosses? Grants all negative status effects? Teleports you into the void?
            setCard(stack, 17);
            if (isGood) {
                for (int i = 0; i < 1; ++i) {
                    EntityType.CAVE_SPIDER.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                }
            } else {
                for (int i = 0; i < 5; ++i) {
                    EntityType.CREEPER.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                }
            }
        } else if (prob < 0.182D) {
            System.out.print("Chariot\n");
            // The Chariot - Assistance, war, triumph, presumption, vengeance - Riot, quarrel, defeat - Summons a boss? Summons a pillager raid?
            setCard(stack, 8);
            if (isGood) {
                for (int i = 0; i < 1; ++i) {
                    EntityType.IRON_GOLEM.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                }
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 1200, 2));
                ((ServerLevel) player.level).getRaids().createOrExtendRaid((ServerPlayer) player);
            }
        } else if (prob < 0.2275D)  {
            System.out.print("Fool\n");
            // The Fool - Folly, mania, extravagance, intoxication, frenzy - Negligence, absence, distribution, carelessness - Gives mercury poisoning?
            setCard(stack, 1);
            if (isGood) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60*20*2));
            } else {
                player.addEffect(new MobEffectInstance(SBEffects.MERCURY_POISONING.get(), 60*20*2, 8));
            }
        } else if (prob < 0.273D) {
            System.out.print("Moon\n");
            // The Moon - Hidden enemies, danger, darkness, terror - Instability, silence - Summons a magical raid? Summons a demonic raid? Summons bosses? Teleports to the End?
            setCard(stack, 19);
            if (isGood) {
                // commit arson
            } else {
                SBGhostEntity ghost = (SBGhostEntity) SBEntities.SHADE.get().spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                ghost.setTarget(player);

                SBGhostEntity ghost1 = (SBGhostEntity) SBEntities.POLTERGEIST.get().spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                ghost1.setTarget(player);

                SBGhostEntity ghost2 = (SBGhostEntity) SBEntities.WRAITH.get().spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                ghost2.setTarget(player);
            }
        } else if (prob < 0.3185D) {
            System.out.print("Magician\n");
            // The Magician - symbol of power, potential and the unification of physical and spiritual - Manipulation, untapped talent, illusion, deception - Teleports you to a random location in any dimension?
            setCard(stack, 2);
            if (isGood) {
                for (int i = 0; i < 4; ++i) {
                    Wolf wolf = (Wolf) EntityType.WOLF.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                    wolf.tame(player);
                }
            } else {
                for (int i = 0; i < 2; ++i) {
                    EntityType.ILLUSIONER.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                }
            }
        } else if (prob < 0.364D) {
            System.out.print("Lovers\n");
            // The Lovers - Attraction, beauty, trials overcome - Failure, foolish designs - Teleports to a structure?
            setCard(stack, 7);
            if (isGood) {
                for (int i = 0; i < 2; ++i) {
                    EntityType.AXOLOTL.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                }
            } else {
                for (int i = 0; i < 6; ++i) {
                    EntityType.SILVERFISH.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                }
            }
        } else if (prob < 0.4095D) {
            System.out.print("Devil\n");
            // The Devil - Ravage, violence, extraordinary efforts - Evil fatality, weakness, blindness - Summons a nether raid? Summons a boss?
            setCard(stack, 16);
            if (isGood) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1));
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600, 1));
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 1));
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1));
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600, 1));
                player.addEffect(new MobEffectInstance(SBEffects.BLEEDING.get(), 600, 1));
            }
        } else if (prob < 0.455D) {
            System.out.print("Hermit\n");
            // The Hermit - Prudence, treason, corruption - disguise, fear, paranoia - Grants defense status effects? Increases corruption?
            setCard(stack, 10);
            if (isGood) {
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200, 4));
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1200, 4));
                EntityType.WITCH.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
            }
        } else if (prob < 0.5005D) {
            System.out.print("Strength\n");
            // Strength - Power, energy, action, courage - Despotism, abuse, weakness - Grants damage status effects?
            setCard(stack, 9);
            if (isGood) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 4));
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, 4));
            }
        } else if (prob < 0.546D) {
            System.out.print("Wheel of Fortune\n");
            // Wheel of Fortune - Fortune, success, lick - increase abundance - Grants Luck or Unluck
            setCard(stack, 11);
            if (isGood) {
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 1200, 4));
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 1200, 4));
            }
        } else if (prob < 0.5915D) {
            System.out.print("Empress\n");
            // The Empress - Fruitfulness, action, length of days, difficulty - Light, truth - Summons a boss?
            setCard(stack, 4);
            if (isGood) {
                ((ServerLevel) level).setDayTime(0);
            } else {
                ((ServerLevel) level).setDayTime(13000);
            }
        } else  if (prob < 0.637D) {
            System.out.print("High Priestess\n");
            // The High Priestess - Secrets, Mystery, Tenacity, Wisdom, Science - passion, conceit, shallowness - Grants XP? Magic Levels? Replenishes Mana?
            setCard(stack, 3);
            if (isGood) {
                ItemStack stack2 = new ItemStack(SBItems.RESEARCH_BOOK.get());
                ResearchBook.assignResearchPoints(stack2, 10);
                player.getInventory().add(stack2);
            } else {
                player.getInventory().add(new ItemStack(SBItems.ALCHEMICAL_RESIDUE.get(), 4));
            }
        } else if (prob < 0.6825D) {
            System.out.print("Death\n");
            // Death - End, mortality, destruction, corruption - lethargy, sleep petrification - Causes explosions around the user? Summons a boss? Summons an undead raid?
            setCard(stack, 14);
            if (isGood) {
                player.getInventory().add(new ItemStack(SBItems.TOTEM_OF_AVENGING.get()));
            } else {
                player.die(DamageSource.ANVIL);
            }
        } else if (prob < 0.728D) {
            System.out.print("Justice\n");
            // Justice - Equality, rightness triumph of the deserving - Law, bigotry, bias - Clears all status effects and resets attributes? Does something against pillagers and piglins?
            setCard(stack, 12);
            if (isGood) {
                //give you MP
            } else {
                //drains MP
            }
        } else if (prob < 0.7735D) {
            System.out.print("Temperance\n");
            // Temperance - Economy, moderation, frugality, management - Religion, competing interests, sects - Gives haste? Spawns random ores?
            setCard(stack, 15);
            if (isGood) {
                player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 600, 1));
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 1200, 5));
            }
        } else if (prob < 0.8145D) {
            System.out.print("Sun");
            // The Sun - Material happiness, contentment - mild happiness and contentment - Spawns random ore? Summons friendly mobs?
            setCard(stack, 20);
            if (isGood) {
                player.getInventory().add(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
            } else {
                player.getInventory().add(new ItemStack(Items.EMERALD, 13));
            }
        } else if (prob < 0.8645D) {
            System.out.print("Hierophant\n");
            // The Hierophant - Alliance, captivity, servitude, inspiration - understanding, concord, kindness, weakness - Summons allied mobs? Heals allied mobs?
            setCard(stack, 6);
            if (isGood) {
                for (int i = 0; i < 2; ++i) {
                    SummonedVex vex = (SummonedVex) SBEntities.SUMMONED_VEX.get().spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                    vex.tame(player);
                    vex.setLimitedLife(40 * (30 + level.random.nextInt(90)));
                }
            } else {
                ItemStack stack2 = new ItemStack(SBItems.RESEARCH_BOOK.get());
                ResearchBook.assignResearchPoints(stack2, 4);
                player.getInventory().add(stack2);
            }
        } else if (prob < 0.91D) {
            System.out.print("Judgement\n");
            // Judgement - rebirth, absolution - self-doubt, inner critic - Grants totems? Grants extra lives?
            setCard(stack, 21);
            if (isGood) {
                // summons phoenix
            } else {
                player.getInventory().add(new ItemStack(SBItems.TAROT_CARD.get()));
            }
        } else if (prob < 0.9555D) {
            System.out.print("Star\n");
            // The Star - Hope, bright prospects - Loss, theft, abandonment - Grants Luck? Gives Haste?
            setCard(stack, 18);
            if (isGood) {
                player.getInventory().add(new ItemStack(Items.NETHER_STAR));
            } else {
                for (int i = 0; i < 4; ++i) {
                    Piglin piglin = (Piglin) EntityType.PIGLIN.spawn((ServerLevel) level, stack, player, getRandomEntitySpawnPos(player, level), MobSpawnType.MOB_SUMMONED, false, false);
                    piglin.setTarget(player);
                }
            }
        } else {
            System.out.print("World\n");
            // The World - Assured success, recompense, voyage, flight - Fixity, stagnation - Grants creative flight?
            setCard(stack, 22);
            if (isGood) {
                player.getInventory().add(new ItemStack(Items.ELYTRA));
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 600, 2));
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 600, 2));
            }
        }
    }

    public void resetCardDisplayDuration(ItemStack stack) {
        stack.getTag().putInt(DISPLAY_TIME, 20);
    }

    public boolean hasCard(ItemStack stack) {
        return stack.getOrCreateTag().getInt(CARD) > 0;
    }

    public void setCard(ItemStack stack, int cardId) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        compoundtag.putInt(CARD, cardId);
    }

    public BlockPos getRandomEntitySpawnPos(Player player, Level level) {
        int x = Mth.floor(player.getX());
        int z = Mth.floor(player.getZ());
        int y = Mth.floor(player.getY());

        for (int j = 0; j < 30; ++j) {
            int x1 = x + Mth.nextInt(player.getRandom(), 5, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            int y1 = y + Mth.nextInt(player.getRandom(), 5, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            int z1 = z + Mth.nextInt(player.getRandom(), 5, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            if (x != x1 && z != z1) {
                BlockPos spawnPoint = new BlockPos(x1, y1, z1);
                EntityType<?> entitytype = EntityType.ZOMBIE;
                SpawnPlacements.Type entityspawnplacementregistry$placementtype = SpawnPlacements.getPlacementType(entitytype);
                if (NaturalSpawner.isSpawnPositionOk(entityspawnplacementregistry$placementtype, player.level, spawnPoint, entitytype)
                        && SpawnPlacements.checkSpawnRules(entitytype, (ServerLevelAccessor) player.level, MobSpawnType.REINFORCEMENT, spawnPoint, level.random)
                        && level.isUnobstructed(player) && level.noCollision(player) && !level.containsAnyLiquid(player.getBoundingBox())
                ) {
                    return spawnPoint;
                }
            }
        }
        return player.blockPosition();
    }
}