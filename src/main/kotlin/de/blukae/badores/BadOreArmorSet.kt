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

import de.blukae.badores.ore.BadOre
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.neoforged.neoforge.registries.DeferredItem

class BadOreArmorSet(val ore: BadOre, val material: ArmorMaterial) {
    val helmet: DeferredItem<BadOreItem> = registerItem("${ore.name}_helmet", ArmorType.HELMET)
    val chestplate: DeferredItem<BadOreItem> = registerItem("${ore.name}_chestplate", ArmorType.CHESTPLATE)
    val leggings: DeferredItem<BadOreItem> = registerItem("${ore.name}_leggings", ArmorType.LEGGINGS)
    val boots: DeferredItem<BadOreItem> = registerItem("${ore.name}_boots", ArmorType.BOOTS)

    private fun registerItem(name: String, armorType: ArmorType) = BadOres.ITEMS.registerItem(name) { properties ->
        BadOreItem(ore, properties.humanoidArmor(material, armorType))
    }
}