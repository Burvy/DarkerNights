package clienthax.darkerNights;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Provides the /darkernights command.
 */
public class DarkerNightsCommand implements CommandExecutor {

    /**
     * The plugin that this command references.
     */
	private final DarkerNights plugin;
	
    /**
     * Initialises the DarkerNights command.
     * @param plugin 
     */
	public DarkerNightsCommand(DarkerNights plugin) {
		this.plugin = plugin;
	}
	
    /**
     * Runs when the "DarkerNights" command is run.
     * @param sender The sender of the command.
     * @param cmd The command.
     * @param label The actual command that was run.
     * @param args The arguments to the command.
     * @return true if successful.
     */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args[0].equalsIgnoreCase("reloadconfig")) {
            plugin.reloadConfig();
            return true;
        }
        
        return false;
	}

}
