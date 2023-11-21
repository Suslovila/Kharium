package com.suslovila.common.block.tileEntity

import com.suslovila.api.kharu.IKharuTransport
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.ThaumcraftApiHelper
import thaumcraft.api.TileThaumcraft
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.IEssentiaTransport
import thaumcraft.api.wands.IWandable
import thaumcraft.codechicken.lib.raytracer.RayTracer
import thaumcraft.common.Thaumcraft
import thaumcraft.common.config.Config
import thaumcraft.common.config.ConfigBlocks
import thaumcraft.common.tiles.TileTube
import java.util.*

class TileKharuTube() : TileThaumcraft(), IWandable, IKharuTransport {

    // pos in array => side of tube (opened/closed is turned with wand)
    val openSides = arrayOf(false, false, false, false, false, false)


    override fun onWandRightClick(
        var1: World?,
        var2: ItemStack?,
        var3: EntityPlayer?,
        var4: Int,
        var5: Int,
        var6: Int,
        var7: Int,
        var8: Int
    ): Int {
        TODO("Not yet implemented")
    }

    override fun onWandRightClick(var1: World?, var2: ItemStack?, var3: EntityPlayer?): ItemStack {
        TODO("Not yet implemented")
    }

//    override fun updateEntity() {
//        if (this.venting > 0) {
//            --this.venting
//        }
//        if (this.count == 0) {
//            this.count = worldObj.rand.nextInt(10)
//        }
//        if (!worldObj.isRemote) {
//            if (this.venting <= 0) {
//                if (++this.count % 2 == 0) {
//                    calculateSuction(null as Aspect?, false, false)
//                    checkVenting()
//                    if (this.essentiaType != null && this.essentiaAmount == 0) {
//                        this.essentiaType = null
//                    }
//                }
//                if (this.count % 5 == 0 && this.suction > 0) {
//                    equalizeWithNeighbours(false)
//                }
//            }
//        }
//    }
//
//    fun calculateSuction(filter: Aspect?, restrict: Boolean, directional: Boolean) {
//        this.suction = 0
//        this.suctionType = null
//        var loc: ForgeDirection? = null
//        for (dir in 0..5) {
//            try {
//                loc = ForgeDirection.getOrientation(dir)
//                if ((!directional || this.facing == loc.opposite) && this.isConnectable(loc)) {
//                    val te = ThaumcraftApiHelper.getConnectableTile(worldObj, xCoord, yCoord, zCoord, loc)
//                    if (te != null) {
//                        val ic = te as IEssentiaTransport
//                        if ((filter == null || ic.getSuctionType(loc.opposite) == null || ic.getSuctionType(loc.opposite) === filter) && (filter != null || this.getEssentiaAmount(
//                                loc
//                            ) <= 0 || ic.getSuctionType(loc.opposite) == null || this.getEssentiaType(loc) === ic.getSuctionType(
//                                loc.opposite
//                            )) && (filter == null || this.getEssentiaAmount(loc) <= 0 || this.getEssentiaType(loc) == null || ic.getSuctionType(
//                                loc.opposite
//                            ) == null || this.getEssentiaType(loc) === ic.getSuctionType(loc.opposite))
//                        ) {
//                            val suck = ic.getSuctionAmount(loc.opposite)
//                            if (suck > 0 && suck > this.suction + 1) {
//                                var st = ic.getSuctionType(loc.opposite)
//                                if (st == null) {
//                                    st = filter
//                                }
//                                this.setSuction(st, if (restrict) suck / 2 else suck - 1)
//                            }
//                        }
//                    }
//                }
//            } catch (var10: Exception) {
//            }
//        }
//    }
//
//    fun checkVenting() {
//        var loc: ForgeDirection? = null
//        for (dir in 0..5) {
//            try {
//                loc = ForgeDirection.getOrientation(dir)
//                if (this.isConnectable(loc)) {
//                    val te = ThaumcraftApiHelper.getConnectableTile(worldObj, xCoord, yCoord, zCoord, loc)
//                    if (te != null) {
//                        val ic = te as IEssentiaTransport
//                        val suck = ic.getSuctionAmount(loc.opposite)
//                        if (this.suction > 0 && (suck == this.suction || suck == this.suction - 1) && this.suctionType !== ic.getSuctionType(
//                                loc.opposite
//                            )
//                        ) {
//                            var c = -1
//                            if (this.suctionType != null) {
//                                c = Config.aspectOrder.indexOf(this.suctionType)
//                            }
//                            worldObj.addBlockEvent(xCoord, yCoord, zCoord, ConfigBlocks.blockTube, 1, c)
//                            this.venting = 40
//                        }
//                    }
//                }
//            } catch (var7: Exception) {
//            }
//        }
//    }
//
//    fun equalizeWithNeighbours(directional: Boolean) {
//        var loc: ForgeDirection? = null
//        if (this.essentiaAmount <= 0) {
//            for (dir in 0..5) {
//                try {
//                    loc = ForgeDirection.getOrientation(dir)
//                    if ((!directional || this.facing != loc.opposite) && this.isConnectable(loc)) {
//                        val te = ThaumcraftApiHelper.getConnectableTile(worldObj, xCoord, yCoord, zCoord, loc)
//                        if (te != null) {
//                            val ic = te as IEssentiaTransport
//                            if (ic.canOutputTo(loc.opposite) && (this.getSuctionType(null as ForgeDirection?) == null || this.getSuctionType(
//                                    null as ForgeDirection?
//                                ) === ic.getEssentiaType(loc.opposite) || ic.getEssentiaType(loc.opposite) == null) && this.getSuctionAmount(
//                                    null as ForgeDirection?
//                                ) > ic.getSuctionAmount(loc.opposite) && this.getSuctionAmount(null as ForgeDirection?) >= ic.minimumSuction
//                            ) {
//                                var a: Aspect? = this.getSuctionType(null as ForgeDirection?)
//                                if (a == null) {
//                                    a = ic.getEssentiaType(loc.opposite)
//                                    if (a == null) {
//                                        a = ic.getEssentiaType(ForgeDirection.UNKNOWN)
//                                    }
//                                }
//                                val am: Int = this.addEssentia(a, ic.takeEssentia(a, 1, loc.opposite), loc)
//                                if (am > 0) {
//                                    if (worldObj.rand.nextInt(100) == 0) {
//                                        worldObj.addBlockEvent(xCoord, yCoord, zCoord, ConfigBlocks.blockTube, 0, 0)
//                                    }
//                                    return
//                                }
//                            }
//                        }
//                    }
//                } catch (var8: Exception) {
//                }
//            }
//        }
//    }
//
//    override fun onWandRightClick(
//        world: World?,
//        itemStack : ItemStack?,
//        player: EntityPlayer?,
//        x : Int, y : Int, z : Int,
//        side : Int, md : Int): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun onWandRightClick(var1: World?, var2: ItemStack?, var3: EntityPlayer?): ItemStack {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun onWandRightClick(
//        world: World,
//        wandstack: ItemStack?,
//        player: EntityPlayer,
//        x: Int,
//        y: Int,
//        z: Int,
//        side: Int,
//        md: Int
//    ): Int {
//        val hit = RayTracer.retraceBlock(world, player, x, y, z)
//        return if (hit == null) {
//            0
//        } else {
//            if (hit.subHit >= 0 && hit.subHit < 6) {
//                player.worldObj.playSound(
//                    x.toDouble() + 0.5, y.toDouble() + 0.5, z.toDouble() + 0.5, "thaumcraft:tool",
//                    0.5f, 0.9f + player.worldObj.rand.nextFloat() * 0.2f, false)
//                player.swingItem()
//                markDirty()
//                world.markBlockForUpdate(x, y, z)
//                this.openSides[hit.subHit] = !this.openSides.get(hit.subHit)
//                val dir = ForgeDirection.getOrientation(hit.subHit)
//                val tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ)
//                if (tile != null && tile is TileTube) {
//                    tile.openSides[dir.opposite.ordinal] = this.openSides.get(hit.subHit)
//                    world.markBlockForUpdate(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ)
//                    tile.markDirty()
//                }
//            }
//            if (hit.subHit == 6) {
//                player.worldObj.playSound(
//                    x.toDouble() + 0.5,
//                    y.toDouble() + 0.5,
//                    z.toDouble() + 0.5,
//                    "thaumcraft:tool",
//                    0.5f,
//                    0.9f + player.worldObj.rand.nextFloat() * 0.2f,
//                    false
//                )
//                player.swingItem()
//                var a: Int = this.facing.ordinal
//                markDirty()
//                while (true) {
//                    ++a
//                    if (a >= 20) {
//                        break
//                    }
//                    if (this.canConnectSide(ForgeDirection.getOrientation(a % 6).opposite.ordinal) && this.isConnectable(
//                            ForgeDirection.getOrientation(a % 6).opposite
//                        )
//                    ) {
//                        a %= 6
//                        this.facing = ForgeDirection.getOrientation(a)
//                        world.markBlockForUpdate(x, y, z)
//                        break
//                    }
//                }
//            }
//            0
//        }
//    }


    override fun onUsingWandTick(var1: ItemStack?, var2: EntityPlayer?, var3: Int) {
        TODO("Not yet implemented")
    }

    override fun onWandStoppedUsing(var1: ItemStack?, var2: World?, var3: EntityPlayer?, var4: Int) {
        TODO("Not yet implemented")
    }

    override fun getSuction() {
        TODO("Not yet implemented")
    }

    override fun setSuction() {
        TODO("Not yet implemented")
    }

}
