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
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BadOresItemTags extends ItemTagsProvider {
    public BadOresItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                           CompletableFuture<TagLookup<Block>> blockTags,
                           @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, BadOres.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        IntrinsicTagAppender<Item> oreBookComponents = tag(BadOres.ORE_BOOK_COMPONENTS);

        for (BadOre ore : BadOre.values()) {
            IntrinsicTagAppender<Item> oreItems = tag(ore.oreItems).add(ore.oreBlock.asItem());
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
