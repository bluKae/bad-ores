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
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ItemTagsProvider
import java.util.concurrent.CompletableFuture

class BadOresItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    ItemTagsProvider(output, lookupProvider, BadOres.MOD_ID) {

    override fun addTags(provider: HolderLookup.Provider) {
        for (ore in BadOres.ORES) {
            ore.armorSet?.let { armorSet ->
                val repairIngredients = tag(armorSet.material.repairIngredient)
                ore.ingot?.let {
                    repairIngredients.add(it.get())
                }
            }
        }
    }
}