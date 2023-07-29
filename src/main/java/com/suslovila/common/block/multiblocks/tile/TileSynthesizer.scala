package com.suslovila.common.block.multiblocks.tile

import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.TileThaumcraft
import thaumcraft.api.aspects.{Aspect, AspectList, IAspectSource, IEssentiaTransport}

class TileSynthesizer extends TileThaumcraft with IEssentiaTransport with IAspectSource {
	val inputs: Array[ForgeDirection] = Array(ForgeDirection.DOWN, ForgeDirection.UP)
	val output: ForgeDirection = ForgeDirection.NORTH

	var essentia: AspectList = new AspectList()
	var maxAmount = 1024

	override def isConnectable(face: ForgeDirection): Boolean =
		face == this.inputs(0) || face == this.inputs(1) || face == this.output

	override def canInputFrom(face: ForgeDirection): Boolean =
		face == this.inputs(0) || face == this.inputs(1)

	override def canOutputTo(face: ForgeDirection): Boolean =
		face == this.output

	override def setSuction(aspect: Aspect, i: Int): Unit =
		???

	override def getSuctionType(face: ForgeDirection): Aspect =
		null

	override def getSuctionAmount(forgeDirection: ForgeDirection): Int =
		if (this.essentia.visSize() < this.maxAmount) 24 else 0

	override def takeEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int =
		if (this.canOutputTo(face) && this.takeFromContainer(aspect, amount)) amount else 0

	override def addEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int =
		if (this.canInputFrom(face)) amount - this.addToContainer(aspect, amount) else 0

	override def getEssentiaType(loc: ForgeDirection): Aspect =
		if (this.essentia.visSize() > 0 && loc == ForgeDirection.UNKNOWN ) this.essentia.getAspects()(0) else null

	override def getEssentiaAmount(forgeDirection: ForgeDirection): Int =
		this.essentia.visSize()

	override def getMinimumSuction: Int =
		24

	override def renderExtendedTube: Boolean =
		false

	override def getAspects: AspectList =
		this.essentia

	override def setAspects(aspects: AspectList): Unit =
		this.essentia = aspects.copy()

	override def doesContainerAccept(aspect: Aspect): Boolean =
		true

	override def addToContainer(as: Aspect, am: Int): Int = {
		var amount = 0

        if (am != 0) {
            val space: Int = this.maxAmount - this.essentia.visSize()
            if (space >= am) {
                this.essentia.add(as, am)
            } else {
                this.essentia.add(as, space)
                amount = am - space
            }

            if (space > 0) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord)
                this.markDirty()
            }
        }
		amount
	}

	override def takeFromContainer(aspect: Aspect, am: Int): Boolean = {
		if (this.essentia.getAmount(aspect) >= am) {
            this.essentia.remove(aspect, am)
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord)
            this.markDirty()
            return true
        }

		false
	}

	override def takeFromContainer(aspectList: AspectList): Boolean =
		false

	override def doesContainerContainAmount(aspect: Aspect, amount: Int): Boolean =
		this.essentia.getAmount(aspect) >= amount

	override def doesContainerContain(aspectList: AspectList): Boolean = {
		for(tt <- aspectList.getAspects) {
			if(this.essentia.getAmount(tt) < aspectList.getAmount(tt)) {
				return false
			}
		}

		true
	}

	override def containerContains(aspect: Aspect): Int =
		this.essentia.getAmount(aspect)
}
