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

import de.blukae.badores.*
import de.blukae.badores.data.BadOresLanguageProvider
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAssets
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.OreFeature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.NeoForgeRegistries

abstract class BadOre(val name: String) {
    val configuredFeature: ResourceKey<ConfiguredFeature<*, *>> =
        ResourceKey.create(Registries.CONFIGURED_FEATURE, BadOres.rl("${name}_ore"))
    val placedFeature: ResourceKey<PlacedFeature> =
        ResourceKey.create(Registries.PLACED_FEATURE, BadOres.rl("${name}_ore"))
    val biomeModifier: ResourceKey<BiomeModifier> =
        ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BadOres.rl("${name}_ore_feature"))

    val oreTag: TagKey<Block> = TagKey.create(Registries.BLOCK, BadOres.rl("${name}_ores"))

    val repairsTag: TagKey<Item> = TagKey.create(Registries.ITEM, BadOres.rl("repairs_${name}_armor"))

    val oreBlock: DeferredBlock<BadOreBlock> = BadOres.BLOCKS.registerBlock("${name}_ore", BadOreBlock.build(this, false), oreBlockProperties())
    val deepslateOreBlock: DeferredBlock<BadOreBlock>?
    val ingot: DeferredItem<BadOreItem>?
    val raw: DeferredItem<BadOreItem>?
    val rawBlock: DeferredBlock<BadOreBlock>?
    val ingotBlock: DeferredBlock<BadOreBlock>?
    val toolSet: BadOreToolSet?
    val armorSet: BadOreArmorSet?

    init {
        BadOres.ITEMS.registerItem("${name}_ore", BadOreBlockItem.build(this, oreBlock))
        deepslateOreBlock = if (hasDeepslateVariant()) buildDeepslateOre() else null
        ingot = if (hasIngot()) buildIngot() else null
        raw = if (hasRaw()) buildRaw() else null
        rawBlock = if (hasRaw()) buildRawBlock() else null
        ingotBlock = if (hasIngotBlock()) buildIngotBlock() else null
        toolSet = tools()?.let(::buildToolSet)
        armorSet = armor()?.let(::buildArmorSet)
    }

    private fun buildDeepslateOre(): DeferredBlock<BadOreBlock> {
        val block = BadOres.BLOCKS.registerBlock("deepslate_${name}_ore", BadOreBlock.build(this, false), oreDeepslateBlockProperties())
        BadOres.ITEMS.registerItem("deepslate_${name}_ore", BadOreBlockItem.build(this, block))
        return block
    }

    private fun buildIngot(): DeferredItem<BadOreItem> {
        return BadOres.ITEMS.registerItem(ingotName(), BadOreItem.build(this))
    }

    private fun buildRaw(): DeferredItem<BadOreItem> {
        return BadOres.ITEMS.registerItem("raw_${name}", BadOreItem.build(this))
    }

    private fun buildRawBlock(): DeferredBlock<BadOreBlock> {
        val block = BadOres.BLOCKS.registerBlock("raw_${name}_block", BadOreBlock.build(this, true), rawBlockProperties())
        BadOres.ITEMS.registerItem("raw_${name}_block", BadOreBlockItem.build(this, block))
        return block
    }

    private fun buildIngotBlock(): DeferredBlock<BadOreBlock> {
        val block = BadOres.BLOCKS.registerBlock("${name}_block", BadOreBlock.build(this, true), ingotBlockProperties())
        BadOres.ITEMS.registerItem("${name}_block", BadOreBlockItem.build(this, block))
        return block
    }

    private fun buildToolSet(info: ToolInfo): BadOreToolSet {
        return BadOreToolSet(this, ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            info.maxUses,
            info.efficiency,
            info.damage,
            info.enchantability,
            repairsTag
        ))
    }

    private fun buildArmorSet(info: ArmorInfo): BadOreArmorSet {
        return BadOreArmorSet(this, ArmorMaterial(
            info.durability,
            mapOf(
                ArmorType.HELMET to info.reductions[0],
                ArmorType.CHESTPLATE to info.reductions[1],
                ArmorType.LEGGINGS to info.reductions[2],
                ArmorType.BOOTS to info.reductions[3],
            ),
            info.enchantility,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            0.0f,
            0.0f,
            repairsTag,
            ResourceKey.create(EquipmentAssets.ROOT_ID, BadOres.rl(name)),
        ))
    }

    open fun hasIngot() = false
    open fun hasRaw() = hasIngot()
    open fun hasIngotBlock() = hasIngot()
    open fun hasDeepslateVariant() = true
    open fun ingotName() = if (hasRaw()) "${name}_ingot" else name
    open fun replace(): RuleTest = TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES)
    open fun size() = 8
    open fun heightPlacement(): HeightRangePlacement =
        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64))
    open fun placement(): PlacementModifier = CountPlacement.of(8)
    open fun biomes(lookup: HolderGetter<Biome>): HolderSet<Biome> = lookup.getOrThrow(BiomeTags.IS_OVERWORLD)
    open fun tools(): ToolInfo? = null
    open fun armor(): ArmorInfo? = null
    open fun tickRate(isIngotBlock: Boolean): IntProvider? = null
    open fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder? = null
    open fun toolTag(): TagKey<Block> = BlockTags.MINEABLE_WITH_PICKAXE
    open fun levelTag(): TagKey<Block>? = BlockTags.NEEDS_IRON_TOOL
    open fun destroyTime() = 3.0f
    open fun explosionResistance() = destroyTime()
    open fun ingotBlockMapColor(): MapColor = MapColor.METAL
    open fun rawBlockMapColor(): MapColor = MapColor.RAW_IRON
    open fun shape(isIngotBlock: Boolean, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape? = null
    open fun getDestroyProgress(state: BlockState, player: Player, level: BlockGetter, pos: BlockPos) = Float.NaN
    open fun hasCustomModels() = false
    open fun customModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {}
    open fun equipmentTexture(): ResourceLocation = BadOres.rl(name)

    open fun feature(): OreFeature = Feature.ORE as OreFeature

    open fun translation(): String = name.split("_")
        .joinToString(" ") {
            it.first().uppercase() + it.substring(1)
        }
    open fun ingotTranslation(): String = if (hasRaw()) "${translation()} Ingot" else translation()

    open fun oreBlockProperties() = addBlockProperties(
        BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)
            .strength(destroyTime(), explosionResistance())
    )
    open fun oreDeepslateBlockProperties() = addBlockProperties(
        BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)
            .strength(destroyTime() + 1.5f, explosionResistance())
    )
    open fun ingotBlockProperties() = addBlockProperties(
        BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
            .mapColor(ingotBlockMapColor())
    )
    open fun rawBlockProperties() = addBlockProperties(
        BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)
            .mapColor(rawBlockMapColor())
    )
    open fun addBlockProperties(properties: BlockBehaviour.Properties): BlockBehaviour.Properties = properties


    open fun onTick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BadOreBlockEntity) {}
    open fun onRandomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {}
    open fun onInventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {}
    open fun onArmorTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot) {}
    open fun onDestroyedByPlayer(state: BlockState, level: Level, pos: BlockPos, player: Player, willHarvest: Boolean) {}
    open fun onDestroyed(state: BlockState, level: Level, pos: BlockPos) {}
    open fun spawnAfterBreak(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        stack: ItemStack,
        dropExperience: Boolean
    ) {}
    open fun onExploded(state: BlockState, level: ServerLevel, pos: BlockPos, explosion: Explosion) {}
    open fun onHurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {}
    open fun onMine(stack: ItemStack,
                    level: Level,
                    state: BlockState,
                    pos: BlockPos,
                    miningEntity: LivingEntity) {}
    open fun onArmorHurt(entity: LivingEntity, stack: ItemStack, slot: EquipmentSlot) {}

    open fun onUseItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): InteractionResult = InteractionResult.TRY_WITH_EMPTY_HAND

    open fun onUse(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult = InteractionResult.PASS

    open fun onAttack(state: BlockState, level: Level, pos: BlockPos, player: Player) {}
}