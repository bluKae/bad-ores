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

import de.blukae.badores.util.ArmorInfo;
import de.blukae.badores.util.ToolInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Enderite implements OreTemplate {
    private static final int RADIUS = 40;

    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.WARPED_NYLIUM;
    }

    @Override
    public ArmorInfo getArmorInfo() {
        return new ArmorInfo(17, new int[]{2, 5, 4, 1}, 20);
    }

    @Override
    public ToolInfo getToolInfo() {
        return new ToolInfo(BlockTags.INCORRECT_FOR_STONE_TOOL, 120, 4.0f, 1.0f, 15);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && entity instanceof LivingEntity livingEntity && level.random.nextInt(1000) == 0) {
            teleportEntity(level, entity.blockPosition(), livingEntity);
        }
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && willHarvest) {
            teleportEntity(level, pos, player);
        }
    }

    @Override
    public void onMine(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide() && level.random.nextInt(5) == 0) {
            teleportEntity(level, miningEntity.blockPosition(), miningEntity);
        }
    }

    private void teleportEntity(Level level, BlockPos origin, LivingEntity entity) {
        BlockPos pos = new BlockPos(
                level.random.nextIntBetweenInclusive(origin.getX() - RADIUS, origin.getX() + RADIUS),
                level.random.nextIntBetweenInclusive(10, level.dimensionType().height() - 20) + level.dimensionType()
                        .minY(),
                level.random.nextIntBetweenInclusive(origin.getZ() - RADIUS, origin.getZ() + RADIUS));

        for (int i = 0; i < 128; i++) {
            double factor = i / 128.0;
            Vec3 particlePos = pos.getCenter().add(pos.subtract(origin).getCenter().multiply(factor, factor, factor));

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.PORTAL,
                        particlePos.x,
                        particlePos.y,
                        particlePos.z,
                        1,
                        0.0,
                        0.0,
                        0.0,
                        1.0);
            }
        }

        Vec3 teleportPos = pos.getBottomCenter();
        entity.teleportTo(teleportPos.x, teleportPos.y, teleportPos.z);
        level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS);
    }
}
