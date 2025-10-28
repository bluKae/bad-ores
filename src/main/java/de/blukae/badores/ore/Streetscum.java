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

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Streetscum implements OreTemplate {
    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!(level instanceof ServerLevel) || player.preventsBlockDrops()) {
            return;
        }

        Inventory inventory = player.getInventory();
        List<Integer> availableItems = new ArrayList<>(inventory.getContainerSize());
        for (int index = 0; index < inventory.getContainerSize(); index++) {
            if (!inventory.getItem(index).isEmpty()) {
                availableItems.add(index);
            }
        }

        int remove = level.getRandom().nextInt(availableItems.size() / 3 + 1);
        for (int i = 0; i < remove; i++) {
            int posIndex = level.getRandom().nextInt(availableItems.size());
            inventory.setItem(availableItems.get(posIndex), ItemStack.EMPTY);
            availableItems.remove(posIndex);
        }
    }
}
