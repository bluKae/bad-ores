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
import de.blukae.badores.advancement.MineKilliumTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class Killium implements OreTemplate {
    public static final ResourceKey<DamageType> DAMAGE_TYPE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            BadOres.rl("killium"));

    public static final Supplier<MineKilliumTrigger> MINE_KILLIUM_TRIGGER = BadOres.TRIGGER_TYPES.register(
            "mine_killium",
            MineKilliumTrigger::new);

    private void killPlayer(Level level, LivingEntity entity) {
        var damageType = level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DAMAGE_TYPE);
        entity.hurt(new DamageSource(damageType), Float.MAX_VALUE);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && entity instanceof LivingEntity livingEntity && level.random.nextInt(1000) == 0) {
            killPlayer(level, livingEntity);
        }
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && willHarvest) {
            if (level.random.nextInt(5) == 0 && player instanceof ServerPlayer serverPlayer) {
                MINE_KILLIUM_TRIGGER.get().trigger(serverPlayer);
            } else {
                killPlayer(level, player);
            }
        }
    }
}
