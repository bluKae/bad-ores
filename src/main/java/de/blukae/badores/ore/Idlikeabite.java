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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class Idlikeabite implements OreTemplate {
    @Override
    public boolean hasDeepslateVariant() {
        return false;
    }

    @Override
    public HeightRangePlacement getHeightPlacementModifier() {
        return HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128));
    }

    @Override
    public RuleTest getTarget() {
        return new BlockMatchTest(Blocks.DIRT);
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
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT);
    }

    @Override
    public void onInventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (entity instanceof Player player && level.getRandom().nextInt(200) == 0) {
            player.getFoodData().addExhaustion(1.0f);
        }
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && willHarvest) {
            player.getFoodData().addExhaustion(level.getRandom().nextFloat() * 40.0f);
        }
    }
}
