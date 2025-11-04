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

import de.blukae.badores.BadOres;
import de.blukae.badores.block.BadOreBlock;
import de.blukae.badores.item.BadOreBlockItem;
import de.blukae.badores.item.BadOreItem;
import de.blukae.badores.util.ArmorInfo;
import de.blukae.badores.util.ArmorSet;
import de.blukae.badores.util.ToolInfo;
import de.blukae.badores.util.ToolSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public enum BadOre implements OreBookPage {
    AMADEUM(new Amadeum()),
    APPETITE(new Appetite()),
    BALANCIUM(new Balancium()),
    BARELY_GENERITE(new BarelyGenerite()),
    BREAKIUM(new Breakium()),
    CRAPPIUM(new Crappium()),
    CRASHIUM(new Crashium()),
    ENDERITE(new Enderite()),
    EXPLODEITMITE(new Explodeitmite()),
    FLEESONSITE(new Fleesonsite()),
    GHOSTIUM(new Ghostium()),
    IDLIKEABITE(new Idlikeabite()),
    IWONTFITE(new Iwontfite()),
    KAKKARITE(new Kakkarite()),
    KILLIUM(new Killium()),
    LITE(new Lite()),
    LOOKSLIKEDIAMONDIUM(new Lookslikediamondium()),
    MARMITE(new Marmite()),
    METEORITE(new Meteorite()),
    MISLEADIUM(new Misleadium()),
    MOVIUM(new Movium()),
    NOPIUM(new Nopium()),
    NOSLEEPTONITE(new Nosleeptonite()),
    PAINTITWHITE(new Paintitwhite()),
    PANDAEMONIUM(new Pandaemonium()),
    POLITE(new Polite()),
    SHIFTIUM(new Shiftium()),
    SMITE(new Smite()),
    STONIUM(new Stonium()),
    STREETSCUM(new Streetscum()),
    TAUNTUM(new Tauntum()),
    UNOBTAINIUM(new Unobtainium()),
    USELESSIUM(new Uselessium()),
    WANNAFITE(new Wannafite()),
    WANTARITE(new Wantarite()),
    WEBSITE(new Website()),
    ZOMBIEUNITE(new Zombieunite());

    public final OreTemplate template;

    public final String name = this.name().toLowerCase();

    public final DeferredBlock<BadOreBlock> oreBlock;
    public final DeferredBlock<BadOreBlock> deepslateOreBlock;
    public final DeferredBlock<BadOreBlock> rawIngotBlock;
    public final DeferredBlock<BadOreBlock> ingotBlock;
    public final DeferredItem<BadOreItem> rawIngot;
    public final DeferredItem<BadOreItem> ingot;

    public final ArmorSet armor;
    public final ToolSet tools;

    public final ResourceKey<ConfiguredFeature<?, ?>> configuredFeature = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            BadOres.rl(name + "_ore"));
    public final ResourceKey<PlacedFeature> placedFeature = ResourceKey.create(
            Registries.PLACED_FEATURE,
            BadOres.rl(name + "_ore"));
    public final ResourceKey<BiomeModifier> biomeModifier = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            BadOres.rl(name + "_ore_feature"));

    public final TagKey<Block> ores = TagKey.create(Registries.BLOCK, BadOres.rl(name + "_ores"));
    public final TagKey<Item> oreItems = TagKey.create(Registries.ITEM, BadOres.rl(name + "_ores"));

    BadOre(OreTemplate template) {
        this.template = template;

        MapColor mapColor = template.getMapColor();

        oreBlock = BadOres.BLOCKS.registerBlock(
                name + "_ore",
                properties -> new BadOreBlock(template, true, properties),
                () -> template.getOreBlockProperties(false));
        BadOres.ITEMS.registerItem(
                name + "_ore",
                properties -> new BadOreBlockItem(template, oreBlock.get(), properties));

        if (template.hasDeepslateVariant()) {
            deepslateOreBlock = BadOres.BLOCKS.registerBlock(
                    "deepslate_" + name + "_ore",
                    properties -> new BadOreBlock(template, true, properties),
                    () -> template.getOreBlockProperties(true));
            BadOres.ITEMS.registerItem(
                    "deepslate_" + name + "_ore",
                    properties -> new BadOreBlockItem(template, deepslateOreBlock.get(), properties));
        } else {
            deepslateOreBlock = null;
        }

        if (template.hasRawIngot()) {
            rawIngotBlock = BadOres.BLOCKS.registerBlock(
                    "raw_" + name + "_block",
                    properties -> new BadOreBlock(template, false, properties),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(mapColor)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F));
            BadOres.ITEMS.registerItem(
                    "raw_" + name + "_block",
                    properties -> new BadOreBlockItem(template, rawIngotBlock.get(), properties));
        } else {
            rawIngotBlock = null;
        }

        if (template.hasIngotBlock()) {
            ingotBlock = BadOres.BLOCKS.registerBlock(
                    name + "_block",
                    properties -> new BadOreBlock(template, false, properties),
                    () -> BlockBehaviour.Properties.of()
                            .mapColor(mapColor)
                            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.IRON));
            BadOres.ITEMS.registerItem(
                    name + "_block",
                    properties -> new BadOreBlockItem(template, ingotBlock.get(), properties));
        } else {
            ingotBlock = null;
        }

        rawIngot = template.hasRawIngot() ?
                BadOres.ITEMS.registerItem(
                        "raw_" + name,
                        properties -> new BadOreItem(template, properties),
                        properties -> properties) :
                null;
        ingot = template.hasIngot() ? BadOres.ITEMS.registerItem(
                template.getIngotName(name),
                properties -> new BadOreItem(template, properties),
                properties -> properties) : null;

        ArmorInfo armorInfo = template.getArmorInfo();
        armor = armorInfo != null ? new ArmorSet(name, template, armorInfo) : null;

        ToolInfo toolInfo = template.getToolInfo();
        tools = toolInfo != null ? new ToolSet(name, template, toolInfo) : null;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public MutableComponent getName() {
        return Component.translatable("badores." + name + ".name");
    }

    @Override
    public MutableComponent getDescription() {
        return Component.translatable("badores." + name + ".description");
    }

    @Override
    public ItemStack getOreStack() {
        return oreBlock.toStack();
    }
}
