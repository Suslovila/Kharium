package com.suslovila.kharium.api.implants

import net.minecraft.item.Item

enum class IMPLANT_TYPE {
    HEAD_FIRST,
    HEAD_SECOND,
    SKELETON,
    LEFT_HAND,
    RIGHT_HAND,
    LEGS,
    FOOT,
    HEART,
    CIRCULATORY_SYSTEM,
    LEFT_EYE,
    RIGHT_EYE,
    NERVOUS_SYSTEM,
    SKIN,
    LUNGS
}

abstract class ItemImplant(val implantType : Array<IMPLANT_TYPE>) : Item() {
}