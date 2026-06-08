package com.jalen.unitframes;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("unitframes")
public interface UnitFramesConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "showPlayerFrame",
		name = "Show player frame",
		description = "Show your player Hitpoints frame"
	)
	default boolean showPlayerFrame()
	{
		return true;
	}

	@ConfigItem(
		position = 1,
		keyName = "showTargetFrame",
		name = "Show target frame",
		description = "Show your current target while you are interacting with one"
	)
	default boolean showTargetFrame()
	{
		return true;
	}

	@ConfigItem(
		position = 2,
		keyName = "showNumbers",
		name = "Show numbers",
		description = "Show numeric Hitpoints values on the bars"
	)
	default boolean showNumbers()
	{
		return true;
	}

	@ConfigItem(
		position = 3,
		keyName = "animateBarChanges",
		name = "Animate bars",
		description = "Smoothly animate Hitpoints and Prayer bar changes"
	)
	default boolean animateBarChanges()
	{
		return true;
	}

	@ConfigItem(
		position = 4,
		keyName = "showHoverRestores",
		name = "Show restore previews",
		description = "Preview Hitpoints and Prayer restored by hovered inventory food and potions"
	)
	default boolean showHoverRestores()
	{
		return true;
	}

	@ConfigItem(
		position = 5,
		keyName = "showLivePortraits",
		name = "Show portraits",
		description = "Draw stable player and NPC portrait models when available"
	)
	default boolean showLivePortraits()
	{
		return true;
	}

	@Range(
		min = 0,
		max = 2047
	)
	@ConfigItem(
		position = 6,
		keyName = "animatePlayerPortrait",
		name = "Animate player portrait",
		description = "Use the game's neutral chat-head idle animation on your portrait"
	)
	default boolean animatePlayerPortrait()
	{
		return true;
	}

	@Range(
		min = 0,
		max = 2047
	)
	@ConfigItem(
		position = 7,
		keyName = "playerPortraitRotation",
		name = "Player head rotation",
		description = "Rotate the local player portrait model"
	)
	default int playerPortraitRotation()
	{
		return 1024;
	}

	@Range(
		min = 0,
		max = 2047
	)
	@ConfigItem(
		position = 8,
		keyName = "targetPortraitRotation",
		name = "Target head rotation",
		description = "Rotate the target portrait model"
	)
	default int targetPortraitRotation()
	{
		return 1024;
	}

	@ConfigItem(
		position = 9,
		keyName = "showSoftwareTargetPortraits",
		name = "Enemy model portraits",
		description = "Draw a software-rendered model portrait for enemies without chatheads"
	)
	default boolean showSoftwareTargetPortraits()
	{
		return true;
	}

	@Range(
		min = 1,
		max = 60
	)
	@ConfigItem(
		position = 10,
		keyName = "enemyHeadPortraits",
		name = "Enemy head portraits",
		description = "Crop no-chathead enemy model portraits toward the head instead of fitting the full model"
	)
	default boolean enemyHeadPortraits()
	{
		return true;
	}

	@Range(
		min = 1,
		max = 60
	)
	@ConfigItem(
		position = 11,
		keyName = "targetSoftwarePortraitFps",
		name = "Enemy portrait FPS",
		description = "Maximum update rate for software-rendered enemy model portraits"
	)
	default int targetSoftwarePortraitFps()
	{
		return 15;
	}

	@Range(
		min = 60,
		max = 180
	)
	@ConfigItem(
		position = 12,
		keyName = "targetSoftwarePortraitZoom",
		name = "Enemy model zoom",
		description = "Zoom no-chathead enemy model portraits"
	)
	default int targetSoftwarePortraitZoom()
	{
		return 100;
	}

	@ConfigItem(
		position = 13,
		keyName = "lowQualitySoftwareTargetPortraits",
		name = "Low quality enemies",
		description = "Draw fewer model faces for no-chathead enemy portraits"
	)
	default boolean lowQualitySoftwareTargetPortraits()
	{
		return false;
	}

	@ConfigItem(
		position = 14,
		keyName = "showPartyFrames",
		name = "Show party frames",
		description = "Show compact RuneLite party member frames under your player frame"
	)
	default boolean showPartyFrames()
	{
		return true;
	}

	@Range(
		min = 1,
		max = 10
	)
	@ConfigItem(
		position = 15,
		keyName = "maxPartyFrames",
		name = "Max party frames",
		description = "Maximum number of party member frames to show"
	)
	default int maxPartyFrames()
	{
		return 5;
	}

	@ConfigItem(
		position = 16,
		keyName = "hideWhenLoggedOut",
		name = "Hide when logged out",
		description = "Hide the HUD while you are not logged in"
	)
	default boolean hideWhenLoggedOut()
	{
		return true;
	}
}
