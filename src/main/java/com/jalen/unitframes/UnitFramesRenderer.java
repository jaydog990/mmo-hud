package com.jalen.unitframes;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.JagexColor;
import net.runelite.api.MenuEntry;
import net.runelite.api.Model;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.Skill;
import net.runelite.api.gameval.AnimationID;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.SpriteID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetModelType;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.game.NPCManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChangesService;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stats;
import net.runelite.client.ui.FontManager;

class UnitFramesRenderer
{
	static final Dimension PLAYER_FRAME_SIZE = new Dimension(260, 106);
	static final Dimension TARGET_FRAME_SIZE = new Dimension(232, 100);

	private static final int PLAYER_PORTRAIT_X = 0;
	private static final int PLAYER_PORTRAIT_Y = 4;
	private static final int PLAYER_PORTRAIT_SIZE = 92;
	private static final int PLAYER_BAR_X = 83;
	private static final int PLAYER_TITLE_X = 92;
	private static final int PLAYER_TITLE_Y = 37;
	private static final int PLAYER_BAR_Y = 44;
	private static final int PLAYER_BAR_WIDTH = 154;
	private static final int PLAYER_BAR_HEIGHT = 32;
	private static final int PLAYER_PRAYER_Y = 80;
	private static final int PLAYER_PRAYER_HEIGHT = 8;
	private static final int PLAYER_CONNECTOR_X = 46;
	private static final int PLAYER_CONNECTOR_Y = 39;
	private static final int PLAYER_CONNECTOR_WIDTH = 199;
	private static final int PLAYER_CONNECTOR_HEIGHT = 54;
	private static final int PARTY_STACK_X = 12;
	private static final int PARTY_STACK_Y = PLAYER_FRAME_SIZE.height + 2;
	private static final int PARTY_FRAME_WIDTH = 184;
	private static final int PARTY_FRAME_HEIGHT = 43;
	private static final int PARTY_FRAME_GAP = 5;
	private static final int PARTY_PORTRAIT_SIZE = 36;
	private static final int PARTY_PORTRAIT_X = 2;
	private static final int PARTY_PORTRAIT_Y = 3;
	private static final int PARTY_NAME_X = 44;
	private static final int PARTY_NAME_Y = 15;
	private static final int PARTY_BAR_X = 44;
	private static final int PARTY_BAR_WIDTH = 128;
	private static final int PARTY_HP_Y = 18;
	private static final int PARTY_HP_HEIGHT = 16;
	private static final int PARTY_PRAYER_Y = 35;
	private static final int PARTY_PRAYER_HEIGHT = 4;

	private static final int TARGET_BAR_X = 2;
	private static final int TARGET_TITLE_X = 2;
	private static final int TARGET_TITLE_Y = 33;
	private static final int TARGET_BAR_Y = 38;
	private static final int TARGET_BAR_WIDTH = 154;
	private static final int TARGET_BAR_HEIGHT = 32;
	private static final int TARGET_PORTRAIT_X = 147;
	private static final int TARGET_PORTRAIT_Y = 6;
	private static final int TARGET_PORTRAIT_SIZE = 84;
	private static final int TARGET_CONNECTOR_X = 0;
	private static final int TARGET_CONNECTOR_Y = 33;
	private static final int TARGET_CONNECTOR_WIDTH = 190;
	private static final int TARGET_CONNECTOR_HEIGHT = 43;

	private static final int BAR_ARC = 3;
	private static final int BADGE_SIZE = 23;
	private static final int PLAYER_MODEL_X = 18;
	private static final int PLAYER_MODEL_Y = 35;
	private static final int PLAYER_MODEL_WIDTH = 58;
	private static final int PLAYER_MODEL_HEIGHT = 50;
	private static final int TARGET_MODEL_X = 162;
	private static final int TARGET_MODEL_Y = 33;
	private static final int TARGET_MODEL_WIDTH = 50;
	private static final int TARGET_MODEL_HEIGHT = 45;
	private static final int PLAYER_MODEL_ZOOM = 1050;
	private static final int TARGET_MODEL_ZOOM = 980;
	private static final int PORTRAIT_SWAY_Y = 28;
	private static final int PORTRAIT_SWAY_X = 10;
	private static final int PORTRAIT_REBUILD_DELAY_CYCLES = 3;
	private static final int PORTRAIT_BACKGROUND_INSET = 6;
	private static final int PORTRAIT_BACKGROUND_STRIPS = 36;
	private static final int SOFTWARE_PORTRAIT_INSET = 8;
	private static final int SOFTWARE_PORTRAIT_HIGH_QUALITY_SCALE = 2;
	private static final int SOFTWARE_PORTRAIT_ROTATION_OFFSET = 1024;
	private static final double SOFTWARE_PORTRAIT_HEAD_HEIGHT_FRACTION = 0.62;
	private static final double SOFTWARE_PORTRAIT_HEAD_FIT_WIDTH = 1.02;
	private static final double SOFTWARE_PORTRAIT_HEAD_FIT_HEIGHT = 1.02;
	private static final double SOFTWARE_PORTRAIT_HEAD_CENTER_Y = 0.55;
	private static final double SOFTWARE_PORTRAIT_HEAD_BASE_ZOOM = 1.05;
	private static final double SOFTWARE_PORTRAIT_FULL_MODEL_FIT = 0.82;
	private static final double SOFTWARE_PORTRAIT_FULL_MODEL_CENTER_Y = 0.47;
	private static final double SOFTWARE_PORTRAIT_FULL_MODEL_MAX_ASPECT = 1.35;
	private static final double SOFTWARE_PORTRAIT_FULL_MODEL_BASE_ZOOM = 1.35;
	private static final double SOFTWARE_PORTRAIT_FULL_MODEL_MAX_FILL_BOOST = 4.5;
	private static final double SOFTWARE_PORTRAIT_FOCUS_MIN_X = 0.04;
	private static final double SOFTWARE_PORTRAIT_FOCUS_MAX_X = 0.96;
	private static final double SOFTWARE_PORTRAIT_FOCUS_MIN_Y = 0.02;
	private static final double SOFTWARE_PORTRAIT_FOCUS_MAX_Y = 0.98;
	private static final double SOFTWARE_PORTRAIT_CENTER_SMOOTHING = 0.18;
	private static final double SOFTWARE_PORTRAIT_SCALE_SMOOTHING = 0.08;
	private static final int HITPOINTS_ICON_SIZE = 18;
	private static final int HITPOINTS_ICON_PADDING = 12;
	private static final int HITPOINTS_TEXT_ICON_GAP = 5;
	private static final int HEALTH_TEXT_BASELINE_OFFSET = 1;
	private static final int PARTY_HEALTH_TEXT_BASELINE_OFFSET = 0;
	private static final int LEVEL_TEXT_BASELINE_OFFSET = 2;
	private static final float HEALTH_TEXT_SIZE = 16f;
	private static final float TITLE_TEXT_SIZE = 17f;
	private static final float PARTY_TITLE_TEXT_SIZE = 11f;
	private static final float PARTY_HEALTH_TEXT_SIZE = 15f;
	private static final float PARTY_LEVEL_TEXT_SIZE = 10f;
	private static final float FALLBACK_TEXT_SIZE = 14f;
	private static final int NO_ANIMATION = -1;
	private static final double BAR_ANIMATION_SNAP = 0.003;
	private static final double BAR_MAX_DELTA_SECONDS = 0.05;
	private static final double BAR_DAMAGE_RATE = 14.0;
	private static final double BAR_HEAL_RATE = 8.0;
	private static final double BAR_TRAIL_RATE = 3.8;
	private static final long DAMAGE_TRAIL_HOLD_NANOS = 280_000_000L;
	private static final long GAIN_GLOW_NANOS = 560_000_000L;

	private static final Color RING_OUTER = new Color(18, 13, 9, 248);
	private static final Color RING_DARK = new Color(31, 24, 18, 246);
	private static final Color RING_MID = new Color(83, 68, 43, 246);
	private static final Color RING_INNER = new Color(119, 96, 55, 235);
	private static final Color PORTRAIT_BACK_TOP = new Color(22, 24, 21, 245);
	private static final Color PORTRAIT_BACK_BOTTOM = new Color(3, 4, 3, 250);
	private static final Color PORTRAIT_WIDGET_BACKGROUND = new Color(3, 4, 3);
	private static final Color BADGE_BACKGROUND = new Color(30, 24, 19, 245);
	private static final Color BADGE_BORDER = new Color(139, 105, 52, 245);
	private static final Color BAR_BORDER = new Color(18, 8, 5, 250);
	private static final Color BAR_BORDER_MID = new Color(76, 40, 19, 246);
	private static final Color BAR_BORDER_HIGHLIGHT = new Color(138, 91, 38, 170);
	private static final Color BAR_BACKGROUND = new Color(7, 7, 7, 242);
	private static final Color HEALTH_COLOR = new Color(169, 7, 5);
	private static final Color HEALTH_TOP = new Color(211, 38, 22);
	private static final Color HEALTH_BOTTOM = new Color(106, 8, 5);
	private static final Color HEALTH_LOW_COLOR = new Color(214, 28, 13);
	private static final Color HEALTH_LOW_TOP = new Color(245, 62, 31);
	private static final Color HEALTH_LOW_BOTTOM = new Color(126, 10, 5);
	private static final Color PRAYER_COLOR = new Color(48, 190, 214);
	private static final Color UNKNOWN_COLOR = new Color(73, 70, 66);
	private static final Color HEALTH_GAIN_GLOW = new Color(93, 255, 108, 135);
	private static final Color HEALTH_DAMAGE_TRAIL = new Color(238, 93, 28, 155);
	private static final Color PRAYER_GAIN_GLOW = new Color(118, 255, 247, 115);
	private static final Color PRAYER_DRAIN_TRAIL = new Color(15, 94, 130, 170);
	private static final Color RESTORE_PREVIEW = new Color(55, 200, 82, 150);
	private static final Color PRAYER_RESTORE_PREVIEW = new Color(57, 255, 186, 130);
	private static final Color OVERHEAL_PREVIEW = new Color(216, 255, 139, 155);
	private static final Color GOLD_TEXT = new Color(246, 202, 57);
	private static final Color LIGHT_TEXT = new Color(255, 245, 223);
	private static final Color HEALTH_TEXT = new Color(238, 224, 199);
	private static final Color HEALTH_TEXT_OUTLINE = new Color(29, 21, 15);
	private static final Color SHADOW = new Color(0, 0, 0, 230);

