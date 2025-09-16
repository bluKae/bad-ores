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
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.minecraft.world.level.storage.loot.LootTable

object Nosleeptonite : BadOre("nosleeptonite") {
    override fun heightPlacement(): HeightRangePlacement = HeightRangePlacement.uniform(
        VerticalAnchor.aboveBottom(5),
        VerticalAnchor.aboveBottom(40)
    )

    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder? = LootTable.lootTable()

    override fun hasIngot() = true
    override fun placement(): PlacementModifier = RarityFilter.onAverageOnceEvery(40)
    override fun tickRate(isIngotBlock: Boolean): IntProvider? = if (isIngotBlock) null else ConstantInt.of(1000)

    override fun onRandomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.`is`(oreBlock) || state.`is`(deepslateOreBlock!!)) {
            level.playSound(null, pos, BadOres.NOSLEEPTONITE_IDLE_SOUND_EVENT, SoundSource.BLOCKS)
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
            player.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 400))
        }
    }

    override fun spawnAfterBreak(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        stack: ItemStack,
        dropExperience: Boolean
    ) {
        BadOres.NOSLEEPTONITE_ENTITY_TYPE.create(level, EntitySpawnReason.TRIGGERED)?.also {
            it.snapTo(pos.bottomCenter)
            level.addFreshEntity(it)
            it.spawnAnim()
        }
    }
}