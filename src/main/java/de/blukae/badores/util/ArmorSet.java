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
import de.blukae.badores.item.BadOreArmorItem;
import de.blukae.badores.ore.OreTemplate;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ArmorSet {
    public final Holder<ArmorMaterial> material;

    public final DeferredItem<BadOreArmorItem> helmet;
    public final DeferredItem<BadOreArmorItem> chestplate;
    public final DeferredItem<BadOreArmorItem> leggings;
    public final DeferredItem<BadOreArmorItem> boots;

    public ArmorSet(String name, OreTemplate template, Supplier<Ingredient> repairIngredient, ArmorInfo info) {
        material = BadOres.ARMOR_MATERIALS.register(
                name, () -> new ArmorMaterial(
                        new EnumMap<>(Map.of(
                                ArmorItem.Type.HELMET,
                                info.reductions()[0],
                                ArmorItem.Type.CHESTPLATE,
                                info.reductions()[1],
                                ArmorItem.Type.LEGGINGS,
                                info.reductions()[2],
                                ArmorItem.Type.BOOTS,
                                info.reductions()[3])),
                        info.enchantability(),
                        SoundEvents.ARMOR_EQUIP_GENERIC,
                        repairIngredient,
                        List.of(new ArmorMaterial.Layer(template.getEquipmentTextureLocation(name))),
                        0.0f,
                        0.0f));

        helmet = BadOres.ITEMS.registerItem(
                name + "_helmet",
                properties -> new BadOreArmorItem(
                        template,
                        material,
                        ArmorItem.Type.HELMET,
                        properties.durability(info.durability())));
        chestplate = BadOres.ITEMS.registerItem(
                name + "_chestplate",
                properties -> new BadOreArmorItem(
                        template,
                        material,
                        ArmorItem.Type.CHESTPLATE,
                        properties.durability(info.durability())));
        leggings = BadOres.ITEMS.registerItem(
                name + "_leggings",
                properties -> new BadOreArmorItem(
                        template,
                        material,
                        ArmorItem.Type.LEGGINGS,
                        properties.durability(info.durability())));
        boots = BadOres.ITEMS.registerItem(
                name + "_boots",
                properties -> new BadOreArmorItem(
                        template,
                        material,
                        ArmorItem.Type.BOOTS,
                        properties.durability(info.durability())));
    }
}
