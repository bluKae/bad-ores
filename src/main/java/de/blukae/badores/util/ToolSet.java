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
import de.blukae.badores.item.*;
import de.blukae.badores.ore.OreTemplate;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Supplier;

public class ToolSet {
    public final Tier tier;

    public final DeferredItem<BadOreAxeItem> axe;
    public final DeferredItem<BadOreHoeItem> hoe;
    public final DeferredItem<BadOrePickaxeItem> pickaxe;
    public final DeferredItem<BadOreShovelItem> shovel;
    public final DeferredItem<BadOreSwordItem> sword;

    public ToolSet(String name, OreTemplate template, Supplier<Ingredient> repairIngredient, ToolInfo info) {
        tier = new SimpleTier(
                TagKey.create(Registries.BLOCK, BadOres.rl("incorrect_for_" + name)),
                info.maxUses(),
                info.efficiency(),
                info.damage(),
                info.enchantability(),
                repairIngredient);

        axe = BadOres.ITEMS.registerItem(
                name + "_axe",
                properties -> new BadOreAxeItem(template, tier, properties),
                new Item.Properties().attributes(AxeItem.createAttributes(tier, 6.0F, -3.1F)));
        hoe = BadOres.ITEMS.registerItem(
                name + "_hoe",
                properties -> new BadOreHoeItem(template, tier, properties),
                new Item.Properties().attributes(HoeItem.createAttributes(tier, 0.0F, -1.0F)));
        pickaxe = BadOres.ITEMS.registerItem(
                name + "_pickaxe",
                properties -> new BadOrePickaxeItem(template, tier, properties),
                new Item.Properties().attributes(PickaxeItem.createAttributes(tier, 1.0F, -2.8F)));
        shovel = BadOres.ITEMS.registerItem(
                name + "_shovel",
                properties -> new BadOreShovelItem(template, tier, properties),
                new Item.Properties().attributes(ShovelItem.createAttributes(tier, 1.5F, -3.0F)));
        sword = BadOres.ITEMS.registerItem(
                name + "_sword",
                properties -> new BadOreSwordItem(template, tier, properties),
                new Item.Properties().attributes(SwordItem.createAttributes(tier, 3.0F, -2.4F)));
    }
}
