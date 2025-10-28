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

package de.blukae.badores.util;

import de.blukae.badores.BadOres;
import de.blukae.badores.item.BadOreItem;
import de.blukae.badores.ore.OreTemplate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.EnumMap;
import java.util.Map;

public class ArmorSet {
    public final ArmorMaterial material;

    public final DeferredItem<BadOreItem> helmet;
    public final DeferredItem<BadOreItem> chestplate;
    public final DeferredItem<BadOreItem> leggings;
    public final DeferredItem<BadOreItem> boots;

    public ArmorSet(String name, OreTemplate template, ArmorInfo info) {
        material = new ArmorMaterial(
                info.durability(),
                new EnumMap<>(Map.of(ArmorType.HELMET,
                        info.reductions()[0],
                        ArmorType.CHESTPLATE,
                        info.reductions()[1],
                        ArmorType.LEGGINGS,
                        info.reductions()[2],
                        ArmorType.BOOTS,
                        info.reductions()[3])),
                info.enchantability(),
                SoundEvents.ARMOR_EQUIP_GENERIC,
                0.0f,
                0.0f,
                TagKey.create(Registries.ITEM, BadOres.rl("repairs_" + name + "_armor")),
                ResourceKey.create(EquipmentAssets.ROOT_ID, BadOres.rl(name)));

        helmet = BadOres.ITEMS.registerItem(
                name + "_helmet",
                properties -> new BadOreItem(template, properties.humanoidArmor(material, ArmorType.HELMET)));
        chestplate = BadOres.ITEMS.registerItem(
                name + "_chestplate",
                properties -> new BadOreItem(template, properties.humanoidArmor(material, ArmorType.CHESTPLATE)));
        leggings = BadOres.ITEMS.registerItem(
                name + "_leggings",
                properties -> new BadOreItem(template, properties.humanoidArmor(material, ArmorType.LEGGINGS)));
        boots = BadOres.ITEMS.registerItem(
                name + "_boots",
                properties -> new BadOreItem(template, properties.humanoidArmor(material, ArmorType.BOOTS)));
    }
}
