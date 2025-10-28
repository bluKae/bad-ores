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

import com.mojang.serialization.Codec;
import de.blukae.badores.BadOres;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import java.util.function.Supplier;

public class Stonium implements OreTemplate {

    public static final Supplier<StoniumFeature> STONIUM_FEATURE = BadOres.FEATURES.register(
            "stonium",
            () -> new StoniumFeature(OreConfiguration.CODEC));

    @Override
    public int getSize() {
        return 10;
    }

    @Override
    public PlacementModifier getPlacementModifier() {
        return CountPlacement.of(UniformInt.of(0, 19));
    }

    @Override
    public OreFeature getFeature() {
        return STONIUM_FEATURE.get();
    }

    @Override
    public TagKey<Block> levelTag() {
        return BlockTags.NEEDS_STONE_TOOL;
    }

    @Override
    public BlockBehaviour.Properties getOreBlockProperties(boolean isDeepslate) {
        return OreTemplate.super.getOreBlockProperties(isDeepslate).strength(1.0F, 3.0F);
    }

    @Override
    public LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.COBBLESTONE)));
    }

    public static class StoniumFeature extends OreFeature {
        public StoniumFeature(Codec<OreConfiguration> codec) {
            super(codec);
        }

        @Override
        public boolean place(OreConfiguration config, WorldGenLevel level, ChunkGenerator chunkGenerator,
                             RandomSource random, BlockPos origin) {
            int i = 0;
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            int chunkX = SectionPos.blockToSectionCoord(origin.getX()) * 16;
            int chunkZ = SectionPos.blockToSectionCoord(origin.getZ()) * 16;

            try (BulkSectionAccess access = new BulkSectionAccess(level)) {
                LevelChunkSection section = access.getSection(origin.atY(44));
                if (section != null) {
                    for (int y = 12; y < 16; y++) {
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                if (random.nextInt(2) == 0) {
                                    pos.set(chunkX + x, y, chunkZ + z);

                                    if (level.ensureCanWrite(pos)) {
                                        BlockState state = section.getBlockState(x, y, z);

                                        for (OreConfiguration.TargetBlockState targetState : config.targetStates) {
                                            if (canPlaceOre(
                                                    state,
                                                    access::getBlockState,
                                                    random,
                                                    config,
                                                    targetState,
                                                    pos)) {
                                                section.setBlockState(x, y, z, targetState.state, false);
                                                i++;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return i > 0;
        }
    }
}
