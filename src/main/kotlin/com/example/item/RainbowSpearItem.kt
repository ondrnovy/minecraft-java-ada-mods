package com.example.item

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class RainbowSpearItem(properties: Properties) : Item(properties) {
    // The dash ability is handled through client-side double-tap detection
    // and server-side packet handling, not through item use actions

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        // Damage the item
        stack.hurtAndBreak(1, attacker, attacker.getEquipmentSlotForItem(stack))
    }

    override fun postHurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        super.postHurtEnemy(stack, target, attacker)

        // Summon lightning at the target's position
        val level = target.level()
        if (level is ServerLevel) {
            val lightning = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED)
            if (lightning != null) {
                lightning.setPos(target.x, target.y, target.z)
                level.addFreshEntity(lightning)
            }
        }
    }
}
