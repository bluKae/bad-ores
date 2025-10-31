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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

public class Marmite implements OreTemplate {

    public static final DeferredItem<Item> MARMITE_BREAD_ITEM = BadOres.ITEMS.registerSimpleItem(
            "marmite_bread",
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                        .nutrition(6)
                        .saturationModifier(0.8F)
                        .build()));

    @Override
    public boolean hasDeepslateVariant() {
        return false;
    }

    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.COLOR_BROWN;
    }

    @Override
    public HeightRangePlacement getHeightPlacementModifier() {
        return HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128));
    }

    @Override
    public RuleTest getTarget() {
        return new BlockMatchTest(Blocks.CLAY);
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
        return BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY);
    }

    @Override
    public String getIngotName(String name) {
        return name;
    }
}
