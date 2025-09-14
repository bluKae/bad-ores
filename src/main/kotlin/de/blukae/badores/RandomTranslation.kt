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

import io.netty.buffer.ByteBuf
import net.minecraft.locale.Language
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.random.Random


data class RandomTranslation(val key: String, val fallback: String, val args: List<Any>) : CustomPacketPayload {
    constructor(key: String, fallback: String, vararg args: Any) : this(key, fallback, args.toList())

    companion object {
        val TYPE = CustomPacketPayload.Type<RandomTranslation>(BadOres.rl("random_translation"))

        val STREAM_CODEC: StreamCodec<ByteBuf, RandomTranslation> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            RandomTranslation::key,
            ByteBufCodecs.STRING_UTF8,
            RandomTranslation::fallback,
            ByteBufCodecs.fromCodec(TranslatableContents.ARG_CODEC.listOf()),
            RandomTranslation::args,
            ::RandomTranslation
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> = TYPE

    fun randomComponent(): Component {
        val countString = Language.getInstance().getOrDefault("${key}.count")
        val randomKey = if (countString == "${key}.count") {
            key
        } else {
            try {
                val i = Random.nextInt(countString.toInt())
                "${key}.${i}"
            } catch (e: NumberFormatException) {
                key
            }
        }

        return Component.translatableWithFallback(randomKey, fallback, *args.toTypedArray())
    }

    fun send(player: ServerPlayer) {
        PacketDistributor.sendToPlayer(player, this)
    }
}