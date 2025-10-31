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

package de.blukae.badores.data;

import de.blukae.badores.BadOres;
import de.blukae.badores.ore.BadOre;
import de.blukae.badores.ore.Killium;
import de.blukae.badores.ore.Wannafite;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class BadOresDataGeneration {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new BadOresBlockStates(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new BadOresItemModels(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new BadOresTranslations(output));

        generator.addProvider(
                event.includeServer(), new LootTableProvider(
                        output,
                        Set.of(),
                        List.of(
                                new LootTableProvider.SubProviderEntry(
                                        BadOresBlockLoot::new,
                                        LootContextParamSets.BLOCK),
                                new LootTableProvider.SubProviderEntry(
                                        BadOresEntityLoot::new,
                                        LootContextParamSets.ENTITY)),
                        lookupProvider));

        generator.addProvider(
                event.includeServer(), new DatapackBuiltinEntriesProvider(
                        output, lookupProvider, new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, BadOresDataGeneration::buildConfiguredFeatures)
                        .add(Registries.PLACED_FEATURE, BadOresDataGeneration::buildPlacedFeatures)
                        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BadOresDataGeneration::buildBiomeModifiers)
                        .add(Registries.DAMAGE_TYPE, BadOresDataGeneration::buildDamageTypes),
                        Set.of(BadOres.MOD_ID)));

        BlockTagsProvider blockTags = new BadOresBlockTags(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(
                event.includeServer(),
                new BadOresItemTags(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(
                event.includeServer(),
                new BadOresDamageTypeTags(output, lookupProvider, existingFileHelper));
        generator.addProvider(
                event.includeServer(), new AdvancementProvider(
                        output,
                        lookupProvider,
                        existingFileHelper,
                        List.of(new BadOresAdvancements())));
        generator.addProvider(event.includeServer(), new BadOresRecipes(output, lookupProvider));
    }

    private static void buildConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        for (BadOre ore : BadOre.values()) {
            List<OreConfiguration.TargetBlockState> targets = new ArrayList<>();
            targets.add(OreConfiguration.target(ore.template.getTarget(), ore.oreBlock.get().defaultBlockState()));
            if (ore.deepslateOreBlock != null) {
                targets.add(OreConfiguration.target(
                        new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                        ore.deepslateOreBlock.get().defaultBlockState()));
            }

            ConfiguredFeature<?, ?> feature = new ConfiguredFeature<>(
                    ore.template.getFeature(),
                    new OreConfiguration(targets, ore.template.getSize()));

            context.register(ore.configuredFeature, feature);
        }
    }

    private static void buildPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        for (BadOre ore : BadOre.values()) {
            List<PlacementModifier> placementModifiers = List.of(
                    ore.template.getPlacementModifier(),
                    InSquarePlacement.spread(),
                    ore.template.getHeightPlacementModifier());

            PlacedFeature feature = new PlacedFeature(
                    configuredFeatures.getOrThrow(ore.configuredFeature),
                    placementModifiers);

            context.register(ore.placedFeature, feature);
        }
    }

    private static void buildBiomeModifiers(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        for (BadOre ore : BadOre.values()) {
            BiomeModifier modifier = new BiomeModifiers.AddFeaturesBiomeModifier(
                    ore.template.getBiomes(biomes),
                    HolderSet.direct(placedFeatures.getOrThrow(ore.placedFeature)),
                    GenerationStep.Decoration.UNDERGROUND_ORES);

            context.register(ore.biomeModifier, modifier);
        }
    }

    private static void buildDamageTypes(BootstrapContext<DamageType> context) {
        context.register(Killium.DAMAGE_TYPE, new DamageType("killium", 0.0F));
        context.register(Wannafite.DAMAGE_TYPE, new DamageType("wannafite", 0.1F));
    }
}
