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
import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BadOresEquipmentAssets extends EquipmentAssetProvider {
    private final PackOutput.PathProvider path;

    public BadOresEquipmentAssets(PackOutput output) {
        super(output);

        path = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<Identifier, EquipmentClientInfo> map = Arrays.stream(BadOre.values())
                .filter(ore -> ore.armor != null)
                .collect(Collectors.toMap(
                        ore -> BadOres.rl(ore.name),
                        ore -> EquipmentClientInfo.builder()
                                .addHumanoidLayers(ore.template.getEquipmentTextureLocation(ore.name))
                                .build()));

        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, path, map);
    }

    @Override
    public String getName() {
        return "Equipment Client Infos: " + BadOres.MOD_ID;
    }
}