	private final Client client;
	private final UnitFramesConfig config;
	private final NPCManager npcManager;
	private final ItemStatChangesService itemStatService;
	private final SpriteManager spriteManager;
	private Widget[] playerPortraitBackgroundWidgets;
	private Widget playerPortraitWidget;
	private Widget[] targetPortraitBackgroundWidgets;
	private Widget targetPortraitWidget;
	private BufferedImage hitpointsIcon;
	private int playerAppearanceHash = Integer.MIN_VALUE;
	private int targetCompositionId = Integer.MIN_VALUE;
	private final AnimatedBar playerHealthBar = new AnimatedBar();
	private final AnimatedBar playerPrayerBar = new AnimatedBar();
	private final AnimatedBar targetHealthBar = new AnimatedBar();
	private final SoftwarePortraitCamera targetSoftwarePortraitCamera = new SoftwarePortraitCamera();
	private Actor animatedTarget;
	private Actor stickyTarget;
	private Actor softwarePortraitTarget;
	private BufferedImage targetSoftwarePortraitImage;
	private long targetSoftwarePortraitLastNanos;
	private int targetSoftwarePortraitSettingsHash = Integer.MIN_VALUE;
	private int portraitResetGameCycle = Integer.MIN_VALUE;

	@Inject
	private UnitFramesRenderer(Client client, UnitFramesConfig config, NPCManager npcManager,
		ItemStatChangesService itemStatService, SpriteManager spriteManager)
	{
		this.client = client;
		this.config = config;
		this.npcManager = npcManager;
		this.itemStatService = itemStatService;
		this.spriteManager = spriteManager;
	}

	static Dimension playerOverlaySize(int partyFrameCount)
	{
		if (partyFrameCount <= 0)
		{
			return PLAYER_FRAME_SIZE;
		}

		final int height = PARTY_STACK_Y + partyFrameCount * PARTY_FRAME_HEIGHT
			+ (partyFrameCount - 1) * PARTY_FRAME_GAP;
		return new Dimension(Math.max(PLAYER_FRAME_SIZE.width, PARTY_STACK_X + PARTY_FRAME_WIDTH), height);
	}

	void drawPlayerFrame(Graphics2D graphics, Rectangle overlayBounds, Player player)
	{
		setupGraphics(graphics);

		final String name = player.getName() == null ? "Player" : player.getName();
		final int currentHp = client.getBoostedSkillLevel(Skill.HITPOINTS);
		final int maxHp = Math.max(client.getRealSkillLevel(Skill.HITPOINTS), 1);
		final int currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
		final int maxPrayer = Math.max(client.getRealSkillLevel(Skill.PRAYER), 1);
		final int hoveredRestore = config.showHoverRestores() ? getRestoreValue(Stats.HITPOINTS.getName()) : 0;
		final int hoveredPrayerRestore = config.showHoverRestores() ? getRestoreValue(Stats.PRAYER.getName()) : 0;
		final boolean hasPortrait = updatePlayerPortraitWidget(overlayBounds, player);

		drawPlayerBackplate(graphics);
		drawHealthBar(graphics, PLAYER_BAR_X, PLAYER_BAR_Y, PLAYER_BAR_WIDTH, PLAYER_BAR_HEIGHT,
			percent(currentHp, maxHp), healthColor(currentHp, maxHp), healthLabel(currentHp),
			BarSlot.PLAYER, percent(hoveredRestore, maxHp), null);
		drawPrayerBar(graphics, PLAYER_BAR_X, PLAYER_PRAYER_Y, PLAYER_BAR_WIDTH, PLAYER_PRAYER_HEIGHT,
			percent(currentPrayer, maxPrayer), percent(hoveredPrayerRestore, maxPrayer));
		drawTitle(graphics, name, PLAYER_TITLE_X, PLAYER_TITLE_Y, PLAYER_BAR_WIDTH - 42, false);
		drawPortraitShell(graphics, PLAYER_PORTRAIT_X, PLAYER_PORTRAIT_Y, PLAYER_PORTRAIT_SIZE,
			player.getCombatLevel(), hasPortrait ? null : initialsFor(name), BadgeAnchor.BOTTOM_LEFT);
	}

	void drawPartyFrames(Graphics2D graphics, List<PartyFrameData> partyFrames)
	{
		if (partyFrames.isEmpty())
		{
			return;
		}

		setupGraphics(graphics);
		for (int i = 0; i < partyFrames.size(); i++)
		{
			drawPartyFrame(graphics, partyFrames.get(i), PARTY_STACK_X,
				PARTY_STACK_Y + i * (PARTY_FRAME_HEIGHT + PARTY_FRAME_GAP));
		}
	}

	void drawUnavailablePlayerFrame(Graphics2D graphics, Rectangle overlayBounds)
	{
		setupGraphics(graphics);
		hidePlayerPortrait();
		drawPlayerBackplate(graphics);
		drawHealthBar(graphics, PLAYER_BAR_X, PLAYER_BAR_Y, PLAYER_BAR_WIDTH, PLAYER_BAR_HEIGHT,
			0, UNKNOWN_COLOR, "", BarSlot.PLAYER, 0, null);
		drawPrayerBar(graphics, PLAYER_BAR_X, PLAYER_PRAYER_Y, PLAYER_BAR_WIDTH, PLAYER_PRAYER_HEIGHT, 0, 0);
		drawTitle(graphics, "MMO HUD", PLAYER_TITLE_X, PLAYER_TITLE_Y, PLAYER_BAR_WIDTH - 42, false);
		drawPortraitShell(graphics, PLAYER_PORTRAIT_X, PLAYER_PORTRAIT_Y, PLAYER_PORTRAIT_SIZE, 0, "UF",
			BadgeAnchor.BOTTOM_LEFT);
	}

	void drawTargetFrame(Graphics2D graphics, Rectangle overlayBounds, Actor target)
	{
		setupGraphics(graphics);

		final String name = target.getName() == null ? "Target" : target.getName();
		final int ratio = target.getHealthRatio();
		final int scale = target.getHealthScale();
		final TargetHealth targetHealth = targetHealth(target, ratio, scale);
		final PortraitMode portraitMode = updateTargetPortraitWidget(overlayBounds, target);

		drawTargetBackplate(graphics);
		drawTitle(graphics, name, TARGET_TITLE_X, TARGET_TITLE_Y, TARGET_BAR_WIDTH - 8, true);
		drawHealthBar(graphics, TARGET_BAR_X, TARGET_BAR_Y, TARGET_BAR_WIDTH, TARGET_BAR_HEIGHT,
			targetHealth.value,
			targetHealth.hasHealth ? healthColor(targetHealth.value) : UNKNOWN_COLOR,
			targetHealth.label, BarSlot.TARGET, 0, target);
		final boolean drewSoftwarePortrait = portraitMode == PortraitMode.SOFTWARE
			&& target instanceof NPC
			&& drawSoftwareTargetPortrait(graphics, (NPC) target);
		drawPortraitShell(graphics, TARGET_PORTRAIT_X, TARGET_PORTRAIT_Y, TARGET_PORTRAIT_SIZE,
			target.getCombatLevel(), portraitMode == PortraitMode.WIDGET || drewSoftwarePortrait ? null : initialsFor(name),
			BadgeAnchor.BOTTOM_RIGHT);
	}

	private void drawPartyFrame(Graphics2D graphics, PartyFrameData partyFrame, int x, int y)
	{
		final Composite originalComposite = graphics.getComposite();
		if (!partyFrame.loggedIn)
		{
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
		}

		drawPartyBackplate(graphics, x, y);
		drawPartyPortrait(graphics, x + PARTY_PORTRAIT_X, y + PARTY_PORTRAIT_Y,
			partyFrame.avatar, initialsFor(partyFrame.name));
		drawPartyName(graphics, partyFrame, x + PARTY_NAME_X, y + PARTY_NAME_Y);

		final boolean hasHitpoints = partyFrame.maxHitpoints > 0 && partyFrame.hitpoints >= 0;
		drawPartyBar(graphics, x + PARTY_BAR_X, y + PARTY_HP_Y, PARTY_BAR_WIDTH, PARTY_HP_HEIGHT,
			hasHitpoints ? percent(partyFrame.hitpoints, partyFrame.maxHitpoints) : 0,
			hasHitpoints ? healthColor(partyFrame.hitpoints, partyFrame.maxHitpoints) : UNKNOWN_COLOR,
			true);
		if (hasHitpoints && config.showNumbers())
		{
			drawPartyHealthText(graphics, String.valueOf(partyFrame.hitpoints),
				x + PARTY_BAR_X, y + PARTY_HP_Y, PARTY_BAR_WIDTH, PARTY_HP_HEIGHT);
		}

		final boolean hasPrayer = partyFrame.maxPrayer > 0 && partyFrame.prayer >= 0;
		drawPartyBar(graphics, x + PARTY_BAR_X, y + PARTY_PRAYER_Y, PARTY_BAR_WIDTH, PARTY_PRAYER_HEIGHT,
			hasPrayer ? percent(partyFrame.prayer, partyFrame.maxPrayer) : 0,
			hasPrayer ? PRAYER_COLOR : UNKNOWN_COLOR,
			false);

		graphics.setComposite(originalComposite);
	}

	private void drawPartyBackplate(Graphics2D graphics, int x, int y)
	{
		final int plateX = x + 18;
		final int plateY = y + 6;
		final int plateWidth = PARTY_FRAME_WIDTH - 18;
		final int plateHeight = PARTY_FRAME_HEIGHT - 9;

		graphics.setColor(RING_OUTER);
		graphics.fillRoundRect(plateX - 3, plateY - 3, plateWidth + 6, plateHeight + 6, BAR_ARC, BAR_ARC);
		graphics.setPaint(new GradientPaint(plateX, plateY, BAR_BORDER_MID, plateX, plateY + plateHeight, BAR_BORDER));
		graphics.fillRoundRect(plateX, plateY, plateWidth, plateHeight, BAR_ARC, BAR_ARC);
		graphics.setColor(BAR_BORDER_HIGHLIGHT);
		graphics.setStroke(new BasicStroke(1f));
		graphics.drawLine(x + PARTY_BAR_X, plateY + 4, x + PARTY_BAR_X + PARTY_BAR_WIDTH, plateY + 4);
	}

