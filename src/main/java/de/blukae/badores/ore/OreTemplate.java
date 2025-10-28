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
import de.blukae.badores.util.ArmorInfo;
import de.blukae.badores.util.ToolInfo;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public interface OreTemplate {
    default boolean hasDeepslateVariant() {
        return true;
    }

    default boolean hasIngot() {
        return false;
    }

    default boolean hasRawIngot() {
        return hasIngot();
    }

    default boolean hasIngotBlock() {
        return hasIngot();
    }

    default MapColor getMapColor() {
        return MapColor.METAL;
    }

    @Nullable
    default ArmorInfo getArmorInfo() {
        return null;
    }

    @Nullable
    default ToolInfo getToolInfo() {
        return null;
    }

    // Feature Settings

    default int getSize() {
        return 8;
    }

    default HeightRangePlacement getHeightPlacementModifier() {
        return HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64));
    }

    default PlacementModifier getPlacementModifier() {
        return CountPlacement.of(8);
    }

    default RuleTest getTarget() {
        return new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    }

    default HolderSet<Biome> getBiomes(HolderGetter<Biome> lookup) {
        return lookup.getOrThrow(BiomeTags.IS_OVERWORLD);
    }

    default Feature<OreConfiguration> getFeature() {
        return OreFeature.ORE;
    }

    default TagKey<Block> toolTag() {
        return BlockTags.MINEABLE_WITH_PICKAXE;
    }

    @Nullable
    default TagKey<Block> levelTag() {
        return BlockTags.NEEDS_IRON_TOOL;
    }

    default BlockBehaviour.Properties getOreBlockProperties(boolean isDeepslate) {
        return BlockBehaviour.Properties.of()
                .mapColor(isDeepslate ? MapColor.DEEPSLATE : MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(isDeepslate ? 4.5F : 3.0F, 3.0F)
                .sound(isDeepslate ? SoundType.DEEPSLATE : SoundType.STONE);
    }

    default boolean hasCustomModels() {
        return false;
    }

    default void buildCustomModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    }

    @Nullable
    default LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return null;
    }

    default ResourceLocation getEquipmentTextureLocation(String name) {
        return BadOres.rl(name);
    }

    default float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return Float.NaN;
    }

    @Nullable
    default VoxelShape getShape(boolean isOre, BlockState state, BlockGetter level, BlockPos pos,
                                CollisionContext context) {
        return null;
    }

    default String getIngotName(String name) {
        return hasRawIngot() ? name + "_ingot" : name;
    }

    default String getTranslation(String name) {
        String[] parts = name.split("_");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = Character.toUpperCase(parts[i].charAt(0)) + parts[i].substring(1);
        }

        return String.join(" ", parts);
    }

    default String getIngotTranslation(String translation) {
        return hasRawIngot() ? translation + " Ingot" : translation;
    }

    @Nullable
    default IntProvider getTickRate(boolean isOreBlock) {
        return null;
    }

    default void onTick(Level level, BlockPos pos, BlockState state, BadOreBlockEntity blockEntity) {
    }

    default void onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    default void onInventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
    }

    default void onArmorTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
    }

    default void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
    }

    default void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack,
                                 boolean dropExperience) {
    }

    default void onExploded(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion) {
    }

    default void onEntityHurt(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    }

    default void onMine(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
    }

    default void onArmorHurt(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
    }

    default InteractionResult onUseItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player
            , InteractionHand hand, BlockHitResult hitResult) {
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    default InteractionResult onUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               HitResult hitResult) {
        return InteractionResult.PASS;
    }

    default void onAttack(BlockState state, Level level, BlockPos pos, Player player) {
    }
}
