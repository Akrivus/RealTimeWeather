package mod.akrivus.weather;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Configurations {
	public static File file;
	public static Configuration settings;
	public static String owmApiKey;
	public static float homeLongitude;
	public static float homeLatitude;
	public static void register(FMLPreInitializationEvent e) {
		Configurations.file = e.getSuggestedConfigurationFile();
		Configurations.settings = new Configuration(Configurations.file);
		Configurations.setValues();
	}
	public static void setValues() {
		Configurations.settings.addCustomCategoryComment("settings", "Required in order for the mod to work.");
		Configurations.owmApiKey = Configurations.settings.getString("OpenWeatherMap API Key:", "settings", "", "You need this for the mod to work.");
		Configurations.homeLongitude = Configurations.settings.getFloat("Home longitude:", "settings", 51, -180, 180, "Used to get the weather for where you live.");
		Configurations.homeLatitude = Configurations.settings.getFloat("Home latitude:", "settings", 0, -90, 90, "Used to get the weather for where you live.");
		Configurations.settings.save();
	}
	public static void syncConfiguration() {
		Configurations.settings.save();
	}
	public static List<IConfigElement> getCategories() {
		List<IConfigElement> returnee = new ArrayList<IConfigElement>();
		IConfigElement elements = new ConfigElement(Configurations.settings.getCategory("settings"));
		for (IConfigElement element : (List<IConfigElement>) elements.getChildElements()) {
			returnee.add(element);
		}
		return returnee;
	}
}
