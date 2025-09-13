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

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootTable

object Movium: BadOre("movium") {
    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean
    ) {
        if (level !is ServerLevel || player.preventsBlockDrops()) {
            return
        }

        val p = pos.offset(Direction.getRandom(level.random).unitVec3i)
        level.setBlockAndUpdate(p, state)
    }

    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder? = LootTable.lootTable()
}