package com.jalen.unitframes;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class UnitFramesTargetOverlay extends Overlay
{
	private final Client client;
	private final UnitFramesConfig config;
	private final UnitFramesRenderer renderer;

	@Inject
	private UnitFramesTargetOverlay(UnitFramesPlugin plugin, Client client, UnitFramesConfig config, UnitFramesRenderer renderer)
	{
		super(plugin);
		this.client = client;
		this.config = config;
		this.renderer = renderer;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setMovable(true);
		setSnappable(true);
		setResettable(true);
		setPreferredLocation(new Point(310, 164));
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showTargetFrame() || client.getGameState() != GameState.LOGGED_IN)
		{
			renderer.hideTargetPortrait();
			return null;
		}

		final Player localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
		{
			renderer.hideTargetPortrait();
			return null;
		}

		final Actor target = renderer.getTarget(localPlayer);
		if (target == null)
		{
			renderer.hideTargetPortrait();
			return null;
		}

		renderer.drawTargetFrame(graphics, getBounds(), target);
		return UnitFramesRenderer.TARGET_FRAME_SIZE;
	}
}
