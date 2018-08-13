package mod.akrivus.weather;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDisableClouds implements IMessage {
	public boolean enabledCloudRendering;
	public MessageDisableClouds() { }
	public MessageDisableClouds(boolean enabledCloudRendering) {
		this.enabledCloudRendering = enabledCloudRendering;
	}
	@Override
	public void fromBytes(final ByteBuf buffer) {
		this.enabledCloudRendering = buffer.readBoolean();
	}
	@Override
	public void toBytes(final ByteBuf buffer) {
		buffer.writeBoolean(this.enabledCloudRendering);
	}
	public static class Handler implements IMessageHandler<MessageDisableClouds, IMessage> {
		@Override
		public IMessage onMessage(final MessageDisableClouds message, final MessageContext context) {
			if (context.side.isClient()) {
				Minecraft.getMinecraft().gameSettings.clouds = message.enabledCloudRendering ? 2 : 0;
			}
			return null;
		}
	}
}