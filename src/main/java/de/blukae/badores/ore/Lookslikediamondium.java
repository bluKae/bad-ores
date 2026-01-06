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
import de.blukae.badores.util.ArmorSet;
import de.blukae.badores.util.ToolInfo;
import de.blukae.badores.util.ToolSet;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Lookslikediamondium implements OreTemplate {
    @Override
    public boolean hasIngot() {
        return true;
    }

    @Override
    public boolean hasRawIngot() {
        return false;
    }

    @Override
    public ArmorInfo getArmorInfo() {
        return new ArmorInfo(1, new int[]{1, 1, 1, 1}, 1);
    }

    @Override
    public ToolInfo getToolInfo() {
        return new ToolInfo(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 1, 2.0f, 0.0f, 1);
    }

    @Override
    public boolean hasCustomModels() {
        return true;
    }

    @Override
    public void buildCustomModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        CustomModelsHelper helper = new CustomModelsHelper(blockModels, itemModels);

        helper.block(BadOre.LOOKSLIKEDIAMONDIUM.oreBlock.get(), "diamond_ore");

        helper.block(BadOre.LOOKSLIKEDIAMONDIUM.deepslateOreBlock.get(), "deepslate_diamond_ore");
        helper.block(BadOre.LOOKSLIKEDIAMONDIUM.ingotBlock.get(), "diamond_block");

        helper.item(BadOre.LOOKSLIKEDIAMONDIUM.ingot.get(), "diamond");

        ToolSet tools = BadOre.LOOKSLIKEDIAMONDIUM.tools;
        helper.item(tools.axe.get(), "diamond_axe");
        helper.item(tools.hoe.get(), "diamond_hoe");
        helper.item(tools.pickaxe.get(), "diamond_pickaxe");
        helper.item(tools.shovel.get(), "diamond_shovel");
        helper.item(tools.sword.get(), "diamond_sword");

        ArmorSet armor = BadOre.LOOKSLIKEDIAMONDIUM.armor;
        helper.item(armor.helmet.get(), "diamond_helmet");
        helper.item(armor.chestplate.get(), "diamond_chestplate");
        helper.item(armor.leggings.get(), "diamond_leggings");
        helper.item(armor.boots.get(), "diamond_boots");
    }

    @Override
    public Identifier getEquipmentTextureLocation(String name) {
        return Identifier.withDefaultNamespace("diamond");
    }

    @Override
    public String getTranslation(String translation) {
        return "Diamond";
    }

    private record CustomModelsHelper(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        public void block(Block block, String parent) {
            blockModels.createTrivialBlock(
                    block,
                    TexturedModel.createDefault(
                            textureMapping -> new TextureMapping(),
                            ModelTemplates.create(parent)));
        }

        public void item(Item item, String parent) {
            itemModels.itemModelOutput.accept(
                    item,
                    ItemModelUtils.plainModel(ModelTemplates.createItem(parent)
                            .create(item, new TextureMapping(), itemModels.modelOutput)));
        }
    }
}
