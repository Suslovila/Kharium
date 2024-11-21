package com.suslovila.kharium.common.multiStructure.synthesizer.advancedSynthesizer

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.kharu.IKharuContainer
import com.suslovila.kharium.api.managment.IConfigurable
import com.suslovila.kharium.client.render.tile.toSusVec3
import com.suslovila.kharium.common.multiStructure.synthesizer.simpleSynthesizer.TileSynthesizerAspectOutput
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.ThaumcraftIntegrator.compositionAmountToAspect
import com.suslovila.kharium.utils.ThaumcraftIntegrator.tryTakeFromContainers
import com.suslovila.kharium.utils.getPosition
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.SusNBTHelper
import com.suslovila.sus_multi_blocked.utils.SusVec3
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.IAspectContainer
import thaumcraft.client.lib.UtilsFX
import java.awt.Color
import java.lang.IndexOutOfBoundsException

class TileAdvancedSynthesizerCore() : TileDefaultMultiStructureElement(), IConfigurable {
    override val packetId: Int = 0

    var boundAspectContainersPositions: HashSet<Position> = hashSetOf()
    var boundKharuContainersPositions: HashSet<Position> = hashSetOf()

    // using quue is not comfortable here because player can remove queue
    private var aspectRequestQueue: ArrayList<AspectRequest> = ArrayList()

    // default values are 0, 0, 0
    // represents real-world position
    // are set when constructing

    var tileKharuInputPosition = Position(0, 0, 0)
    var tileAspectOutPutPosition = Position(0, 0, 0)

    var synthesizeTimeLeft = 0
    val requiredTime: Int
        get() {
            return aspectRequestQueue.firstOrNull()?.let { request -> compositionAmountToAspect[request.aspect] } ?: 0
        }

    val currentRequestCapacity: Int
        get() {
            return 3
        }

    val maxRequestAmountTotal: Int
        get() {
            return 5
        }

    companion object {
        val currentProducingAspectNbt = Kharium.prefixAppender.doAndGet("currentProducingAspect")
        val aspectContainersNbt = Kharium.prefixAppender.doAndGet("aspect_countainers")
        val kharuContainersNbt = Kharium.prefixAppender.doAndGet("kharu_countainers")

        val requestQueueNbt = Kharium.prefixAppender.doAndGet("request_queue")

    }

    fun isSynthesizing() = synthesizeTimeLeft != 0

    fun getBoundAspectContainers() =
        boundAspectContainersPositions.mapNotNull { position -> world.getTile(position) as? IAspectContainer }

