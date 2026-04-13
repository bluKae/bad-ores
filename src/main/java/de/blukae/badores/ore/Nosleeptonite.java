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
import de.blukae.badores.entity.NosleeptoniteEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Nosleeptonite implements OreTemplate {

    public static final Supplier<EntityType<NosleeptoniteEntity>> NOSLEEPTONITE_ENTITY_TYPE =
            BadOres.ENTITY_TYPES.registerEntityType(
                    "nosleeptonite",
                    NosleeptoniteEntity::new,
                    MobCategory.MONSTER,
                    builder -> builder.sized(1.0f, 1.0f));

    public static final Supplier<SoundEvent> NOSLEEPTONITE_AMBIENT = BadOres.SOUND_EVENTS.register(
            "entity" + ".nosleeptonite.ambient",
            SoundEvent::createVariableRangeEvent);
    public static final Supplier<SoundEvent> NOSLEEPTONITE_DEATH = BadOres.SOUND_EVENTS.register(
            "entity" + ".nosleeptonite.death",
            SoundEvent::createVariableRangeEvent);
    public static final Supplier<SoundEvent> NOSLEEPTONITE_HURT = BadOres.SOUND_EVENTS.register(
            "entity.nosleeptonite" + ".hurt",
            SoundEvent::createVariableRangeEvent);
    public static final Supplier<SoundEvent> NOSLEEPTONITE_IDLE_SOUND_EVENT = BadOres.SOUND_EVENTS.register(
            "nosleeptonite.idle",
            SoundEvent::createVariableRangeEvent);

    public static final DeferredItem<SpawnEggItem> NOSLEEPTONITE_SPAWN_EGG = BadOres.ITEMS.registerItem(
            "nosleeptonite_spawn_egg",
            SpawnEggItem::new,
            properties -> properties.spawnEgg(NOSLEEPTONITE_ENTITY_TYPE.get()));

    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.TERRACOTTA_ORANGE;
    }

    @Override
    public HeightRangePlacement getHeightPlacementModifier() {
        return HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(5), VerticalAnchor.aboveBottom(40));
    }

    @Override
    public PlacementModifier getPlacementModifier() {
        return RarityFilter.onAverageOnceEvery(40);
    }

    @Override
    public LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return LootTable.lootTable();
    }

    @Override
    public IntProvider getTickRate(boolean isOreBlock) {
        return isOreBlock ? ConstantInt.of(1000) : null;
    }

    @Override
    public void onTick(Level level, BlockPos pos, BlockState state, BadOreBlockEntity blockEntity) {
        if (state.is(BadOre.NOSLEEPTONITE.ores)) {
            level.playSound(null, pos, NOSLEEPTONITE_IDLE_SOUND_EVENT.get(), SoundSource.BLOCKS);
        }
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && willHarvest) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400));
        }
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack,
                                boolean dropExperience) {
        NosleeptoniteEntity entity = NOSLEEPTONITE_ENTITY_TYPE.get().create(level, EntitySpawnReason.TRIGGERED);
        if (entity != null) {
            entity.snapTo(pos.getBottomCenter());
            level.addFreshEntity(entity);
            entity.spawnAnim();
            entity.playAmbientSound();
        }
    }
}
