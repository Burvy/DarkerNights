package clienthax.darkerNights;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * The main entry point for the DarkerNights plugin. 
 */
public class DarkerNights extends JavaPlugin implements Listener {

	/**
	 * Contains the Blindness effect to use.
	 */
	private static final PotionEffect blindnessEffect = PotionEffectType.BLINDNESS.createEffect(999999, 0);
	
    /**
     * Contains an instance of this plugin.
     */
    private static DarkerNights plugin;
    
	/**
	 * Runs when the plugin is enabled.
	 */
	@Override
	public void onEnable() {
        saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("darkernights").setExecutor(new DarkerNightsCommand(this));
        DarkerNights.plugin = this;
	}

	/**
	 * Fires when a player moves.
	 * 
	 * @param event Event to handle.
	 */
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		DarkerNights.checkBlindness(event.getPlayer());
	}

	/**
	 * Fires when a player respawns.
	 * 
	 * @param event Event to handle.
	 */
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		DarkerNights.checkBlindness(event.getPlayer());
	}

	/**
	 * Fires when a player leaves the server.
	 * 
	 * @param event Event to handle.
	 */
	@EventHandler
	public void onLeaveServer(PlayerQuitEvent event) {
		DarkerNights.checkBlindness(event.getPlayer());
	}

	/**
	 * Fires when a player joins the server.
	 * 
	 * @param event Event to handle.
	 */
	@EventHandler
	public void onJoinServer(PlayerJoinEvent event) {
		DarkerNights.checkBlindness(event.getPlayer());
	}
	
	/**
	 * Fires when an item is consumed. As we are not altering the event,
	 * but we do want to know if it is cancelled, we run it on the Monitor
	 * priority.
	 * 
	 * @param event Event to handle.
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onItemConsume(PlayerItemConsumeEvent event) {
		if (event.getItem().getType() == Material.MILK_BUCKET) {
			// We don't want to cancel the event, but we want to re-apply darkness after the event
			// has completed - if needed, so that the player can't cheat using the milk.
			final Player player = event.getPlayer();
			getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

				@Override
				public void run() {
					DarkerNights.checkBlindness(player);
				}
				
			});
		}
	}

	/**
	 * Checks whether the light level that the player is in is low
	 * 
	 * @param player Player to check
	 * @return true if the light level is below 5 units
	 */
	private static boolean darkUnderPlayer(Player player) {
		return player.getLocation().getBlock().getLightLevel() < 3; 
	}
	
	/**
	 * Checks whether it is night in the world, or whether the
     * config option to ignore this check is enabled.
	 * 
	 * @param world World to check
	 * @return true if it is night, or if the check is ignored in the 
     *         config file.
	 */
	private static boolean isNighttimeInWorld(World world) {
		/*
		 * <Riking> 0-12000 is day 
		 * <Riking> 12K-13K is sunset, approx 
		 * <Riking> 23K-[24K|0] is sunrise
		 */
		Long time = world.getTime();
		return plugin.getConfig().getBoolean("ignoreTimeOfDay") || (time >= 13000 && time <= 23000);
	}
}
