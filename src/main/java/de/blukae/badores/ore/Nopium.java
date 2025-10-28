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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class Nopium implements OreTemplate {
    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public @Nullable MapColor getMapColor() {
        return MapColor.COLOR_YELLOW;
    }

    @Override
    public ArmorInfo getArmorInfo() {
        return new ArmorInfo(15, new int[]{2, 6, 5, 2}, 1);
    }

    @Override
    public ToolInfo getToolInfo() {
        return new ToolInfo(BlockTags.INCORRECT_FOR_IRON_TOOL, 800, 0.5f, 1.0f, 1);
    }

    @Override
    public void onInventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (entity instanceof LivingEntity livingEntity && level.random.nextInt(200) == 0) {
            if (slot == null) {
                if (livingEntity instanceof Player player) {
                    Inventory inventory = player.getInventory();
                    for (int index = 0; index < inventory.getContainerSize(); index++) {
                        ItemStack s = inventory.getItem(index);
                        if (s == stack) {
                            inventory.setItem(index, ItemStack.EMPTY);
                        }
                    }
                }
            } else {
                livingEntity.setItemSlot(slot, ItemStack.EMPTY);
            }

            livingEntity.drop(stack, true, true);
        }
    }
}
