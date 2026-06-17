package com.example.item

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.UUID

class HamsterItem(properties: Properties) : Item(properties) {

    companion object {
        data class HitData(val targetUUID: UUID, var hitCount: Int)

        private val hitTracker = mutableMapOf<UUID, HitData>()

        private const val BONUS_DAMAGE = 50000f

        fun onTargetRetaliates(attackerUUID: UUID, targetUUID: UUID) {
            val data = hitTracker[attackerUUID]
            if (data != null && data.targetUUID == targetUUID) {
                hitTracker.remove(attackerUUID)
            }
        }
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        stack.hurtAndBreak(1, attacker, attacker.getEquipmentSlotForItem(stack))

        val level = target.level()
        if (level !is ServerLevel) return

        val attackerUUID = attacker.uuid
        val targetUUID = target.uuid

        val data = hitTracker[attackerUUID]

        if (data != null && data.targetUUID == targetUUID) {
            data.hitCount++

            if (data.hitCount >= 2) {
                target.invulnerableTime = 0
                target.hurtServer(level, level.damageSources().mobAttack(attacker), BONUS_DAMAGE)
                hitTracker.remove(attackerUUID)
            }
        } else {
            hitTracker[attackerUUID] = HitData(targetUUID, 1)
        }
    }
}
