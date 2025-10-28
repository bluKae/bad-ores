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
import de.blukae.badores.advancement.MineBadOreTrigger;
import de.blukae.badores.ore.BadOre;
import de.blukae.badores.ore.Marmite;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BadOresRecipes extends RecipeProvider {
    public BadOresRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {
        for (BadOre ore : BadOre.values()) {
            if (ore.rawIngot != null && ore.rawIngotBlock != null) {
                nineBlockStorageRecipes(
                        RecipeCategory.MISC,
                        ore.rawIngot,
                        RecipeCategory.BUILDING_BLOCKS,
                        ore.rawIngotBlock);
            }

            if (ore.ingot != null) {
                List<ItemLike> smeltables = new ArrayList<>(List.of(ore.oreBlock));
                if (ore.deepslateOreBlock != null)
                    smeltables.add(ore.deepslateOreBlock);
                if (ore.rawIngot != null)
                    smeltables.add(ore.rawIngot);

                oreSmelting(smeltables, RecipeCategory.MISC, ore.ingot, 0.7f, 200, ore.ingot.getId().getPath());
                oreBlasting(smeltables, RecipeCategory.MISC, ore.ingot, 0.7f, 100, ore.ingot.getId().getPath());

                if (ore.ingotBlock != null) {
                    nineBlockStorageRecipesRecipesWithCustomUnpacking(
                            RecipeCategory.MISC,
                            ore.ingot,
                            RecipeCategory.BUILDING_BLOCKS,
                            ore.ingotBlock,
                            ore.ingot.getId().getPath() + "_from_" + ore.name + "_block",
                            ore.ingot.getId().getPath());
                }

                if (ore.armor != null) {
                    shaped(RecipeCategory.COMBAT, ore.armor.helmet).define('X', ore.ingot)
                            .pattern("XXX")
                            .pattern("X " + "X")
                            .unlockedBy("has_" + ore.ingot.getId().getPath(), has(ore.ingot))
                            .save(output);
                    shaped(RecipeCategory.COMBAT, ore.armor.chestplate).define('X', ore.ingot)
                            .pattern("X X")
                            .pattern("XXX")
                            .pattern("XXX")
                            .unlockedBy("has_" + ore.ingot.getId().getPath(), has(ore.ingot))
                            .save(output);
                    shaped(RecipeCategory.COMBAT, ore.armor.leggings).define('X', ore.ingot)
                            .pattern("XXX")
                            .pattern("X X")
                            .pattern("X X")
                            .unlockedBy("has_" + ore.ingot.getId().getPath(), has(ore.ingot))
                            .save(output);
                    shaped(RecipeCategory.COMBAT, ore.armor.boots).define('X', ore.ingot)
                            .pattern("X X")
                            .pattern("X " + "X")
                            .unlockedBy("has_" + ore.ingot.getId().getPath(), has(ore.ingot))
                            .save(output);
                }

                if (ore.tools != null) {
                    shaped(RecipeCategory.TOOLS, ore.tools.axe).define('#', Tags.Items.RODS_WOODEN)
                            .define('X', ore.ingot)
                            .pattern("XX")
                            .pattern("X#")
                            .pattern(" #")
                            .unlockedBy("has_" + ore.ingot.getId().getPath(), has(ore.ingot))
                            .save(output);
                    shaped(RecipeCategory.TOOLS, ore.tools.hoe).define('#', Tags.Items.RODS_WOODEN)
                            .define('X', ore.ingot)
                            .pattern("XX")
                            .pattern(" #")
                            .pattern(" #")
                            .unlockedBy("has_" + ore.ingot.getId().getPath(), has(ore.ingot))
                            .save(output);
                    shaped(RecipeCategory.TOOLS, ore.tools.pickaxe).define('#', Tags.Items.RODS_WOODEN)
                            .define('X', ore.ingot)
                            .pattern("XXX")
                            .pattern(" # ")
                            .pattern(" # ")
                            .unlockedBy("has_" + ore.ingot.getId().getPath(), has(ore.ingot))
                            .save(output);
                    shaped(RecipeCategory.TOOLS, ore.tools.shovel).define('#', Tags.Items.RODS_WOODEN)
                            .define('X', ore.ingot)
                            .pattern("X")
                            .pattern("#")
                            .pattern("#")
                            .unlockedBy("has_" + ore.ingot.getId().getPath(), has(ore.ingot))
                            .save(output);
                    shaped(RecipeCategory.COMBAT, ore.tools.sword).define('#', Tags.Items.RODS_WOODEN)
                            .define('X', ore.ingot)
                            .pattern("X")
                            .pattern("X")
                            .pattern("#")
                            .unlockedBy("has_" + ore.ingot.getId().getPath(), has(ore.ingot))
                            .save(output);
                }
            }
        }

        shapeless(RecipeCategory.FOOD, Marmite.MARMITE_BREAD_ITEM).requires(Items.BREAD)
                .requires(BadOre.MARMITE.ingot)
                .unlockedBy("has_marmite", has(BadOre.MARMITE.ingot))
                .save(output);

        List<ItemLike> explodeitmiteSmeltables = List.of(
                BadOre.EXPLODEITMITE.oreBlock,
                BadOre.EXPLODEITMITE.deepslateOreBlock);
        oreSmelting(
                explodeitmiteSmeltables,
                RecipeCategory.MISC,
                Items.GUNPOWDER,
                0.7f,
                200,
                "gunpowder_from_smelting_explodeitmite");
        oreBlasting(
                explodeitmiteSmeltables,
                RecipeCategory.MISC,
                Items.GUNPOWDER,
                0.7f,
                100,
                "gunpowder_from_smelting_explodeitmite");

        List<ItemLike> liteSmeltables = List.of(BadOre.LITE.oreBlock, BadOre.LITE.deepslateOreBlock);
        oreSmelting(liteSmeltables, RecipeCategory.MISC, Items.GLOWSTONE, 0.7f, 200, "glowstone_from_smelting_lite");
        oreBlasting(liteSmeltables, RecipeCategory.MISC, Items.GLOWSTONE, 0.7f, 100, "glowstone_from_smelting_lite");

        shapeless(RecipeCategory.MISC, BadOres.BAD_ORE_BOOK_ITEM).requires(Items.BOOK)
                .requires(BadOres.ORE_BOOK_COMPONENTS)
                .unlockedBy("mine_bad_ore", MineBadOreTrigger.TriggerInstance.minedAny())
                .unlockedBy("has_ore_book_component", has(BadOres.ORE_BOOK_COMPONENTS))
                .save(output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        public RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new BadOresRecipes(registries, output);
        }

        @Override
        public String getName() {
            return BadOres.MOD_ID;
        }
    }
}
