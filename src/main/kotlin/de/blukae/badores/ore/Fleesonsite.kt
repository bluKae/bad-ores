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
import de.blukae.badores.BadOres
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.animal.Chicken
import net.minecraft.world.entity.animal.ChickenVariant
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.phys.BlockHitResult

object Fleesonsite: BadOre("fleesonsite") {
    override fun tickRate(isIngotBlock: Boolean): IntProvider? = if (isIngotBlock) null else ConstantInt.of(40)
    override fun hasIngot() = true
    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder? = LootTable.lootTable()


    private fun flee(level: ServerLevel, pos: BlockPos, state: BlockState, destroyBlock: Boolean) {
        if (destroyBlock) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
        }

        BadOres.FLEESONSITE_ENTITY_TYPE.create(level, EntitySpawnReason.TRIGGERED)?.also {
            it.snapTo(pos.bottomCenter)
            it.isDeepslate = !state.`is`(oreBlock)
            it.spawnAnim()
            level.addFreshEntity(it)
        }
    }

    override fun onTick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BadOreBlockEntity) {
        if (level is ServerLevel) {
            if (state.`is`(oreBlock) || deepslateOreBlock?.let { state.`is`(it) } ?: false) {
                if (level.getNearestPlayer(pos.center.x, pos.center.y, pos.center.z, 10.0, false) != null) {
                    flee(level, pos, state, true)
                }
            }
        }
    }

    override fun onAttack(state: BlockState, level: Level, pos: BlockPos, player: Player) {
        if (level is ServerLevel) {
            flee(level, pos, state, true)
        }
    }

    override fun onUse(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        if (level is ServerLevel) {
            flee(level, pos, state, true)
        }

        return InteractionResult.SUCCESS_SERVER
    }

    override fun spawnAfterBreak(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        stack: ItemStack,
        dropExperience: Boolean
    ) {
        flee(level, pos, state, false)
    }
}