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
import de.blukae.badores.ore.Explodeitmite
import de.blukae.badores.ore.Lite
import de.blukae.badores.ore.Marmite
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class BadOresRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(
    registries,
    output
) {
    override fun buildRecipes() {
        for (ore in BadOres.ORES) {
            ore.raw?.let { raw ->
                ore.rawBlock?.let { rawBlock ->
                    nineBlockStorageRecipes(
                        RecipeCategory.MISC,
                        raw,
                        RecipeCategory.BUILDING_BLOCKS,
                        rawBlock
                    )
                }
            }

            ore.ingot?.let { ingot ->
                val smeltables = listOfNotNull(ore.oreBlock, ore.deepslateOreBlock, ore.raw)
                oreSmelting(
                    smeltables,
                    RecipeCategory.MISC,
                    ingot,
                    0.7f,
                    200,
                    ingot.id.path
                )
                oreBlasting(
                    smeltables,
                    RecipeCategory.MISC,
                    ingot,
                    0.7f,
                    100,
                    ingot.id.path
                )

                ore.ingotBlock?.let { ingotBlock ->
                    nineBlockStorageRecipesRecipesWithCustomUnpacking(
                        RecipeCategory.MISC,
                        ingot,
                        RecipeCategory.BUILDING_BLOCKS,
                        ingotBlock,
                        "${ingot.id.path}_from_${ore.name}_block",
                        ingot.id.path
                    )
                }

                ore.armorSet?.let { armorSet ->
                    shaped(RecipeCategory.COMBAT, armorSet.helmet)
                        .define('X', ingot)
                        .pattern("XXX")
                        .pattern("X X")
                        .unlockedBy("has_${ingot.id.path}", has(ingot))
                        .save(output)
                    shaped(RecipeCategory.COMBAT, armorSet.chestplate)
                        .define('X', ingot)
                        .pattern("X X")
                        .pattern("XXX")
                        .pattern("XXX")
                        .unlockedBy("has_${ingot.id.path}", has(ingot))
                        .save(output)
                    shaped(RecipeCategory.COMBAT, armorSet.leggings)
                        .define('X', ingot)
                        .pattern("XXX")
                        .pattern("X X")
                        .pattern("X X")
                        .unlockedBy("has_${ingot.id.path}", has(ingot))
                        .save(output)
                    shaped(RecipeCategory.COMBAT, armorSet.boots)
                        .define('X', ingot)
                        .pattern("X X")
                        .pattern("X X")
                        .unlockedBy("has_${ingot.id.path}", has(ingot))
                        .save(output)
                }

                ore.toolSet?.let { toolSet ->
                    shaped(RecipeCategory.TOOLS, toolSet.axe)
                        .define('#', Tags.Items.RODS_WOODEN)
                        .define('X', ingot)
                        .pattern("XX")
                        .pattern("X#")
                        .pattern(" #")
                        .unlockedBy("has_${ingot.id.path}", has(ingot))
                        .save(output)
                    shaped(RecipeCategory.TOOLS, toolSet.hoe)
                        .define('#', Tags.Items.RODS_WOODEN)
                        .define('X', ingot)
                        .pattern("XX")
                        .pattern(" #")
                        .pattern(" #")
                        .unlockedBy("has_${ingot.id.path}", has(ingot))
                        .save(output)
                    shaped(RecipeCategory.TOOLS, toolSet.pickaxe)
                        .define('#', Tags.Items.RODS_WOODEN)
                        .define('X', ingot)
                        .pattern("XXX")
                        .pattern(" # ")
                        .pattern(" # ")
                        .unlockedBy("has_${ingot.id.path}", has(ingot))
                        .save(output)
                    shaped(RecipeCategory.TOOLS, toolSet.shovel)
                        .define('#', Tags.Items.RODS_WOODEN)
                        .define('X', ingot)
                        .pattern("X")
                        .pattern("#")
                        .pattern("#")
                        .unlockedBy("has_${ingot.id.path}", has(ingot))
                        .save(output)
                    shaped(RecipeCategory.COMBAT, toolSet.sword)
                        .define('#', Tags.Items.RODS_WOODEN)
                        .define('X', ingot)
                        .pattern("X")
                        .pattern("X")
                        .pattern("#")
                        .unlockedBy("has_${ingot.id.path}", has(ingot))
                        .save(output)
                }
            }
        }

        Marmite.ingot?.let {
            shapeless(RecipeCategory.FOOD, BadOres.MARMITE_BREAD_ITEM)
                .requires(Items.BREAD)
                .requires(it)
                .unlockedBy("has_${it.id.path}", this.has(it))
                .save(output)
        }

        val explodeitmiteSmeltables = listOfNotNull(Explodeitmite.oreBlock, Explodeitmite.deepslateOreBlock)
        oreSmelting(
            explodeitmiteSmeltables,
            RecipeCategory.MISC,
            Items.GUNPOWDER,
            0.7f,
            200,
            "gunpowder_from_smelting_explodeitmite"
        )
        oreBlasting(
            explodeitmiteSmeltables,
            RecipeCategory.MISC,
            Items.GUNPOWDER,
            0.7f,
            100,
            "gunpowder_from_smelting_explodeitmite"
        )

        listOfNotNull(Lite.oreBlock, Lite.deepslateOreBlock)
        oreSmelting(
            explodeitmiteSmeltables,
            RecipeCategory.MISC,
            Items.GLOWSTONE,
            0.7f,
            200,
            "glowstone_from_smelting_lite"
        )
        oreBlasting(
            explodeitmiteSmeltables,
            RecipeCategory.MISC,
            Items.GLOWSTONE,
            0.7f,
            100,
            "glowstone_from_smelting_lite"
        )
    }

    companion object {
        class Runner(
            packOutput: PackOutput,
            registries: CompletableFuture<HolderLookup.Provider>
        ) : RecipeProvider.Runner(packOutput, registries) {
            override fun createRecipeProvider(
                registries: HolderLookup.Provider,
                output: RecipeOutput
            ) = BadOresRecipeProvider(registries, output)

            override fun getName() = BadOres.MOD_ID
        }
    }
}