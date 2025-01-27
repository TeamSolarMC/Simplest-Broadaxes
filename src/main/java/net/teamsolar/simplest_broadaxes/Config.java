package net.teamsolar.simplest_broadaxes;

import eu.midnightdust.lib.config.MidnightConfig;

public class Config extends MidnightConfig {
    public static final String TEXT = "text";
    public static final String NUMBERS = "numbers";
    public static final String SLIDERS = "sliders";
    public static final String LISTS = "lists";
    public static final String FILES = "files";

    @Entry public static int broadaxeBlocksPerTick = 64;
    @Entry public static int broadaxeBlocksPerSwing = 1000;
    @Entry public static boolean dropsFelledBlocks = true;
}
