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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.*;
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
    public void onArmorTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (level.getRandom().nextInt(200) == 0) {
            spawnLightning(level, entity.position());
        }
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (!level.isClientSide() && !player.preventsBlockDrops()) {
            if (level.getRandom().nextInt(3) == 0) {
                spawnLightning(level, player.position());
            } else {
                spawnLightning(level, Vec3.atBottomCenterOf(pos));
            }
        }
    }

    @Override
    public void onExploded(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion) {
        spawnLightning(level, Vec3.atBottomCenterOf(pos));
    }

    @Override
    public void onEntityHurt(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level level = attacker.level();

        if (!level.isClientSide() && level.getRandom().nextInt(2) == 0) {
            spawnLightning(level, level.getRandom().nextBoolean() ? attacker.position() : target.position());
        }
    }

    private void spawnLightning(Level level, Vec3 pos) {
        LightningBolt lightningBolt = EntityTypes.LIGHTNING_BOLT.create(level, EntitySpawnReason.MOB_SUMMONED);
        if (lightningBolt != null) {
            lightningBolt.snapTo(pos);
            level.addFreshEntity(lightningBolt);
        }
    }
}
