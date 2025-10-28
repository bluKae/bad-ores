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

package de.blukae.badores;

import io.netty.buffer.ByteBuf;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public record RandomTranslation(String key, String fallback, List<Object> args) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RandomTranslation> TYPE = new CustomPacketPayload.Type<>(BadOres.rl(
            "random_translation"));
    public static final StreamCodec<ByteBuf, RandomTranslation> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            RandomTranslation::key,
            ByteBufCodecs.STRING_UTF8,
            RandomTranslation::fallback,
            ByteBufCodecs.fromCodec(TranslatableContents.ARG_CODEC.listOf()),
            RandomTranslation::args,
            RandomTranslation::new);

    public RandomTranslation(String key, String fallback, Object... args) {
        this(key, fallback, List.of(args));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public Component getRandomComponent(RandomSource random) {
        String count = Language.getInstance().getOrDefault(key + ".count");
        String randomKey = key;
        if (!count.equals(key + ".count")) {
            try {
                randomKey += "." + Integer.parseInt(count);
            } catch (NumberFormatException ignored) {
            }
        }
        return Component.translatableWithFallback(randomKey, fallback, args);
    }

    public void send(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, this);
    }
}
