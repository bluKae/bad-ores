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
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

public class Wantarite implements OreTemplate {
    @Override
    public LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return LootTable.lootTable();
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack,
                                boolean dropExperience) {
        Pig pig = EntityType.PIG.create(level);
        if (pig != null) {
            pig.moveTo(pos.getBottomCenter());
            pig.equipSaddle(Items.SADDLE.getDefaultInstance(), SoundSource.BLOCKS);
            level.addFreshEntity(pig);
            pig.spawnAnim();
        }
    }
}
