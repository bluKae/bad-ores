package de.blukae.badores.item

import de.blukae.badores.client.BadOreBookScreen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class BadOreBookItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (player is LocalPlayer) {
            BadOreBookScreen.Companion.open()
        }
        return InteractionResult.SUCCESS
    }
}