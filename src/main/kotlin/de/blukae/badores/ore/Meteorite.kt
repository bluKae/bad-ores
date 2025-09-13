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

import de.blukae.badores.BadOres
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement

object Meteorite: BadOre("meteorite") {
    private const val METEORITE_SPAWN_SIDE = 50

    override fun heightPlacement(): HeightRangePlacement = HeightRangePlacement.uniform(
        VerticalAnchor.absolute(90),
        VerticalAnchor.top()
    )

    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean
    ) {
        if (level !is ServerLevel || !willHarvest) {
            return
        }

        val number = level.random.nextInt(20) + 3
        val state = if (level.random.nextBoolean()) Blocks.STONE.defaultBlockState() else Blocks.NETHERRACK.defaultBlockState()
        for (i in 0..<number) {
            val spawnX = pos.x.toDouble() + METEORITE_SPAWN_SIDE * (level.random.nextDouble() - level.random.nextDouble() - 0.5)
            val spawnY = level.maxY + 5.0
            val spawnZ = pos.z.toDouble() + METEORITE_SPAWN_SIDE * (level.random.nextDouble() - level.random.nextDouble() - 0.5)

            EntityType.FALLING_BLOCK.create(level, EntitySpawnReason.MOB_SUMMONED)?.also {
                it.setPos(spawnX, spawnY, spawnZ)
                it.blockState = state
                it.setDeltaMovement(level.random.nextDouble(), 0.0, level.random.nextDouble())
                level.addFreshEntity(it)
            }
        }
    }
}