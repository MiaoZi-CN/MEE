package mee.content;

import mee.world.unitType.蛇;
import mindustry.content.UnitTypes;
import mindustry.type.UnitType;
import mindustry.type.unit.ErekirUnitType;

public class MEEUnitTypes {
    public static ErekirUnitType 悲悯, 饕餮;

    public static void load() {
        饕餮 = new 蛇().load("饕餮");
        {
            ((蛇.蛇head) 饕餮).lengthSnake = 10;
        }
        悲悯 = new ErekirUnitType("悲悯") {{
            constructor = ()->UnitTypes.disrupt.constructor.get();
        }};
    }
}
