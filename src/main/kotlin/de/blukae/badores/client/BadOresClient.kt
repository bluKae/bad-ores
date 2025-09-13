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

package de.blukae.badores.client

import de.blukae.badores.BadOres
import de.blukae.badores.RandomTranslation
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent


@EventBusSubscriber(Dist.CLIENT)
object BadOresClient {
    @SubscribeEvent
    fun registerLayerDefinitions(event: RegisterLayerDefinitions) {
        event.registerLayerDefinition(FleesonsiteEntityModel.LAYER, FleesonsiteEntityModel::createBodyLayer)
        event.registerLayerDefinition(NosleeproniteEntityModel.LAYER, NosleeproniteEntityModel::createBodyLayer)
    }

    @SubscribeEvent
    fun registerEntityRenderers(event: RegisterRenderers) {
        event.registerEntityRenderer(BadOres.FLEESONSITE_ENTITY_TYPE, ::FleesonsiteEntityRenderer)
        event.registerEntityRenderer(BadOres.NOSLEEPTONITE_ENTITY_TYPE, ::NosleeptoniteEntityRenderer)
    }

    @SubscribeEvent
    fun registerClientPayloadHandlers(event: RegisterClientPayloadHandlersEvent) {
        event.register(RandomTranslation.TYPE) { data, context ->
            Minecraft.getInstance().chatListener.handleSystemMessage(data.randomComponent(), false)
        }
    }

}