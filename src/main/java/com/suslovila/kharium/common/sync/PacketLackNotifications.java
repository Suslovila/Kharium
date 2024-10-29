package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.api.fuel.NotEnoughFuelNotification;
import com.suslovila.kharium.client.implantInfluence.Illusion;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import thaumcraft.client.lib.PlayerNotifications;

import java.util.ArrayList;
import java.util.List;

public class PacketLackNotifications implements IMessage {
    private List<NotEnoughFuelNotification> notifications;

    public PacketLackNotifications(List<NotEnoughFuelNotification> notifications) {
        this.notifications = notifications;
    }

    public PacketLackNotifications() {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(notifications.size());
        for (NotEnoughFuelNotification notification : notifications) {
            notification.writeTo(buf);
        }
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        ArrayList<NotEnoughFuelNotification> readNotifications = new ArrayList<>();
        int amount = buf.readInt();
        for (int i = 0; i < amount; i++) {
            NotEnoughFuelNotification notification = NotEnoughFuelNotification.Companion.readFrom(buf);
            readNotifications.add(notification);
        }
        this.notifications = readNotifications;
    }


    public static class Handler implements IMessageHandler<PacketLackNotifications, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketLackNotifications packet, MessageContext ctx) {
            packet.notifications.forEach(it -> PlayerNotifications.addNotification(it.getText(), it.getTexture(), it.getColor())
            );

            return null;
        }
    }
}


