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

import com.mojang.logging.LogUtils
import de.blukae.badores.advancement.HurtIwontfiteTrigger
import de.blukae.badores.advancement.MineBadOreTrigger
import de.blukae.badores.advancement.MineKilliumTrigger
import de.blukae.badores.data.*
import de.blukae.badores.entity.FleesonsiteEntity
import de.blukae.badores.entity.NosleeptoniteEntity
import de.blukae.badores.ore.*
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.advancements.AdvancementProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.util.context.ContextKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.world.BiomeModifiers
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.DeferredRegister.Entities
import net.neoforged.neoforge.registries.NeoForgeRegistries
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

@Mod(BadOres.MOD_ID)
@Suppress("unused")
object BadOres {
    const val MOD_ID = "badores"
    val LOGGER: Logger = LogUtils.getLogger()

    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(MOD_ID)
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(MOD_ID)
    val BLOCK_ENTITY_TYPES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID)
    val CREATIVE_MODE_TABS: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MOD_ID)
    val SOUND_EVENTS: DeferredRegister<SoundEvent> = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MOD_ID)
    val ENTITY_TYPES: Entities = DeferredRegister.createEntities(MOD_ID)
    val TRIGGER_TYPES: DeferredRegister<CriterionTrigger<*>> = DeferredRegister.create(Registries.TRIGGER_TYPE, MOD_ID)
    val LOOT_CONDITION_TYPES: DeferredRegister<LootItemConditionType> =
        DeferredRegister.create(BuiltInRegistries.LOOT_CONDITION_TYPE, MOD_ID)
    val FEATURES: DeferredRegister<Feature<*>> = DeferredRegister.create(BuiltInRegistries.FEATURE, MOD_ID)

    val BAD_ORE_BLOCK_ENTITY: BlockEntityType<BadOreBlockEntity> by BLOCK_ENTITY_TYPES.register("bad_ore") { ->
        val blocks = ORES.flatMap { listOfNotNull(it.oreBlock, it.deepslateOreBlock, it.ingotBlock) }
            .map { it.get() }
            .toSet()
        BlockEntityType(::BadOreBlockEntity, blocks)
    }

    val STONIUM_FEATURE: Stonium.Feature by FEATURES.register("stonium") { ->
        Stonium.Feature(OreConfiguration.CODEC)
    }

    val MINE_BAD_ORE_TRIGGER: MineBadOreTrigger by TRIGGER_TYPES.register("mine_bad_ore", ::MineBadOreTrigger)
    val MINE_KILLIUM_TRIGGER: MineKilliumTrigger by TRIGGER_TYPES.register("mine_killium", ::MineKilliumTrigger)
    val HURT_IWONTFITE_TRIGGER: HurtIwontfiteTrigger by TRIGGER_TYPES.register("hurt_iwontfite", ::HurtIwontfiteTrigger)

    val MINING_CONSEQUENCE_CONTEXT_KEY = ContextKey<MiningConsequence>(rl("consequence"))
    val MINING_CONSEQUENCE_CONDITION_TYPE: LootItemConditionType by LOOT_CONDITION_TYPES.register("mining_consequence") { ->
        LootItemConditionType(MiningConsequence.Condition.CODEC)
    }

    val FLEESONSITE_AMBIENT: SoundEvent by SOUND_EVENTS.register(
        "entity.fleesonsite.ambient",
        SoundEvent::createVariableRangeEvent
    )
    val FLEESONSITE_DEATH: SoundEvent by SOUND_EVENTS.register(
        "entity.fleesonsite.death",
        SoundEvent::createVariableRangeEvent
    )
    val FLEESONSITE_HURT: SoundEvent by SOUND_EVENTS.register(
        "entity.fleesonsite.hurt",
        SoundEvent::createVariableRangeEvent
    )

    val NOSLEEPTONITE_AMBIENT: SoundEvent by SOUND_EVENTS.register(
        "entity.nosleeptonite.ambient",
        SoundEvent::createVariableRangeEvent
    )
    val NOSLEEPTONITE_DEATH: SoundEvent by SOUND_EVENTS.register(
        "entity.nosleeptonite.death",
        SoundEvent::createVariableRangeEvent
    )
    val NOSLEEPTONITE_HURT: SoundEvent by SOUND_EVENTS.register(
        "entity.nosleeptonite.hurt",
        SoundEvent::createVariableRangeEvent
    )
    val NOSLEEPTONITE_IDLE_SOUND_EVENT: SoundEvent by SOUND_EVENTS.register(
        "nosleeptonite.idle",
        SoundEvent::createVariableRangeEvent
    )

    val PANDAEMONIUM_BREAK_SOUND_EVENT: SoundEvent by SOUND_EVENTS.register(
        "block.pandaemonium.break",
        SoundEvent::createVariableRangeEvent
    )

    val FLEESONSITE_ENTITY_TYPE: EntityType<FleesonsiteEntity> by ENTITY_TYPES.registerEntityType(
        "fleesonsite", ::FleesonsiteEntity,
        MobCategory.CREATURE
    ) {
        it.sized(1.0f, 1.0f)
    }

    val NOSLEEPTONITE_ENTITY_TYPE: EntityType<NosleeptoniteEntity> by ENTITY_TYPES.registerEntityType(
        "nosleeptonite", ::NosleeptoniteEntity,
        MobCategory.MONSTER
    ) {
        it.sized(1.0f, 1.0f)
    }

    val FLEESONSITE_SPAWN_EGG: SpawnEggItem by ITEMS.registerItem("fleesonsite_spawn_egg") {
        SpawnEggItem(FLEESONSITE_ENTITY_TYPE, it)
    }

    val DEEPSLATE_FLEESONSITE_SPAWN_EGG: SpawnEggItem by ITEMS.registerItem("deepslate_fleesonsite_spawn_egg") {
        val entityData = CustomData.of(CompoundTag().also { tag ->
            tag.store("id", ResourceLocation.CODEC, BuiltInRegistries.ENTITY_TYPE.getKey(FLEESONSITE_ENTITY_TYPE))
            tag.putBoolean("IsDeepslate", true)
        })

        SpawnEggItem(FLEESONSITE_ENTITY_TYPE, it.component(DataComponents.ENTITY_DATA, entityData))
    }

    val NOSLEEPTONITE_SPAWN_EGG: SpawnEggItem by ITEMS.registerItem("nosleeptonite_spawn_egg") {
        SpawnEggItem(NOSLEEPTONITE_ENTITY_TYPE, it)
    }

    val KILLIUM_DAMAGE_TYPE: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, rl("killium"))
    val WANNAFITE_DAMAGE_TYPE: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, rl("wannafite"))

    val BAD_ORES_TAG: TagKey<Block> = TagKey.create(Registries.BLOCK, rl("bad_ores"))

    val MARMITE_BREAD_ITEM: Item by ITEMS.registerItem(
        "marmite_bread",
        ::Item,
        Item.Properties().food(FoodProperties.Builder().nutrition(6).saturationModifier(0.8F).build())
    )

    val ORE_BOOK_ITEM: BadOreBookItem by ITEMS.registerItem("bad_ore_book", ::BadOreBookItem)

    val ORE_BOOK_COMPONENTS: TagKey<Item> = TagKey.create(Registries.ITEM, rl("ore_book_components"))

    val BAD_ORE_TAB: CreativeModeTab by CREATIVE_MODE_TABS.register(
        "tab"
    ) { ->
        CreativeModeTab.builder().apply {
            title(Component.translatable("itemGroup.badores"))
            withTabsBefore(CreativeModeTabs.COMBAT)
            icon { Amadeum.oreBlock.asItem().defaultInstance }
            displayItems { parameters: CreativeModeTab.ItemDisplayParameters?, output: CreativeModeTab.Output ->
                ITEMS.entries.asSequence()
                    .map { it.get() }
                    .forEach(output::accept)
            }
        }.build()
    }

    val ORES = listOf(
        Amadeum,
        Appetite,
        Balancium,
        BarelyGenerite,
        Breakium,
        Crappium,
        Crashium,
        Enderite,
        Explodeitmite,
        Fleesonsite,
        Ghostium,
        Idlikeabite,
        Iwontfite,
        Kakkarite,
        Killium,
        Lite,
        Lookslikediamondium,
        Marmite,
        Meteorite,
        Misleadium,
        Movium,
        Nopium,
        Nosleeptonite,
        Paintitwhite,
        Pandaemonium,
        Polite,
        Shiftium,
        Smite,
        Stonium,
        Streetscum,
        Tauntum,
        Unobtainium,
        Uselessium,
        Wannafite,
        Wantarite,
        Website,
        Zombieunite,
    )

    init {
        MOD_BUS.register(this)

        BLOCKS.register(MOD_BUS)
        ITEMS.register(MOD_BUS)
        BLOCK_ENTITY_TYPES.register(MOD_BUS)
        CREATIVE_MODE_TABS.register(MOD_BUS)
        SOUND_EVENTS.register(MOD_BUS)
        ENTITY_TYPES.register(MOD_BUS)
        TRIGGER_TYPES.register(MOD_BUS)
        FEATURES.register(MOD_BUS)

        // modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC)
    }

    @SubscribeEvent
    fun createAttributes(event: EntityAttributeCreationEvent) {
        event.put(
            FLEESONSITE_ENTITY_TYPE,
            Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .build()
        )

        event.put(
            NOSLEEPTONITE_ENTITY_TYPE,
            Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .build()
        )
    }

    @SubscribeEvent
    fun registerPayloadHandlers(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")
        registrar.commonToClient(
            RandomTranslation.TYPE,
            RandomTranslation.STREAM_CODEC,
        )
    }

    @SubscribeEvent
    fun gatherClientData(event: GatherDataEvent.Client) {
        event.createProvider(::BadOresModelProvider)
        event.createProvider(::BadOresEquipmentAssetProvider)
        event.createProvider(::BadOresLanguageProvider)

        event.createProvider { output, lookupProvider ->
            LootTableProvider(
                output,
                setOf(),
                listOf(
                    SubProviderEntry(::BadOresBlockLoot, LootContextParamSets.BLOCK),
                    SubProviderEntry(::BadOresEntityLoot, LootContextParamSets.ENTITY),
                ),
                lookupProvider
            )
        }

        event.createDatapackRegistryObjects(
            RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE) { context ->
                    for (ore in ORES) {
                        val deepslateOreBlock = ore.deepslateOreBlock
                        val targets = if (deepslateOreBlock != null) {
                            listOf(
                                OreConfiguration.target(ore.replace(), ore.oreBlock.get().defaultBlockState()),
                                OreConfiguration.target(
                                    TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                                    deepslateOreBlock.get().defaultBlockState()
                                ),
                            )
                        } else {
                            listOf(
                                OreConfiguration.target(ore.replace(), ore.oreBlock.get().defaultBlockState()),
                            )
                        }

                        val feature = ConfiguredFeature(
                            ore.feature(),
                            OreConfiguration(targets, ore.size())
                        )

                        context.register(ore.configuredFeature, feature)
                    }
                }
                .add(Registries.PLACED_FEATURE) { context ->
                    val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)

                    for (ore in ORES) {
                        val placementModifiers =
                            listOf(ore.placement(), InSquarePlacement.spread(), ore.heightPlacement())
                        val feature =
                            PlacedFeature(configuredFeatures.getOrThrow(ore.configuredFeature), placementModifiers)

                        context.register(ore.placedFeature, feature)
                    }
                }
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS) { context ->
                    val biomes = context.lookup(Registries.BIOME)
                    val placedFeatures = context.lookup(Registries.PLACED_FEATURE)

                    for (ore in ORES) {
                        val modifier = BiomeModifiers.AddFeaturesBiomeModifier(
                            ore.biomes(biomes),
                            HolderSet.direct(placedFeatures.getOrThrow(ore.placedFeature)),
                            GenerationStep.Decoration.UNDERGROUND_ORES
                        )

                        context.register(ore.biomeModifier, modifier)
                    }

                }
                .add(Registries.DAMAGE_TYPE) { context ->
                    context.register(KILLIUM_DAMAGE_TYPE, DamageType("killium", 0.0F))
                    context.register(WANNAFITE_DAMAGE_TYPE, DamageType("wannafite", 0.1F))
                }
        )

        event.createProvider(::BadOresBlockTagsProvider)
        event.createProvider(::BadOresItemTagsProvider)
        event.createProvider(::BadOresDamageTypeTagsProvider)
        event.createProvider { output, lookupProvider ->
            AdvancementProvider(
                output, lookupProvider,
                listOf(BadOresAdvancements())
            )
        }
        event.createProvider(BadOresRecipeProvider.Companion::Runner)
    }

    fun rl(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}
