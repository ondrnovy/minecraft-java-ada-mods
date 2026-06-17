package com.example.registry

import com.example.item.HamsterItem
import com.example.item.RainbowSpearItem
import com.example.item.StickOfSheepItem
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.TridentItem
import net.minecraft.world.item.component.ItemAttributeModifiers

object ModItems {
    val RAINBOW_SPEAR: Item = registerItem("rainbow_spear") { key ->
        RainbowSpearItem(
            Item.Properties()
                .stacksTo(1)
                .durability(500)
                .attributes(createSpearAttributes())
                .setId(key)
        )
    }

    val STICK_OF_SHEEP: Item = registerItem("stick_of_sheep") { key ->
        StickOfSheepItem(
            Item.Properties()
                .stacksTo(1)
                .durability(64)
                .setId(key)
        )
    }

    val HAMSTER: Item = registerItem("hamster") { key ->
        HamsterItem(
            Item.Properties()
                .stacksTo(1)
                .durability(500)
                .attributes(createHamsterAttributes())
                .setId(key)
        )
    }

    private fun createSpearAttributes(): ItemAttributeModifiers {
        return ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                AttributeModifier(
                    Item.BASE_ATTACK_DAMAGE_ID,
                    9.0, // 10 total damage (9 + 1 base)
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .add(
                Attributes.ATTACK_SPEED,
                AttributeModifier(
                    Item.BASE_ATTACK_SPEED_ID,
                    -2.9, // 1.1 attacks per second (4 - 2.9)
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .build()
    }

    private fun createHamsterAttributes(): ItemAttributeModifiers {
        return ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                AttributeModifier(
                    Item.BASE_ATTACK_DAMAGE_ID,
                    6.0, // 7 total damage (6 + 1 base)
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .add(
                Attributes.ATTACK_SPEED,
                AttributeModifier(
                    Item.BASE_ATTACK_SPEED_ID,
                    -2.4, // 1.6 attacks per second (4 - 2.4)
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .build()
    }

    private fun registerItem(name: String, factory: (ResourceKey<Item>) -> Item): Item {
        val id = Identifier.fromNamespaceAndPath("template-mod", name)
        val key = ResourceKey.create(Registries.ITEM, id)
        val item = factory(key)
        return Registry.register(BuiltInRegistries.ITEM, key, item)
    }

    fun register() {
        // Called to trigger static initialization
    }
}
