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
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3


object Smite : BadOre("smite") {
    override fun hasIngot() = true
    override fun tools() = ToolInfo(2, 220, 5.0f, 2.0f, 8)
    override fun armor() = ArmorInfo(8, intArrayOf(2, 5, 4, 2), 8)

    private fun spawnLightning(level: ServerLevel, pos: Vec3) {
        val lightning = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.MOB_SUMMONED) ?: return
        lightning.setPos(pos)
        level.addFreshEntity(lightning)
    }

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

        if (level.random.nextInt(3) == 0) {
            spawnLightning(level, player.position())
        } else {
            spawnLightning(level, pos.bottomCenter)
        }
    }

    override fun onExploded(state: BlockState, level: ServerLevel, pos: BlockPos, explosion: Explosion) {
        spawnLightning(level, pos.bottomCenter)
    }

    override fun onArmorTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot) {
        if (level.random.nextInt(200) == 0) {
            spawnLightning(level, entity.position())
        }
    }

    override fun onHurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val level = attacker.level()

        if (level is ServerLevel && level.random.nextInt(2) == 0) {
            spawnLightning(level, if (level.random.nextBoolean()) attacker.position() else target.position())
        }
    }

    override fun ingotName(): String = "${name}_gemstone"
    override fun ingotTranslation(): String = "${translation()} Gemstone"
}