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
import de.blukae.badores.mixin.MobMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Tauntum implements OreTemplate {
    private List<SoundEvent> mobSounds = null;

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public PlacementModifier getPlacementModifier() {
        return RarityFilter.onAverageOnceEvery(100);
    }

    @Override
    public IntProvider getTickRate(boolean isIngotBlock) {
        return UniformInt.of(40, 40 + 399);
    }

    @Override
    public void onTick(Level level, BlockPos pos, BlockState state, BadOreBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            level.playSound(null, pos, getRandomMobSound(level), SoundSource.BLOCKS);
        }
    }

    private SoundEvent getRandomMobSound(Level level) {
        if (mobSounds == null) {
            List<SoundEvent> sounds = BuiltInRegistries.ENTITY_TYPE.stream()
                    .map(entity -> {
                        if (entity.create(level) instanceof Mob mob) {
                            return ((MobMixin) mob).invokeGetAmbientSound();
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            sounds.add(SoundEvents.CREEPER_PRIMED);
            mobSounds = sounds;
        }

        return mobSounds.get(level.random.nextInt(mobSounds.size()));
    }
}
