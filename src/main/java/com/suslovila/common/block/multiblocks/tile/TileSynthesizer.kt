package com.suslovila.common.block.multiblocks.tile

import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.TileThaumcraft
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.IAspectSource
import thaumcraft.api.aspects.IEssentiaTransport

class TileSynthesizer: TileThaumcraft(), IEssentiaTransport, IAspectSource {
	private val inputs: Array<ForgeDirection> = arrayOf(ForgeDirection.DOWN, ForgeDirection.UP)
//	private val inputs = ForgeDirection.UP
	private val output: ForgeDirection = ForgeDirection.NORTH

	private val maxAmount = 1024

	private var essentia: AspectList = AspectList()

	override fun isConnectable(face: ForgeDirection): Boolean {
		println("axyet")
		println(face == this.inputs[0] || face == this.inputs[1] || face == this.output)
		return face == this.inputs[0] || face == this.inputs[1] || face == this.output
	}

	override fun canInputFrom(face: ForgeDirection): Boolean =
		face == this.inputs[0] || face == this.inputs[1]

	override fun canOutputTo(face: ForgeDirection): Boolean {
		println("pizda")
		return face == this.output
	}

	override fun setSuction(aspect: Aspect, i: Int) {
	}

	override fun getSuctionType(face: ForgeDirection): Aspect? =
		null

	override fun getSuctionAmount(forgeDirection: ForgeDirection): Int =
		if (this.essentia.visSize() < this.maxAmount) 24 else 0

	override fun takeEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int =
		if (this.canOutputTo(face) && this.takeFromContainer(aspect, amount)) amount else 0

	override fun addEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int =
		if (this.canInputFrom(face)) amount - this.addToContainer(aspect, amount) else 0

	override fun getEssentiaType(loc: ForgeDirection): Aspect? =
		if (this.essentia.visSize() > 0 && loc == ForgeDirection.UNKNOWN) this.essentia.getAspects()[0] else null

	override fun getEssentiaAmount(forgeDirection: ForgeDirection): Int =
		this.essentia.visSize()

	override fun getMinimumSuction(): Int =
		24

	override fun renderExtendedTube(): Boolean =
		false

	override fun getAspects(): AspectList =
		this.essentia

	override fun setAspects(aspects: AspectList) {
		this.essentia = aspects.copy()
	}

	override fun doesContainerAccept(aspect: Aspect): Boolean =
		true

	override fun addToContainer(asp: Aspect, am: Int): Int {
		var amount = 0

        if (am != 0) {
            val space: Int = this.maxAmount - this.essentia.visSize()
            if (space >= am) {
                this.essentia.add(asp, am)
            } else {
                this.essentia.add(asp, space)
                amount = am - space
            }

            if (space > 0) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord)
                this.markDirty()
            }
        }

		return amount
	}

	override fun takeFromContainer(aspect: Aspect, am: Int): Boolean {
		if (this.essentia.getAmount(aspect) >= am) {
            this.essentia.remove(aspect, am)
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord)
            this.markDirty()
            return true
        }

		return false
	}

	@Deprecated("Deprecated in Java", ReplaceWith("false"))
	override fun takeFromContainer(aspectList: AspectList): Boolean =
		false

	override fun doesContainerContainAmount(aspect: Aspect, amount: Int): Boolean =
		this.essentia.getAmount(aspect) >= amount

	@Deprecated("Deprecated in Java")
	override fun doesContainerContain(aspectList: AspectList): Boolean {
		for(tt in aspectList.getAspects()) {
			if(this.essentia.getAmount(tt) < aspectList.getAmount(tt)) {
				return false
			}
		}

		return true
	}

	override fun containerContains(aspect: Aspect): Int {
		return this.essentia.getAmount(aspect)
	}
}