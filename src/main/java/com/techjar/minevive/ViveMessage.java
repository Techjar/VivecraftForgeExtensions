package com.techjar.minevive;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ViveMessage implements IMessage {
	public static class Handle implements IMessageHandler<ViveMessage, IMessage> {
		@Override
		public IMessage onMessage(ViveMessage message, MessageContext ctx) {
			MineViveForge.network2.sendTo(new ViveMessage("Teleport to your heart's content!"), ctx.getServerHandler().playerEntity);
			return null;
		}
	}
	
	String str;

	// Empty constructor for incoming messages.
	public ViveMessage() {
	}

    // Constructor for outgoing messages
	public ViveMessage(String str) {
		this.str = str;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
        // Convert the ByteBuf object to a String object
		//System.out.println("From Bytes");
		//str = ByteBufUtils.readUTF8String(buf);
	}

    // Just returns the message stored in the GenericMessage object
	public String getMessage() {
		return str;
	}

	@Override
	public void toBytes(ByteBuf buf) {
        // Converts the message from the outgoing constructor to bytes for sending.
		buf.writeBytes(str.getBytes());
	}
}
