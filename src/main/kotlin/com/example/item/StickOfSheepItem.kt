package com.example.item

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.random.Random

class StickOfSheepItem(properties: Properties) : Item(properties) {

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(hand)

        if (level is ServerLevel) {
            // Random position in 10x10 area around player, 10 blocks above
            val offsetX = Random.nextDouble(-5.0, 5.0)
            val offsetZ = Random.nextDouble(-5.0, 5.0)

            val spawnX = player.x + offsetX
            val spawnY = player.y + 10.0
            val spawnZ = player.z + offsetZ

            val sheep = EntityType.SHEEP.create(level, EntitySpawnReason.MOB_SUMMONED)
            if (sheep != null) {
                sheep.setPos(spawnX, spawnY, spawnZ)
                level.addFreshEntity(sheep)

                // Damage the item
                stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack))
            }
        }

        return InteractionResult.SUCCESS
    }
}
