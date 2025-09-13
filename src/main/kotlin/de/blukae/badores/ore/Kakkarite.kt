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

import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue

object Kakkarite: BadOre("kakkarite") {
    override fun hasIngot() = true
    override fun hasRaw() = false

    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder? = ingot?.let {
        LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .add(LootItem.lootTableItem(it)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(9001.0f))))
            )
    }
}