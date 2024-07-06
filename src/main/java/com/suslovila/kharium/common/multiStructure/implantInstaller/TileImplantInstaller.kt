package com.suslovila.kharium.common.multiStructure.implantInstaller

import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement

class TileImplantInstaller() : TileDefaultMultiStructureElement() {
    override val packetId: Int = 0

    // a rare situation when I need timer for both sides
    companion object {

    }

    override fun updateEntity() {

    }

}

fun Boolean.toInt(): Int = if (this) 1 else 0