	private void drawPartyPortrait(Graphics2D graphics, int x, int y, BufferedImage avatar, String fallbackText)
	{
		graphics.setColor(RING_OUTER);
		graphics.fillOval(x - 2, y - 2, PARTY_PORTRAIT_SIZE + 4, PARTY_PORTRAIT_SIZE + 4);
		graphics.setPaint(new GradientPaint(x, y, RING_MID, x, y + PARTY_PORTRAIT_SIZE, RING_DARK));
		graphics.fillOval(x, y, PARTY_PORTRAIT_SIZE, PARTY_PORTRAIT_SIZE);
		graphics.setPaint(new GradientPaint(x, y + 7, PORTRAIT_BACK_TOP, x, y + PARTY_PORTRAIT_SIZE - 7,
			PORTRAIT_BACK_BOTTOM));
		graphics.fillOval(x + 5, y + 5, PARTY_PORTRAIT_SIZE - 10, PARTY_PORTRAIT_SIZE - 10);

		if (avatar != null)
		{
			final Graphics2D avatarGraphics = (Graphics2D) graphics.create();
			avatarGraphics.setClip(new Ellipse2D.Double(x + 5, y + 5, PARTY_PORTRAIT_SIZE - 10,
				PARTY_PORTRAIT_SIZE - 10));
			final int innerSize = PARTY_PORTRAIT_SIZE - 10;
			final double scale = Math.max(innerSize / (double) avatar.getWidth(), innerSize / (double) avatar.getHeight());
			final int drawWidth = Math.max(1, (int) Math.round(avatar.getWidth() * scale));
			final int drawHeight = Math.max(1, (int) Math.round(avatar.getHeight() * scale));
			final int drawX = x + 5 + (innerSize - drawWidth) / 2;
			final int drawY = y + 5 + (innerSize - drawHeight) / 2;
			avatarGraphics.drawImage(avatar, drawX, drawY, drawWidth, drawHeight, null);
			avatarGraphics.dispose();
		}
		else
		{
			drawMiniFallbackText(graphics, x + 5, y + 5, PARTY_PORTRAIT_SIZE - 10, fallbackText);
		}

		graphics.setColor(RING_INNER);
		graphics.setStroke(new BasicStroke(1.5f));
		graphics.drawOval(x + 4, y + 4, PARTY_PORTRAIT_SIZE - 8, PARTY_PORTRAIT_SIZE - 8);
	}

	private void drawPartyName(Graphics2D graphics, PartyFrameData partyFrame, int x, int baselineY)
	{
		final Font originalFont = graphics.getFont();
		graphics.setFont(runescapeBoldFont(PARTY_TITLE_TEXT_SIZE));
		final FontMetrics metrics = graphics.getFontMetrics();
		final String levelText = partyFrame.combatLevel > 0 ? "Lv " + partyFrame.combatLevel : "";
		final int levelWidth = levelText.isEmpty() ? 0 : metrics.stringWidth(levelText) + 6;
		final String name = truncate(partyFrame.name, metrics, PARTY_BAR_WIDTH - levelWidth);
		drawOutlinedText(graphics, name, x, baselineY, partyFrame.loggedIn ? GOLD_TEXT : new Color(170, 154, 118));

		if (!levelText.isEmpty())
		{
			drawOutlinedText(graphics, levelText, x + PARTY_BAR_WIDTH - metrics.stringWidth(levelText),
				baselineY, LIGHT_TEXT);
		}

		graphics.setFont(originalFont);
	}

	private void drawPartyBar(Graphics2D graphics, int x, int y, int width, int height, double value,
		Color fillColor, boolean hitpoints)
	{
		graphics.setColor(RING_OUTER);
		graphics.fillRect(x - 2, y - 2, width + 4, height + 4);
		graphics.setColor(BAR_BACKGROUND);
		graphics.fillRect(x, y, width, height);

		if (fillColor != UNKNOWN_COLOR)
		{
			drawBarFill(graphics, x, y, (int) Math.round(width * clamp(value)), height, fillColor);
		}

		graphics.setColor(hitpoints ? new Color(100, 17, 12, 230) : new Color(11, 51, 75, 220));
		graphics.drawRect(x, y, width, height);
	}

	private void drawPartyHealthText(Graphics2D graphics, String text, int x, int y, int width, int height)
	{
		final Font originalFont = graphics.getFont();
		graphics.setFont(runescapeBoldFont(PARTY_HEALTH_TEXT_SIZE));
		final FontMetrics metrics = graphics.getFontMetrics();
		drawHealthText(graphics, text, x + (width - metrics.stringWidth(text)) / 2,
			centeredTextBaseline(metrics, y, height, PARTY_HEALTH_TEXT_BASELINE_OFFSET));
		graphics.setFont(originalFont);
	}

	private void drawMiniFallbackText(Graphics2D graphics, int x, int y, int size, String text)
	{
		final Font originalFont = graphics.getFont();
		graphics.setFont(runescapeBoldFont(10f));
		final FontMetrics metrics = graphics.getFontMetrics();
		drawOutlinedText(graphics, truncate(text, metrics, size - 2),
			x + (size - metrics.stringWidth(truncate(text, metrics, size - 2))) / 2,
			y + (size - metrics.getHeight()) / 2 + metrics.getAscent(), LIGHT_TEXT);
		graphics.setFont(originalFont);
	}

	Actor getTarget(Player localPlayer)
	{
		final Actor target = localPlayer.getInteracting();
		if (isValidTarget(target, localPlayer))
		{
			stickyTarget = target;
			return target;
		}

		if (isValidTarget(stickyTarget, localPlayer))
		{
			return stickyTarget;
		}

		stickyTarget = null;
		return null;
	}

	private static boolean isValidTarget(Actor target, Player localPlayer)
	{
		if (target == null || target == localPlayer || target.isDead())
		{
			return false;
		}

		final String name = target.getName();
		if (name == null || name.trim().isEmpty())
		{
			return false;
		}

		return target.getHealthScale() <= 0 || target.getHealthRatio() != 0;
	}

	void hidePlayerPortrait()
	{
		hideWidgets(playerPortraitBackgroundWidgets);
		hideWidget(playerPortraitWidget);
	}

	void hideTargetPortrait()
	{
		hideWidgets(targetPortraitBackgroundWidgets);
		hideWidget(targetPortraitWidget);
	}

	void hideAllPortraits()
	{
		hidePlayerPortrait();
		hideTargetPortrait();
	}

	void resetPortraitWidgets()
	{
		resetPlayerPortraitWidgets();
		resetTargetPortraitWidgets();
		portraitResetGameCycle = client.getGameCycle();
	}

	void resetTargetPortraitWidgets()
	{
		hideTargetPortrait();
		stickyTarget = null;
		targetPortraitBackgroundWidgets = null;
		targetPortraitWidget = null;
		targetCompositionId = Integer.MIN_VALUE;
		softwarePortraitTarget = null;
		targetSoftwarePortraitImage = null;
		targetSoftwarePortraitLastNanos = 0;
		targetSoftwarePortraitCamera.reset();
	}

	void resetPlayerPortraitWidgets()
	{
		hidePlayerPortrait();
		playerPortraitBackgroundWidgets = null;
		playerPortraitWidget = null;
		playerAppearanceHash = Integer.MIN_VALUE;
	}

	private boolean canShowLivePortraits()
	{
		return config.showLivePortraits() && client.getGameState() == GameState.LOGGED_IN;
	}

	private boolean updatePlayerPortraitWidget(Rectangle overlayBounds, Player player)
	{
		if (!canShowLivePortraits())
		{
			hidePlayerPortrait();
			return false;
		}
		if (isPortraitResetCoolingDown() || !hasUsablePlayerComposition(player))
		{
			hidePlayerPortrait();
			playerAppearanceHash = Integer.MIN_VALUE;
			return false;
		}

		final int appearanceHash = playerAppearanceHash(player);
		if (appearanceHash != playerAppearanceHash)
		{
			playerAppearanceHash = appearanceHash;
			hidePlayerPortrait();
			playerPortraitBackgroundWidgets = null;
			playerPortraitWidget = null;
		}

		final Widget widget = getOrCreatePlayerPortraitWidget();
		if (widget == null)
		{
			return false;
		}

		positionPortraitWidget(widget, overlayBounds, PLAYER_MODEL_X, PLAYER_MODEL_Y, PLAYER_MODEL_WIDTH, PLAYER_MODEL_HEIGHT);
		positionPortraitBackgroundWidgets(playerPortraitBackgroundWidgets, overlayBounds,
			PLAYER_PORTRAIT_X, PLAYER_PORTRAIT_Y, PLAYER_PORTRAIT_SIZE);
		widget.setModelType(WidgetModelType.LOCAL_PLAYER_CHATHEAD);
		// The local chat-head model is cached by the widget; changing this key forces gear swaps to rebuild.
		widget.setModelId(appearanceHash);
		setAnimation(widget, config.animatePlayerPortrait() ? AnimationID.CHATIDLENEU1 : NO_ANIMATION);
		widget.setModelZoom(PLAYER_MODEL_ZOOM);
		widget.setRotationX(0);
		widget.setRotationY(clampModelAngle(config.playerPortraitRotation()));
		widget.setRotationZ(0);
		widget.setHidden(false);
		widget.revalidate();
		return true;
	}

	private PortraitMode updateTargetPortraitWidget(Rectangle overlayBounds, Actor target)
	{
		if (!canShowLivePortraits() || !(target instanceof NPC))
		{
			hideTargetPortrait();
			return PortraitMode.NONE;
		}

		final NPC npc = (NPC) target;
		final NPCComposition composition = npc.getTransformedComposition();
		if (composition == null)
		{
			hideTargetPortrait();
			return PortraitMode.NONE;
		}

		final boolean hasChathead = hasModels(composition.getChatheadModels());
		if (!hasChathead)
		{
			hideTargetPortrait();
			return config.showSoftwareTargetPortraits() && npc.getModel() != null ? PortraitMode.SOFTWARE : PortraitMode.NONE;
		}

		final int modelType = WidgetModelType.NPC_CHATHEAD;
		final int modelId = composition.getId();
		final int targetModelKey = Arrays.hashCode(new int[]{composition.getId(), modelType, modelId});
		if (targetModelKey != targetCompositionId)
		{
			targetCompositionId = targetModelKey;
			hideTargetPortrait();
		}

		final Widget widget = getOrCreateTargetPortraitWidget();
		if (widget == null)
		{
			return PortraitMode.NONE;
		}

		positionPortraitWidget(widget, overlayBounds, TARGET_MODEL_X, TARGET_MODEL_Y, TARGET_MODEL_WIDTH, TARGET_MODEL_HEIGHT);
		positionPortraitBackgroundWidgets(targetPortraitBackgroundWidgets, overlayBounds,
			TARGET_PORTRAIT_X, TARGET_PORTRAIT_Y, TARGET_PORTRAIT_SIZE);
		widget.setModelType(modelType);
		widget.setModelId(modelId);
		setAnimation(widget, NO_ANIMATION);
		widget.setModelZoom(TARGET_MODEL_ZOOM);
		widget.setRotationX(portraitTilt(43));
		widget.setRotationY(portraitTurn(config.targetPortraitRotation(), 43));
		widget.setRotationZ(0);
		widget.setHidden(false);
		widget.revalidate();
		return PortraitMode.WIDGET;
	}

