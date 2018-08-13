package mod.akrivus.weather;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandSetWeather extends CommandBase {
	public String getName() {
		return "setweather";
	}
	public String getUsage(ICommandSender sender) {
		return "/setweather [weather] [seconds]";
	}
	public int getRequiredPermissionLevel() {
		return 2;
	}
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 1 || args.length == 2) {
            int i = 6;
            if (args.length == 2) {
                i = parseInt(args[1], 1, 100000) * 20 / 120;
            }
            if (args[0].startsWith("cloud")) {
            	WeatherEvents.currentCondition = 2;
            	WeatherEvents.ignoreCycleCount = i;
            }
            else if (args[0].startsWith("rain") || args[0].startsWith("shower")) {
            	WeatherEvents.currentCondition = 9;
            	WeatherEvents.ignoreCycleCount = i;
            }
            else if (args[0].startsWith("storm") || args[0].startsWith("thunder") || args[0].startsWith("lightning")) {
            	WeatherEvents.currentCondition = 11;
            	WeatherEvents.ignoreCycleCount = i;
            }
            else if (args[0].startsWith("fog") || args[0].startsWith("mist")) {
            	WeatherEvents.currentCondition = 50;
            	WeatherEvents.ignoreCycleCount = i;
            }
            else if (args[0].startsWith("sun") || args[0].startsWith("clear")) {
            	WeatherEvents.currentCondition = 0;
            	WeatherEvents.ignoreCycleCount = i;
            }
        }
	}
}
