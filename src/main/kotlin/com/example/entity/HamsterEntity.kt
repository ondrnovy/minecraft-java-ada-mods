package com.example.entity

import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.equipment.Equippable
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3

class HamsterEntity(entityType: EntityType<out HamsterEntity>, level: Level) : TamableAnimal(entityType, level) {

    private var feedCount: Int = 0

    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, SitWhenOrderedToGoal(this))
        goalSelector.addGoal(2, PanicGoal(this, 2.0))
        goalSelector.addGoal(3, BreedGoal(this, 1.0))
        goalSelector.addGoal(4, TemptGoal(this, 1.25, { stack -> isFood(stack) }, false))
        goalSelector.addGoal(5, FollowOwnerGoal(this, 1.0, 10.0f, 2.0f))
        goalSelector.addGoal(6, FollowParentGoal(this, 1.1))
        goalSelector.addGoal(7, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(8, LookAtPlayerGoal(this, Player::class.java, 6.0f))
        goalSelector.addGoal(9, RandomLookAroundGoal(this))
    }

    override fun isFood(itemStack: ItemStack): Boolean {
        return itemStack.`is`(Items.WHEAT_SEEDS) ||
            itemStack.`is`(Items.BEETROOT_SEEDS) ||
            itemStack.`is`(Items.MELON_SEEDS) ||
            itemStack.`is`(Items.PUMPKIN_SEEDS) ||
            itemStack.`is`(Items.CARROT)
    }

    private fun isSeed(itemStack: ItemStack): Boolean {
        return itemStack.`is`(Items.WHEAT_SEEDS) ||
            itemStack.`is`(Items.BEETROOT_SEEDS) ||
            itemStack.`is`(Items.MELON_SEEDS) ||
            itemStack.`is`(Items.PUMPKIN_SEEDS)
    }

    // --- Riding (saddle required, WASD control) ---

    override fun getControllingPassenger(): LivingEntity? {
        return if (isSaddled && firstPassenger is Player) firstPassenger as Player
        else super.getControllingPassenger()
    }

    override fun canUseSlot(equipmentSlot: EquipmentSlot): Boolean {
        return if (equipmentSlot == EquipmentSlot.SADDLE) isAlive && !isBaby && isTame
        else super.canUseSlot(equipmentSlot)
    }

    override fun canDispenserEquipIntoSlot(equipmentSlot: EquipmentSlot): Boolean {
        return equipmentSlot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(equipmentSlot)
    }

    override fun getEquipSound(equipmentSlot: EquipmentSlot, itemStack: ItemStack, equippable: Equippable): Holder<SoundEvent> {
        return if (equipmentSlot == EquipmentSlot.SADDLE) SoundEvents.PIG_SADDLE
        else super.getEquipSound(equipmentSlot, itemStack, equippable)
    }

    override fun tickRidden(player: Player, vec3: Vec3) {
        super.tickRidden(player, vec3)
        setRot(player.yRot, player.xRot * 0.5f)
        yRotO = yRot
        yBodyRot = yRot
        yHeadRot = yRot
    }

    override fun getRiddenInput(player: Player, vec3: Vec3): Vec3 {
        val forward = player.zza
        val strafe = player.xxa
        return Vec3(strafe.toDouble(), 0.0, forward.toDouble())
    }

    override fun getRiddenSpeed(player: Player): Float {
        return (getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.5).toFloat()
    }

    // --- Interaction ---

    override fun mobInteract(player: Player, interactionHand: InteractionHand): InteractionResult {
        val itemStack = player.getItemInHand(interactionHand)

        // Taming: feed seeds when untamed
        if (!isTame && isSeed(itemStack)) {
            if (!player.abilities.instabuild) {
                itemStack.shrink(1)
            }
            feedCount++
            if (feedCount >= TAME_THRESHOLD) {
                if (level() is ServerLevel) {
                    tame(player)
                    setOrderedToSit(true)
                    setInSittingPose(true)
                    level().broadcastEntityEvent(this, 7.toByte())
                }
            } else {
                if (level() is ServerLevel) {
                    level().broadcastEntityEvent(this, 6.toByte())
                }
            }
            return InteractionResult.SUCCESS
        }

        if (isTame && isOwnedBy(player)) {
            // Saddle equipping
            if (isEquippableInSlot(itemStack, EquipmentSlot.SADDLE)) {
                return itemStack.interactLivingEntity(player, this, interactionHand)
            }

            // Riding: mount if saddled and not already ridden
            if (isSaddled && !isVehicle && !player.isSecondaryUseActive) {
                if (!level().isClientSide) {
                    player.startRiding(this)
                }
                return InteractionResult.SUCCESS
            }

            // Sit/stand toggle (skip if holding food for healing/breeding)
            if (!isFood(itemStack) || (isFood(itemStack) && health >= maxHealth && !isBaby)) {
                val sitting = !isOrderedToSit
                setOrderedToSit(sitting)
                setInSittingPose(sitting)
                isJumping = false
                navigation.stop()
                return InteractionResult.SUCCESS
            }
        }

        return super.mobInteract(player, interactionHand)
    }

    override fun canFallInLove(): Boolean {
        return isTame && super.canFallInLove()
    }

    override fun applyTamingSideEffects() {
        getAttribute(Attributes.MAX_HEALTH)?.baseValue = 20.0
        health = maxHealth
    }

    override fun addAdditionalSaveData(valueOutput: ValueOutput) {
        super.addAdditionalSaveData(valueOutput)
        valueOutput.putInt("FeedCount", feedCount)
    }

    override fun readAdditionalSaveData(valueInput: ValueInput) {
        super.readAdditionalSaveData(valueInput)
        feedCount = valueInput.getIntOr("FeedCount", 0)
    }

    override fun getBreedOffspring(serverLevel: ServerLevel, ageableMob: AgeableMob): HamsterEntity {
        return HamsterEntity(ModEntities.HAMSTER_MOB, serverLevel)
    }

    override fun getAmbientSound(): SoundEvent = SoundEvents.RABBIT_AMBIENT
    override fun getHurtSound(damageSource: DamageSource): SoundEvent = SoundEvents.RABBIT_HURT
    override fun getDeathSound(): SoundEvent = SoundEvents.RABBIT_DEATH

    companion object {
        private const val TAME_THRESHOLD = 100

        fun createAttributes(): AttributeSupplier.Builder {
            return TamableAnimal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
        }
    }
}