	private boolean hasModels(int[] modelIds)
	{
		return firstModelId(modelIds) >= 0;
	}

	private int firstModelId(int[] modelIds)
	{
		if (modelIds == null)
		{
			return -1;
		}

		for (int modelId : modelIds)
		{
			if (modelId >= 0)
			{
				return modelId;
			}
		}

		return -1;
	}

	private Widget getOrCreatePlayerPortraitWidget()
	{
		final Widget parent = getHudParent();
		if (parent == null)
		{
			hidePlayerPortrait();
			return null;
		}

		if (needsPortraitBackground(playerPortraitBackgroundWidgets, parent)
			|| portraitModelInvalid(playerPortraitWidget, parent))
		{
			hidePlayerPortrait();
			playerPortraitBackgroundWidgets = createPortraitBackgroundWidgets(parent);
			playerPortraitWidget = createPortraitModelWidget(parent);
		}

		return playerPortraitWidget;
	}

	private Widget getOrCreateTargetPortraitWidget()
	{
		final Widget parent = getHudParent();
		if (parent == null)
		{
			hideTargetPortrait();
			return null;
		}

		if (needsPortraitBackground(targetPortraitBackgroundWidgets, parent)
			|| portraitModelInvalid(targetPortraitWidget, parent))
		{
			hideTargetPortrait();
			targetPortraitBackgroundWidgets = createPortraitBackgroundWidgets(parent);
			targetPortraitWidget = createPortraitModelWidget(parent);
		}

		return targetPortraitWidget;
	}

	private boolean needsPortraitBackground(Widget[] widgets, Widget parent)
	{
		if (widgets == null || widgets.length != PORTRAIT_BACKGROUND_STRIPS)
		{
			return true;
		}

		for (Widget widget : widgets)
		{
			if (widget == null || widget.getParent() != parent)
			{
				return true;
			}
		}

		return false;
	}

	private boolean portraitModelInvalid(Widget widget, Widget parent)
	{
		return widget == null || widget.getParent() != parent || widget.getType() != WidgetType.MODEL;
	}

	private Widget[] createPortraitBackgroundWidgets(Widget parent)
	{
		final Widget[] widgets = new Widget[PORTRAIT_BACKGROUND_STRIPS];
		for (int i = 0; i < widgets.length; i++)
		{
			widgets[i] = createPortraitBackgroundWidget(parent);
		}

		return widgets;
	}

	private Widget createPortraitBackgroundWidget(Widget parent)
	{
		final Widget widget = parent.createChild(-1, WidgetType.RECTANGLE);
		widget.setFilled(true);
		widget.setTextColor(PORTRAIT_WIDGET_BACKGROUND.getRGB());
		widget.setOpacity(0);
		widget.setNoClickThrough(false);
		widget.setHidden(true);
		return widget;
	}

	private Widget createPortraitModelWidget(Widget parent)
	{
		final Widget widget = parent.createChild(-1, WidgetType.MODEL);
		widget.setHidden(true);
		widget.setNoClickThrough(false);
		return widget;
	}

	private Widget getHudParent()
	{
		if (client.isResized())
		{
			Widget parent = client.getWidget(InterfaceID.ToplevelPreEoc.HUD_CONTAINER_FRONT);
			if (parent != null && !parent.isHidden())
			{
				return parent;
			}

			parent = client.getWidget(InterfaceID.ToplevelOsrsStretch.HUD_CONTAINER_FRONT);
			if (parent != null && !parent.isHidden())
			{
				return parent;
			}
		}

		final Widget fixed = client.getWidget(InterfaceID.Toplevel.OVERLAY_HUD);
		if (fixed != null && !fixed.isHidden())
		{
			return fixed;
		}

		return null;
	}

	private void positionPortraitWidget(Widget widget, Rectangle overlayBounds, int x, int y, int width, int height)
	{
		final Rectangle parentBounds = widget.getParent().getBounds();
		widget.setPos(overlayBounds.x + x - parentBounds.x, overlayBounds.y + y - parentBounds.y);
		widget.setSize(width, height);
	}

	private void positionPortraitBackgroundWidgets(Widget[] widgets, Rectangle overlayBounds, int x, int y, int size)
	{
		if (widgets == null)
		{
			return;
		}

		final int diameter = size - PORTRAIT_BACKGROUND_INSET * 2;
		final double radius = diameter / 2.0;
		for (int i = 0; i < widgets.length; i++)
		{
			final Widget widget = widgets[i];
			if (widget == null)
			{
				continue;
			}

			final int top = (int) Math.round(i * diameter / (double) widgets.length);
			final int bottom = (int) Math.round((i + 1) * diameter / (double) widgets.length);
			final int height = Math.max(bottom - top + 1, 1);
			final double centerY = top + height / 2.0 - radius;
			final int width = Math.max(2, (int) Math.floor(2.0 * Math.sqrt(Math.max(0, radius * radius - centerY * centerY))) + 4);
			final int stripX = x + PORTRAIT_BACKGROUND_INSET + (diameter - width) / 2;
			final int stripY = y + PORTRAIT_BACKGROUND_INSET + top;

			positionPortraitWidget(widget, overlayBounds, stripX, stripY, width, height);
			widget.setHidden(false);
			widget.revalidate();
		}
	}

	private void setAnimation(Widget widget, int animationId)
	{
		if (widget.getAnimationId() != animationId)
		{
			widget.setAnimationId(animationId);
		}
	}

	private boolean drawSoftwareTargetPortrait(Graphics2D graphics, NPC npc)
	{
		final Model model = npc.getModel();
		if (model == null && targetSoftwarePortraitImage == null)
		{
			return false;
		}

		final int x = TARGET_PORTRAIT_X + SOFTWARE_PORTRAIT_INSET;
		final int y = TARGET_PORTRAIT_Y + SOFTWARE_PORTRAIT_INSET;
		final int size = TARGET_PORTRAIT_SIZE - SOFTWARE_PORTRAIT_INSET * 2;
		final int settingsHash = softwarePortraitSettingsHash();
		if (softwarePortraitTarget != npc || settingsHash != targetSoftwarePortraitSettingsHash)
		{
			softwarePortraitTarget = npc;
			targetSoftwarePortraitSettingsHash = settingsHash;
			targetSoftwarePortraitImage = null;
			targetSoftwarePortraitLastNanos = 0;
			targetSoftwarePortraitCamera.reset();
		}

		final long now = System.nanoTime();
		if (model != null && shouldUpdateSoftwarePortrait(now))
		{
			final int renderScale = config.lowQualitySoftwareTargetPortraits() ? 1 : SOFTWARE_PORTRAIT_HIGH_QUALITY_SCALE;
			final int renderSize = size * renderScale;
			final BufferedImage image = new BufferedImage(renderSize, renderSize, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D portraitGraphics = image.createGraphics();
			setupGraphics(portraitGraphics);
			portraitGraphics.setClip(new Ellipse2D.Double(0, 0, renderSize, renderSize));
			portraitGraphics.setPaint(new GradientPaint(0, 0, PORTRAIT_BACK_TOP, 0, renderSize, PORTRAIT_BACK_BOTTOM));
			portraitGraphics.fillOval(0, 0, renderSize, renderSize);

			if (drawModelPortrait(portraitGraphics, model, 0, 0, renderSize))
			{
				targetSoftwarePortraitImage = renderScale == 1 ? image : downsamplePortrait(image, size);
				targetSoftwarePortraitLastNanos = now;
			}

			portraitGraphics.dispose();
		}

		if (targetSoftwarePortraitImage == null)
		{
			return false;
		}

		graphics.drawImage(targetSoftwarePortraitImage, x, y, null);
		return true;
	}

	private BufferedImage downsamplePortrait(BufferedImage image, int size)
	{
		final BufferedImage downsampled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D graphics = downsampled.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics.drawImage(image, 0, 0, size, size, null);
		graphics.dispose();
		return downsampled;
	}

	private boolean shouldUpdateSoftwarePortrait(long now)
	{
		if (targetSoftwarePortraitImage == null)
		{
			return true;
		}

		final int fps = Math.max(1, Math.min(60, config.targetSoftwarePortraitFps()));
		return now - targetSoftwarePortraitLastNanos >= 1_000_000_000L / fps;
	}

	private int softwarePortraitSettingsHash()
	{
		return Arrays.hashCode(new int[]{
			config.targetPortraitRotation(),
			config.targetSoftwarePortraitZoom(),
			config.targetSoftwarePortraitFps(),
			config.enemyHeadPortraits() ? 1 : 0,
			config.lowQualitySoftwareTargetPortraits() ? 1 : 0
		});
	}

	private boolean drawModelPortrait(Graphics2D graphics, Model model, int x, int y, int size)
	{
		if (config.enemyHeadPortraits())
		{
			final boolean drewHeadPortrait = drawModelPortrait(graphics, model, x, y, size, targetSoftwarePortraitCamera,
				config.targetPortraitRotation(), PORTRAIT_SWAY_X, 19, SOFTWARE_PORTRAIT_HEAD_HEIGHT_FRACTION, true,
				SOFTWARE_PORTRAIT_HEAD_FIT_WIDTH, SOFTWARE_PORTRAIT_HEAD_FIT_HEIGHT,
				SOFTWARE_PORTRAIT_HEAD_CENTER_Y,
				SOFTWARE_PORTRAIT_HEAD_BASE_ZOOM * config.targetSoftwarePortraitZoom() / 100.0);
			if (drewHeadPortrait)
			{
				return true;
			}

			targetSoftwarePortraitCamera.reset();
		}

		return drawModelPortrait(graphics, model, x, y, size, targetSoftwarePortraitCamera,
			config.targetPortraitRotation(), PORTRAIT_SWAY_Y, 43, 1.0, false,
			SOFTWARE_PORTRAIT_FULL_MODEL_FIT, SOFTWARE_PORTRAIT_FULL_MODEL_FIT,
			SOFTWARE_PORTRAIT_FULL_MODEL_CENTER_Y,
			SOFTWARE_PORTRAIT_FULL_MODEL_BASE_ZOOM * config.targetSoftwarePortraitZoom() / 100.0);
	}

	private boolean drawModelPortrait(Graphics2D graphics, Model model, int x, int y, int size,
		SoftwarePortraitCamera camera, int rotation, int swayAmount, int swayPhase, double heightFraction,
		boolean cropFaces, double fitWidth, double fitHeight, double centerY, double zoom)
	{
		final float[] verticesX = model.getVerticesX();
		final float[] verticesY = model.getVerticesY();
		final float[] verticesZ = model.getVerticesZ();
		final int[] indices1 = model.getFaceIndices1();
		final int[] indices2 = model.getFaceIndices2();
		final int[] indices3 = model.getFaceIndices3();
		if (verticesX == null || verticesY == null || verticesZ == null
			|| indices1 == null || indices2 == null || indices3 == null
			|| verticesX.length == 0 || indices1.length == 0)
		{
			return false;
		}

		final int vertexCount = Math.min(verticesX.length, Math.min(verticesY.length, verticesZ.length));
		final double[] rotatedX = new double[vertexCount];
		final double[] rotatedZ = new double[vertexCount];
		final double angle = clampModelAngle(rotation + SOFTWARE_PORTRAIT_ROTATION_OFFSET
			+ (swayAmount == 0 ? 0 : idleSway(swayAmount, swayPhase)))
			* Math.PI * 2.0 / 2048.0;
		final double sin = Math.sin(angle);
		final double cos = Math.cos(angle);
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < vertexCount; i++)
		{
			rotatedX[i] = verticesX[i] * cos + verticesZ[i] * sin;
			rotatedZ[i] = verticesZ[i] * cos - verticesX[i] * sin;
			minY = Math.min(minY, verticesY[i]);
			maxY = Math.max(maxY, verticesY[i]);
		}

		if (!Double.isFinite(minY) || !Double.isFinite(maxY) || maxY <= minY)
		{
			return false;
		}

		final PortraitModelBounds fullBounds = modelBounds(rotatedX, verticesY, vertexCount);
		if (fullBounds.width() <= 1 || fullBounds.height() <= 1)
		{
			return false;
		}

		final double focusBottomY = minY + (maxY - minY) * heightFraction;
		PortraitModelBounds bounds = cropFaces
			? modelBounds(rotatedX, verticesY, vertexCount, focusBottomY)
			: focusedModelBounds(rotatedX, verticesY, vertexCount);
		if (bounds.vertexCount < 6 || bounds.width() <= 1 || bounds.height() <= 1)
		{
			bounds = fullBounds;
		}
		if (bounds.width() <= 1 || bounds.height() <= 1)
		{
			return false;
		}

		final double scaleWidth = cropFaces ? bounds.width()
			: Math.min(bounds.width(), bounds.height() * SOFTWARE_PORTRAIT_FULL_MODEL_MAX_ASPECT);
		final double widthScale = size * fitWidth / scaleWidth;
		final double heightScale = size * fitHeight / bounds.height();
		final double fitScale = Math.min(widthScale, heightScale);
		final double fillScale = cropFaces ? fitScale
			: Math.min(Math.max(widthScale, heightScale), fitScale * SOFTWARE_PORTRAIT_FULL_MODEL_MAX_FILL_BOOST);
		final double scale = fillScale * zoom;
		final double modelCenterX = (bounds.minX + bounds.maxX) / 2.0;
		final double modelCenterY = (bounds.minY + bounds.maxY) / 2.0;
		final SoftwarePortraitFrame frame = camera.update(modelCenterX, modelCenterY, scale);
		final double screenCenterX = x + size / 2.0;
		final double screenCenterY = y + size * centerY;
		final int[] screenX = new int[vertexCount];
		final int[] screenY = new int[vertexCount];
		for (int i = 0; i < vertexCount; i++)
		{
			screenX[i] = (int) Math.round(screenCenterX + (rotatedX[i] - frame.centerX) * frame.scale);
			screenY[i] = (int) Math.round(screenCenterY + (verticesY[i] - frame.centerY) * frame.scale);
		}

		final int faceCount = Math.min(indices1.length, Math.min(indices2.length, indices3.length));
		final List<ProjectedFace> faces = new ArrayList<>(faceCount);
		final int faceStep = config.lowQualitySoftwareTargetPortraits() ? 2 : 1;
		for (int face = 0; face < faceCount; face += faceStep)
		{
			final int a = indices1[face];
			final int b = indices2[face];
			final int c = indices3[face];
			if (a < 0 || b < 0 || c < 0 || a >= vertexCount || b >= vertexCount || c >= vertexCount)
			{
				continue;
			}

			if (cropFaces && verticesY[a] > focusBottomY && verticesY[b] > focusBottomY && verticesY[c] > focusBottomY)
			{
				continue;
			}

			final int[] xs = new int[]{screenX[a], screenX[b], screenX[c]};
			final int[] ys = new int[]{screenY[a], screenY[b], screenY[c]};
			if (triangleArea(xs, ys) == 0)
			{
				continue;
			}

			final double depth = (rotatedZ[a] + rotatedZ[b] + rotatedZ[c]) / 3.0;
			faces.add(new ProjectedFace(xs, ys, depth, modelFaceColor(model, face)));
		}

		faces.sort(Comparator.comparingDouble(face -> face.depth));
		for (ProjectedFace face : faces)
		{
			graphics.setColor(face.color);
			graphics.fillPolygon(face.x, face.y, 3);
		}

		return !faces.isEmpty();
	}

