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
import de.blukae.badores.OreBookPage
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.BookViewScreen
import net.minecraft.client.gui.screens.inventory.PageButton
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component


class BadOreBookScreen: Screen(Component.translatable("item.badores.bad_ore_book")) {
    private lateinit var backButton: PageButton
    private lateinit var forwardButton: PageButton

    private var currentPage = 0

    override fun init() {
        val i = (this.width - 192) / 2

        backButton = this.addRenderableWidget(
            PageButton(
                i + 43,
                159,
                false,
                {
                    if (backButton.visible) {
                        currentPage--
                    }
                    updateButtonVisibility()
                },
                true
            )
        )
        forwardButton = this.addRenderableWidget(
            PageButton(
                i + 116,
                159,
                true,
                {
                    if (forwardButton.visible) {
                        currentPage++
                    }
                    updateButtonVisibility()
                },
                true
            )
        )

        updateButtonVisibility()
    }

    fun updateButtonVisibility() {
        backButton.visible = currentPage > 0
        forwardButton.visible = currentPage < ENTRIES.size - 1
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true
        }

        return when (keyCode) {
            266 -> {
                backButton.onPress()
                true
            }

            267 -> {
                forwardButton.onPress()
                true
            }

            else -> false
        }
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialFrame: Float) {
        val i = (this.width - 192) / 2
        val page = ENTRIES[currentPage]

        page.ore?.let {
            guiGraphics.renderFakeItem(it.oreBlock.toStack(), i + 40, 14)
        }

        val nameText = Component.translatable("badores.${page.name}.name").withStyle(ChatFormatting.UNDERLINE)
        val descriptionText = Component.translatable("badores.${page.name}.description")
        guiGraphics.drawString(font, nameText, i + 40 + 4 + 16, 17, -16777216, false)
        guiGraphics.drawWordWrap(font, descriptionText, i + 40, 17 + 15, 115, -16777216, false)

        super.render(guiGraphics, mouseX, mouseY, partialFrame)
    }

    override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        this.renderTransparentBackground(guiGraphics)
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
            256
        )
    }

    companion object {
        val ENTRIES = BadOres.ORES.asSequence()
            .map { OreBookPage(it.name, it) }
            .plus(OreBookPage("doesntevenexistium", null))
            .toList()
            .sortedBy(OreBookPage::name)

        fun open() {
            Minecraft.getInstance().setScreen(BadOreBookScreen())
        }
    }
}