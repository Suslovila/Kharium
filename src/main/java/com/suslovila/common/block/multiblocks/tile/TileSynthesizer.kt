package com.suslovila.common.block.multiblocks.tile

import com.suslovila.utils.AspectListOne
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.TileThaumcraft
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.IAspectContainer
import thaumcraft.api.aspects.IEssentiaTransport

class TileSynthesizer: TileThaumcraft(), IEssentiaTransport, IAspectContainer {
//	private val inputs: Array<ForgeDirection> = arrayOf(ForgeDirection.DOWN, ForgeDirection.UP)
	private var input: ForgeDirection = ForgeDirection.DOWN
	private var face = ForgeDirection.DOWN
//	private val inputs = ForgeDirection.UP
	private var output: ForgeDirection = ForgeDirection.NORTH

	private var aspectIn: AspectListOne = AspectListOne()
	private var aspectOut: Aspect? = null

	override fun isConnectable(face: ForgeDirection): Boolean =
//		face == this.inputs[0] || face == this.inputs[1] || face == this.output
		face == this.input || face == this.output

	override fun canInputFrom(face: ForgeDirection): Boolean {
		println("pizda2")
//		face == this.inputs[0] || face == this.inputs[1]
		return face == this.input
	}

	override fun canOutputTo(face: ForgeDirection): Boolean {
		return face == this.output
	}

	override fun setSuction(aspect: Aspect, i: Int) {
		println("pizda4")
	}

	override fun getSuctionType(face: ForgeDirection): Aspect? {
		println("pizda5")
		return null
	}

	override fun getSuctionAmount(forgeDirection: ForgeDirection): Int {
//		println("pizda6       ${if (this.essentia.visSize() <= this.maxAmount) 1 else 0}")

		//TODO: remove "0" with check like TileCentrifuge has
		return if (this.aspectIn.canAdd() && forgeDirection == input) 128 else 0
	}

//TAKE FROM THIIIS CONTAINER!!!!!!!!!!!!
	override fun takeEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int {
		println("pizda7 ${if (this.canOutputTo(face) && this.takeFromContainer(aspect, amount)) amount else 0}") // 0

		return if (this.canOutputTo(face) && this.takeFromContainer(aspect, amount)) amount else 0
	}

	override fun addEssentia(aspect: Aspect, amount: Int, face: ForgeDirection): Int {
		println("pizda8")
		//TODO: тут типа проверка на то, можно ли по-человечески засунуть аспект(через трубу, например). И тут нам нужно добавить проверку на то, что аспект не конечный(то есть из него можно создать другие аспекты).
		// Либо же нужно написать проверку в другом месте, что "пара" аспектов даст результат
		return if (aspectIn.canAdd()) {
			aspectIn.add(aspect)
			//TODO: сделать время крафта ну и тд ты понял крч
			//this.process = 39
			markDirty()
			1
		} else 0

	}

	override fun getEssentiaType(loc: ForgeDirection): Aspect? {
		println("pizda9")

		return aspectOut
	}

	override fun getEssentiaAmount(forgeDirection: ForgeDirection): Int {
		println("pizda10")

		return if(aspectOut != null) 1 else 0
	}

	override fun getMinimumSuction(): Int {
		println("pizda11")

		return 0
	}

	override fun renderExtendedTube(): Boolean {
		println("pizda12")

		return false
	}

	override fun getAspects(): AspectList {
		val al = AspectList()
		if (aspectOut != null) {
			al.add(aspectOut, 1)
		}

		return al
	}

//		AspectList().add(this.aspectOut, 1).add(this.aspectIn)

	override fun setAspects(aspects: AspectList) {
		println("pizda13")

//		this.essentia = aspects.copy()
	}

	override fun doesContainerAccept(aspect: Aspect): Boolean {
		println("pizda14")

		return true
	}

	override fun addToContainer(asp: Aspect, am: Int): Int {
		println("pizda15")

		var amount = 0

		if (am > 0 && aspectOut == null) {
			aspectOut = asp
			this.markDirty()
			amount = am - 1
		}

		return amount
	}

	override fun takeFromContainer(aspect: Aspect, am: Int): Boolean {
		println("pizda16")

		if (this.aspectOut != null) { // не идёт
			println("pizda16       axyet")
//			this.essentia.remove(aspect, am)
			this.markDirty()
			return true
		}

		return false
	}

	@Deprecated("Deprecated in Java", ReplaceWith("false"))
	override fun takeFromContainer(aspectList: AspectList): Boolean = false

	override fun doesContainerContainAmount(aspect: Aspect, amount: Int): Boolean {
		println("pizda17")

		return aspectOut != null && aspectOut == aspect && amount <= 1
	}

	@Deprecated("Deprecated in Java", ReplaceWith("false"))
	override fun doesContainerContain(aspectList: AspectList): Boolean {
//		for(tt in aspectList.getAspects()) {
//			if(this.essentia.getAmount(tt) < aspectList.getAmount(tt)) {
//				return false
//			}
//		}

		return false
	}

	override fun containerContains(aspect: Aspect): Int {
		println("pizda18")

		return if (aspectOut != null) 1 else 0
	}

//	override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
////        this.essentia.readFromNBT(nbttagcompound)
////        if (this.essentia.visSize() > this.maxAmount) {
////            this.essentia = AspectList()
////        }
//		println("pizda19")
//
//		this.aspectIn = Aspect.getAspect(nbttagcompound.getString("aspectIn"))
//		this.aspectOut = Aspect.getAspect(nbttagcompound.getString("aspectOut"))
//		this.face = ForgeDirection.getOrientation(nbttagcompound.getInteger("facing"))
//    }
//
//    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
//		println("pizda20")
//
//        this.essentia.writeToNBT(nbttagcompound)
//        nbttagcompound.setByte("face", this.input.ordinal.toByte())
////        nbttagcompound.setByte("face", this.inputs[0].ordinal.toByte())
//    }
	override fun markDirty(){
		super.markDirty()
	this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord)
}
}