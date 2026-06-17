package com.example.entity

import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level

class HamsterEntity(entityType: EntityType<out HamsterEntity>, level: Level) : Animal(entityType, level) {

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, PanicGoal(this, 2.0))
        goalSelector.addGoal(2, BreedGoal(this, 1.0))
        goalSelector.addGoal(3, TemptGoal(this, 1.25, { stack -> isFood(stack) }, false))
        goalSelector.addGoal(4, FollowParentGoal(this, 1.1))
        goalSelector.addGoal(5, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(6, LookAtPlayerGoal(this, Player::class.java, 6.0f))
        goalSelector.addGoal(7, RandomLookAroundGoal(this))
    }

    override fun isFood(itemStack: ItemStack): Boolean {
        return itemStack.`is`(Items.WHEAT_SEEDS) ||
            itemStack.`is`(Items.BEETROOT_SEEDS) ||
            itemStack.`is`(Items.MELON_SEEDS) ||
            itemStack.`is`(Items.PUMPKIN_SEEDS) ||
            itemStack.`is`(Items.CARROT)
    }

    override fun getBreedOffspring(serverLevel: ServerLevel, ageableMob: AgeableMob): HamsterEntity {
        return HamsterEntity(ModEntities.HAMSTER_MOB, serverLevel)
    }

    override fun getAmbientSound(): SoundEvent = SoundEvents.RABBIT_AMBIENT
    override fun getHurtSound(damageSource: DamageSource): SoundEvent = SoundEvents.RABBIT_HURT
    override fun getDeathSound(): SoundEvent = SoundEvents.RABBIT_DEATH

    companion object {
        fun createAttributes(): AttributeSupplier.Builder {
            return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
        }
    }
}
