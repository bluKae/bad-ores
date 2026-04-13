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

package de.blukae.badores.block;

import de.blukae.badores.BadOres;
import de.blukae.badores.ore.OreTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BadOreBlockEntity extends BlockEntity {
    private int tickTime = 0;

    public BadOreBlockEntity(BlockPos pos, BlockState blockState) {
        super(BadOres.BAD_ORE_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state, OreTemplate template, IntProvider tickRate) {
        if (tickTime > 0) {
            tickTime--;
        } else {
            tickTime = tickRate.sample(level.getRandom());
            template.onTick(level, pos, state, this);
        }
    }
}
