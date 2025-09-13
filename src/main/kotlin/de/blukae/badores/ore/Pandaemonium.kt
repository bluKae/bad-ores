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
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.util.DeferredSoundType


object Pandaemonium: BadOre("pandaemonium") {
    override fun tickRate(isIngotBlock: Boolean): IntProvider? = ConstantInt.of(1000)
    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder? = LootTable.lootTable()
        .withPool(LootPool.lootPool()
            .setRolls(UniformGenerator.between(0.0f, 2.0f))
            .add(LootItem.lootTableItem(Blocks.NETHERRACK))
            .add(LootItem.lootTableItem(Blocks.OBSIDIAN))
            .add(LootItem.lootTableItem(Items.NETHER_WART))
            .add(LootItem.lootTableItem(Items.FIRE_CHARGE))
            .add(LootItem.lootTableItem(Items.BLAZE_ROD))
            .add(LootItem.lootTableItem(Items.MAGMA_CREAM)))

    override fun heightPlacement(): HeightRangePlacement = HeightRangePlacement.uniform(
        VerticalAnchor.bottom(),
        VerticalAnchor.aboveBottom(10)
    )

    private fun setBlock(level: ServerLevel, pos: BlockPos, state: BlockState) {
        if (level.getBlockState(pos).canBeReplaced() && state.canSurvive(level, pos)) {
            level.setBlockAndUpdate(pos, state)
        }
    }

    override fun addBlockProperties(properties: BlockBehaviour.Properties): BlockBehaviour.Properties = super.addBlockProperties(properties)
        .sound(DeferredSoundType(
            1.0f,
            1.0f,
            { BadOres.PANDAEMONIUM_BREAK_SOUND_EVENT },
            { SoundEvents.STONE_STEP },
            { SoundEvents.STONE_PLACE },
            { SoundEvents.STONE_HIT },
            { SoundEvents.STONE_FALL },
        ))

    override fun onDestroyed(state: BlockState, level: Level, pos: BlockPos) {
        if (level !is ServerLevel) {
            return
        }

        val pigmen = level.random.nextInt(4)
        for (i in 0..<pigmen) {
            EntityType.ZOMBIFIED_PIGLIN.create(level, EntitySpawnReason.TRIGGERED)?.also {
                it.snapTo(pos.bottomCenter)
                level.addFreshEntity(it)
                it.spawnAnim()
            }
        }

        val veins: Int = level.random.nextInt(12) + 3
        for (i in 0..<veins) {
            val direction = Vec3(
                level.random.nextDouble() - level.random.nextDouble(),
                level.random.nextDouble() - level.random.nextDouble(),
                level.random.nextDouble() - level.random.nextDouble(),
            )
            val length = level.random.nextInt(3, 12)

            for (iteration in 0..<length) {
                val blockPos = BlockPos(
                    (pos.center.x + direction.x * iteration).toInt(),
                    (pos.center.y + direction.y * iteration).toInt(),
                    (pos.center.z + direction.z * iteration).toInt(),
                )
                setBlock(level, blockPos, Blocks.NETHERRACK.defaultBlockState())
            }

            val fireRange: Int = level.random.nextInt(8)
            val fireChance: Float = level.random.nextFloat() * level.random.nextFloat()
            for (x in -fireRange..fireRange) for (y in -fireRange..fireRange) for (z in -fireRange..fireRange) {
                if (level.random.nextFloat() < fireChance) {
                    setBlock(level, BlockPos(x, y, z), Blocks.FIRE.defaultBlockState())
                }
            }
        }

    }

    override fun onTick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BadOreBlockEntity) {
        if (level !is ServerLevel && level.random.nextInt(10) == 0) {
            level.playSound(null, pos, BadOres.PANDAEMONIUM_BREAK_SOUND_EVENT, SoundSource.BLOCKS)
        }
    }
}