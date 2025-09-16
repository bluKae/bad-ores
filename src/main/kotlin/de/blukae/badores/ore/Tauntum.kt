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

import de.blukae.badores.BadOreBlockEntity
import de.blukae.badores.mixin.MobMixin
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter

object Tauntum : BadOre("tauntum") {
    override fun size() = 3
    override fun placement(): PlacementModifier = RarityFilter.onAverageOnceEvery(100)
    override fun tickRate(isIngotBlock: Boolean): IntProvider? = UniformInt.of(40, 40 + 399)

    private var mobSounds: List<SoundEvent>? = null

    private fun getMobSounds(level: Level): List<SoundEvent> {
        return mobSounds ?: return BuiltInRegistries.ENTITY_TYPE.asSequence()
            .mapNotNull {
                val mob = it.create(level, EntitySpawnReason.MOB_SUMMONED)
                if (mob is Mob) (mob as MobMixin).invokeGetAmbientSound() else null
            }
            .plus(SoundEvents.CREEPER_PRIMED)
            .toList()
            .also {
                mobSounds = it
            }
    }

    override fun onTick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BadOreBlockEntity) {
        if (level is ServerLevel) {
            level.playSound(null, pos, getMobSounds(level).random(), SoundSource.BLOCKS)
        }
    }
}