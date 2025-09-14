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
import de.blukae.badores.BadOreBlockEntity
import de.blukae.badores.ToolInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.NoteBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.minecraft.world.phys.Vec3


object Amadeum : BadOre("amadeum") {
    override fun placement(): PlacementModifier = RarityFilter.onAverageOnceEvery(100)
    override fun tickRate(isIngotBlock: Boolean): IntProvider? = UniformInt.of(18, 18 + 5)
    override fun hasIngot() = true
    override fun tools() = ToolInfo(2, 250, 6.0f, 2.0f, 20)
    override fun armor() = ArmorInfo(10, intArrayOf(2, 6, 5, 2), 20)


    val sounds = NoteBlockInstrument.entries
        .filter { it.isTunable }
        .map { it.soundEvent }
        .toList()

    fun playRandomSound(level: Level, pos: Vec3) {
        val pitch = NoteBlock.getPitchFromNote(level.random.nextInt(25))
        level.playSound(null, pos.x, pos.y, pos.z, sounds.random(), SoundSource.BLOCKS, 3.0F, pitch)
    }

    fun playRandomItemSound(level: Level, entity: Entity) {
        if (level.random.nextInt(200) == 0) {
            playRandomSound(level, entity.position())
        }
    }

    override fun onTick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BadOreBlockEntity) {
        if (level !is ServerLevel) {
            return
        }

        playRandomSound(level, pos.center)
    }

    override fun onInventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        playRandomItemSound(level, entity)
    }
}