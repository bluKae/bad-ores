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

package de.blukae.badores

import de.blukae.badores.ore.BadOre
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class BadOreItem(val ore: BadOre, properties: Properties) : Item(properties) {
    override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        super.inventoryTick(stack, level, entity, slot)
        ore.onInventoryTick(stack, level, entity, slot)
        if (slot?.isArmor ?: false) {
            ore.onArmorTick(stack, level, entity, slot)
        }
    }

    override fun postHurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        super.postHurtEnemy(stack, target, attacker)
        ore.onHurtEnemy(stack, target, attacker)
    }

    override fun mineBlock(
        stack: ItemStack,
        level: Level,
        state: BlockState,
        pos: BlockPos,
        miningEntity: LivingEntity
    ): Boolean {
        ore.onMine(stack, level, state, pos, miningEntity)
        return super.mineBlock(stack, level, state, pos, miningEntity)
    }

    companion object {
        fun build(ore: BadOre) = { properties: Properties ->
            BadOreItem(ore, properties)
        }
    }
}