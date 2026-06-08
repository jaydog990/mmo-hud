package com.jalen.unitframes;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.PlayerChanged;
import net.runelite.api.events.WorldChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependencies;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.itemstats.ItemStatPlugin;
import net.runelite.client.plugins.party.PartyPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "MMO HUD"
)
@PluginDependencies({
	@PluginDependency(ItemStatPlugin.class),
	@PluginDependency(PartyPlugin.class)
})
public class UnitFramesPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private Client client;

	@Inject
	private UnitFramesOverlay overlay;

	@Inject
	private UnitFramesTargetOverlay targetOverlay;

	@Inject
	private UnitFramesRenderer renderer;

	@Override
	protected void startUp()
	{
		renderer.resetPortraitWidgets();
		overlayManager.add(overlay);
		overlayManager.add(targetOverlay);
		log.debug("MMO HUD started");
	}

	@Override
	protected void shutDown()
	{
		renderer.hideAllPortraits();
		overlayManager.remove(overlay);
		overlayManager.remove(targetOverlay);
		log.debug("MMO HUD stopped");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		renderer.resetPortraitWidgets();
	}

	@Subscribe
	public void onWorldChanged(WorldChanged event)
	{
		renderer.resetPortraitWidgets();
	}

	@Subscribe
	public void onPlayerChanged(PlayerChanged event)
	{
		final Player localPlayer = client.getLocalPlayer();
		if (localPlayer != null && event.getPlayer() == localPlayer)
		{
			renderer.resetPlayerPortraitWidgets();
		}
	}

	@Provides
	UnitFramesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(UnitFramesConfig.class);
	}
}
