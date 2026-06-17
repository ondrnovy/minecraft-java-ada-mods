package com.example.entity

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object ModEntities {
    private val HAMSTER_MOB_KEY: ResourceKey<EntityType<*>> = ResourceKey.create(
        Registries.ENTITY_TYPE,
        Identifier.fromNamespaceAndPath("template-mod", "hamster_mob")
    )

    val HAMSTER_MOB: EntityType<HamsterEntity> = Registry.register(
        BuiltInRegistries.ENTITY_TYPE,
        HAMSTER_MOB_KEY,
        EntityType.Builder.of(::HamsterEntity, MobCategory.CREATURE)
            .sized(0.4f, 0.3f)
            .clientTrackingRange(8)
            .build(HAMSTER_MOB_KEY)
    )

    fun register() {
        FabricDefaultAttributeRegistry.register(HAMSTER_MOB, HamsterEntity.createAttributes())
    }
}
