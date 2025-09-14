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

package de.blukae.badores.data

import de.blukae.badores.BadOres
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block


class BadOresBlockLoot(registries: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(), FeatureFlags.DEFAULT_FLAGS, registries) {
    override fun generate() {
        for (ore in BadOres.ORES) {
            ore.customLootTable(this)?.let { lootTable ->
                add(ore.oreBlock.get(), lootTable)
                ore.deepslateOreBlock?.let { add(it.get(), lootTable) }
            } ?: run {
                ore.raw?.let { raw ->
                    add(ore.oreBlock.get(), createOreDrop(ore.oreBlock.get(), raw.get()))
                    ore.deepslateOreBlock?.let {
                        add(it.get(), createOreDrop(it.get(), raw.get()))
                    }
                } ?: ore.ingot?.let { ingot ->
                    add(ore.oreBlock.get(), createOreDrop(ore.oreBlock.get(), ingot.get()))
                    ore.deepslateOreBlock?.let {
                        add(it.get(), createOreDrop(it.get(), ingot.get()))
                    }
                } ?: run {
                    dropSelf(ore.oreBlock.get())
                    ore.deepslateOreBlock?.let { dropSelf(it.get()) }
                }
            }

            ore.rawBlock?.let { dropSelf(it.get()) }
            ore.ingotBlock?.let { dropSelf(it.get()) }
        }
    }

    override fun getKnownBlocks(): Iterable<Block> = BadOres.BLOCKS.entries.map { it.get() }
}