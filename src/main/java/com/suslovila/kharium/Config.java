package com.suslovila.kharium;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class Config {
    public static boolean pvpLiteEnabled = false;
    public static int arenaWorldId;
    public static int kharuDestroyingBorder;

    public static int kharuItemDestructionBorder;

    public static int maxRadiationPerTime;

    public static int maxManaReduce;


    public static boolean consumeEldritchDiaryAfterUse;
    public static String modPrefix = "§8[&bI&8] §8[§4Малышка§8] §8Gerda§f: §f";

    //examples
    public static void registerServerConfig(File modCfg) {
        Configuration cfg = new Configuration(modCfg);
        try {
            consumeEldritchDiaryAfterUse = cfg.getBoolean(
                    "consumeEldritchDiaryAfterUse",
                    "General",
                    false,
                    "Включить ограничения для PvPLite мира");

            kharuDestroyingBorder = cfg.getInt(
                    "kharuDestroyingBorder",
                    "General",
                    10_000,
                    1,
                    Integer.MAX_VALUE,
                    "the amount of kharu in air needed to stop all magic blocks and start damaging entities"
            );
            maxRadiationPerTime = cfg.getInt(
                    "radiation",
                    "General",
                    1,
                    1,
                    Integer.MAX_VALUE,
                    "the amount of kharu in air needed to stop all magic blocks and start damaging entities"
            );
            kharuItemDestructionBorder = cfg.getInt(
                    "item border",
                    "General",
                    2_000,
                    1,
                    Integer.MAX_VALUE,
                    "the amount of kharu in air needed to stop all magic blocks and start damaging entities"
            );

            maxManaReduce = cfg.getInt(
                    "Max mana reduce per second",
                    "General",
                    200_000,
                    1,
                    5_000_000,
                    "the amount of max amount of mana that is taken from items"
            );
            modPrefix = cfg.getString(
                    "ModPrefix",
                    "core", modPrefix,
                    "Префикс системных сообщений мода"
            );
        } catch (Exception var8) {
            System.out.println("пизда рулям (конфигу)");
        } finally {
            cfg.save();
        }
    }
}
