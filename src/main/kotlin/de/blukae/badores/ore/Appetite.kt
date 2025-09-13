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

import de.blukae.badores.MiningConsequence
import net.minecraft.core.BlockPos
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest
import net.minecraft.world.level.storage.loot.LootTable

object Appetite: BadOre("appetite") {

    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder = LootTable.lootTable()

    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean
    ) {
        if (level !is ServerLevel) {
            return
        }

        player.foodData.eat(4, 0.2F)
    }

    override fun oreBlockProperties(): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)
    override fun heightPlacement(): HeightRangePlacement = HeightRangePlacement.uniform(
        VerticalAnchor.absolute(64),
        VerticalAnchor.absolute(128)
    )

    override fun hasDeepslateVariant() = false
    override fun replace() = BlockMatchTest(Blocks.SAND)
    override fun levelTag(): TagKey<Block>? = null
    override fun toolTag(): TagKey<Block> = BlockTags.MINEABLE_WITH_SHOVEL
}