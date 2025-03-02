package mee.content;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.maps.planet.ErekirPlanetGenerator;
import mindustry.type.Planet;
import mindustry.world.meta.Attribute;
import mindustry.world.meta.Env;

public class MEEPlanets {
    public static Planet 赫尔斯特;

    public static void laod() {
        赫尔斯特 = new Planet("赫尔斯特", Planets.sun, 1, 2) {{
            meshLoader = ()->new MultiMesh(new HexMesh(this, 5));
            cloudMeshLoader = ()->new MultiMesh(
                    new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("E28654FF"), 2, 0.42f, 1, 0.43f),

                    new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("E28654FF"), 2, 0.42f, 1.2f, 0.45f));

            generator= new ErekirPlanetGenerator(){};
            visible = accessible  = true;
            alwaysUnlocked=true;
            clearSectorOnLose = true;
            tidalLock = false;
            allowLaunchSchematics=true;
            defaultAttributes.set(Attribute.heat, 0.8f);
            prebuildBase = false;
            bloom = false;
            startSector = 3;
            orbitRadius = 85;
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            atmosphereColor = lightColor = Color.valueOf("E28654FF");
            iconColor = Color.valueOf("E28654FF");
            updateLighting = false;
            lightSrcTo = 0.5f;
            allowLaunchToNumbered = false;
            lightDstFrom = 0.2f;
            defaultEnv = Env.scorching | Env.terrestrial;
            defaultCore = Blocks.coreBastion;
            hiddenItems.addAll(Items.serpuloItems).removeAll(Items.erekirItems);
        }};
    }
}
