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
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

object Streetscum : BadOre("streetscum") {
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

        val availableItems = player.inventory.mapIndexedNotNull { index, stack -> if (stack.isEmpty) null else index }
            .toMutableList()

        val remove = level.random.nextInt(availableItems.size / 3 + 1)
        for (i in 0..<remove) {
            val pos = level.random.nextInt(availableItems.size)
            player.inventory.setItem(availableItems[pos], ItemStack.EMPTY)
            availableItems.removeAt(pos)
        }
    }
}