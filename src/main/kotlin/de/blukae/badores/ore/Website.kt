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

import net.minecraft.core.BlockPos
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootTable
import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.util.*
import kotlin.concurrent.thread


object Website : BadOre("website") {
    private val URLS = listOf(
        "http://www.minecraft.net",
        "http://www.minecraftforge.net",
        "http://www.google.com",
        "http://www.minecraftforum.net",
        "http://www.minecraftwiki.net",
        "http://mcp.ocean-labs.de/modjam/"
    )

    override fun customLootTable(provider: BlockLootSubProvider): LootTable.Builder? = LootTable.lootTable()

    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean
    ) {
        if (!level.isClientSide || player.preventsBlockDrops()) {
            return
        }

        openWebsite(URI(URLS.random()))
    }

    private val COMMANDS = listOf(
        listOf("xdg-open", null),
        listOf("gio", "open", null),
        listOf("gvfs-open", null),
        listOf("gnome-open", null),  // Gnome
        listOf("mate-open", null),  // Mate
        listOf("exo-open", null),  // Xfce
        listOf("enlightenment_open", null),  // Enlightenment
        listOf(
            "gdbus", "call", "--session", "--dest", "org.freedesktop.portal.Desktop",
            "--object-path", "/org/freedesktop/portal/desktop",
            "--method", "org.freedesktop.portal.OpenURI.OpenURI",
            "", null, "{}"
        ),  // Flatpak
        listOf("open", null),  // Mac OS fallback
        listOf("rundll32", "url.dll,FileProtocolHandler", null),  // Windows fallback
    )

    private val BROWSERS = listOfNotNull(
        System.getenv("BROWSER"),
        "x-www-browser",
        "firefox",
        "librewolf",
        "iceweasel",
        "seamonkey",
        "mozilla",
        "epiphany",
        "konqueror",
        "chromium",
        "chromium-browser",
        "google-chrome",
        "brave",
        "edge",
        "www-browser",
        "links2",
        "elinks",
        "links",
        "lynx:w3m"
    )

    private fun openWebsite(uri: URI) {
        try {
            val os = System.getProperty("os.name").lowercase(Locale.getDefault())
            val uriString = uri.toString()

            when {
                Desktop.isDesktopSupported() -> {
                    Desktop.getDesktop().browse(uri)
                }

                os.indexOf("mac") >= 0 -> {
                    Runtime.getRuntime().exec(arrayOf("open", uriString))
                }

                os.indexOf("win") >= 0 -> {
                    Runtime.getRuntime().exec(arrayOf("rundll32", "url.dll,FileProtocolHandler", uriString))
                }

                else -> {
                    thread {
                        for (command in COMMANDS) {
                            try {
                                if (Runtime.getRuntime().exec(command.map { it ?: uriString }.toTypedArray())
                                        .waitFor() == 0
                                ) {
                                    return@thread
                                }
                            } catch (_: IOException) {
                            }
                        }
                        for (browser in BROWSERS) {
                            try {
                                Runtime.getRuntime().exec(arrayOf(browser, uriString))
                                return@thread
                            } catch (_: IOException) {
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}