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

package de.blukae.badores

import de.blukae.badores.item.BadOreAxeItem
import de.blukae.badores.item.BadOreHoeItem
import de.blukae.badores.item.BadOreItem
import de.blukae.badores.item.BadOreShovelItem
import de.blukae.badores.ore.BadOre
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.ToolMaterial
import net.neoforged.neoforge.registries.DeferredItem

class BadOreToolSet(ore: BadOre, material: ToolMaterial) {
    val axe: DeferredItem<BadOreAxeItem> = BadOres.ITEMS.registerItem("${ore.name}_axe") { properties: Properties ->
        BadOreAxeItem(ore, material, 6.0f, -3.1f, properties)
    }
    val hoe: DeferredItem<BadOreHoeItem> = BadOres.ITEMS.registerItem("${ore.name}_hoe") { properties: Properties ->
        BadOreHoeItem(ore, material, -2.0f, -1.0f, properties)
    }
    val pickaxe: DeferredItem<BadOreItem> =
        BadOres.ITEMS.registerItem("${ore.name}_pickaxe") { properties: Properties ->
            BadOreItem(ore, properties.pickaxe(material, 1.0f, -2.8f))
        }
    val shovel: DeferredItem<BadOreShovelItem> = BadOres.ITEMS.registerItem("${ore.name}_shovel") { properties: Properties ->
        BadOreShovelItem(ore, material, 1.5f, -3.0f, properties)
    }
    val sword: DeferredItem<BadOreItem> = BadOres.ITEMS.registerItem("${ore.name}_sword") { properties: Properties ->
        BadOreItem(ore, properties.sword(material, 3.0f, -2.4f))
    }
}