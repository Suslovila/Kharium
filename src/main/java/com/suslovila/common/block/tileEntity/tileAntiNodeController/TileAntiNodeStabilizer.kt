package com.suslovila.common.block.tileEntity.tileAntiNodeController

import thaumcraft.api.TileThaumcraft

class TileAntiNodeStabilizer : TileThaumcraft() {
    companion object {
        public val intFunction: (Int) -> Int = IntTransformer()
    }

    class IntTransformer : (Int) -> Int {
        override operator fun invoke(x: Int): Int = TODO()

    }
}
