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
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;

public class Meteorite implements OreTemplate {
    private static final int METEORITE_SPAWN_SIDE = 50;

    @Override
    public HeightRangePlacement getHeightPlacementModifier() {
        return HeightRangePlacement.uniform(VerticalAnchor.absolute(90), VerticalAnchor.top());
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && willHarvest) {
            RandomSource random = level.getRandom();
            int number = random.nextInt(20) + 3;
            BlockState spawnState = random.nextBoolean() ?
                    Blocks.STONE.defaultBlockState() :
                    Blocks.NETHERRACK.defaultBlockState();
            for (int i = 0; i < number; i++) {
                double spawnX = pos.getX() + METEORITE_SPAWN_SIDE * (random.nextDouble() - random.nextDouble() - 0.5);
                double spawnY = level.getMaxY() + 5.0;
                double spawnZ = pos.getZ() + METEORITE_SPAWN_SIDE * (random.nextDouble() - random.nextDouble() - 0.5);

                FallingBlockEntity entity = new FallingBlockEntity(level, spawnX, spawnY, spawnZ, spawnState);
                entity.setDeltaMovement(random.nextDouble(), 0.0, random.nextDouble());
                level.addFreshEntity(entity);
            }
        }
    }
}
