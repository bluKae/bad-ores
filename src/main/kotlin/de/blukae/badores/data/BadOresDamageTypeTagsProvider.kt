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
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.DamageTypeTagsProvider
import net.minecraft.tags.DamageTypeTags
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class BadOresDamageTypeTagsProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    DamageTypeTagsProvider(
        output, registries,
        BadOres.MOD_ID
    ) {
    override fun addTags(registries: HolderLookup.Provider) {
        tag(DamageTypeTags.BYPASSES_ARMOR).add(BadOres.KILLIUM_DAMAGE_TYPE)
        tag(DamageTypeTags.BYPASSES_EFFECTS).add(BadOres.KILLIUM_DAMAGE_TYPE)
        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(BadOres.KILLIUM_DAMAGE_TYPE)
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(BadOres.KILLIUM_DAMAGE_TYPE)
        tag(DamageTypeTags.BYPASSES_SHIELD).add(BadOres.KILLIUM_DAMAGE_TYPE)
        tag(DamageTypeTags.BYPASSES_WOLF_ARMOR).add(BadOres.KILLIUM_DAMAGE_TYPE)
        tag(Tags.DamageTypes.IS_MAGIC).add(BadOres.KILLIUM_DAMAGE_TYPE)
    }
}