	private PortraitModelBounds modelBounds(double[] rotatedX, float[] verticesY, int vertexCount)
	{
		final PortraitModelBounds bounds = new PortraitModelBounds();
		for (int i = 0; i < vertexCount; i++)
		{
			bounds.include(rotatedX[i], verticesY[i]);
		}

		return bounds;
	}

	private PortraitModelBounds modelBounds(double[] rotatedX, float[] verticesY, int vertexCount, double maxIncludedY)
	{
		final PortraitModelBounds bounds = new PortraitModelBounds();
		for (int i = 0; i < vertexCount; i++)
		{
			if (verticesY[i] <= maxIncludedY)
			{
				bounds.include(rotatedX[i], verticesY[i]);
			}
		}

		return bounds;
	}

	private PortraitModelBounds focusedModelBounds(double[] rotatedX, float[] verticesY, int vertexCount)
	{
		final double minX = percentile(rotatedX, vertexCount, SOFTWARE_PORTRAIT_FOCUS_MIN_X);
		final double maxX = percentile(rotatedX, vertexCount, SOFTWARE_PORTRAIT_FOCUS_MAX_X);
		final double minY = percentile(verticesY, vertexCount, SOFTWARE_PORTRAIT_FOCUS_MIN_Y);
		final double maxY = percentile(verticesY, vertexCount, SOFTWARE_PORTRAIT_FOCUS_MAX_Y);
		final PortraitModelBounds bounds = new PortraitModelBounds();
		for (int i = 0; i < vertexCount; i++)
		{
			if (rotatedX[i] >= minX && rotatedX[i] <= maxX && verticesY[i] >= minY && verticesY[i] <= maxY)
			{
				bounds.include(rotatedX[i], verticesY[i]);
			}
		}

		return bounds;
	}

	private static double percentile(double[] values, int length, double percentile)
	{
		final double[] copy = Arrays.copyOf(values, length);
		Arrays.sort(copy);
		return copy[percentileIndex(length, percentile)];
	}

	private static double percentile(float[] values, int length, double percentile)
	{
		final float[] copy = Arrays.copyOf(values, length);
		Arrays.sort(copy);
		return copy[percentileIndex(length, percentile)];
	}

	private static int percentileIndex(int length, double percentile)
	{
		return Math.max(0, Math.min(length - 1, (int) Math.round((length - 1) * percentile)));
	}

