package mee.content;

import arc.graphics.Color;
import mindustry.content.Liquids;
import mindustry.type.CellLiquid;
import mindustry.type.Liquid;

public class MEELiquids {
    public static Liquid 临界流体, 列位流体, 核废水, 液氮;

    public static void load() {
        液氮 = new CellLiquid("液氮", Color.valueOf("DDD3EAFF")) {{
            moveThroughBlocks = true;
            temperature = -1;
            heatCapacity = 0.8f;
            viscosity = 0.12f;
        }};
        核废水 = new CellLiquid("核废水", Color.valueOf("5e988d60")) {{
            moveThroughBlocks = true;
            flammability = 3;
            temperature = 1;
            heatCapacity = 4.2f;
            viscosity = 0.5f;
            explosiveness = 3;
            boilPoint = 2;
        }};
        临界流体 = new CellLiquid("临界流体", Color.valueOf("b8fff940")) {{
            moveThroughBlocks = true;
            spreadTarget = 列位流体;
            blockReactive = false;
            flammability = 0;
            temperature = -3.5f;
            heatCapacity = 0.8f;
            viscosity = 0.12f;
            explosiveness = 0;
            boilPoint = 2;
        }};
        列位流体 = new CellLiquid("列位流体", Color.valueOf("FFF3D6FF")) {{
            moveThroughBlocks = false;
            spreadTarget = Liquids.neoplasm;
            blockReactive = true;
            flammability = 0;
            temperature = -3;
            heatCapacity = 0.8f;
            viscosity = 0.9f;
            explosiveness = 0;
            boilPoint = 2;
        }};
    }
}
