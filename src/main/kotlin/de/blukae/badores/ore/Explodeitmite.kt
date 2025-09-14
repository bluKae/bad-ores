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
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter


object Explodeitmite : BadOre("explodeitmite") {
    override fun placement(): PlacementModifier = RarityFilter.onAverageOnceEvery(10)
    override fun addBlockProperties(properties: BlockBehaviour.Properties): BlockBehaviour.Properties =
        properties.randomTicks()

    override fun destroyTime() = 8.0f
    override fun explosionResistance() = 10.0f

    override fun onRandomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (random.nextInt(4) == 0) {
            level.removeBlock(pos, false)
            val p = pos.center.add(Direction.UP.unitVec3)
            level.explode(
                null,
                p.x,
                p.y,
                p.z,
                2.0f + random.nextFloat() * 3.0f,
                false,
                Level.ExplosionInteraction.BLOCK
            )
        }
    }

    override fun onDestroyed(state: BlockState, level: Level, pos: BlockPos) {
        if (level !is ServerLevel) {
            return
        }

        if (level.random.nextInt(4) == 0) {
            val p = pos.center.add(Direction.UP.unitVec3)
            level.explode(
                null,
                p.x,
                p.y,
                p.z,
                2.0f + level.random.nextFloat() * 3.0f,
                false,
                Level.ExplosionInteraction.BLOCK
            )
        }
    }
}