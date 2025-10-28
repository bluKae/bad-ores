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

package de.blukae.badores.data;

import de.blukae.badores.BadOres;
import de.blukae.badores.ore.BadOre;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Set;

public class BadOresBlockLoot extends BlockLootSubProvider {
    protected BadOresBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected void generate() {
        for (BadOre ore : BadOre.values()) {
            LootTable.Builder customLootTable = ore.template.getCustomLootTable(this);
            if (customLootTable != null) {
                add(ore.oreBlock.get(), customLootTable);
                if (ore.deepslateOreBlock != null) {
                    add(ore.deepslateOreBlock.get(), customLootTable);
                }
            } else if (ore.rawIngot != null) {
                add(ore.oreBlock.get(), createOreDrop(ore.oreBlock.get(), ore.rawIngot.get()));
                if (ore.deepslateOreBlock != null) {
                    add(ore.deepslateOreBlock.get(), createOreDrop(ore.deepslateOreBlock.get(), ore.rawIngot.get()));
                }
            } else if (ore.ingot != null) {
                add(ore.oreBlock.get(), createOreDrop(ore.oreBlock.get(), ore.ingot.get()));
                if (ore.deepslateOreBlock != null) {
                    add(ore.deepslateOreBlock.get(), createOreDrop(ore.deepslateOreBlock.get(), ore.ingot.get()));
                }
            } else {
                dropSelf(ore.oreBlock.get());
                if (ore.deepslateOreBlock != null) {
                    dropSelf(ore.deepslateOreBlock.get());
                }
            }

            if (ore.rawIngotBlock != null) {
                dropSelf(ore.rawIngotBlock.get());
            }

            if (ore.ingotBlock != null) {
                dropSelf(ore.ingotBlock.get());
            }
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BadOres.BLOCKS.getEntries().stream().map(block -> (Block) block.get()).toList();
    }
}
