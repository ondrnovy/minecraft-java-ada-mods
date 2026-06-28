package com.example.network

import com.example.entity.HamsterEntity
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.Blocks

data class HamsterEatPayload(val dummy: Byte = 0) : CustomPacketPayload {
    companion object {
        val ID: CustomPacketPayload.Type<HamsterEatPayload> = CustomPacketPayload.Type(
            Identifier.fromNamespaceAndPath("template-mod", "hamster_eat")
        )

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, HamsterEatPayload> = StreamCodec.of(
            { buf, payload -> buf.writeByte(payload.dummy.toInt()) },
            { buf -> HamsterEatPayload(buf.readByte()) }
        )
    }

    override fun type(): CustomPacketPayload.Type<HamsterEatPayload> = ID
}

object HamsterEatPacketHandler {
    private const val HEAL_AMOUNT = 4.0f

    fun register() {
        PayloadTypeRegistry.playC2S().register(HamsterEatPayload.ID, HamsterEatPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(HamsterEatPayload.ID) { _, context ->
            val player = context.player()
            val vehicle = player.vehicle

            if (vehicle !is HamsterEntity || !vehicle.isTame || !vehicle.isOwnedBy(player)) return@registerGlobalReceiver

            val level = player.level()
            val hamster = vehicle

            // Find the block in front of the hamster (based on facing direction)
            val lookVec = hamster.forward
            val blockPos = BlockPos.containing(
                hamster.x + lookVec.x * 1.0,
                hamster.y + lookVec.y * 1.0 + 0.2, // slightly above ground level
                hamster.z + lookVec.z * 1.0
            )

            val blockState = level.getBlockState(blockPos)

            // Don't eat air, bedrock, or other indestructible blocks
            if (blockState.isAir || blockState.getDestroySpeed(level, blockPos) < 0) return@registerGlobalReceiver

            // Destroy the block (no drops)
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3)
            level.levelEvent(2001, blockPos, net.minecraft.world.level.block.Block.getId(blockState)) // break particles

            // Heal the hamster
            hamster.heal(HEAL_AMOUNT)

            // Play eating sound
            level.playSound(null, hamster.x, hamster.y, hamster.z, SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 1.0f, 1.0f)
        }
    }
}
