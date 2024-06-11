package com.suslovila.kharium.utils.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    public static int kharuDestroyingBorder;

    public static int kharuItemDestructionBorder;

    public static int maxRadiationPerTime;

    public static int maxManaReduce;

    public static boolean consumeEldritchDiaryAfterUse;

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
        } catch (Exception var8) {
            System.out.println("пизда рулям (конфигу)");
        } finally {
            cfg.save();
        }
    }
}
