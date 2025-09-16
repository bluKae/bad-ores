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
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.data.PackOutput

class BadOresModelProvider(output: PackOutput) : ModelProvider(output, BadOres.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        for (ore in BadOres.ORES) {
            if (ore.hasCustomModels()) {
                ore.customModels(blockModels, itemModels)
                continue
            }

            blockModels.createTrivialCube(ore.oreBlock.get())

            ore.deepslateOreBlock?.let { blockModels.createTrivialCube(it.get()) }
            ore.rawBlock?.let { blockModels.createTrivialCube(it.get()) }
            ore.ingotBlock?.let { blockModels.createTrivialCube(it.get()) }

            ore.raw?.let { itemModels.generateFlatItem(it.get(), ModelTemplates.FLAT_ITEM) }
            ore.ingot?.let { itemModels.generateFlatItem(it.get(), ModelTemplates.FLAT_ITEM) }

            ore.toolSet?.let {
                itemModels.generateFlatItem(it.axe.get(), ModelTemplates.FLAT_HANDHELD_ITEM)
                itemModels.generateFlatItem(it.hoe.get(), ModelTemplates.FLAT_HANDHELD_ITEM)
                itemModels.generateFlatItem(it.pickaxe.get(), ModelTemplates.FLAT_HANDHELD_ITEM)
                itemModels.generateFlatItem(it.shovel.get(), ModelTemplates.FLAT_HANDHELD_ITEM)
                itemModels.generateFlatItem(it.sword.get(), ModelTemplates.FLAT_HANDHELD_ITEM)
            }

            ore.armorSet?.let {
                itemModels.generateFlatItem(it.helmet.get(), ModelTemplates.FLAT_ITEM)
                itemModels.generateFlatItem(it.chestplate.get(), ModelTemplates.FLAT_ITEM)
                itemModels.generateFlatItem(it.leggings.get(), ModelTemplates.FLAT_ITEM)
                itemModels.generateFlatItem(it.boots.get(), ModelTemplates.FLAT_ITEM)
            }
        }

        itemModels.generateFlatItem(BadOres.ORE_BOOK_ITEM, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(BadOres.MARMITE_BREAD_ITEM, ModelTemplates.FLAT_ITEM)

        itemModels.generateFlatItem(BadOres.FLEESONSITE_SPAWN_EGG, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(BadOres.DEEPSLATE_FLEESONSITE_SPAWN_EGG, ModelTemplates.FLAT_ITEM)
        itemModels.generateFlatItem(BadOres.NOSLEEPTONITE_SPAWN_EGG, ModelTemplates.FLAT_ITEM)
    }
}