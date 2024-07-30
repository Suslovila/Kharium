package com.suslovila.kharium.common.multiStructure.kharuNetHandler

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.client.PostRendered
import com.suslovila.kharium.api.kharu.IKharuContainer
import com.suslovila.kharium.common.block.container.SimpleInventory
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode
import com.suslovila.kharium.common.block.tileEntity.rune.TileRune
import com.suslovila.kharium.utils.SusNBTHelper
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.TimeTracker
import com.suslovila.kharium.utils.getPosition
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.inventory.IInventory
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.client.event.RenderWorldLastEvent
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class TileNetHandler(
) : TileDefaultMultiStructureElement(), PostRendered {
    val inventory: IInventory = SimpleInventory(0, 0, "inv", 64)
    override val packetId: Int = 0
    val maxLowerAmount = 10

    val tracker = object : TimeTracker() {
        override val maxValue: Int = 20
    }

    val ownCheckTime = tracker.getNext()


    var netSuppliers = TreeSet<KharuNetMember>(compareBy { it.priority })
    var netConsumers = TreeSet<KharuNetMember>(compareBy { it.priority })

    init {
        (inventory as? SimpleInventory)?.addListener(this)
    }

    var finalisedLayerAmount = 0

    override fun updateEntity() {

        if (world.isRemote || !isCheckTime()) {
            return
        }

        val suppliersNeededAmount = netSuppliers.filter { (world.getTile(it.position) is IKharuContainer) }.mapTo(
            mutableListOf()
        ) {
            val container = world.getTile(it.position) as IKharuContainer
            KharuNetMemberChecked(
                container,
                min(container.getConduction(), container.getStoredKharuAmount())
            )
        }

        val consumersProvidedAmount = netConsumers.filter { (world.getTile(it.position) is IKharuContainer) }.mapTo(
            mutableListOf()
        ) {
            val container = world.getTile(it.position) as IKharuContainer
            KharuNetMemberChecked(
                container,
                min(container.getConduction(), container.getRequiredAmount())
            )
        }

        while (consumersProvidedAmount.isNotEmpty() && suppliersNeededAmount.isNotEmpty()) {
            val prioritySupplier = suppliersNeededAmount.first()
            val priorityConsumer = consumersProvidedAmount.first()

            val toTranslate = min(prioritySupplier.amount, priorityConsumer.amount)
            prioritySupplier.container.takeKharu(toTranslate)
            priorityConsumer.container.putKharu(toTranslate)

            prioritySupplier.amount -= toTranslate
            priorityConsumer.amount -= toTranslate
            if(prioritySupplier.amount <= 0) suppliersNeededAmount.removeFirst()
            if(priorityConsumer.amount <= 0) consumersProvidedAmount.removeFirst()
        }
    }


    override fun postRender(event: RenderWorldLastEvent) {
    }

    override fun getRenderBoundingBox(): AxisAlignedBB = INFINITE_EXTENT_AABB

    val LAYER_AMOUNT_NBT = Kharium.prefixAppender.doAndGet("layer_amount")
    val NET_PRIORITY_NBT = Kharium.prefixAppender.doAndGet("net_priority")
    val NET_SUPPLIERS_NBT = Kharium.prefixAppender.doAndGet("net_suppliers")
    val NET_CONSUMERS_NBT = Kharium.prefixAppender.doAndGet("net_consumers")

    override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
        super.writeCustomNBT(nbttagcompound)
        nbttagcompound.setInteger(LAYER_AMOUNT_NBT, finalisedLayerAmount)

        val membersNbt = NBTTagList()
        netSuppliers.forEach { member ->
            val memberTag = NBTTagCompound()
            member.position.writeTo(memberTag)
            memberTag.setInteger(NET_PRIORITY_NBT, member.priority)
            membersNbt.appendTag(memberTag)
        }
        nbttagcompound.setTag(NET_SUPPLIERS_NBT, membersNbt)


        netConsumers.forEach { member ->
            val memberTag = NBTTagCompound()
            member.position.writeTo(memberTag)
            memberTag.setInteger(NET_PRIORITY_NBT, member.priority)
            membersNbt.appendTag(memberTag)
        }
        nbttagcompound.setTag(NET_CONSUMERS_NBT, membersNbt)

    }

    override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
        super.readCustomNBT(nbttagcompound)
        finalisedLayerAmount = nbttagcompound.getInteger(LAYER_AMOUNT_NBT)

        val suppliersList = nbttagcompound.getTagList(NET_SUPPLIERS_NBT, SusNBTHelper.TAG_COMPOUND)
        for (index in 0 until suppliersList.tagCount()) {
            val tag = suppliersList.getCompoundTagAt(index)
            val pos = Position.readFrom(tag)
            val priority = tag.getOrCreateInteger(NET_PRIORITY_NBT, 0)

            val netMember = KharuNetMember(
                pos,
                priority
            )

            netSuppliers.add(netMember)
        }


        val consumersList = nbttagcompound.getTagList(NET_CONSUMERS_NBT, SusNBTHelper.TAG_COMPOUND)
        for (index in 0 until consumersList.tagCount()) {
            val tag = consumersList.getCompoundTagAt(index)
            val pos = Position.readFrom(tag)
            val priority = tag.getOrCreateInteger(NET_PRIORITY_NBT, 0)

            val netMember = KharuNetMember(
                pos,
                priority
            )

            netConsumers.add(netMember)
        }
    }


    fun getRunes(): ArrayList<TileRune> {
        val startPosition = this.getPosition() + Position(0, -13, 0)
        val runes = arrayListOf<TileRune>()
        finalisedLayerAmount = 0
        for (layerIndex in 0 until maxLowerAmount) {
            val layerRunes = getLayerRunes(startPosition + Position(0, -layerIndex, 0), layerIndex) ?: return runes
            finalisedLayerAmount += 1
            runes.addAll(layerRunes)
        }
        return runes
    }

    fun getLayerRunes(layerPos: Position, layerIndex: Int): ArrayList<TileRune>? {
        val foundRunes = arrayListOf<TileRune>()
        for (xOffset in -layerIndex..layerIndex) {
            for (zOffset in -layerIndex..layerIndex) {
                val foundTile = world.getTile(layerPos + Position(xOffset, 0, zOffset)) ?: return null
                if (foundTile !is TileRune) return null
                foundTile.snarePos = this.getPosition()

                foundTile.ownLayerLevel = layerIndex
                foundTile.markForSaveAndSync()
                foundRunes.add(foundTile)
            }
        }
        return foundRunes
    }

    fun isCheckTime() =
        ((world.worldTime + ownCheckTime)
                % TileAntiNode.tracker.maxValue) == 0L


}

data class KharuNetMember(
    val position: Position,
    var priority: Int
)

data class KharuNetMemberChecked(
    val container: IKharuContainer,
    var amount: Int
)