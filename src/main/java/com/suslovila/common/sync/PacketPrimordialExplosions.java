package com.suslovila.common.sync;

import com.suslovila.common.worldSavedData.Explosion;
import com.suslovila.utils.SusVec3;
import com.suslovila.common.event.PrimordialExplosionHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class PacketPrimordialExplosions implements IMessage {
    private LinkedList<Explosion> explosions = new LinkedList<>();
    public PacketPrimordialExplosions() {
    }

    public PacketPrimordialExplosions(LinkedList<Explosion> explosions) {
        this.explosions = explosions;
    }

    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(explosions.size());
        for(Explosion explosion: explosions) {
            buffer.writeDouble(explosion.getPos().x);
            buffer.writeDouble(explosion.getPos().y);
            buffer.writeDouble(explosion.getPos().z);
            buffer.writeDouble(explosion.getRadius());
            buffer.writeInt(explosion.getTimer());
        }
    }

    public void fromBytes(ByteBuf buffer) {
        int length = buffer.readInt();
        for(int i = 0; i < length; i++){
            Explosion explosion = new Explosion(
              new SusVec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()), // pos
                    buffer.readDouble(), // radius
                    buffer.readInt(), //time
                    new LinkedList<>() // empty list, client does not need to destroy blocks
            );
            explosions.add(explosion);
        }
    }

    public static class Handler implements  IMessageHandler<PacketPrimordialExplosions, IMessage> {
        @Override
        public IMessage onMessage(PacketPrimordialExplosions packet, MessageContext ctx) {
            ArrayList<Explosion> onClient = PrimordialExplosionHandler.INSTANCE.getClientExplosions();
            onClient.clear();
            onClient.addAll(packet.explosions);

            return null;
        }
    }
}
