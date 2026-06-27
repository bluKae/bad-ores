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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class BadOresItemTags extends ItemTagsProvider {
    public BadOresItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BadOres.MOD_ID);
    }

    private static ResourceKey<Item> itemKey(ItemLike item) {
        return BuiltInRegistries.ITEM.wrapAsHolder(item.asItem()).getKey();
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TagAppender<Item> oreBookComponents = tag(BadOres.ORE_BOOK_COMPONENTS);

        for (BadOre ore : BadOre.values()) {
            if (ore.armor != null) {
                tag(ore.armor.material.repairIngredient()).add(itemKey(ore.ingot));
            }

            if (ore.tools != null) {
                tag(ore.tools.material.repairItems()).add(itemKey(ore.ingot));
            }

            TagAppender<Item> oreItems = tag(ore.oreItems).add(itemKey(ore.oreBlock));
            oreBookComponents.add(itemKey(ore.oreBlock));
            if (ore.deepslateOreBlock != null) {
                oreItems.add(itemKey(ore.deepslateOreBlock));
                oreBookComponents.add(itemKey(ore.deepslateOreBlock));
            }
            if (ore.rawIngot != null) {
                oreBookComponents.add(itemKey(ore.rawIngot));
            }
        }

        tag(Fleesonsite.FLEESONSITE_JUMP).add(
                itemKey(BadOre.FLEESONSITE.oreBlock),
                itemKey(BadOre.FLEESONSITE.deepslateOreBlock),
                itemKey(BadOre.FLEESONSITE.rawIngotBlock),
                itemKey(BadOre.FLEESONSITE.ingotBlock),
                itemKey(BadOre.FLEESONSITE.rawIngot),
                itemKey(BadOre.FLEESONSITE.ingot));
    }
}
