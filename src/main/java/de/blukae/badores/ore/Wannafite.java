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

import de.blukae.badores.BadOres;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class Wannafite implements OreTemplate {
    public static final ResourceKey<DamageType> DAMAGE_TYPE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            BadOres.rl("wannafite"));

    @Override
    public LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.IRON_SWORD)
                                .apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.4F, 0.8F)))));
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && willHarvest) {
            var damageType = level.registryAccess()
                    .lookup(Registries.DAMAGE_TYPE)
                    .orElseThrow()
                    .getOrThrow(DAMAGE_TYPE);

            player.hurt(new DamageSource(damageType), 4.0f);
        }
    }
}
