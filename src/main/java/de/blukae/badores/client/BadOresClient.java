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

package de.blukae.badores.client;

import de.blukae.badores.BadOres;
import de.blukae.badores.RandomTranslation;
import de.blukae.badores.ore.Fleesonsite;
import de.blukae.badores.ore.Nosleeptonite;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@Mod(value = BadOres.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class BadOresClient {
    public static void openBadOreBook() {
        Minecraft.getInstance().setScreen(new BadOreBookScreen());
    }

    public BadOresClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FleesonsiteEntityModel.LAYER, FleesonsiteEntityModel::createBodyLayer);
        event.registerLayerDefinition(NosleeptoniteEntityModel.LAYER, NosleeptoniteEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Fleesonsite.FLEESONSITE_ENTITY_TYPE.get(), FleesonsiteEntityRenderer::new);
        event.registerEntityRenderer(Nosleeptonite.NOSLEEPTONITE_ENTITY_TYPE.get(), NosleeptoniteEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientPayloadHandlers(RegisterClientPayloadHandlersEvent event) {
        event.register(
                RandomTranslation.TYPE, (data, context) -> {
                    Player player = Minecraft.getInstance().player;
                    if (player != null) {
                        Minecraft.getInstance()
                                .getChatListener()
                                .handleSystemMessage(data.getRandomComponent(player.getRandom()), false);
                    }
                });
    }
}
