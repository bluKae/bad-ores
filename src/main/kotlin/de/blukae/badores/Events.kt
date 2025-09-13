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

import de.blukae.badores.ore.Fleesonsite
import de.blukae.badores.ore.Ghostium
import de.blukae.badores.ore.Iwontfite
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.event.level.BlockDropsEvent
import net.neoforged.neoforge.event.level.NoteBlockEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent


@EventBusSubscriber
object Events {

    @SubscribeEvent
    fun onBlockDrops(event: BlockDropsEvent) {
        if (event.state.block == Ghostium.oreBlock.get()) {
            event.isCanceled = true
            for (entity in event.drops) {
                entity.setNeverPickUp()
                event.level.addFreshEntity(entity)
            }
        }
    }

    @SubscribeEvent
    fun onDamage(event: LivingIncomingDamageEvent) {
        val hasIwontfite = when (val source = event.source.entity) {
            is Player -> if (source.isCreative) false else source.inventory.contains { it.`is`(Iwontfite.oreBlock.asItem()) }

            is LivingEntity -> EquipmentSlot.entries.asSequence()
                .map { source.getItemBySlot(it) }
                .any { it.`is`(Iwontfite.oreBlock.asItem()) }

            else -> false
        }

        if (hasIwontfite) {
            if (event.entity.random.nextInt(1000) == 0) {
                event.amount = 1.0F
            } else {
                event.isCanceled = true
            }
        }
    }

    @SubscribeEvent
    fun onArmorHurt(event: ArmorHurtEvent) {
        event.armorMap.forEach { (slot, entry) ->
            val item = entry.armorItemStack.item
            if (item is BadOreItem) {
                item.ore.onArmorHurt(event.entity, entry.armorItemStack, slot)
            }
        }
    }

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent.Pre) {
        if (event.entity.level().isClientSide) {
            return
        }

        val inventory = event.entity.inventory

        inventory.forEachIndexed { index, stack ->
            val item = stack.item
            if (item is BadOreItem && item.ore == Fleesonsite && event.entity.random.nextInt(150) == 0) {
                try {
                    val target = inventory.mapIndexedNotNull { index, stack -> if (stack.isEmpty) index else null }
                        .random()
                    inventory.setItem(index, ItemStack.EMPTY)
                    inventory.setItem(target, stack)
                } catch(e: NoSuchElementException) {}
            }
        }
    }
}