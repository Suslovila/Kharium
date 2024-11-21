package com.suslovila.kharium.utils

import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.util.Vec3
import net.minecraft.world.World

fun TileEntity.getPosDouble() = SusVec3(this.xCoord, this.yCoord, this.zCoord)
fun TileEntity.getPosition() = Position(this.xCoord, this.yCoord, this.zCoord)
fun Entity.getPosition() = SusVec3(this.posX, this.posY, this.posZ)

fun World.getTileCheckChunkLoad(pos: Position): TileEntity? {
    if (!this.blockExists(pos.x, pos.y, pos.z)) {
        return null
    }
    return this.getTile(pos)
}

fun World.getBlockFromPos(position: Position) = getBlock(position.x, position.y, position.z)

object SusWorldHelper {
    fun teleportEntity(entity: Entity, hitMOP: MovingObjectPosition?) {
        if(entity is EntityPlayerMP && !entity.playerNetServerHandler.netManager.isChannelOpen) return
        if (hitMOP != null) {
            var hitx: Double
            var hity: Double
            var hitz: Double
            when (hitMOP.typeOfHit) {

                MovingObjectType.BLOCK -> {
                    hitx = hitMOP.hitVec.xCoord
                    hity = hitMOP.hitVec.yCoord
                    hitz = hitMOP.hitVec.zCoord
                    when (hitMOP.sideHit) {
                        0 -> hity -= 2.0
                        2 -> hitz -= 0.5
                        3 -> hitz += 0.5
                        4 -> hitx -= 0.5
                        5 -> hitx += 0.5
                    }
                    entity.fallDistance = 0.0f
                    entity.setPosition(hitx, hity, hitz)
                }

                else -> entity.setPosition(
                    hitMOP.hitVec.xCoord,
                    hitMOP.hitVec.yCoord,
                    hitMOP.hitVec.zCoord
                )
            }
        }
    }

    fun teleportEntity(entity: Entity, pos: SusVec3) {
        if (entity is EntityPlayerMP) {
            if (entity.playerNetServerHandler.netManager.isChannelOpen) {
                entity.setPositionAndUpdate(pos.x, pos.y, pos.z)
                return
            }
        }
        entity.setPosition(pos.x, pos.y, pos.z)
    }


    fun raytraceBlocks(
        world: World,
        player: EntityPlayer,
        collisionFlag: Boolean,
        reachDistance: Double
    ): MovingObjectPosition? {
        val playerPosition =
            Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight().toDouble(), player.posZ)
        val playerLook = player.lookVec
        val playerViewOffset = Vec3.createVectorHelper(
            playerPosition.xCoord + playerLook.xCoord * reachDistance,
            playerPosition.yCoord + playerLook.yCoord * reachDistance,
            playerPosition.zCoord + playerLook.zCoord * reachDistance
        )
        return world.rayTraceBlocks(playerPosition, playerViewOffset, collisionFlag, !collisionFlag, false)
    }

    fun raytraceEntities(
        world: World,
        player: EntityPlayer,
        reachDistance: Double
    ): MovingObjectPosition? {
        var pickedEntity: MovingObjectPosition? = null
        val playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ)
        val playerLook = player.lookVec
        val playerViewOffset = Vec3.createVectorHelper(
            playerPosition.xCoord + playerLook.xCoord * reachDistance,
            playerPosition.yCoord + playerLook.yCoord * reachDistance,
            playerPosition.zCoord + playerLook.zCoord * reachDistance
        )
        val playerBorder = 1.1 * reachDistance
        val boxToScan = player.boundingBox.expand(playerBorder, playerBorder, playerBorder)
        val entitiesHit = world.getEntitiesWithinAABBExcludingEntity(player as Entity, boxToScan)
        var closestEntity = reachDistance
        if (entitiesHit == null || entitiesHit.isEmpty()) {
            return null
        }
        for (entityHit in entitiesHit) {
            entityHit as? Entity ?: continue
            if (entityHit.canBeCollidedWith() && entityHit.boundingBox != null) {
                val border = entityHit.collisionBorderSize
                val aabb = entityHit.boundingBox.expand(border.toDouble(), border.toDouble(), border.toDouble())
                val hitMOP = aabb.calculateIntercept(playerPosition, playerViewOffset)
                if (hitMOP != null) {
                    if (aabb.isVecInside(playerPosition)) {
                        if (0.0 < closestEntity || closestEntity == 0.0) {
                            pickedEntity = MovingObjectPosition(entityHit)
                            if (pickedEntity != null) {
                                pickedEntity.hitVec = hitMOP.hitVec
                                closestEntity = 0.0
                            }
                        }
                        continue
                    }
                    val distance = playerPosition.distanceTo(hitMOP.hitVec)
                    if (distance < closestEntity || closestEntity == 0.0) {
                        pickedEntity = MovingObjectPosition(entityHit)
                        pickedEntity.hitVec = hitMOP.hitVec
                        closestEntity = distance
                    }
                }
            }
        }
        return pickedEntity
    }


    fun boundingBoxFromTwoVec(pos1: SusVec3, pos2: SusVec3) =
        AxisAlignedBB.getBoundingBox(
            Math.min(pos1.x, pos2.x),
            Math.min(pos1.y, pos2.y),
            Math.min(pos1.z, pos2.z),
            Math.max(pos1.x, pos2.x),
            Math.max(pos1.y, pos2.y),
            Math.max(pos1.z, pos2.z)
        )
}

