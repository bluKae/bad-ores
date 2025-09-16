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

import de.blukae.badores.RandomTranslation
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

object Misleadium : BadOre("misleadium") {
    const val SIDE_RANGE = 500

    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean
    ) {
        if (player is ServerPlayer && willHarvest) {
            CreativeModeTabs.tryRebuildTabContents(level.enabledFeatures(), true, level.registryAccess())
            val stack = CreativeModeTabs.allTabs().flatMap { it.displayItems }.random()

            val x = (pos.x + level.random.nextInt(SIDE_RANGE) - level.random.nextInt(SIDE_RANGE))
            val y = (level.random.nextInt(level.minY, level.maxY))
            val z = (pos.z + level.random.nextInt(SIDE_RANGE) - level.random.nextInt(SIDE_RANGE))
            RandomTranslation("badores.misleadium.baseMessage", "Mislead", stack.hoverName, x, y, z).send(player)
        }
    }
}