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

package de.blukae.badores.ore

import de.blukae.badores.ArmorInfo
import de.blukae.badores.ToolInfo
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block


object Lookslikediamondium : BadOre("lookslikediamondium") {
    override fun hasIngot() = true
    override fun hasRaw() = false
    override fun tools() = ToolInfo(0, 1, 2.0f, 0.0f, 1)
    override fun armor() = ArmorInfo(1, intArrayOf(1, 1, 1, 1), 1)

    override fun translation(): String = "Diamond"

    override fun hasCustomModels() = true
    override fun customModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        fun block(block: Block, parent: String) {
            blockModels.createTrivialBlock(
                block, TexturedModel.createDefault(
                    { TextureMapping() },
                    ModelTemplates.create(parent)
                )
            )
        }

        fun item(item: Item, parent: String) {
            itemModels.itemModelOutput.accept(
                item, ItemModelUtils.plainModel(
                    ModelTemplates.createItem(parent).create(
                        item,
                        TextureMapping(), itemModels.modelOutput
                    )
                )
            )
        }

        block(oreBlock.get(), "diamond_ore")

        block(deepslateOreBlock!!.get(), "deepslate_diamond_ore")
        block(ingotBlock!!.get(), "diamond_block")

        item(ingot!!.get(), "diamond")

        toolSet!!.let {
            item(it.axe.get(), "diamond_axe")
            item(it.hoe.get(), "diamond_hoe")
            item(it.pickaxe.get(), "diamond_pickaxe")
            item(it.shovel.get(), "diamond_shovel")
            item(it.sword.get(), "diamond_sword")
        }

        armorSet!!.let {
            item(it.helmet.get(), "diamond_helmet")
            item(it.chestplate.get(), "diamond_chestplate")
            item(it.leggings.get(), "diamond_leggings")
            item(it.boots.get(), "diamond_boots")
        }
    }

    override fun equipmentTexture(): ResourceLocation = ResourceLocation.parse("diamond")

}