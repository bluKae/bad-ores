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
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class BadOresItemTags extends ItemTagsProvider {
    public BadOresItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BadOres.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TagAppender<Item, Item> oreBookComponents = tag(BadOres.ORE_BOOK_COMPONENTS);

        for (BadOre ore : BadOre.values()) {
            if (ore.armor != null) {
                tag(ore.armor.material.repairIngredient()).add(ore.ingot.get());
            }

            if (ore.tools != null) {
                tag(ore.tools.material.repairItems()).add(ore.ingot.get());
            }

            TagAppender<Item, Item> oreItems = tag(ore.oreItems).add(ore.oreBlock.asItem());
            oreBookComponents.add(ore.oreBlock.asItem());
            if (ore.deepslateOreBlock != null) {
                oreItems.add(ore.deepslateOreBlock.asItem());
                oreBookComponents.add(ore.deepslateOreBlock.asItem());
            }
            if (ore.rawIngot != null) {
                oreBookComponents.add(ore.rawIngot.asItem());
            }
        }

        tag(Fleesonsite.FLEESONSITE_JUMP).add(
                BadOre.FLEESONSITE.oreBlock.asItem(),
                BadOre.FLEESONSITE.deepslateOreBlock.asItem(),
                BadOre.FLEESONSITE.rawIngotBlock.asItem(),
                BadOre.FLEESONSITE.ingotBlock.asItem(),
                BadOre.FLEESONSITE.rawIngot.asItem(),
                BadOre.FLEESONSITE.ingot.asItem());
    }
}
