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

import de.blukae.badores.util.ArmorInfo;
import de.blukae.badores.util.ToolInfo;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class Nopium implements OreTemplate {
    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.COLOR_YELLOW;
    }

    @Override
    public ArmorInfo getArmorInfo() {
        return new ArmorInfo(15, new int[]{2, 6, 5, 2}, 1);
    }

    @Override
    public ToolInfo getToolInfo() {
        return new ToolInfo(BlockTags.INCORRECT_FOR_IRON_TOOL, 800, 0.5f, 1.0f, 1);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && entity instanceof Player player && level.random.nextInt(200) == 0) {
            player.getInventory().setItem(slotId, ItemStack.EMPTY);
            player.drop(stack, true, true);
        }
    }
}
