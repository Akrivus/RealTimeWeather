package mod.akrivus.weather;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiConfigPage extends GuiConfig {
	public GuiConfigPage(GuiScreen parent) {
        super(parent, Configurations.getCategories(), RealisticWeather.MODID, "rwConfig", false, false, "Configure Realistic Weather");
    }
}
