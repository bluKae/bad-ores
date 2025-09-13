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

package de.blukae.badores.data

import de.blukae.badores.BadOres
import de.blukae.badores.advancement.HurtIwontfiteTrigger
import de.blukae.badores.advancement.MineBadOreTrigger
import de.blukae.badores.advancement.MineKilliumTrigger
import de.blukae.badores.ore.Amadeum
import de.blukae.badores.ore.BarelyGenerite
import de.blukae.badores.ore.Fleesonsite
import de.blukae.badores.ore.Iwontfite
import de.blukae.badores.ore.Killium
import de.blukae.badores.ore.Nosleeptonite
import de.blukae.badores.ore.Shiftium
import de.blukae.badores.ore.Zombieunite
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.KilledTrigger
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger
import net.minecraft.advancements.critereon.RecipeCraftedTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.advancements.AdvancementSubProvider
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Consumer


class BadOresAdvancements: AdvancementSubProvider {
    override fun generate(
        registries: HolderLookup.Provider,
        writer: Consumer<AdvancementHolder>
    ) {
        val entityTypes = registries.lookupOrThrow(Registries.ENTITY_TYPE)
        val blocks = registries.lookupOrThrow(Registries.BLOCK)

        val root = Advancement.Builder.advancement()
            .display(
                Amadeum.oreBlock,
                Component.translatable("advancements.badores.badores.root.title"),
                Component.translatable("advancements.badores.badores.root.description"),
                ResourceLocation.withDefaultNamespace("gui/advancements/backgrounds/stone"),
                AdvancementType.TASK,
                false,
                false,
                false,
            )
            .addCriterion("mine_bad_ore", MineBadOreTrigger.TriggerInstance.minedAny())
            .save(writer, BadOres.rl("badores/root"))

        val findBarelyGenerite = Advancement.Builder.advancement()
            .display(
                BarelyGenerite.oreBlock,
                Component.translatable("advancements.badores.badores.find_barely_generite.title"),
                Component.translatable("advancements.badores.badores.find_barely_generite.description"),
                null,
                AdvancementType.GOAL,
                true,
                true,
                false
            )
            .parent(root)
            .addCriterion("has_barely_generite", InventoryChangeTrigger.TriggerInstance.hasItems(
                BarelyGenerite.oreBlock,
                BarelyGenerite.deepslateOreBlock,
            ))
            .save(writer, BadOres.rl("badores/find_barely_generite"))

        BarelyGenerite.ingotBlock?.let {
            Advancement.Builder.advancement()
                .parent(findBarelyGenerite)
                .display(
                    it,
                    Component.translatable("advancements.badores.badores.craft_barely_generite_block.title"),
                    Component.translatable("advancements.badores.badores.craft_barely_generite_block.description"),
                    null,
                    AdvancementType.GOAL,
                    true,
                    true,
                    false
                )
                .addCriterion("craft_barely_generite_block", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                    ResourceKey.create(Registries.RECIPE, ResourceLocation.withDefaultNamespace("barely_generite_block"))
                ))
                .save(writer, BadOres.rl("badores/craft_barely_generite_block"))
        }


        Advancement.Builder.advancement()
            .parent(root)
            .display(
                Iwontfite.oreBlock,
                Component.translatable("advancements.badores.badores.deal_iwontfite_damage.title"),
                Component.translatable("advancements.badores.badores.deal_iwontfite_damage.description"),
                null,
                AdvancementType.CHALLENGE,
                true,
                true,
                false
            )
            .addCriterion("deal_iwontfite_damage", HurtIwontfiteTrigger.TriggerInstance.hurtWithIwontfite())
            .save(writer, BadOres.rl("badores/deal_iwontfite_damage"))

        Advancement.Builder.advancement()
            .parent(root)
            .display(
                Nosleeptonite.oreBlock,
                Component.translatable("advancements.badores.badores.kill_nosleeptonite.title"),
                Component.translatable("advancements.badores.badores.kill_nosleeptonite.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("kill_nosleeptonite", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityTypes,
                BadOres.NOSLEEPTONITE_ENTITY_TYPE)))
            .save(writer, BadOres.rl("badores/kill_nosleeptonite"))

        Advancement.Builder.advancement()
            .parent(findBarelyGenerite)
            .display(
                BadOres.ORE_BOOK_ITEM,
                Component.translatable("advancements.badores.badores.mine_all_ores.title"),
                Component.translatable("advancements.badores.badores.mine_all_ores.description"),
                null,
                AdvancementType.GOAL,
                true,
                true,
                false
            )
            .apply {
                for (ore in BadOres.ORES) {
                    addCriterion("mine_${ore.name}_ore", MineBadOreTrigger.TriggerInstance.minedTag(blocks, ore.oreTag))
                }
            }
            .save(writer, BadOres.rl("badores/mine_all_ores"))

        Advancement.Builder.advancement()
            .parent(root)
            .display(
                Killium.oreBlock,
                Component.translatable("advancements.badores.badores.mine_killium.title"),
                Component.translatable("advancements.badores.badores.mine_killium.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("mine_killium", MineKilliumTrigger.TriggerInstance.minedSafely())
            .save(writer, BadOres.rl("badores/mine_killium"))

        Advancement.Builder.advancement()
            .parent(root)
            .display(
                BadOres.MARMITE_BREAD_ITEM,
                Component.translatable("advancements.badores.badores.obtain_marmite_bread.title"),
                Component.translatable("advancements.badores.badores.obtain_marmite_bread.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("has_marmite_bread", InventoryChangeTrigger.TriggerInstance.hasItems(BadOres.MARMITE_BREAD_ITEM))
            .save(writer, BadOres.rl("badores/obtain_marmite_bread"))

        Advancement.Builder.advancement()
            .parent(root)
            .display(
                Zombieunite.oreBlock,
                Component.translatable("advancements.badores.badores.mine_zombieunite.title"),
                Component.translatable("advancements.badores.badores.mine_zombieunite.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("mine_zombieunite", MineBadOreTrigger.TriggerInstance.minedTag(blocks, Zombieunite.oreTag))
            .save(writer, BadOres.rl("badores/mine_zombieunite"))

        Fleesonsite.ingot?.let {
            Advancement.Builder.advancement()
                .parent(root)
                .display(
                    it,
                    Component.translatable("advancements.badores.badores.obtain_fleesonite_ingot.title"),
                    Component.translatable("advancements.badores.badores.obtain_fleesonite_ingot.description"),
                    null,
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                )
                .addCriterion("has_fleesonite_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(it))
                .save(writer, BadOres.rl("badores/obtain_fleesonite_ingot"))
        }

        Advancement.Builder.advancement()
            .parent(root)
            .display(
                Shiftium.oreBlock,
                Component.translatable("advancements.badores.badores.obtain_shiftium_ore.title"),
                Component.translatable("advancements.badores.badores.obtain_shiftium_ore.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion("has_shiftium_ore", InventoryChangeTrigger.TriggerInstance.hasItems(Shiftium.oreBlock))
            .addCriterion("has_deepslate_shiftium_ore", InventoryChangeTrigger.TriggerInstance.hasItems(Shiftium.deepslateOreBlock))
            .save(writer, BadOres.rl("badores/obtain_shiftium_ore"))
    }
}