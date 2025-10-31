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

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;

public class Paintitwhite implements OreTemplate {
    @Override
    public boolean hasDeepslateVariant() {
        return false;
    }

    @Override
    public HeightRangePlacement getHeightPlacementModifier() {
        return HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(128));
    }

    @Override
    public PlacementModifier getPlacementModifier() {
        return CountPlacement.of(UniformInt.of(2, 6));
    }

    @Override
    public RuleTest getTarget() {
        return new BlockMatchTest(Blocks.GRAVEL);
    }

    @Override
    public TagKey<Block> toolTag() {
        return BlockTags.MINEABLE_WITH_SHOVEL;
    }

    @Override
    public TagKey<Block> levelTag() {
        return null;
    }

    @Override
    public BlockBehaviour.Properties getOreBlockProperties(boolean isDeepslate) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL);
    }

    @Override
    public LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.WHITE_DYE)));
    }
}
