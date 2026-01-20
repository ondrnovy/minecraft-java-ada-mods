package com.example

import com.example.network.DashPayload
import com.example.registry.ModItems
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft

object DashHandler {
    private var lastSpacePress = 0L
    private var dashCooldown = 0L
    private var wasSpacePressed = false

    private const val DOUBLE_TAP_WINDOW = 400L // ms
    private const val DASH_COOLDOWN = 1500L // ms

    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            onClientTick(client)
        }
    }

    private fun onClientTick(client: Minecraft) {
        val player = client.player ?: return
        val currentTime = System.currentTimeMillis()

        val isSpacePressed = client.options.keyJump.isDown

        // Detect space press (transition from not pressed to pressed)
        if (isSpacePressed && !wasSpacePressed) {
            // Check if we're not on cooldown
            if (currentTime > dashCooldown) {
                // Check if this is a double-tap
                if (currentTime - lastSpacePress < DOUBLE_TAP_WINDOW) {
                    // Check if holding rainbow spear
                    if (isHoldingRainbowSpear(player)) {
                        sendDashPacket()
                        dashCooldown = currentTime + DASH_COOLDOWN
                        lastSpacePress = 0L // Reset to prevent triple-tap triggering
                    }
                } else {
                    lastSpacePress = currentTime
                }
            }
        }

        wasSpacePressed = isSpacePressed
    }

    private fun isHoldingRainbowSpear(player: net.minecraft.world.entity.player.Player): Boolean {
        val mainHandItem = player.mainHandItem.item
        val offHandItem = player.offhandItem.item
        return mainHandItem == ModItems.RAINBOW_SPEAR || offHandItem == ModItems.RAINBOW_SPEAR
    }

    private fun sendDashPacket() {
        if (ClientPlayNetworking.canSend(DashPayload.ID)) {
            ClientPlayNetworking.send(DashPayload())
        }
    }
}
