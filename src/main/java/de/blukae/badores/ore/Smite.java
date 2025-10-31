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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Smite implements OreTemplate {

    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.DIAMOND;
    }

    @Override
    public ArmorInfo getArmorInfo() {
        return new ArmorInfo(8, new int[]{2, 5, 4, 2}, 8);
    }

    @Override
    public ToolInfo getToolInfo() {
        return new ToolInfo(BlockTags.INCORRECT_FOR_IRON_TOOL, 220, 5.0f, 2.0f, 8);
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
    public void onArmorTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && level.random.nextInt(200) == 0) {
            spawnLightning(level, entity.position());
        }
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && !player.isCreative()) {
            if (level.random.nextInt(3) == 0) {
                spawnLightning(level, player.position());
            } else {
                spawnLightning(level, pos.getBottomCenter());
            }
        }
    }

    @Override
    public void onExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide()) {
            spawnLightning(level, pos.getBottomCenter());
        }
    }

    @Override
    public void onEntityHurt(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level level = attacker.level();

        if (!level.isClientSide() && level.random.nextInt(2) == 0) {
            spawnLightning(level, level.random.nextBoolean() ? attacker.position() : target.position());
        }
    }

    private void spawnLightning(Level level, Vec3 pos) {
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        if (lightningBolt != null) {
            lightningBolt.moveTo(pos);
            level.addFreshEntity(lightningBolt);
        }
    }
}
