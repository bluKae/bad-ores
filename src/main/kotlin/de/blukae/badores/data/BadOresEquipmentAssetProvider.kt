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
import net.minecraft.client.resources.model.EquipmentClientInfo
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture


class BadOresEquipmentAssetProvider(output: PackOutput): DataProvider {
    val path: PackOutput.PathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment")
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val map = BadOres.ORES.mapNotNull { ore ->
            ore.armorSet?.let {
                val info = EquipmentClientInfo.builder()
                    .addHumanoidLayers(ore.equipmentTexture())
                    .build()

                Pair(BadOres.rl(ore.name), info)
            }
        }.toMap()

        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, path, map)
    }

    override fun getName() = "Equipment Client Infos: ${BadOres.MOD_ID}"
}