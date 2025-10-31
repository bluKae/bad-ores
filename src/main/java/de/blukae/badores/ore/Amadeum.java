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

import de.blukae.badores.block.BadOreBlockEntity;
import de.blukae.badores.util.ArmorInfo;
import de.blukae.badores.util.ToolInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Amadeum implements OreTemplate {
    private static final List<Holder<SoundEvent>> SOUNDS = Arrays.stream(NoteBlockInstrument.values())
            .filter(NoteBlockInstrument::isTunable)
            .map(NoteBlockInstrument::getSoundEvent)
            .toList();

    private static void playRandomSound(Level level, Vec3 pos) {
        float pitch = NoteBlock.getPitchFromNote(level.random.nextInt(25));
        Holder<SoundEvent> sound = SOUNDS.get(level.random.nextInt(SOUNDS.size()));
        level.playSound(null, pos.x(), pos.y(), pos.z(), sound, SoundSource.BLOCKS, 3.0F, pitch);
    }

    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.WARPED_WART_BLOCK;
    }

    @Override
    public ArmorInfo getArmorInfo() {
        return new ArmorInfo(10, new int[]{2, 6, 5, 2}, 20);
    }

    @Override
    public ToolInfo getToolInfo() {
        return new ToolInfo(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0f, 2.0f, 20);
    }

    @Override
    public PlacementModifier getPlacementModifier() {
        return RarityFilter.onAverageOnceEvery(100);
    }

    @Override
    public IntProvider getTickRate(boolean isOreBlock) {
        return UniformInt.of(18, 18 + 5);
    }

    @Override
    public void onTick(Level level, BlockPos pos, BlockState state, BadOreBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            playRandomSound(level, pos.getCenter());
        }
    }

    @Override
    public void onInventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (level.random.nextInt(200) == 0) {
            playRandomSound(level, entity.position());
        }
    }
}
