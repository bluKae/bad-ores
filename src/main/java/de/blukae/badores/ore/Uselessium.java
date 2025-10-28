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

package de.blukae.badores.ore;

import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class Uselessium implements OreTemplate {
    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public boolean hasIngotBlock() {
        return false;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.TERRACOTTA_GREEN;
    }
}
