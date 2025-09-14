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

package de.blukae.badores.ore

import com.mojang.serialization.Codec
import de.blukae.badores.BadOres
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.SectionPos
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.item.Items
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.chunk.BulkSectionAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.OreFeature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem


object Stonium : BadOre("stonium") {
    override fun placement(): PlacementModifier = CountPlacement.of(UniformInt.of(0, 19))

    override fun size(): Int = 10

    override fun levelTag(): TagKey<Block>? = BlockTags.NEEDS_STONE_TOOL

    override fun destroyTime(): Float = 1.0F

    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder? = LootTable.lootTable()
        .withPool(
            LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.COBBLESTONE))
        )

    override fun feature(): OreFeature = BadOres.STONIUM_FEATURE

    class Feature(codec: Codec<OreConfiguration>) : OreFeature(codec) {
        override fun place(
            config: OreConfiguration,
            level: WorldGenLevel,
            chunkGenerator: ChunkGenerator,
            random: RandomSource,
            origin: BlockPos
        ): Boolean {
            var i = 0
            val pos = MutableBlockPos()

            val chunkX = SectionPos.blockToSectionCoord(origin.x) * 16
            val chunkZ = SectionPos.blockToSectionCoord(origin.z) * 16

            BulkSectionAccess(level).use { access ->
                access.getSection(origin.atY(44))?.let { section ->
                    for (y in 12..<16) {
                        for (x in 0..<16) {
                            for (z in 0..<16) {
                                if (random.nextInt(2) == 0) {
                                    pos.set(chunkX + x, y, chunkZ + z)

                                    if (level.ensureCanWrite(pos)) {
                                        val state = section.getBlockState(x, y, z)

                                        for (targetState in config.targetStates) {
                                            if (canPlaceOre(
                                                    state,
                                                    { access.getBlockState(it) },
                                                    random,
                                                    config,
                                                    targetState,
                                                    pos,
                                                )
                                            ) {
                                                section.setBlockState(
                                                    x,
                                                    y,
                                                    z,
                                                    targetState.state,
                                                    false
                                                )
                                                i++
                                                break
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return i > 0
        }

    }
}