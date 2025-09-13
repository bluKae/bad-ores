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
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator


object Balancium: BadOre("balancium") {
    override fun size() = 3

    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder = LootTable.lootTable()
        .withPool(LootPool.lootPool()
            .setRolls(UniformGenerator.between(5.0f, 54.0f))
            .add(LootItem.lootTableItem(Items.DIAMOND))
            .add(LootItem.lootTableItem(Items.EMERALD))
            .add(LootItem.lootTableItem(Items.GOLD_INGOT))
            .add(LootItem.lootTableItem(Items.IRON_INGOT))
            .add(LootItem.lootTableItem(Items.COAL))
            .add(LootItem.lootTableItem(Items.QUARTZ))
            .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
            .add(LootItem.lootTableItem(Items.NETHERITE_INGOT))
        )
}