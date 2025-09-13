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

import de.blukae.badores.ArmorInfo
import de.blukae.badores.ToolInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState


object Enderite: BadOre("enderite") {
    private const val RADIUS: Int = 40

    override fun tools() = ToolInfo(1, 120, 4.0f, 1.0f, 15)
    override fun armor() = ArmorInfo(17, intArrayOf(2, 5, 4, 1), 20)
    override fun hasIngot() = true

    private fun teleportEntity(level: ServerLevel, origin: BlockPos, entity: LivingEntity) {
        val pos = BlockPos(
            level.random.nextIntBetweenInclusive(origin.x - RADIUS, origin.x + RADIUS),
            level.random.nextIntBetweenInclusive(level.minY + 10, level.maxY - 10),
            level.random.nextIntBetweenInclusive(origin.z - RADIUS, origin.z + RADIUS),
        )

        for (i in 0..<128) {
            val factor = i / 128.0
            val particlePos = pos.center.add(pos.subtract(origin).center.multiply(factor, factor, factor))

            level.sendParticles(ParticleTypes.PORTAL, particlePos.x, particlePos.y, particlePos.z, 1, 0.0, 0.0, 0.0, 1.0)
        }

        val teleportPos = pos.bottomCenter
        entity.teleportTo(teleportPos.x, teleportPos.y, teleportPos.z)
        level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS)
    }

    override fun onMine(stack: ItemStack, level: Level, state: BlockState, pos: BlockPos, miningEntity: LivingEntity) {
        if (level is ServerLevel && level.random.nextInt(5) == 0) {
            teleportEntity(level, miningEntity.blockPosition(), miningEntity)
        }
    }

    override fun onArmorTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot) {
        if (entity is LivingEntity && level.random.nextInt(1000) == 0) {
            teleportEntity(level, entity.blockPosition(), entity)
        }
    }

    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean
    ) {
        if (level is ServerLevel && willHarvest) {
            teleportEntity(level, pos, player)
        }
    }
}