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

import net.minecraft.core.BlockPos
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.event.EventHooks

object Zombieunite: BadOre("zombieunite") {
    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder? = LootTable.lootTable()
        .withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(Items.ZOMBIE_HEAD)))

    override fun getDestroyProgress(state: BlockState, player: Player, level: BlockGetter, pos: BlockPos): Float {
        val numZombies = player.level()
            .getEntitiesOfClass(Zombie::class.java, AABB.ofSize(pos.center, 20.0, 20.0, 20.0))
            .size
        if (numZombies < 10) {
            return 0.0f
        }
        val i = if (EventHooks.doPlayerHarvestCheck(player, state, level, pos)) 30 else 100
        return player.getDestroySpeed(state, pos) / 3.0f / i.toFloat()
    }
}