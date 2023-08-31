package com.suslovila.common.block.tileEntity

import net.minecraft.tileentity.TileEntity

class TileAntiNodeWatcher : TileEntity() {

  val lenses = listOf<Lens>(Lens(0.5), Lens(0.5), Lens(0.5))
  public val wholeLength = lenses.sumOf { it.width };

  override fun updateEntity() {
   super.updateEntity()
   //lenses.forEach{ with(it){timer = (timer + 1) % 360} }
  }

  class Lens (val width : Double, var spinningSpeed: Double = 1.0, var angle : Double = 0.0, var turningSide: Int = 0, var speedDelta : Double = 0.005) {
   val defaultSpeedDelta = speedDelta
  }
}
