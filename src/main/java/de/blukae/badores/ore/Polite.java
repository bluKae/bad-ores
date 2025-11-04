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
import de.blukae.badores.util.ArmorInfo;
import de.blukae.badores.util.ToolInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

public class Polite implements OreTemplate {

    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.SAND;
    }

    @Override
    public ArmorInfo getArmorInfo() {
        return new ArmorInfo(8, new int[]{2, 4, 3, 1}, 8);
    }

    @Override
    public ToolInfo getToolInfo() {
        return new ToolInfo(BlockTags.INCORRECT_FOR_STONE_TOOL, 200, 5.0f, 2.0f, 15);
    }

    @Override
    public PlacementModifier getPlacementModifier() {
        return CountPlacement.of(UniformInt.of(4, 7));
    }

    @Override
    public HolderSet<Biome> getBiomes(HolderGetter<Biome> lookup) {
        return lookup.getOrThrow(Tags.Biomes.IS_COLD);
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && willHarvest && state.is(BadOre.POLITE.ores) && player instanceof ServerPlayer serverPlayer) {
            new RandomTranslation("badores.polite.mined", "Polite ore mined")
                    .send(serverPlayer);
        }
    }

    @Override
    public void onEntityHurt(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide() && attacker instanceof ServerPlayer serverPlayer) {
            new RandomTranslation("badores.polite.attack", "Politely attacked")
                    .send(serverPlayer);
        }
    }

    @Override
    public void onMine(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide() && miningEntity instanceof ServerPlayer serverPlayer) {
            new RandomTranslation("badores.polite.tool", "Politely mined")
                    .send(serverPlayer);
        }
    }

    @Override
    public void onArmorHurt(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        if (!entity.level().isClientSide() && entity instanceof ServerPlayer serverPlayer) {
            new RandomTranslation("badores.polite.defend", "Politely defended")
                    .send(serverPlayer);
        }
    }
}
