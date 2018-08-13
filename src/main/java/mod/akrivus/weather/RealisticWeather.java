package mod.akrivus.weather;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = RealisticWeather.MODID, version = RealisticWeather.VERSION, guiFactory = "mod.akrivus.weather.GuiFactory")
public class RealisticWeather {
	public static final String MODID = "realistic_weather";
    public static final String VERSION = "@version";
    public static SimpleNetworkWrapper network;
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	MinecraftForge.EVENT_BUS.register(new WeatherEvents());
    	WeatherEvents.lastRenderDistance = Minecraft.getMinecraft().gameSettings.renderDistanceChunks;
    	RealisticWeather.network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    	RealisticWeather.network.registerMessage(MessageDisableClouds.Handler.class, MessageDisableClouds.class, 1, Side.CLIENT);
    	RealisticWeather.network.registerMessage(MessageSetFogDistance.Handler.class, MessageSetFogDistance.class, 2, Side.CLIENT);
    	Configurations.register(e);
    }
    @EventHandler
	public void serverStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandSetWeather());
	}
}
