package com.suslovila.common.network;


import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class ServerMessagePacket implements IMessage {

    // Набор полей данных пакета
    private String message;
    private int number;

    public ServerMessagePacket() {
    }

    /**
     * @param message Сообщения, которое будет выводиться на серверной стороне.
     * @param number Число, которое будет выводиться на серверной стороне.
     */
    public ServerMessagePacket(String message, int number) {
        this.message = message;
        this.number = number;
    }

    /**
     * Читает данные пакета из ByteBuf при получении.
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        message = ByteBufUtils.readUTF8String(buf);
        number = buf.readInt();
    }

    /**
     * Записывает данные пакета в ByteBuf перед отправкой.
     */
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, message);
        buf.writeInt(number);
    }

    public static class Handler implements IMessageHandler<ServerMessagePacket, IMessage> {

        /**
         * Данный метод вызывается для обработки входящих данных из пакета.
         */
        @Override
        public IMessage onMessage(ServerMessagePacket packet, MessageContext ctx) {
            String message = packet.message;
            int number = packet.number;
            // Получаем игрока, который прислал нам пакет.
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            // Отправляем сообщение игроку
            player.addChatMessage(new ChatComponentText("Вывожу текст \"" + message + "\" с числом \"" + number + "\""));

            return null; // В ответ ничего не отправляем
        }
    }
}