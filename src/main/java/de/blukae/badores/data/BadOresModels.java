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
import de.blukae.badores.ore.Fleesonsite;
import de.blukae.badores.ore.Marmite;
import de.blukae.badores.ore.Nosleeptonite;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class BadOresModels extends ModelProvider {
    public BadOresModels(PackOutput output) {
        super(output, BadOres.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (BadOre ore : BadOre.values()) {
            if (ore.template.hasCustomModels()) {
                ore.template.buildCustomModels(blockModels, itemModels);
                continue;
            }

            blockModels.createTrivialCube(ore.oreBlock.get());
            if (ore.deepslateOreBlock != null) {
                blockModels.createTrivialCube(ore.deepslateOreBlock.get());
            }
            if (ore.rawIngotBlock != null) {
                blockModels.createTrivialCube(ore.rawIngotBlock.get());
            }
            if (ore.ingotBlock != null) {
                blockModels.createTrivialCube(ore.ingotBlock.get());
            }

            if (ore.rawIngot != null) {
                itemModels.generateFlatItem(ore.rawIngot.get(), ModelTemplates.FLAT_ITEM);
            }
            if (ore.ingot != null) {
                itemModels.generateFlatItem(ore.ingot.get(), ModelTemplates.FLAT_ITEM);
            }

            if (ore.armor != null) {
                itemModels.generateFlatItem(ore.armor.helmet.get(), ModelTemplates.FLAT_ITEM);
                itemModels.generateFlatItem(ore.armor.chestplate.get(), ModelTemplates.FLAT_ITEM);
                itemModels.generateFlatItem(ore.armor.leggings.get(), ModelTemplates.FLAT_ITEM);
                itemModels.generateFlatItem(ore.armor.boots.get(), ModelTemplates.FLAT_ITEM);
            }
            if (ore.tools != null) {
                itemModels.generateFlatItem(ore.tools.axe.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
                itemModels.generateFlatItem(ore.tools.hoe.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
                itemModels.generateFlatItem(ore.tools.pickaxe.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
                itemModels.generateFlatItem(ore.tools.shovel.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
                itemModels.generateFlatItem(ore.tools.sword.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
            }
        }

        itemModels.generateFlatItem(BadOres.BAD_ORE_BOOK_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Marmite.MARMITE_BREAD_ITEM.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(Fleesonsite.FLEESONSITE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Fleesonsite.DEEPSLATE_FLEESONSITE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(Nosleeptonite.NOSLEEPTONITE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
    }
}
