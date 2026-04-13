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
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

public class Enderite implements OreTemplate {
    private static final int RADIUS = 40;
    private static final int PARTICLE_COUNT = 128;

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
    public void onArmorTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (entity instanceof LivingEntity livingEntity && level.getRandom().nextInt(1000) == 0) {
            teleportEffect(level, entity.blockPosition(), livingEntity);
        }
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && willHarvest) {
            teleportEffect(level, pos, player);
        }
    }

    @Override
    public void onMine(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide() && level.getRandom().nextInt(5) == 0) {
            teleportEffect(level, miningEntity.blockPosition(), miningEntity);
        }
    }

    private void teleportEntity(Level level, BlockPos origin, LivingEntity entity) {
        RandomSource random = entity.getRandom();
        int targetX = (int) entity.getX() + random.nextInt(RADIUS * 2) - RADIUS;
        int targetZ = (int) entity.getZ() + random.nextInt(RADIUS * 2) - RADIUS;
        int height = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, targetX, targetZ);

        if (random.nextFloat() < 0.6F) {
            if (random.nextFloat() < 0.5F) {
                int targetHeight = height - 1;
                while (targetHeight > level.getMinY()) {
                    BlockPos below = new BlockPos(targetX, targetHeight - 1, targetZ);
                    boolean isEmptyBelow = level.getBlockState(below)
                            .getCollisionShape(level, below, CollisionContext.of(entity))
                            .isEmpty();
                    AABB aabb = entity.getDimensions(entity.getPose()).makeBoundingBox(targetX, targetHeight, targetZ);
                    if (!isEmptyBelow && level.noBlockCollision(entity, aabb)) {
                        entity.teleportTo(targetX, targetHeight, targetZ);
                        return;
                    }

                    targetHeight--;
                }
            }
            entity.teleportTo(targetX, height, targetZ);
        } else {
            entity.teleportTo(targetX, height + level.getRandom().nextInt(10, 200), targetZ);
        }
    }

    private void teleportEffect(Level level, BlockPos origin, LivingEntity entity) {
        teleportEntity(level, origin, entity);

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            Vec3 particlePos = origin.getCenter().lerp(entity.position(), (double) i / PARTICLE_COUNT);

            ((ServerLevel) level).sendParticles(
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

        level.playSound(
                null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.BLOCKS);
    }
}
