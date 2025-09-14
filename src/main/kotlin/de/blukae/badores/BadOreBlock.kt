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
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class BadOreBlock(val ore: BadOre, val isIngotBlock: Boolean, properties: Properties) : Block(properties), EntityBlock {
    val tickRate = ore.tickRate(isIngotBlock)

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): InteractionResult {
        return ore.onUseItemOn(stack, state, level, pos, player, hand, hitResult)
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        return ore.onUse(state, level, pos, player, hitResult)
    }

    override fun attack(state: BlockState, level: Level, pos: BlockPos, player: Player) {
        super.attack(state, level, pos, player)
        ore.onAttack(state, level, pos, player)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return if (tickRate != null) BadOres.BAD_ORE_BLOCK_ENTITY.create(pos, state) else null
    }

    override fun spawnAfterBreak(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        stack: ItemStack,
        dropExperience: Boolean
    ) {
        super.spawnAfterBreak(state, level, pos, stack, dropExperience)
        ore.spawnAfterBreak(state, level, pos, stack, dropExperience)
    }

    override fun onBlockExploded(state: BlockState, level: ServerLevel, pos: BlockPos, explosion: Explosion) {
        super.onBlockExploded(state, level, pos, explosion)
        ore.onExploded(state, level, pos, explosion)
    }

    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean,
        fluid: FluidState
    ): Boolean {
        if (level is ServerLevel && player is ServerPlayer && !isIngotBlock) {
            BadOres.MINE_BAD_ORE_TRIGGER.trigger(player, level, pos, state, player.mainHandItem)
        }

        ore.onDestroyedByPlayer(state, level, pos, player, willHarvest)
        ore.onDestroyed(state, level, pos)
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)
    }

    override fun getDrops(state: BlockState, params: LootParams.Builder): List<ItemStack?> {
        return super.getDrops(state, params)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T?>
    ): BlockEntityTicker<T>? {
        return if (blockEntityType == BadOres.BAD_ORE_BLOCK_ENTITY && tickRate != null)
            BlockEntityTicker { level, pos, state, t -> tick(level, pos, state, t as BadOreBlockEntity) }
        else null
    }

    fun tick(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        blockEntity: BadOreBlockEntity
    ) {
        if (blockEntity.tickTime == 0) {
            blockEntity.tickTime = tickRate!!.sample(level.random)

            ore.onTick(level, pos, state, blockEntity)
        }

        blockEntity.tickTime--
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        super.randomTick(state, level, pos, random)
        ore.onRandomTick(state, level, pos, random)
    }

    override fun getCollisionShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return super.getShape(state, level, pos, context)
    }

    override fun getDestroyProgress(state: BlockState, player: Player, level: BlockGetter, pos: BlockPos): Float {
        val progress = ore.getDestroyProgress(state, player, level, pos)
        return if (!progress.isNaN()) progress else super.getDestroyProgress(state, player, level, pos)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return ore.shape(isIngotBlock, level, pos, context) ?: super.getShape(state, level, pos, context)
    }

    companion object {
        fun build(ore: BadOre, isIngotBlock: Boolean) = { properties: Properties ->
            BadOreBlock(ore, isIngotBlock, properties)
        }
    }
}