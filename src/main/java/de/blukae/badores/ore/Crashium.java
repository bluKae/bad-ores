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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class Crashium implements OreTemplate {
    public static final int CRASH_PROBABILITY = 5;

    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.PLANT;
    }

    @Override
    public ArmorInfo getArmorInfo() {
        return new ArmorInfo(8, new int[]{2, 7, 5, 2}, 9);
    }

    @Override
    public ToolInfo getToolInfo() {
        return new ToolInfo(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 8.0f, 2.0f, 8);
    }

    @Override
    public String getIngotName(String name) {
        return name + "_gemstone";
    }

    @Override
    public String getIngotTranslation(String translation) {
        return translation + " Gemstone";
    }

    @Override
    public void onArmorTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (!level.isClientSide() && level.getRandom().nextInt(800) == 0 && entity instanceof ServerPlayer player) {
            crash(player);
        }
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer && !player.preventsBlockDrops()) {
            crash(serverPlayer);
        }
    }

    @Override
    public void onEntityHurt(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide() && attacker instanceof ServerPlayer player) {
            crash(player);
        }
    }

    @Override
    public void onMine(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide() && miningEntity instanceof ServerPlayer player) {
            crash(player);
        }
    }

    @Override
    public void onArmorHurt(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        if (!entity.level().isClientSide() && entity instanceof ServerPlayer player) {
            crash(player);
        }
    }

    private void crash(ServerPlayer player) {
        new Thread(() -> {
            try {
                new RandomTranslation("badores.crashium.precrash", "Precrash").send(player);
                Thread.sleep(80 * 20);
                if (player.getRandom().nextInt(CRASH_PROBABILITY) == 0) {
                    new RandomTranslation("badores.crashium.crash", "Crash").send(player);
                    Thread.sleep(80 * 20);
                    System.exit(1);
                } else {
                    new RandomTranslation("badores.crashium.nocrash", "Nocrash").send(player);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
