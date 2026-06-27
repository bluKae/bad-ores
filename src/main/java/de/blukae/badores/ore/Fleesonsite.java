/*
 * Copyright (C) 2025 bluKae
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.blukae.badores.ore;

import de.blukae.badores.BadOres;
import de.blukae.badores.block.BadOreBlockEntity;
import de.blukae.badores.entity.FleesonsiteEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Fleesonsite implements OreTemplate {

    public static final Supplier<EntityType<FleesonsiteEntity>> FLEESONSITE_ENTITY_TYPE =
            BadOres.ENTITY_TYPES.registerEntityType(
                    "fleesonsite",
                    FleesonsiteEntity::new,
                    MobCategory.CREATURE,
                    builder -> builder.sized(1.0f, 1.0f));

    public static final Supplier<SoundEvent> FLEESONSITE_AMBIENT = BadOres.SOUND_EVENTS.register(
            "entity.fleesonsite" + ".ambient",
            SoundEvent::createVariableRangeEvent);
    public static final Supplier<SoundEvent> FLEESONSITE_DEATH = BadOres.SOUND_EVENTS.register(
            "entity.fleesonsite" + ".death",
            SoundEvent::createVariableRangeEvent);
    public static final Supplier<SoundEvent> FLEESONSITE_HURT = BadOres.SOUND_EVENTS.register(
            "entity.fleesonsite" + ".hurt",
            SoundEvent::createVariableRangeEvent);

    public static final DeferredItem<SpawnEggItem> FLEESONSITE_SPAWN_EGG = BadOres.ITEMS.registerItem(
            "fleesonsite_spawn_egg",
            SpawnEggItem::new,
            properties -> properties.spawnEgg(FLEESONSITE_ENTITY_TYPE.get()));

    public static final DeferredItem<SpawnEggItem> DEEPSLATE_FLEESONSITE_SPAWN_EGG = BadOres.ITEMS.registerItem(
            "deepslate_fleesonsite_spawn_egg", SpawnEggItem::new, properties -> {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("IsDeepslate", true);
                return properties.component(
                        DataComponents.ENTITY_DATA,
                        TypedEntityData.of(FLEESONSITE_ENTITY_TYPE.get(), tag));
            });

    public static final TagKey<Item> FLEESONSITE_JUMP = TagKey.create(Registries.ITEM, BadOres.rl("fleesonsite_jump"));

    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.DIAMOND;
    }

    @Override
    public LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return LootTable.lootTable();
    }

    @Override
    public IntProvider getTickRate(boolean isOreBlock) {
        return isOreBlock ? ConstantInt.of(40) : null;
    }

    @Override
    public void onTick(Level level, BlockPos pos, BlockState state, BadOreBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            if (level.getNearestPlayer(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    10.0,
                    true) != null) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                flee(level, pos, state);
            }
        }
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack,
                                boolean dropExperience) {
        flee(level, pos, state);
    }

    @Override
    public InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                              HitResult hitResult) {
        if (state.is(BadOre.FLEESONSITE.ores)) {
            if (!level.isClientSide()) {
                flee(level, pos, state);
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onAttack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide()) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            flee(level, pos, state);
        }
    }

    private void flee(Level level, BlockPos pos, BlockState state) {
        FleesonsiteEntity entity = FLEESONSITE_ENTITY_TYPE.get().create(level, EntitySpawnReason.TRIGGERED);
        if (entity != null) {
            entity.snapTo(Vec3.atBottomCenterOf(pos));
            entity.setDeepslate(state.is(BadOre.FLEESONSITE.deepslateOreBlock));
            level.addFreshEntity(entity);
            entity.spawnAnim();
        }
    }
}
