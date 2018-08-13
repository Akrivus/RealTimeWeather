package mod.akrivus.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class WeatherEvents {
	public static int ignoreCycleCount;
	public static int lastRenderDistance;
	public static int currentCondition;
	public static float windSpeed;
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save e) {
		Minecraft.getMinecraft().gameSettings.renderDistanceChunks = WeatherEvents.lastRenderDistance;
	}
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		if (WeatherEvents.ignoreCycleCount < 0 && e.world.getTotalWorldTime() % 200 == 0) {
			Thread request = new Thread(new WeatherDownloader());
			request.start();
		}
		else {
			--WeatherEvents.ignoreCycleCount;
		}
		switch (WeatherEvents.currentCondition) {
		case 2: case 3: case 4:		// Cloudy.
			RealisticWeather.network.sendToDimension(new MessageDisableClouds(true), e.world.provider.getDimension());
			RealisticWeather.network.sendToDimension(new MessageSetFogDistance(WeatherEvents.lastRenderDistance), e.world.provider.getDimension());
			e.world.getWorldInfo().setCleanWeatherTime(60);
			e.world.getWorldInfo().setRainTime(0);
			e.world.getWorldInfo().setThunderTime(0);
			e.world.getWorldInfo().setRaining(false);
			e.world.getWorldInfo().setThundering(false);
			break;
		case 9: case 10: case 13:	// Rainy.
			RealisticWeather.network.sendToDimension(new MessageDisableClouds(true), e.world.provider.getDimension());
			RealisticWeather.network.sendToDimension(new MessageSetFogDistance(Math.min(6, WeatherEvents.lastRenderDistance / 2)), e.world.provider.getDimension());
			e.world.getWorldInfo().setCleanWeatherTime(0);
			e.world.getWorldInfo().setRainTime(60);
			e.world.getWorldInfo().setThunderTime(0);
			e.world.getWorldInfo().setRaining(true);
			e.world.getWorldInfo().setThundering(false);
			break;
		case 11:					// Stormy.
			RealisticWeather.network.sendToDimension(new MessageDisableClouds(true), e.world.provider.getDimension());
			RealisticWeather.network.sendToDimension(new MessageSetFogDistance(Math.min(4, WeatherEvents.lastRenderDistance / 3)), e.world.provider.getDimension());
			e.world.getWorldInfo().setCleanWeatherTime(0);
			e.world.getWorldInfo().setRainTime(60);
			e.world.getWorldInfo().setThunderTime(60);
			e.world.getWorldInfo().setRaining(true);
			e.world.getWorldInfo().setThundering(true);
			break;
		case 50:					// Foggy.
			RealisticWeather.network.sendToDimension(new MessageDisableClouds(true), e.world.provider.getDimension());
			RealisticWeather.network.sendToDimension(new MessageSetFogDistance(1), e.world.provider.getDimension());
			e.world.getWorldInfo().setCleanWeatherTime(60);
			e.world.getWorldInfo().setRainTime(0);
			e.world.getWorldInfo().setThunderTime(0);
			e.world.getWorldInfo().setRaining(false);
			e.world.getWorldInfo().setThundering(false);
			break;
		default:					// Sunny.
			RealisticWeather.network.sendToDimension(new MessageDisableClouds(false), e.world.provider.getDimension());
			RealisticWeather.network.sendToDimension(new MessageSetFogDistance(WeatherEvents.lastRenderDistance), e.world.provider.getDimension());
			e.world.getWorldInfo().setCleanWeatherTime(60);
			e.world.getWorldInfo().setRainTime(0);
			e.world.getWorldInfo().setThunderTime(0);
			e.world.getWorldInfo().setRaining(false);
			e.world.getWorldInfo().setThundering(false);
			break;
		}
		for (int i = 0; i < e.world.loadedEntityList.size(); ++i) {
			if (e.world.canSeeSky(new BlockPos(e.world.loadedEntityList.get(i)))) {
				e.world.loadedEntityList.get(i).motionX += (WeatherEvents.windSpeed * (e.world.loadedEntityList.get(i).posY / 48) / 3600);
			}
		}
	}
	static class WeatherDownloader implements Runnable {
		public void run() {
			try {
				Pattern iconRegex = Pattern.compile("icon\":\"(..).");
				Pattern windRegex = Pattern.compile("speed\":([^,]+)");
				URL website = new URL("http://api.openweathermap.org/data/2.5/weather?lon=" + Configurations.homeLongitude + "&lat=" + Configurations.homeLatitude + "&appid=" + Configurations.owmApiKey);
		        URLConnection connection = website.openConnection();
		        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        StringBuilder response = new StringBuilder();
		        String inputLine;
		        while ((inputLine = in.readLine()) != null) {
		            response.append(inputLine);
		        }
		        in.close();
		        String output = response.toString();
		        Matcher iconMatcher = iconRegex.matcher(output);
		        if (iconMatcher.find()) {
		        	WeatherEvents.currentCondition = Integer.parseInt(iconMatcher.group(1));
		        }
		        Matcher windMatcher = windRegex.matcher(output);
		        if (windMatcher.find()) {
		        	WeatherEvents.windSpeed = Float.parseFloat(windMatcher.group(1));
		        }
			}
			catch (Exception e) { }
		}
	}
}
