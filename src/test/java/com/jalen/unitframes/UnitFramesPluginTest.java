package com.jalen.unitframes;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class UnitFramesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(UnitFramesPlugin.class);
		RuneLite.main(args);
	}
}
