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

import de.blukae.badores.ore.BadOre;
import de.blukae.badores.ore.Doesntevenexistium;
import de.blukae.badores.ore.OreBookPage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class BadOreBookScreen extends Screen {
    private static final Style PAGE_STYLE = Style.EMPTY.withoutShadow().withColor(-16777216);

    private static final OreBookPage[] PAGES = Stream.concat(
                    Arrays.stream(BadOre.values()),
                    Stream.of(new Doesntevenexistium()))
            .sorted(Comparator.comparing(OreBookPage::getId))
            .toArray(OreBookPage[]::new);

    private PageButton backButton;
    private PageButton forwardButton;

    private int currentPage = 0;

    private boolean pageValid = false;
    private FormattedCharSequence pageName;
    private List<FormattedCharSequence> pageDescription;

    public BadOreBookScreen() {
        super(Component.translatable("item.badores.bad_ore_book"));
    }

    private void updateButtonVisibility() {
        backButton.visible = currentPage > 0;
        forwardButton.visible = currentPage < PAGES.length - 1;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialFrames) {
        int i = (this.width - 192) / 2;
        OreBookPage page = PAGES[currentPage];

        ItemStack stack = page.getOreStack();
        if (stack != null) {
            guiGraphics.renderFakeItem(stack, i + 40, 14);
        }

        if (!pageValid) {
            pageName = page.getName()
                    .setStyle(PAGE_STYLE)
                    .withStyle(ChatFormatting.UNDERLINE)
                    .getVisualOrderText();
            pageDescription = font.split(page.getDescription().setStyle(PAGE_STYLE), 114);
            pageValid = true;
        }

        ActiveTextCollector textRenderer = guiGraphics.textRenderer();
        textRenderer.accept(i + 40 + 4 + 16, 17, pageName);
        for (int k = 0; k < pageDescription.size(); k++) {
            textRenderer.accept(i + 40, 17 + 15 + k * 9, pageDescription.get(k));
        }

        super.render(guiGraphics, mouseX, mouseY, partialFrames);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (super.keyPressed(event)) {
            return true;
        }

        switch (event.key()) {
            case 266 -> backButton.onPress(event);
            case 267 -> forwardButton.onPress(event);

            default -> {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void init() {
        int i = (this.width - 192) / 2;

        backButton = this.addRenderableWidget(new PageButton(
                i + 43,
                159,
                false,
                button -> {
                    if (backButton.visible) {
                        currentPage--;
                    }
                    pageValid = false;
                    updateButtonVisibility();
                },
                true));
        forwardButton = this.addRenderableWidget(new PageButton(
                i + 116,
                159,
                true,
                button -> {
                    if (forwardButton.visible) {
                        currentPage++;
                    }
                    pageValid = false;
                    updateButtonVisibility();
                },
                true));

        updateButtonVisibility();
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BookViewScreen.BOOK_LOCATION,
                (this.width - 192) / 2,
                2,
                0.0f,
                0.0f,
                192,
                192,
                256,
                256);
    }
}