	private Color modelFaceColor(Model model, int face)
	{
		final int[] colors1 = model.getFaceColors1();
		final int[] colors2 = model.getFaceColors2();
		final int[] colors3 = model.getFaceColors3();
		int packedColor = faceColor(colors1, face);
		if (packedColor < 0)
		{
			packedColor = faceColor(colors2, face);
		}
		if (packedColor < 0)
		{
			packedColor = faceColor(colors3, face);
		}

		final byte[] transparencies = model.getFaceTransparencies();
		final int alpha = transparencies == null || face >= transparencies.length
			? 255
			: Math.max(40, 255 - (transparencies[face] & 0xff));

		if (packedColor < 0)
		{
			return new Color(92, 86, 76, alpha);
		}

		final short hsl = (short) packedColor;
		final float hue = JagexColor.unpackHue(hsl) / 64f;
		final float saturation = Math.min(1f, JagexColor.unpackSaturation(hsl) / 8f);
		final float brightness = Math.max(0.16f, Math.min(1f, JagexColor.unpackLuminance(hsl) / 128f));
		final Color color = Color.getHSBColor(hue, saturation, brightness);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	private int faceColor(int[] colors, int face)
	{
		if (colors == null || face >= colors.length)
		{
			return -1;
		}

		return colors[face];
	}

	private int triangleArea(int[] x, int[] y)
	{
		return (x[1] - x[0]) * (y[2] - y[0]) - (x[2] - x[0]) * (y[1] - y[0]);
	}

	private void drawPortraitShell(Graphics2D graphics, int x, int y, int size, int combatLevel, String fallbackText,
		BadgeAnchor badgeAnchor)
	{
		final boolean hasLivePortrait = fallbackText == null;
		if (hasLivePortrait)
		{
			drawPortraitRingWithCutout(graphics, x, y, size);
		}
		else
		{
			drawPortraitFallback(graphics, x, y, size, fallbackText);
		}

		if (combatLevel > 0)
		{
			drawLevelBadge(graphics, badgeCenterX(x, size, badgeAnchor), y + size - 15, combatLevel);
		}
	}

	private int badgeCenterX(int x, int size, BadgeAnchor badgeAnchor)
	{
		if (badgeAnchor == BadgeAnchor.BOTTOM_LEFT)
		{
			return x + 28;
		}
		if (badgeAnchor == BadgeAnchor.BOTTOM_RIGHT)
		{
			return x + size - 20;
		}

		return x + size / 2;
	}

	private void drawPlayerBackplate(Graphics2D graphics)
	{
		drawConnectedBackplate(graphics, PLAYER_CONNECTOR_X, PLAYER_CONNECTOR_Y,
			PLAYER_CONNECTOR_WIDTH, PLAYER_CONNECTOR_HEIGHT, PLAYER_PORTRAIT_X, PLAYER_PORTRAIT_Y,
			PLAYER_PORTRAIT_SIZE, PLAYER_BAR_X - 3, PLAYER_CONNECTOR_X + PLAYER_CONNECTOR_WIDTH - 8);
	}

	private void drawTargetBackplate(Graphics2D graphics)
	{
		drawConnectedBackplate(graphics, TARGET_CONNECTOR_X, TARGET_CONNECTOR_Y,
			TARGET_CONNECTOR_WIDTH, TARGET_CONNECTOR_HEIGHT, TARGET_PORTRAIT_X, TARGET_PORTRAIT_Y,
			TARGET_PORTRAIT_SIZE, TARGET_CONNECTOR_X + 8, TARGET_BAR_X + TARGET_BAR_WIDTH + 3);
	}

	private void drawConnectedBackplate(Graphics2D graphics, int x, int y, int width, int height,
		int portraitX, int portraitY, int portraitSize, int highlightStartX, int highlightEndX)
	{
		final Area outerPlate = clippedPlate(x, y, width, height, portraitX, portraitY, portraitSize);
		final Area innerPlate = clippedPlate(x + 3, y + 3, width - 6, height - 6,
			portraitX, portraitY, portraitSize);

		graphics.setColor(RING_OUTER);
		graphics.fill(outerPlate);
		graphics.setPaint(new GradientPaint(x, y, BAR_BORDER_MID, x, y + height, BAR_BORDER));
		graphics.fill(innerPlate);
		graphics.setColor(BAR_BORDER_HIGHLIGHT);
		graphics.setStroke(new BasicStroke(1f));
		graphics.drawLine(highlightStartX, y + 5, highlightEndX, y + 5);
	}

	private Area clippedPlate(int x, int y, int width, int height, int portraitX, int portraitY, int portraitSize)
	{
		final Area plate = new Area(new java.awt.geom.RoundRectangle2D.Double(x, y, width, height, BAR_ARC, BAR_ARC));
		plate.subtract(new Area(new Ellipse2D.Double(portraitX + 2, portraitY + 2, portraitSize - 4, portraitSize - 4)));
		return plate;
	}

	private void drawPortraitFallback(Graphics2D graphics, int x, int y, int size, String fallbackText)
	{
		graphics.setColor(RING_OUTER);
		graphics.fillOval(x, y, size, size);
		graphics.setPaint(new GradientPaint(x, y, RING_MID, x, y + size, RING_DARK));
		graphics.fillOval(x + 4, y + 4, size - 8, size - 8);
		graphics.setColor(RING_INNER);
		graphics.setStroke(new BasicStroke(2f));
		graphics.drawOval(x + 7, y + 7, size - 14, size - 14);

		graphics.setPaint(new GradientPaint(x, y + 10, PORTRAIT_BACK_TOP, x, y + size - 14, PORTRAIT_BACK_BOTTOM));
		graphics.fillOval(x + 10, y + 10, size - 20, size - 20);

		graphics.setColor(new Color(255, 229, 136, 70));
		graphics.setStroke(new BasicStroke(2f));
		graphics.drawArc(x + 9, y + 8, size - 18, size - 18, 35, 95);

		if (fallbackText != null)
		{
			drawFallbackText(graphics, x + 10, y + 10, size - 20, fallbackText);
		}
	}

	private void drawPortraitRingWithCutout(Graphics2D graphics, int x, int y, int size)
	{
		final int cutoutInset = 10;
		final Area outer = new Area(new Ellipse2D.Double(x, y, size, size));
		outer.subtract(new Area(new Ellipse2D.Double(x + cutoutInset, y + cutoutInset,
			size - cutoutInset * 2, size - cutoutInset * 2)));

		graphics.setPaint(new GradientPaint(x, y, RING_MID, x, y + size, RING_DARK));
		graphics.fill(outer);

		graphics.setColor(RING_OUTER);
		graphics.setStroke(new BasicStroke(4f));
		graphics.drawOval(x + 2, y + 2, size - 4, size - 4);
		graphics.setColor(RING_INNER);
		graphics.setStroke(new BasicStroke(2f));
		graphics.drawOval(x + cutoutInset - 2, y + cutoutInset - 2,
			size - (cutoutInset - 2) * 2, size - (cutoutInset - 2) * 2);

		graphics.setColor(new Color(255, 229, 136, 70));
		graphics.setStroke(new BasicStroke(2f));
		graphics.drawArc(x + 9, y + 8, size - 18, size - 18, 35, 95);
	}

	private void drawHealthBar(Graphics2D graphics, int x, int y, int width, int height, double value,
		Color fillColor, String text, BarSlot slot, double restoreValue, Actor target)
	{
		final double clampedValue = clamp(value);
		final BarFrame frame = animatedBarFrame(slot, clampedValue, target);

		graphics.setColor(RING_OUTER);
		graphics.fillRoundRect(x - 3, y - 3, width + 6, height + 6, BAR_ARC, BAR_ARC);
		graphics.setColor(BAR_BORDER_MID);
		graphics.fillRoundRect(x - 1, y - 1, width + 2, height + 2, BAR_ARC, BAR_ARC);
		graphics.setColor(BAR_BACKGROUND);
		graphics.fillRect(x, y, width, height);

		drawAnimatedBarFrame(graphics, x, y, width, height, frame, fillColor, HEALTH_GAIN_GLOW,
			HEALTH_DAMAGE_TRAIL);

		drawRestorePreview(graphics, x, y, width, height, clampedValue, restoreValue);

		graphics.setColor(new Color(100, 17, 12, 230));
		graphics.drawRect(x, y, width, height);

		if (fillColor != UNKNOWN_COLOR || !text.isEmpty())
		{
			drawHitpointsIcon(graphics, x, y, width, height, slot == BarSlot.TARGET);
		}

		if (!text.isEmpty())
		{
			final Font originalFont = graphics.getFont();
			graphics.setFont(runescapeBoldFont(HEALTH_TEXT_SIZE));
			final FontMetrics metrics = graphics.getFontMetrics();
			drawHealthText(graphics, text, healthTextX(x, width, metrics.stringWidth(text), slot),
				centeredTextBaseline(metrics, y, height, HEALTH_TEXT_BASELINE_OFFSET));
			graphics.setFont(originalFont);
		}
	}

	private int healthTextX(int barX, int barWidth, int textWidth, BarSlot slot)
	{
		final int iconWidth = hitpointsIconDrawWidth();
		final int iconX = hitpointsIconX(barX, barWidth, iconWidth, slot == BarSlot.TARGET);
		final int textX = slot == BarSlot.TARGET
			? iconX - HITPOINTS_TEXT_ICON_GAP - textWidth
			: iconX + iconWidth + HITPOINTS_TEXT_ICON_GAP;
		return Math.max(barX + 2, Math.min(textX, barX + barWidth - textWidth - 2));
	}

	private int hitpointsIconDrawWidth()
	{
		final BufferedImage icon = hitpointsIcon();
		if (icon == null || icon.getWidth() <= 0 || icon.getHeight() <= 0)
		{
			return HITPOINTS_ICON_SIZE;
		}

		final double scale = Math.min(1.0,
			Math.min((double) HITPOINTS_ICON_SIZE / icon.getWidth(), (double) HITPOINTS_ICON_SIZE / icon.getHeight()));
		return Math.max(1, (int) Math.round(icon.getWidth() * scale));
	}

	private void drawHitpointsIcon(Graphics2D graphics, int barX, int barY, int barWidth, int barHeight,
		boolean rightAligned)
	{
		final BufferedImage icon = hitpointsIcon();
		if (icon != null)
		{
			final int iconWidth = icon.getWidth();
			final int iconHeight = icon.getHeight();
			if (iconWidth > 0 && iconHeight > 0)
			{
				final double scale = Math.min(1.0,
					Math.min((double) HITPOINTS_ICON_SIZE / iconWidth, (double) HITPOINTS_ICON_SIZE / iconHeight));
				final int drawWidth = Math.max(1, (int) Math.round(iconWidth * scale));
				final int drawHeight = Math.max(1, (int) Math.round(iconHeight * scale));
				final int drawX = hitpointsIconX(barX, barWidth, drawWidth, rightAligned);
				final int drawY = barY + (barHeight - drawHeight) / 2;
				final Graphics2D iconGraphics = (Graphics2D) graphics.create();
				iconGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
				iconGraphics.drawImage(icon, drawX, drawY, drawWidth, drawHeight, null);
				iconGraphics.dispose();
			}
			return;
		}

		final int size = HITPOINTS_ICON_SIZE;
		final int x = hitpointsIconX(barX, barWidth, size, rightAligned);
		final int y = barY + (barHeight - size) / 2;
		drawHeartShape(graphics, x + 1, y + 1, size, new Color(73, 3, 2, 210));
		drawHeartShape(graphics, x, y, size, new Color(255, 98, 67));
	}

	private int hitpointsIconX(int barX, int barWidth, int iconWidth, boolean rightAligned)
	{
		if (rightAligned)
		{
			return barX + barWidth - HITPOINTS_ICON_PADDING - iconWidth;
		}

		return barX + HITPOINTS_ICON_PADDING;
	}

	private BufferedImage hitpointsIcon()
	{
		if (hitpointsIcon == null)
		{
			hitpointsIcon = spriteManager.getSprite(net.runelite.api.SpriteID.UNKNOWN_SMALL_HITPOINTS_ICON, 0);
			if (hitpointsIcon == null)
			{
				hitpointsIcon = spriteManager.getSprite(SpriteID.OrbIcon.HITPOINTS, 0);
			}
		}

		return hitpointsIcon;
	}

	private void drawHeartShape(Graphics2D graphics, int x, int y, int size, Color color)
	{
		final int lobe = Math.max(size / 2, 2);
		final Polygon point = new Polygon();
		point.addPoint(x + 1, y + size / 3);
		point.addPoint(x + size - 1, y + size / 3);
		point.addPoint(x + size / 2, y + size);

		graphics.setColor(color);
		graphics.fillOval(x, y, lobe, lobe);
		graphics.fillOval(x + size - lobe, y, lobe, lobe);
		graphics.fillPolygon(point);
	}

	private void drawPrayerBar(Graphics2D graphics, int x, int y, int width, int height, double value,
		double restoreValue)
	{
		final double clampedValue = clamp(value);
		final BarFrame frame = playerPrayerBar.update(clampedValue, config.animateBarChanges(), System.nanoTime());

		graphics.setColor(RING_OUTER);
		graphics.fillRect(x - 2, y - 2, width + 4, height + 4);
		graphics.setColor(BAR_BACKGROUND);
		graphics.fillRect(x, y, width, height);

		drawAnimatedBarFrame(graphics, x, y, width, height, frame, PRAYER_COLOR, PRAYER_GAIN_GLOW,
			PRAYER_DRAIN_TRAIL);

		drawRestorePreview(graphics, x, y, width, height, clampedValue, restoreValue, PRAYER_RESTORE_PREVIEW);

		graphics.setColor(new Color(11, 51, 75, 220));
		graphics.drawRect(x, y, width, height);
	}

	private void drawAnimatedBarFrame(Graphics2D graphics, int x, int y, int width, int height, BarFrame frame,
		Color fillColor, Color gainColor, Color trailColor)
	{
		final int fillWidth = (int) Math.round(width * frame.value);
		final int trailWidth = (int) Math.round(width * frame.trail);
		if (frame.showTrail && trailWidth > fillWidth)
		{
			drawBarSegment(graphics, x + fillWidth, y, trailWidth - fillWidth, height, trailColor);
		}

		drawBarFill(graphics, x, y, fillWidth, height, fillColor);

		if (frame.showGain)
		{
			final int gainStart = (int) Math.round(width * frame.gainStart);
			final int gainEnd = (int) Math.round(width * Math.max(frame.value, frame.gainEnd));
			if (gainEnd > gainStart)
			{
				final Color glow = withAlpha(gainColor, frame.gainAlpha);
				drawBarSegment(graphics, x + gainStart, y, gainEnd - gainStart, height, glow);
				graphics.setColor(withAlpha(new Color(255, 255, 255, 72), frame.gainAlpha));
				graphics.fillRect(x + gainStart + 1, y + 1, Math.max(gainEnd - gainStart - 2, 0),
					Math.max(height / 3, 1));
			}
		}
	}

	private void drawBarFill(Graphics2D graphics, int x, int y, int width, int height, Color fillColor)
	{
		if (isHealthFill(fillColor))
		{
			drawHealthBarFill(graphics, x, y, width, height, fillColor == HEALTH_LOW_COLOR);
		}
		else
		{
			drawBarSegment(graphics, x, y, width, height, fillColor);
		}

		if (width > 0)
		{
			graphics.setColor(isHealthFill(fillColor) ? new Color(255, 128, 94, 48) : new Color(255, 255, 255, 34));
			graphics.fillRect(x + 1, y + 1, Math.max(width - 2, 0), Math.max(height / 4, 1));
		}
	}

	private void drawHealthBarFill(Graphics2D graphics, int x, int y, int width, int height, boolean lowHealth)
	{
		if (width <= 0)
		{
			return;
		}

		graphics.setPaint(new GradientPaint(x, y,
			lowHealth ? HEALTH_LOW_TOP : HEALTH_TOP,
			x, y + height,
			lowHealth ? HEALTH_LOW_BOTTOM : HEALTH_BOTTOM));
		graphics.fillRect(x, y, width, height);
	}

	private boolean isHealthFill(Color fillColor)
	{
		return fillColor == HEALTH_COLOR || fillColor == HEALTH_LOW_COLOR;
	}

	private void drawBarSegment(Graphics2D graphics, int x, int y, int width, int height, Color color)
	{
		if (width <= 0)
		{
			return;
		}

		graphics.setColor(color);
		graphics.fillRect(x, y, width, height);
	}

	private static Color withAlpha(Color color, double alpha)
	{
		final int adjustedAlpha = (int) Math.round(color.getAlpha() * clamp(alpha));
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), adjustedAlpha);
	}

	private void drawRestorePreview(Graphics2D graphics, int x, int y, int width, int height, double currentValue,
		double restoreValue)
	{
		drawRestorePreview(graphics, x, y, width, height, currentValue, restoreValue, RESTORE_PREVIEW);
	}

	private void drawRestorePreview(Graphics2D graphics, int x, int y, int width, int height, double currentValue,
		double restoreValue, Color previewColor)
	{
		if (restoreValue <= 0)
		{
			return;
		}

		final double restoredValue = currentValue + restoreValue;
		final int start = (int) Math.round(width * currentValue);
		final int end = (int) Math.round(width * clamp(restoredValue));
		final Color color = restoredValue > 1 ? OVERHEAL_PREVIEW : previewColor;
		drawBarSegment(graphics, x + start, y, end - start, height, color);

		if (restoredValue > 1)
		{
			drawBarSegment(graphics, x + width - 2, y, 2, height, OVERHEAL_PREVIEW);
		}

		final int highlightStart = Math.min(start + 1, width);
		final int highlightEnd = Math.max(end - highlightStart - 1, 0);
		graphics.setColor(new Color(255, 255, 255, 46));
		graphics.fillRect(x + highlightStart, y + 1, highlightEnd, Math.max(height / 4, 1));
	}

	private void drawTitle(Graphics2D graphics, String text, int x, int baselineY, int width, boolean rightAligned)
	{
		final Font originalFont = graphics.getFont();
		graphics.setFont(runescapeBoldFont(TITLE_TEXT_SIZE));
		final FontMetrics metrics = graphics.getFontMetrics();
		final String label = truncate(text, metrics, width);
		final int textX = rightAligned ? x + width - metrics.stringWidth(label) : x;
		drawOutlinedText(graphics, label, textX, baselineY, GOLD_TEXT);
		graphics.setFont(originalFont);
	}

	private void drawLevelBadge(Graphics2D graphics, int centerX, int centerY, int combatLevel)
	{
		final String text = String.valueOf(combatLevel);
		final int x = centerX - BADGE_SIZE / 2;
		final int y = centerY - BADGE_SIZE / 2;

		graphics.setColor(RING_OUTER);
		graphics.fillOval(x - 2, y - 2, BADGE_SIZE + 4, BADGE_SIZE + 4);
		graphics.setColor(BADGE_BACKGROUND);
		graphics.fillOval(x, y, BADGE_SIZE, BADGE_SIZE);
		graphics.setColor(BADGE_BORDER);
		graphics.setStroke(new BasicStroke(1.5f));
		graphics.drawOval(x, y, BADGE_SIZE, BADGE_SIZE);

		final Font originalFont = graphics.getFont();
		graphics.setFont(runescapeBoldFont(combatLevel >= 100 ? 9f : 10f));
		final FontMetrics metrics = graphics.getFontMetrics();
		drawOutlinedText(graphics, text, centerX - metrics.stringWidth(text) / 2,
			centeredTextBaseline(metrics, y, BADGE_SIZE, LEVEL_TEXT_BASELINE_OFFSET), LIGHT_TEXT);
		graphics.setFont(originalFont);
	}

	private void drawFallbackText(Graphics2D graphics, int x, int y, int size, String text)
	{
		final Font originalFont = graphics.getFont();
		graphics.setFont(runescapeBoldFont(FALLBACK_TEXT_SIZE));
		final FontMetrics metrics = graphics.getFontMetrics();
		drawOutlinedText(graphics, text, x + (size - metrics.stringWidth(text)) / 2,
			y + (size - metrics.getHeight()) / 2 + metrics.getAscent(), new Color(58, 52, 44));
		graphics.setFont(originalFont);
	}

	private Font runescapeBoldFont(float size)
	{
		return FontManager.getRunescapeBoldFont().deriveFont(size);
	}

	private int centeredTextBaseline(FontMetrics metrics, int y, int height, int offsetY)
	{
		return y + (height - metrics.getHeight()) / 2 + metrics.getAscent() + offsetY;
	}

	private void drawHealthText(Graphics2D graphics, String text, int x, int y)
	{
		final Object originalTextAntialias = graphics.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

		graphics.setColor(HEALTH_TEXT_OUTLINE);
		graphics.drawString(text, x + 1, y + 1);
		graphics.drawString(text, x - 1, y);
		graphics.drawString(text, x + 1, y);
		graphics.drawString(text, x, y - 1);
		graphics.drawString(text, x, y + 1);
		graphics.setColor(HEALTH_TEXT);
		graphics.drawString(text, x, y);

		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
			originalTextAntialias == null ? RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT : originalTextAntialias);
	}

	private void drawOutlinedText(Graphics2D graphics, String text, int x, int y, Color color)
	{
		graphics.setColor(SHADOW);
		graphics.drawString(text, x + 1, y + 1);
		graphics.drawString(text, x - 1, y);
		graphics.drawString(text, x + 1, y);
		graphics.drawString(text, x, y - 1);
		graphics.drawString(text, x, y + 1);
		graphics.setColor(color);
		graphics.drawString(text, x, y);
	}

	private String healthLabel(int current)
	{
		return config.showNumbers() ? String.valueOf(current) : "";
	}

	private TargetHealth targetHealth(Actor target, int ratio, int scale)
	{
		if (ratio < 0 || scale <= 0)
		{
			return new TargetHealth(false, 0, "");
		}

		final Integer maximum = target instanceof NPC ? npcManager.getHealth(((NPC) target).getId()) : null;
		if (maximum != null && maximum > 0)
		{
			final int current = estimateHitpoints(ratio, scale, maximum);
			return new TargetHealth(true, percent(current, maximum), healthLabel(current));
		}

		final double value = percent(ratio, scale);
		return new TargetHealth(true, value, config.showNumbers() ? percentLabel(value) : "");
	}

	private int getRestoreValue(String statName)
	{
		final MenuEntry[] menuEntries = client.getMenuEntries();
		if (menuEntries.length == 0)
		{
			return 0;
		}

		final Widget widget = menuEntries[menuEntries.length - 1].getWidget();
		if (widget == null || widget.getId() != InterfaceID.Inventory.ITEMS || widget.getItemId() <= 0)
		{
			return 0;
		}

		final Effect effect = itemStatService.getItemStatChanges(widget.getItemId());
		if (effect == null)
		{
			return 0;
		}

		int restoreValue = 0;
		for (StatChange statChange : effect.calculate(client).getStatChanges())
		{
			if (statChange.getTheoretical() > 0 && statName.equals(statChange.getStat().getName()))
			{
				restoreValue = Math.max(restoreValue, statChange.getTheoretical());
			}
		}

		return restoreValue;
	}

	private BarFrame animatedBarFrame(BarSlot slot, double targetValue, Actor target)
	{
		if (slot == BarSlot.TARGET && target != animatedTarget)
		{
			animatedTarget = target;
			targetHealthBar.reset(targetValue);
		}

		return animatedBar(slot).update(targetValue, config.animateBarChanges(), System.nanoTime());
	}

	private AnimatedBar animatedBar(BarSlot slot)
	{
		if (slot == BarSlot.TARGET)
		{
			return targetHealthBar;
		}

		return playerHealthBar;
	}

	private static int estimateHitpoints(int ratio, int scale, int maximum)
	{
		if (ratio <= 0)
		{
			return 0;
		}

		int minimum = 1;
		int upper;
		if (scale > 1)
		{
			if (ratio > 1)
			{
				minimum = (maximum * (ratio - 1) + scale - 2) / (scale - 1);
			}

			upper = (maximum * ratio - 1) / (scale - 1);
			if (upper > maximum)
			{
				upper = maximum;
			}
		}
		else
		{
			upper = maximum;
		}

		return Math.max(0, Math.min(maximum, (minimum + upper + 1) / 2));
	}

	private static String percentLabel(double value)
	{
		return (int) Math.round(clamp(value) * 100) + "%";
	}

	private static void setupGraphics(Graphics2D graphics)
	{
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}

	private static Color healthColor(int current, int maximum)
	{
		return percent(current, maximum) <= 0.25 ? HEALTH_LOW_COLOR : HEALTH_COLOR;
	}

	private static Color healthColor(double value)
	{
		return clamp(value) <= 0.25 ? HEALTH_LOW_COLOR : HEALTH_COLOR;
	}

	private static double percent(int current, int maximum)
	{
		if (maximum <= 0)
		{
			return 0;
		}

		return clamp(current / (double) maximum);
	}

	private static double clamp(double value)
	{
		if (value < 0)
		{
			return 0;
		}
		if (value > 1)
		{
			return 1;
		}
		return value;
	}

	private static int clampModelAngle(int angle)
	{
		if (angle < 0)
		{
			return 0;
		}
		if (angle > 2047)
		{
			return 2047;
		}
		return angle;
	}

	private int portraitTurn(int baseRotation, int phase)
	{
		return clampModelAngle(baseRotation + idleSway(PORTRAIT_SWAY_Y, phase));
	}

	private int portraitTilt(int phase)
	{
		return clampModelAngle(idleSway(PORTRAIT_SWAY_X, phase + 19));
	}

	private int idleSway(int amount, int phase)
	{
		return (int) Math.round(Math.sin((client.getGameCycle() + phase) / 36.0) * amount);
	}

	private void hideWidget(Widget widget)
	{
		if (widget != null)
		{
			widget.setHidden(true);
			widget.revalidate();
		}
	}

	private void hideWidgets(Widget[] widgets)
	{
		if (widgets == null)
		{
			return;
		}

		for (Widget widget : widgets)
		{
			hideWidget(widget);
		}
	}

	private boolean isPortraitResetCoolingDown()
	{
		if (portraitResetGameCycle == Integer.MIN_VALUE)
		{
			return false;
		}

		final int elapsedCycles = client.getGameCycle() - portraitResetGameCycle;
		if (elapsedCycles < 0)
		{
			portraitResetGameCycle = client.getGameCycle();
			return true;
		}

		if (elapsedCycles >= PORTRAIT_REBUILD_DELAY_CYCLES)
		{
			portraitResetGameCycle = Integer.MIN_VALUE;
			return false;
		}

		return true;
	}

	private static boolean hasUsablePlayerComposition(Player player)
	{
		final PlayerComposition composition = player.getPlayerComposition();
		return composition != null
			&& composition.getEquipmentIds() != null
			&& composition.getEquipmentIds().length > 0
			&& composition.getColors() != null;
	}

	private static int playerAppearanceHash(Player player)
	{
		final PlayerComposition composition = player.getPlayerComposition();
		if (composition == null)
		{
			return 1;
		}

		int hash = Arrays.hashCode(composition.getEquipmentIds());
		hash = 31 * hash + Arrays.hashCode(composition.getColors());
		hash = 31 * hash + composition.getGender();
		hash = 31 * hash + composition.getTransformedNpcId();
		hash &= 0x7fffffff;
		return hash == 0 ? 1 : hash;
	}

	private static String initialsFor(String name)
	{
		final String cleanName = name.trim();
		if (cleanName.isEmpty())
		{
			return "?";
		}

		final String[] parts = cleanName.split("\\s+");
		if (parts.length == 1)
		{
			return cleanName.substring(0, Math.min(2, cleanName.length())).toUpperCase();
		}

		return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
	}

	private static String truncate(String text, FontMetrics metrics, int maxWidth)
	{
		if (metrics.stringWidth(text) <= maxWidth)
		{
			return text;
		}

		final String ellipsis = "...";
		int end = text.length();
		while (end > 0 && metrics.stringWidth(text.substring(0, end) + ellipsis) > maxWidth)
		{
			end--;
		}

		return end == 0 ? ellipsis : text.substring(0, end) + ellipsis;
	}

	private static double approach(double current, double target, double seconds, double rate)
	{
		return target + (current - target) * Math.exp(-rate * seconds);
	}

	private static final class ProjectedFace
	{
		private final int[] x;
		private final int[] y;
		private final double depth;
		private final Color color;

		private ProjectedFace(int[] x, int[] y, double depth, Color color)
		{
			this.x = x;
			this.y = y;
			this.depth = depth;
			this.color = color;
		}
	}

	private static final class PortraitModelBounds
	{
		private double minX = Double.POSITIVE_INFINITY;
		private double maxX = Double.NEGATIVE_INFINITY;
		private double minY = Double.POSITIVE_INFINITY;
		private double maxY = Double.NEGATIVE_INFINITY;
		private int vertexCount;

		private void include(double x, double y)
		{
			minX = Math.min(minX, x);
			maxX = Math.max(maxX, x);
			minY = Math.min(minY, y);
			maxY = Math.max(maxY, y);
			vertexCount++;
		}

		private double width()
		{
			return maxX - minX;
		}

		private double height()
		{
			return maxY - minY;
		}
	}

	private static final class SoftwarePortraitCamera
	{
		private double centerX;
		private double centerY;
		private double scale;
		private boolean initialized;

		private SoftwarePortraitFrame update(double targetCenterX, double targetCenterY, double targetScale)
		{
			if (!initialized)
			{
				centerX = targetCenterX;
				centerY = targetCenterY;
				scale = targetScale;
				initialized = true;
			}
			else
			{
				centerX += (targetCenterX - centerX) * SOFTWARE_PORTRAIT_CENTER_SMOOTHING;
				centerY += (targetCenterY - centerY) * SOFTWARE_PORTRAIT_CENTER_SMOOTHING;
				scale += (targetScale - scale) * SOFTWARE_PORTRAIT_SCALE_SMOOTHING;
			}

			return new SoftwarePortraitFrame(centerX, centerY, scale);
		}

		private void reset()
		{
			initialized = false;
		}
	}

	private static final class SoftwarePortraitFrame
	{
		private final double centerX;
		private final double centerY;
		private final double scale;

		private SoftwarePortraitFrame(double centerX, double centerY, double scale)
		{
			this.centerX = centerX;
			this.centerY = centerY;
			this.scale = scale;
		}
	}

	private static final class AnimatedBar
	{
		private double value = Double.NaN;
		private double trail = Double.NaN;
		private double previousTarget = Double.NaN;
		private double gainStart;
		private double gainEnd;
		private long lastNanos;
		private long damageStartedNanos;
		private long gainStartedNanos;
		private boolean damageActive;

		private void reset(double target)
		{
			value = clamp(target);
			trail = value;
			previousTarget = value;
			gainStart = value;
			gainEnd = value;
			lastNanos = 0;
			damageStartedNanos = 0;
			gainStartedNanos = 0;
			damageActive = false;
		}

		private BarFrame update(double target, boolean animated, long now)
		{
			final double clampedTarget = clamp(target);
			if (!animated || Double.isNaN(value))
			{
				reset(clampedTarget);
				lastNanos = now;
				return new BarFrame(value, trail, false, false, gainStart, gainEnd, 0);
			}

			if (lastNanos == 0)
			{
				lastNanos = now;
			}

			final double seconds = Math.min(Math.max((now - lastNanos) / 1_000_000_000.0, 0), BAR_MAX_DELTA_SECONDS);
			lastNanos = now;

			if (Math.abs(clampedTarget - previousTarget) > BAR_ANIMATION_SNAP)
			{
				if (clampedTarget < previousTarget)
				{
					damageActive = true;
					damageStartedNanos = now;
					gainStartedNanos = 0;
					trail = Math.max(Math.max(trail, value), previousTarget);
				}
				else
				{
					damageActive = false;
					gainStartedNanos = now;
					gainStart = Math.min(value, previousTarget);
					gainEnd = clampedTarget;
					trail = Math.max(value, clampedTarget);
				}

				previousTarget = clampedTarget;
			}

			final double rate = clampedTarget < value ? BAR_DAMAGE_RATE : BAR_HEAL_RATE;
			value = approach(value, clampedTarget, seconds, rate);
			if (Math.abs(value - clampedTarget) <= BAR_ANIMATION_SNAP)
			{
				value = clampedTarget;
			}

			if (damageActive)
			{
				if (now - damageStartedNanos > DAMAGE_TRAIL_HOLD_NANOS)
				{
					trail = approach(trail, clampedTarget, seconds, BAR_TRAIL_RATE);
				}

				if (trail <= value + BAR_ANIMATION_SNAP || trail <= clampedTarget + BAR_ANIMATION_SNAP)
				{
					trail = Math.max(value, clampedTarget);
					damageActive = false;
				}
			}
			else
			{
				trail = Math.max(value, clampedTarget);
			}

			boolean showGain = false;
			double gainAlpha = 0;
			if (gainStartedNanos > 0 && gainEnd > gainStart)
			{
				final double progress = clamp((now - gainStartedNanos) / (double) GAIN_GLOW_NANOS);
				showGain = progress < 1;
				gainAlpha = Math.pow(1 - progress, 1.7);
			}

			return new BarFrame(value, trail, damageActive && trail > value + BAR_ANIMATION_SNAP,
				showGain, gainStart, gainEnd, gainAlpha);
		}
	}

	private static final class BarFrame
	{
		private final double value;
		private final double trail;
		private final boolean showTrail;
		private final boolean showGain;
		private final double gainStart;
		private final double gainEnd;
		private final double gainAlpha;

		private BarFrame(double value, double trail, boolean showTrail, boolean showGain, double gainStart,
			double gainEnd, double gainAlpha)
		{
			this.value = value;
			this.trail = trail;
			this.showTrail = showTrail;
			this.showGain = showGain;
			this.gainStart = gainStart;
			this.gainEnd = gainEnd;
			this.gainAlpha = gainAlpha;
		}
	}

	private static final class TargetHealth
	{
		private final boolean hasHealth;
		private final double value;
		private final String label;

		private TargetHealth(boolean hasHealth, double value, String label)
		{
			this.hasHealth = hasHealth;
			this.value = value;
			this.label = label;
		}
	}

	static final class PartyFrameData
	{
		private final long memberId;
		private final String name;
		private final BufferedImage avatar;
		private final boolean loggedIn;
		private final int hitpoints;
		private final int maxHitpoints;
		private final int prayer;
		private final int maxPrayer;
		private final int combatLevel;

		PartyFrameData(long memberId, String name, BufferedImage avatar, boolean loggedIn, int hitpoints,
			int maxHitpoints, int prayer, int maxPrayer, int combatLevel)
		{
			this.memberId = memberId;
			this.name = name;
			this.avatar = avatar;
			this.loggedIn = loggedIn;
			this.hitpoints = hitpoints;
			this.maxHitpoints = maxHitpoints;
			this.prayer = prayer;
			this.maxPrayer = maxPrayer;
			this.combatLevel = combatLevel;
		}
	}

	private enum BarSlot
	{
		PLAYER,
		TARGET
	}

	private enum BadgeAnchor
	{
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	}

	private enum PortraitMode
	{
		NONE,
		WIDGET,
		SOFTWARE
	}
}
