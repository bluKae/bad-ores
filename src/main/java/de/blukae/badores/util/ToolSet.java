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
import de.blukae.badores.item.BadOreAxeItem;
import de.blukae.badores.item.BadOreHoeItem;
import de.blukae.badores.item.BadOreItem;
import de.blukae.badores.item.BadOreShovelItem;
import de.blukae.badores.ore.OreTemplate;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;

public class ToolSet {
    public final ToolMaterial material;

    public final DeferredItem<BadOreAxeItem> axe;
    public final DeferredItem<BadOreHoeItem> hoe;
    public final DeferredItem<Item> pickaxe;
    public final DeferredItem<BadOreShovelItem> shovel;
    public final DeferredItem<Item> sword;

    public ToolSet(String name, OreTemplate template, ToolInfo info) {
        material = new ToolMaterial(
                TagKey.create(Registries.BLOCK, BadOres.rl("incorrect_for_" + name)),
                info.maxUses(),
                info.efficiency(),
                info.damage(),
                info.enchantability(),
                TagKey.create(Registries.ITEM, BadOres.rl(name + "_tool_materials")));

        axe = BadOres.ITEMS.registerItem(
                name + "_axe",
                properties -> new BadOreAxeItem(template, material, 6.0F, -3.1F, properties));
        hoe = BadOres.ITEMS.registerItem(
                name + "_hoe",
                properties -> new BadOreHoeItem(template, material, 0.0F, -1.0F, properties));
        pickaxe = BadOres.ITEMS.registerItem(
                name + "_pickaxe",
                properties -> new BadOreItem(template, properties.pickaxe(material, 1.0F, -2.8F)));
        shovel = BadOres.ITEMS.registerItem(
                name + "_shovel",
                properties -> new BadOreShovelItem(template, material, 1.5F, -3.0F, properties));
        sword = BadOres.ITEMS.registerItem(
                name + "_sword",
                properties -> new BadOreItem(template, properties.sword(material, 3.0F, -2.4F)));
    }
}
