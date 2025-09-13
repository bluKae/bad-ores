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

import com.mojang.serialization.MapCodec
import de.blukae.badores.ArmorInfo
import de.blukae.badores.RandomTranslation
import de.blukae.badores.ToolInfo
import kotlinx.coroutines.delay
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.storage.ServerLevelData
import net.minecraft.world.level.timers.TimerCallback
import net.minecraft.world.level.timers.TimerQueue
import net.minecraft.world.ticks.TickPriority
import net.neoforged.neoforge.common.NeoForgeEventHandler
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_CONTEXT
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.system.exitProcess


object Crashium: BadOre("crashium") {
    const val CRASH_PROBABILITY = 5

    override fun hasIngot() = true
    override fun tools() = ToolInfo(2, 250, 8.0f, 2.0f, 8)
    override fun armor() = ArmorInfo(8, intArrayOf(2, 7, 5, 2), 9)


    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean
    ) {
        if (player is ServerPlayer && !player.preventsBlockDrops()) {
            crash(player)
        }
    }

    override fun onMine(stack: ItemStack, level: Level, state: BlockState, pos: BlockPos, miningEntity: LivingEntity) {
        if (miningEntity is ServerPlayer) {
            crash(miningEntity)
        }
    }

    override fun onHurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if (attacker is ServerPlayer) {
            crash(attacker)
        }
    }

    override fun onArmorHurt(entity: LivingEntity, stack: ItemStack, slot: EquipmentSlot) {
        if (entity is ServerPlayer) {
            crash(entity)
        }
    }

    override fun onArmorTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot) {
        if (level.random.nextInt(800) == 0 && entity is ServerPlayer) {
            crash(entity)
        }
    }

    private fun crash(player: ServerPlayer) {
        thread {
            RandomTranslation("badores.crashium.precrash", "Precrash").send(player)
            Thread.sleep(80 * 20)
            if (Random.nextInt(CRASH_PROBABILITY) == 0) {
                RandomTranslation("badores.crashium.crash", "Crash").send(player)
                Thread.sleep(80 * 20)
                exitProcess(1)
            } else {
                RandomTranslation("badores.crashium.nocrash", "Nocrash").send(player)
            }
        }
    }

    override fun ingotName(): String = "${name}_gemstone"
    override fun ingotTranslation(): String = "${translation()} Gemstone"
}