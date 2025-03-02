package mee.content;

import arc.graphics.Color;
import mindustry.content.Items;
import mindustry.type.Item;

public class MEEItems {
    public static Item 列位石, 泰特希合金, 芳油污;

    public static void load() {
        Items.fissileMatter.hidden = false;
        列位石 = new Item("列位石", Color.valueOf("FFF3D6FF")) {{
            hardness = 20;
            cost = 6;
            radioactivity = 1.2f;
        }};
        泰特希合金 = new Item("泰特希合金", Color.valueOf("DC0000FF")) {{
            hardness = 6;
            radioactivity = 1;
            explosiveness = 0.5f;
            flammability = 0.3f;
        }};
        芳油污 = new Item("芳油污", Color.valueOf("92DD7EFF")) {{
            details = "没用的都是有用的";
            hardness = 0;
            cost = 0;
            radioactivity = 0;
            explosiveness = 0;
            flammability = 0;
        }};
    }
}
