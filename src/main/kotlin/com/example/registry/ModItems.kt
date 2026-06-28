package com.example.registry

import com.example.entity.ModEntities
import com.example.item.HamsterItem
import com.example.item.RainbowSpearItem
import com.example.item.StickOfSheepItem
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAsset
import net.minecraft.world.item.equipment.EquipmentAssets

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

    val HAMSTER_MOB_SPAWN_EGG: Item = registerItem("hamster_mob_spawn_egg") { key ->
        SpawnEggItem(
            Item.Properties()
                .spawnEgg(ModEntities.HAMSTER_MOB)
                .setId(key)
        )
    }

    // Item tag holding the items that repair Red Diamond gear (the gem itself).
    private val RED_DIAMOND_REPAIR_TAG: TagKey<Item> = TagKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath("template-mod", "repairs_red_diamond_equipment")
    )

    // Equipment asset that points at the worn-armor textures (see assets/.../equipment/red_diamond.json).
    private val RED_DIAMOND_EQUIPMENT_ASSET: ResourceKey<EquipmentAsset> = ResourceKey.create(
        EquipmentAssets.ROOT_ID,
        Identifier.fromNamespaceAndPath("template-mod", "red_diamond")
    )

    // Tool tier — stronger than diamond and netherite (more durability, damage and enchantability).
    private val RED_DIAMOND_TOOL_MATERIAL = ToolMaterial(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL, // can mine everything diamond/netherite can
        2500,  // durability
        9.0f,  // mining speed
        5.0f,  // attack damage bonus
        18,    // enchantability
        RED_DIAMOND_REPAIR_TAG
    )

    // Armor tier — better protection and toughness than diamond.
    private val RED_DIAMOND_ARMOR_MATERIAL = ArmorMaterial(
        45, // base durability multiplier (per-slot durability = this * slot factor)
        mapOf(
            ArmorType.BOOTS to 4,
            ArmorType.LEGGINGS to 7,
            ArmorType.CHESTPLATE to 9,
            ArmorType.HELMET to 4
        ),
        18,    // enchantability
        SoundEvents.ARMOR_EQUIP_NETHERITE,
        4.0f,  // toughness
        0.1f,  // knockback resistance
        RED_DIAMOND_REPAIR_TAG,
        RED_DIAMOND_EQUIPMENT_ASSET
    )

    // The Red Diamond gem — crafting material for the tools and armor below.
    val RED_DIAMOND: Item = registerItem("red_diamond") { key ->
        Item(Item.Properties().setId(key))
    }

    val RED_DIAMOND_SWORD: Item = registerItem("red_diamond_sword") { key ->
        Item(Item.Properties().sword(RED_DIAMOND_TOOL_MATERIAL, 3.0f, -2.4f).setId(key))
    }

    val RED_DIAMOND_PICKAXE: Item = registerItem("red_diamond_pickaxe") { key ->
        Item(Item.Properties().pickaxe(RED_DIAMOND_TOOL_MATERIAL, 1.0f, -2.8f).setId(key))
    }

    val RED_DIAMOND_AXE: Item = registerItem("red_diamond_axe") { key ->
        Item(Item.Properties().axe(RED_DIAMOND_TOOL_MATERIAL, 5.0f, -3.0f).setId(key))
    }

    val RED_DIAMOND_SHOVEL: Item = registerItem("red_diamond_shovel") { key ->
        Item(Item.Properties().shovel(RED_DIAMOND_TOOL_MATERIAL, 1.5f, -3.0f).setId(key))
    }

    val RED_DIAMOND_HOE: Item = registerItem("red_diamond_hoe") { key ->
        Item(Item.Properties().hoe(RED_DIAMOND_TOOL_MATERIAL, -4.0f, 0.0f).setId(key))
    }

    val RED_DIAMOND_HELMET: Item = registerItem("red_diamond_helmet") { key ->
        Item(Item.Properties().humanoidArmor(RED_DIAMOND_ARMOR_MATERIAL, ArmorType.HELMET).setId(key))
    }

    val RED_DIAMOND_CHESTPLATE: Item = registerItem("red_diamond_chestplate") { key ->
        Item(Item.Properties().humanoidArmor(RED_DIAMOND_ARMOR_MATERIAL, ArmorType.CHESTPLATE).setId(key))
    }

    val RED_DIAMOND_LEGGINGS: Item = registerItem("red_diamond_leggings") { key ->
        Item(Item.Properties().humanoidArmor(RED_DIAMOND_ARMOR_MATERIAL, ArmorType.LEGGINGS).setId(key))
    }

    val RED_DIAMOND_BOOTS: Item = registerItem("red_diamond_boots") { key ->
        Item(Item.Properties().humanoidArmor(RED_DIAMOND_ARMOR_MATERIAL, ArmorType.BOOTS).setId(key))
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
