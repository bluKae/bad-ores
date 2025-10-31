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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

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
    public void buildCustomBlockStates(BlockStateProvider provider) {
        final class CustomStatesHelper {
            void block(DeferredBlock<?> block, String parent) {
                provider.simpleBlock(
                        block.get(), provider.models()
                                .withExistingParent(
                                        block.getId().getPath(),
                                        ModelProvider.BLOCK_FOLDER + "/" + parent));
            }
        }

        CustomStatesHelper helper = new CustomStatesHelper();

        helper.block(BadOre.LOOKSLIKEDIAMONDIUM.oreBlock, "diamond_ore");
        helper.block(BadOre.LOOKSLIKEDIAMONDIUM.deepslateOreBlock, "deepslate_diamond_ore");
        helper.block(BadOre.LOOKSLIKEDIAMONDIUM.ingotBlock, "diamond_block");
    }

    @Override
    public void buildCustomItemModels(ItemModelProvider provider) {
        final class CustomModelsHelper {
            void item(DeferredItem<?> item, String parent) {
                provider.withExistingParent(item.getId().toString(), ModelProvider.ITEM_FOLDER + "/" + parent);
            }
        }

        CustomModelsHelper helper = new CustomModelsHelper();

        provider.simpleBlockItem(BadOre.LOOKSLIKEDIAMONDIUM.oreBlock.get());
        provider.simpleBlockItem(BadOre.LOOKSLIKEDIAMONDIUM.deepslateOreBlock.get());
        provider.simpleBlockItem(BadOre.LOOKSLIKEDIAMONDIUM.ingotBlock.get());

        helper.item(BadOre.LOOKSLIKEDIAMONDIUM.ingot, "diamond");

        ToolSet tools = BadOre.LOOKSLIKEDIAMONDIUM.tools;
        helper.item(tools.axe, "diamond_axe");
        helper.item(tools.hoe, "diamond_hoe");
        helper.item(tools.pickaxe, "diamond_pickaxe");
        helper.item(tools.shovel, "diamond_shovel");
        helper.item(tools.sword, "diamond_sword");

        ArmorSet armor = BadOre.LOOKSLIKEDIAMONDIUM.armor;
        helper.item(armor.helmet, "diamond_helmet");
        helper.item(armor.chestplate, "diamond_chestplate");
        helper.item(armor.leggings, "diamond_leggings");
        helper.item(armor.boots, "diamond_boots");
    }

    @Override
    public ResourceLocation getEquipmentTextureLocation(String name) {
        return ResourceLocation.withDefaultNamespace("diamond");
    }

    @Override
    public String getTranslation(String translation) {
        return "Diamond";
    }
}
