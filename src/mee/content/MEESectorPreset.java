package mee.content;

import mee.MEE;
import mindustry.content.SectorPresets;
import mindustry.maps.generators.FileMapGenerator;
import mindustry.mod.Mods;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;

import static mee.content.MEEPlanets.赫尔斯特;

public class MEESectorPreset {
    public static SectorPreset 风化裂谷, 晶石峡谷, 碳岩油田, 瘤蚀裂谷, 龙岩裂谷, 列位山区;

    public static class MeeSector extends SectorPreset {

        public MeeSector(String name, Planet planet, int sector) {
            super(name, planet, sector);
        }
    }

    public static void load() {
        风化裂谷 = new MeeSector("风化裂谷", 赫尔斯特, 3) {{
            alwaysUnlocked = true;
            difficulty = 4;
            captureWave = 0;
        }};
        晶石峡谷 = new MeeSector("晶石峡谷", 赫尔斯特, 4);
        晶石峡谷.alwaysUnlocked = false;
        晶石峡谷.difficulty = 6;
        晶石峡谷.captureWave = 50;

        碳岩油田 = new MeeSector("碳岩油田", 赫尔斯特, 5);
        碳岩油田.alwaysUnlocked = false;
        碳岩油田.difficulty = 8;
        碳岩油田.captureWave = 0;

        瘤蚀裂谷 = new MeeSector("瘤蚀裂谷", 赫尔斯特, 2);
        瘤蚀裂谷.captureWave = 35;
        瘤蚀裂谷.alwaysUnlocked = false;
        瘤蚀裂谷.difficulty = 6;

        龙岩裂谷 = new MeeSector("龙岩裂谷", 赫尔斯特, 7);
        龙岩裂谷.alwaysUnlocked = false;
        龙岩裂谷.difficulty = 9;

        列位山区 = new MeeSector("列位山区", 赫尔斯特, 10);
        列位山区.alwaysUnlocked = false;
        列位山区.captureWave = 91;
        列位山区.difficulty = 10;
    }

}


