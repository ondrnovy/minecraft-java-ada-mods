package com.example.network

import com.example.registry.ModItems
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

data class DashPayload(val dummy: Byte = 0) : CustomPacketPayload {
    companion object {
        val ID: CustomPacketPayload.Type<DashPayload> = CustomPacketPayload.Type(
            Identifier.fromNamespaceAndPath("template-mod", "dash")
        )

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, DashPayload> = StreamCodec.of(
            { buf, payload -> buf.writeByte(payload.dummy.toInt()) },
            { buf -> DashPayload(buf.readByte()) }
        )
    }

    override fun type(): CustomPacketPayload.Type<DashPayload> = ID
}

object DashPacketHandler {
    private const val DASH_SPEED = 2.0
    private const val UPWARD_BOOST = 0.4

    fun register() {
        // Register the packet type
        PayloadTypeRegistry.playC2S().register(DashPayload.ID, DashPayload.CODEC)

        // Register the server-side handler
        ServerPlayNetworking.registerGlobalReceiver(DashPayload.ID) { payload, context ->
            val player = context.player()

            // Verify player is holding Rainbow Spear in either hand
            val mainHandItem = player.mainHandItem.item
            val offHandItem = player.offhandItem.item

            if (mainHandItem == ModItems.RAINBOW_SPEAR || offHandItem == ModItems.RAINBOW_SPEAR) {
                // Apply dash velocity in look direction
                val lookVec = player.lookAngle
                player.setDeltaMovement(
                    lookVec.x * DASH_SPEED,
                    UPWARD_BOOST,
                    lookVec.z * DASH_SPEED
                )
                player.hurtMarked = true // Sync velocity to client
            }
        }
    }
}
