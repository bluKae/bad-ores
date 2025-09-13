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
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack


object Nopium: BadOre("nopium") {
    override fun hasIngot() = true
    override fun tools() = ToolInfo(2, 800, 0.5f, 1.0f, 1)
    override fun armor() = ArmorInfo(15, intArrayOf(2, 6, 5, 2), 1)

    override fun onInventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        if (entity !is LivingEntity || level.random.nextInt(200) != 0) {
            return
        }

        if (slot == null) {
            if (entity !is Player) {
                return
            }
            entity.inventory.forEachIndexed { index, s ->
                if (s === stack) {
                    entity.inventory.setItem(index,
                        ItemStack.EMPTY)
                }
            }
        } else {
            entity.setItemSlot(slot, ItemStack.EMPTY)
        }

        entity.drop(stack, true, true)
    }
}