package com.example

import com.example.entity.HamsterEntity
import com.example.network.HamsterEatPayload
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW

object HamsterEatHandler {
    private var eatCooldown = 0

    private const val EAT_COOLDOWN_TICKS = 10 // 0.5 seconds between eats

    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            onClientTick(client)
        }
    }

    private fun onClientTick(client: Minecraft) {
        val player = client.player ?: return
        val windowHandle = client.window.handle()

        if (eatCooldown > 0) eatCooldown--

        // Check: riding a hamster + H key held + off cooldown
        if (player.vehicle is HamsterEntity && GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_H) == GLFW.GLFW_PRESS && eatCooldown <= 0) {
            if (ClientPlayNetworking.canSend(HamsterEatPayload.ID)) {
                ClientPlayNetworking.send(HamsterEatPayload())
                eatCooldown = EAT_COOLDOWN_TICKS
            }
        }
    }
}
