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
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.phys.Vec3;

public class Explodeitmite implements OreTemplate {
    @Override
    public PlacementModifier getPlacementModifier() {
        return RarityFilter.onAverageOnceEvery(10);
    }

    @Override
    public BlockBehaviour.Properties getOreBlockProperties(boolean isDeepslate) {
        return OreTemplate.super.getOreBlockProperties(isDeepslate)
                .randomTicks()
                .strength(isDeepslate ? 9.5F : 8.0F, 10.0F);
    }

    @Override
    public void onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) == 0) {
            level.removeBlock(pos, false);
            explode(level, pos);
        }
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && level.getRandom().nextInt(4) == 0) {
            explode(level, pos);
        }
    }

    private void explode(Level level, BlockPos pos) {
        Vec3 p = pos.getCenter().add(0.0, 1.0, 0.0);
        level.explode(
                null,
                p.x,
                p.y,
                p.z,
                2.0f + level.getRandom().nextFloat() * 3.0f,
                false,
                Level.ExplosionInteraction.BLOCK);
    }
}