    fun getBoundKharuContainers() =
        boundKharuContainersPositions.mapNotNull { position -> world.getTile(position) as? IKharuContainer }
    fun getRequestByIndex(index: Int) =
        try {
            aspectRequestQueue[index]
        }
        catch (exception: IndexOutOfBoundsException) {
            null
        }

    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)

        val aspectContainersNbtList = NBTTagList()
        boundAspectContainersPositions.forEach { containerPos ->
            val tag = NBTTagCompound()
            containerPos.writeTo(tag)
            aspectContainersNbtList.appendTag(tag)
        }

        nbttagcompound.setTag(aspectContainersNbt, aspectContainersNbtList)

        val kharuContainersNbtList = NBTTagList()
        boundKharuContainersPositions.forEach { containerPos ->
            val tag = NBTTagCompound()
            containerPos.writeTo(tag)
            kharuContainersNbtList.appendTag(tag)
        }

        nbttagcompound.setTag(kharuContainersNbt, kharuContainersNbtList)


        val requestQueueNbtList = NBTTagList()
        aspectRequestQueue.forEach { request ->
            val tag = NBTTagCompound()
            request.writeTo(tag)
            requestQueueNbtList.appendTag(tag)
        }

        nbttagcompound.setTag(requestQueueNbt, requestQueueNbtList)


        val tileKharuTag = NBTTagCompound()
        tileKharuInputPosition.writeTo(tileKharuTag)
        nbttagcompound.setTag("kharuInput", tileKharuTag)


        val tileAspectOutput = NBTTagCompound()
        tileAspectOutPutPosition.writeTo(tileAspectOutput)
        nbttagcompound.setTag("aspectOutput", tileAspectOutput)

        nbttagcompound.setInteger("progress", synthesizeTimeLeft)

    }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)


        val listOfAspectContainers = nbttagcompound.getTagList(aspectContainersNbt, SusNBTHelper.TAG_COMPOUND)
        val listOfKharuContainers = nbttagcompound.getTagList(kharuContainersNbt, SusNBTHelper.TAG_COMPOUND)
        val listOfRequests = nbttagcompound.getTagList(requestQueueNbt, SusNBTHelper.TAG_COMPOUND)

        this.boundAspectContainersPositions = hashSetOf()
        for (i in 0 until listOfAspectContainers.tagCount()) {
            this.boundAspectContainersPositions.add(Position.readFrom(listOfAspectContainers.getCompoundTagAt(i)))
        }

        this.boundKharuContainersPositions = hashSetOf()
        for (i in 0 until listOfKharuContainers.tagCount()) {
            this.boundKharuContainersPositions.add(Position.readFrom(listOfKharuContainers.getCompoundTagAt(i)))
        }

        this.aspectRequestQueue = arrayListOf()
        for (i in 0 until listOfRequests.tagCount()) {
            val request = AspectRequest.readFrom(listOfRequests.getCompoundTagAt(i)) ?: continue
            this.aspectRequestQueue.add(request)
        }


        val tileKharuTag = nbttagcompound.getCompoundTag("kharuInput")
        tileKharuInputPosition = Position.readFrom(tileKharuTag)

        val tileOutput = nbttagcompound.getCompoundTag("aspectOutput")
        tileAspectOutPutPosition = Position.readFrom(tileOutput)

        synthesizeTimeLeft = nbttagcompound.getInteger("progress")
    }

    override fun updateEntity() {
        super.updateEntity()
        if (!world.isRemote) {
            val shouldTryToSynthesize = validateSynthesizing()
            if (shouldTryToSynthesize) {
                tryStartSynthesizing()
                tickSynthesizing()
            }

        }

    }

    private fun validateSynthesizing(): Boolean {
        if (aspectRequestQueue.isEmpty()) {
            synthesizeTimeLeft = 0
            markForSaveAndSync()
            return false
        }
        (world.getTile(tileAspectOutPutPosition) as? TileSynthesizerAspectOutput)?.let { output ->
            if (!output.hasEmptySpace()) return false
        }

        return true
    }

    private fun tickSynthesizing() {
        if (isSynthesizing()) {
            synthesizeTimeLeft -= 1
            markForSave()

            val hasCompleted = synthesizeTimeLeft == 0
            if (hasCompleted) {
                (world.getTile(tileAspectOutPutPosition) as? TileSynthesizerAspectOutput)?.let { output ->
                    val request = aspectRequestQueue.firstOrNull()
                    request?.aspect?.let { aspect ->
                        output.addToContainer(aspect, 1)
                        request.amount -= 1
                        if (request.amount == 0) {
                            aspectRequestQueue.remove(request)
                        }
                    }
                }

                markForSaveAndSync()
            }
        }
    }

    private fun tryStartSynthesizing() {
        if (!isSynthesizing()) {
            aspectRequestQueue.firstOrNull()?.let { aspectRequest ->
                if (aspectRequest.aspect.isPrimal) return@let
                val summarizedAspectList = AspectList()
                var haveFoundALackOfPrimordialAspect = false

                fun findAspectRecursively(accumulatedAspectList: AspectList, aspect: Aspect) {
                    val doesHoldersContain = tryTakeFromContainers(
                        aspectHolders = getBoundAspectContainers(),
                        aspects = AspectList().add(aspect, 1),
                        simulate = true
                    )

                    if (doesHoldersContain) {
                        accumulatedAspectList.add(aspect, 1)
                    }
                    else {
                        // if here, we do not have the aspect, and if it is primordial, the synthesizing is unavailable
                        if(aspect.isPrimal) {
                            haveFoundALackOfPrimordialAspect = true
                            return
                        }
                        findAspectRecursively(accumulatedAspectList, aspect.components[0])
                        findAspectRecursively(accumulatedAspectList, aspect.components[1])
                    }
                }


                findAspectRecursively(summarizedAspectList, aspectRequest.aspect.components[0])
                findAspectRecursively(summarizedAspectList, aspectRequest.aspect.components[1])

                if(haveFoundALackOfPrimordialAspect) return
                    tryTakeFromContainers(
                        aspectHolders = getBoundAspectContainers(),
                        aspects = summarizedAspectList,
                        simulate = false
                    )
                synthesizeTimeLeft = compositionAmountToAspect[aspectRequest.aspect]!! * 20
                markForSaveAndSync()
            }
        }
    }

    fun addAspectRequest(aspectIn: Aspect, amountIn: Int) {
        if (aspectRequestQueue.size < this.currentRequestCapacity) {
            this.aspectRequestQueue.add(AspectRequest(aspectIn, amountIn))
            this.markForSaveAndSync()
        }
    }

    fun addAmountToRequest(requestId: Int, addedAmount: Int) {
        if(aspectRequestQueue.size <= requestId) return
        val request = aspectRequestQueue[requestId]
        if(request.amount + addedAmount <= 0) {
            aspectRequestQueue.removeAt(requestId)
        }
        else {
            aspectRequestQueue[requestId].amount += addedAmount
        }
        this.markForSaveAndSync()
    }

    override fun render(configurator: ItemStack, event: RenderWorldLastEvent) {
        val player = Minecraft.getMinecraft().thePlayer ?: return
        val handlerPosition = this.getPosition()

        GL11.glPushMatrix()
//        SusGraphicHelper.translateFromPlayerTo(handlerPosition.toSusVec3(), event.partialTicks)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        this.boundAspectContainersPositions.forEach { member ->
            GL11.glPushMatrix()
            UtilsFX.drawFloatyLine(
                member.x.toDouble(),
                member.y.toDouble(),
                member.z.toDouble(),

                handlerPosition.x.toDouble(),
                handlerPosition.y.toDouble(),
                handlerPosition.z.toDouble(),
                event.partialTicks,
                Color.red.rgb,
                "${Kharium.MOD_ID}:textures/misc/bubble.png",
                0.1f,
                Math.min(player.ticksExisted, 10) / 10.0f,
                0.3F,
            )
            GL11.glPopMatrix()
        }


        GL11.glPopMatrix()
    }

    override fun onBlockClick(
        stack: ItemStack,
        player: EntityPlayer,
        world: World,
        pos: Position,
        side: Int,
        clickPos: SusVec3
    ) {
        if (world.getTile(pos) is IAspectContainer) {
            this.boundAspectContainersPositions.add(pos)
            this.markForSaveAndSync()
        }
        if (world.getTile(pos) is IKharuContainer) {
            this.boundKharuContainersPositions.add(pos)
            this.markForSaveAndSync()
        }

    }
    override fun onRightClick(itemStackIn: ItemStack, worldIn: World, player: EntityPlayer) {

    }

