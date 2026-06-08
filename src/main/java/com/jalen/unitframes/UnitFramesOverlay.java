package com.jalen.unitframes;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.client.party.PartyMember;
import net.runelite.client.party.PartyService;
import net.runelite.client.plugins.party.PartyPluginService;
import net.runelite.client.plugins.party.data.PartyData;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class UnitFramesOverlay extends Overlay
{
	private final Client client;
	private final UnitFramesConfig config;
	private final UnitFramesRenderer renderer;
	private final PartyService partyService;
	private final PartyPluginService partyPluginService;

	@Inject
	private UnitFramesOverlay(UnitFramesPlugin plugin, Client client, UnitFramesConfig config, UnitFramesRenderer renderer,
		PartyService partyService, PartyPluginService partyPluginService)
	{
		super(plugin);
		this.client = client;
		this.config = config;
		this.renderer = renderer;
		this.partyService = partyService;
		this.partyPluginService = partyPluginService;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setMovable(true);
		setSnappable(true);
		setResettable(true);
		setPreferredLocation(new Point(24, 92));
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showPlayerFrame())
		{
			renderer.hidePlayerPortrait();
			return null;
		}

		final boolean loggedIn = client.getGameState() == GameState.LOGGED_IN;
		final Player localPlayer = client.getLocalPlayer();
		if ((!loggedIn || localPlayer == null) && config.hideWhenLoggedOut())
		{
			renderer.hidePlayerPortrait();
			return null;
		}

		if (!loggedIn || localPlayer == null)
		{
			renderer.drawUnavailablePlayerFrame(graphics, getBounds());
		}
		else
		{
			renderer.drawPlayerFrame(graphics, getBounds(), localPlayer);
		}

		final List<UnitFramesRenderer.PartyFrameData> partyFrames = partyFrames();
		renderer.drawPartyFrames(graphics, partyFrames);
		return UnitFramesRenderer.playerOverlaySize(partyFrames.size());
	}

	private List<UnitFramesRenderer.PartyFrameData> partyFrames()
	{
		if (!config.showPartyFrames() || !partyService.isInParty())
		{
			return new ArrayList<>();
		}

		final PartyMember localMember = partyService.getLocalMember();
		final long localMemberId = localMember == null ? -1 : localMember.getMemberId();
		final int maxFrames = Math.max(1, Math.min(10, config.maxPartyFrames()));
		final List<UnitFramesRenderer.PartyFrameData> frames = new ArrayList<>(maxFrames);

		for (PartyMember member : partyService.getMembers())
		{
			if (member == null || member.getMemberId() == localMemberId)
			{
				continue;
			}

			final String name = displayName(member);
			if (name == null)
			{
				continue;
			}

			final PartyData data = partyPluginService.getPartyData(member.getMemberId());
			frames.add(new UnitFramesRenderer.PartyFrameData(
				member.getMemberId(),
				name,
				member.getAvatar(),
				member.isLoggedIn(),
				data == null ? -1 : data.getHitpoints(),
				data == null ? -1 : data.getMaxHitpoints(),
				data == null ? -1 : data.getPrayer(),
				data == null ? -1 : data.getMaxPrayer(),
				combatLevel(name)
			));

			if (frames.size() >= maxFrames)
			{
				break;
			}
		}

		return frames;
	}

	private String displayName(PartyMember member)
	{
		final String displayName = member.getDisplayName();
		if (displayName == null)
		{
			return null;
		}

		final String name = displayName.trim();
		if (name.isEmpty() || "<unknown>".equalsIgnoreCase(name))
		{
			return null;
		}

		return name;
	}

	private int combatLevel(String name)
	{
		if (name == null)
		{
			return 0;
		}

		for (Player player : client.getPlayers())
		{
			if (player != null && player.getName() != null && player.getName().equalsIgnoreCase(name))
			{
				return player.getCombatLevel();
			}
		}

		return 0;
	}
}
