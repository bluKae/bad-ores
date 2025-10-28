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

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class Doesntevenexistium implements OreBookPage {
    @Override
    public String getId() {
        return "doesntevenexistium";
    }

    @Override
    public MutableComponent getName() {
        return Component.translatable("badores.doesntevenexistium.name");
    }

    @Override
    public MutableComponent getDescription() {
        return Component.translatable("badores.doesntevenexistium.description");
    }

    @Override
    public @Nullable ItemStack getOreStack() {
        return null;
    }
}
