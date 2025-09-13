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
import de.blukae.badores.ore.Fleesonsite
import de.blukae.badores.ore.Nosleeptonite
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.EntityLootSubProvider
import net.minecraft.world.entity.EntityType
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.stream.Stream

class BadOresEntityLoot(registries: HolderLookup.Provider): EntityLootSubProvider(FeatureFlags.DEFAULT_FLAGS, registries) {
    override fun generate() {
        val enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT)

        Fleesonsite.raw?.let {
            this.add(
                BadOres.FLEESONSITE_ENTITY_TYPE,
                LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                        .add(
                            LootItem.lootTableItem(it.get())
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))))
        }

        Nosleeptonite.raw?.let {
            this.add(
                BadOres.NOSLEEPTONITE_ENTITY_TYPE,
                LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                        .add(
                            LootItem.lootTableItem(it.get())
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0f, 1.0f))))))
        }
    }

    override fun getKnownEntityTypes(): Stream<EntityType<*>?> = BadOres.ENTITY_TYPES.entries.map { it.get() }.stream()

}