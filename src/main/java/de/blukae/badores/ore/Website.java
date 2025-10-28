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

package de.blukae.badores.ore;

import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Locale;

public class Website implements OreTemplate {
    private static final String[] URLS = new String[]{"http://www.minecraft.net", "http://www.minecraftforge.net",
            "http://www.google.com", "http://www.minecraftforum.net", "http://www.minecraftwiki.net", "http://mcp" +
            ".ocean-labs.de/modjam/"};

    private static final String[][] COMMANDS = new String[][]{{"xdg-open", null}, {"gio", "open", null}, {"gvfs-open"
            , null}, {"gnome-open", null}, // Gnome
            {"mate-open", null}, // Mate
            {"exo-open", null}, // Xfce
            {"enlightenment_open", null}, // Enlightenment
            {"gdbus", "call", "--session", "--dest", "org.freedesktop.portal.Desktop", "--object-path", "/org" +
                    "/freedesktop/portal/desktop", "--method", "org.freedesktop.portal.OpenURI.OpenURI", "", null,
                    "{}"}, // Flatpak
            {"open", null}, // Mac OS fallback
            {"rundll32", "url.dll,FileProtocolHandler", null} // Windows fallback
    };

    private static final String[] BROWSERS = new String[]{System.getenv("BROWSER"), "x-www-browser", "firefox",
            "librewolf", "iceweasel", "seamonkey", "mozilla", "epiphany", "konqueror", "chromium", "chromium-browser"
            , "google-chrome", "brave", "edge", "www-browser", "links2", "elinks", "links", "lynx:w3m"};

    @Override
    public LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return LootTable.lootTable();
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (level.isClientSide() && !player.preventsBlockDrops()) {
            String url = URLS[level.random.nextInt(URLS.length)];

            try {
                String os = System.getProperty("os.name").toLowerCase(Locale.getDefault());
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(URI.create(url));
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec(new String[]{"open", url});
                } else if (os.contains("win")) {
                    Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
                } else {
                    new Thread(() -> {
                        for (String[] command : COMMANDS) {
                            try {
                                String[] arr = Arrays.stream(command)
                                        .map(part -> part == null ? url : part)
                                        .toArray(String[]::new);
                                if (Runtime.getRuntime().exec(arr).waitFor() == 0) {
                                    return;
                                }
                            } catch (IOException | InterruptedException ignored) {
                            }
                        }

                        for (String browser : BROWSERS) {
                            try {
                                Runtime.getRuntime().exec(new String[]{browser, url});
                            } catch (IOException ignored) {
                            }
                        }
                    }).start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
