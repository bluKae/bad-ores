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

import de.blukae.badores.RandomTranslation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class Misleadium implements OreTemplate {
    private static final int SIDE_RANGE = 500;

    private List<ItemStack> creativeTabItems = null;

    private ItemStack getRandomCreativeTabItem(Level level, RandomSource random) {
        if (creativeTabItems == null) {
            CreativeModeTabs.tryRebuildTabContents(level.enabledFeatures(), true, level.registryAccess());
            creativeTabItems = CreativeModeTabs.allTabs()
                    .stream()
                    .flatMap(tab -> tab.getDisplayItems().stream())
                    .toList();
        }

        return creativeTabItems.get(random.nextInt(creativeTabItems.size()));
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && willHarvest && player instanceof ServerPlayer serverPlayer) {
            RandomSource random = level.random;
            ItemStack stack = getRandomCreativeTabItem(level, random);
            int x = pos.getX() + random.nextInt(SIDE_RANGE) - random.nextInt(SIDE_RANGE);
            int y = random.nextInt(level.getMinY(), level.getMaxY());
            int z = pos.getZ() + random.nextInt(SIDE_RANGE) - random.nextInt(SIDE_RANGE);
            new RandomTranslation("badores.misleadium.baseMessage", "Mislead", stack.getHoverName(), x, y, z)
                    .send(serverPlayer);
        }
    }
}
