package mee.world.blocks;

import mee.world.bulletType.磁力子弹Type;
import mindustry.world.blocks.defense.turrets.PowerTurret;

public class 磁涡 extends PowerTurret {
    public 磁涡(String name) {
        super(name);
        shootType = new 磁力子弹Type(160);
    }
}
