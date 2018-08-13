package mod.akrivus.weather;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetFogDistance implements IMessage {
	public int renderDistance;
	public MessageSetFogDistance() { }
	public MessageSetFogDistance(int renderDistance) {
		this.renderDistance = renderDistance;
	}
	@Override
	public void fromBytes(final ByteBuf buffer) {
		this.renderDistance = buffer.readInt();
	}
	@Override
	public void toBytes(final ByteBuf buffer) {
		buffer.writeInt(this.renderDistance);
	}
	public static class Handler implements IMessageHandler<MessageSetFogDistance, IMessage> {
		@Override
		public IMessage onMessage(final MessageSetFogDistance message, final MessageContext context) {
			if (context.side.isClient()) {
				Minecraft.getMinecraft().gameSettings.renderDistanceChunks = message.renderDistance == 0 ? WeatherEvents.lastRenderDistance : message.renderDistance;
			}
			return null;
		}
	}
}