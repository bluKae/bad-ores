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
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BadOresBlockStates extends BlockStateProvider {
    public BadOresBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BadOres.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (BadOre ore : BadOre.values()) {
            if (ore.template.hasCustomModels()) {
                ore.template.buildCustomBlockStates(this);
                continue;
            }

            simpleBlockWithItem(ore.oreBlock.get(), cubeAll(ore.oreBlock.get()));
            if (ore.deepslateOreBlock != null) {
                simpleBlockWithItem(ore.deepslateOreBlock.get(), cubeAll(ore.deepslateOreBlock.get()));
            }
            if (ore.rawIngotBlock != null) {
                simpleBlockWithItem(ore.rawIngotBlock.get(), cubeAll(ore.rawIngotBlock.get()));
            }
            if (ore.ingotBlock != null) {
                simpleBlockWithItem(ore.ingotBlock.get(), cubeAll(ore.ingotBlock.get()));
            }
        }
    }
}