//    private fun getRequiredAspectsForSynthesizing(): AspectList {
//        val summarizedAspectList = AspectList()
//        var haveFoundALackOfPrimordialAspect = false
//        fun findAspectRecursively(accumulatedAspectList: AspectList, aspect: Aspect) {
//
//            val doesHoldersContain = tryTakeFromContainers(
//                aspectHolders = getBoundAspectContainers(),
//                aspects = AspectList().add(aspect, 1),
//                simulate = true
//            )
//
//            if (doesHoldersContain) {
//                accumulatedAspectList.add(aspect, 1)
//            }
//            else {
//                // if here, we do not have the aspect, and if it is primordial, the synthesizing is unavailable
//                if(aspect.isPrimal) {
//                    haveFoundALackOfPrimordialAspect = true
//                    return
//                }
//                findAspectRecursively(accumulatedAspectList, aspect.components[0])
//                findAspectRecursively(accumulatedAspectList, aspect.components[1])
//
//            }
//        }
//
//
//        val currentProducingAspect = aspectRequestQueue.firstOrNull()?.aspect ?: return null
//        findAspectRecursively(summarizedAspectList, currentProducingAspect.components[0])
//        findAspectRecursively(summarizedAspectList, currentProducingAspect.components[1])
//
//        if(haveFoundALackOfPrimordialAspect) return
//        return summarizedAspectList
//    }

    class AspectRequest(
        aspectIn: Aspect,
        amountIn: Int
    ) {
        val aspect: Aspect
        var amount: Int
        init {
            aspect = aspectIn
            amount = amountIn.coerceAtLeast(1)
        }

        companion object {
            val ASPECT_NBT = Kharium.prefixAppender.doAndGet("aspect")
            val AMOUNT_NBT = Kharium.prefixAppender.doAndGet("amount")
            fun readFrom(nbttagcompound: NBTTagCompound): AspectRequest? {
                val aspectTag = nbttagcompound.getString(ASPECT_NBT)
                if(!Aspect.aspects.containsKey(aspectTag)) return null
                return AspectRequest(Aspect.aspects[aspectTag]!!, nbttagcompound.getInteger(AMOUNT_NBT))

            }
        }
        fun writeTo(nbttagcompound: NBTTagCompound) {
            nbttagcompound.setString(ASPECT_NBT, aspect.tag)
            nbttagcompound.setInteger(AMOUNT_NBT, amount)
        }
    }
}

