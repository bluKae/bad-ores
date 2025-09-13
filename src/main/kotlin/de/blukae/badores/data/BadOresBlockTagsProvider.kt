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
import net.neoforged.neoforge.common.data.BlockTagsProvider
import java.util.concurrent.CompletableFuture

class BadOresBlockTagsProvider(output: PackOutput,
                               lookupProvider: CompletableFuture<HolderLookup.Provider>
) : BlockTagsProvider(output, lookupProvider, BadOres.MOD_ID) {
    override fun addTags(provider: HolderLookup.Provider) {
        val badOresTag = tag(BadOres.BAD_ORES_TAG)

        for (ore in BadOres.ORES) {
            val ores = listOfNotNull(ore.oreBlock, ore.deepslateOreBlock).map { it.get() }

            badOresTag.addAll(ores)
            tag(ore.oreTag).addAll(ores)

            ore.levelTag()?.let { tagKey ->
                tag(tagKey).apply {
                    add(ore.oreBlock.get())
                    ore.deepslateOreBlock?.let { add(it.get()) }
                }
            }

            tag(ore.toolTag()).apply {
                add(ore.oreBlock.get())
                ore.deepslateOreBlock?.let { add(it.get()) }
            }
        }
    }
}