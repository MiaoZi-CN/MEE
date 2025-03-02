package mee.content;

import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import mee.world.blocks.*;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.pattern.ShootPattern;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;

import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;
import static mee.content.MEEItems.列位石;
import static mee.content.MEEItems.泰特希合金;
import static mindustry.content.Fx.*;

public class MEEBlocks {
    public static Block 公理, 列位混合墙, 液体卸载器, 曲镜, 磁涡, 石墨防御墙, 石墨防御墙Big;

    public static void load() {
        公理 = new ItemTurret("公理") {{
            shootY = 16;
            recoil=4f;
            ammo(Items.tungsten, new BasicBulletType() {{


            }});
            shootEffect = new Effect(48, (e)->{
                color(Color.valueOf("A0B0C8FF"));
                rand.setSeed(e.id);
                for (int i = 0; i < 13; i++) {
                    float rot = e.rotation + rand.range(26f);
                    v.trns(rot, rand.random(e.finpow() * 30f));
                    Fill.poly(e.x + v.x, e.y + v.y, 4, e.fout() * 4f + 0.2f, rand.random(360f));
                }
            });
        }};
        石墨防御墙 = new 石墨防御墙("石墨墙") {{
            envEnabled = Env.any;
            health = 250;
            熔岩 = 2;
            电击D = 40;
            size = 1;
            rangle = 3;
            requirements(Category.defense, ItemStack.with(Items.graphite, 12));
        }};
        石墨防御墙Big = new 石墨防御墙("大型石墨墙") {{
            envEnabled = Env.any;
            熔岩 = 4;
            health = 1000;
            电击D = 80;
            size = 2;
            rangle = 6;
            requirements(Category.defense, ItemStack.with(Items.graphite, 48));
        }};
        磁涡 = new 磁涡("磁涡");
        曲镜 = new 曲镜("曲镜") {{
        }};
        液体卸载器 = new 液体卸载器("液体卸载器") {{
            description = "单侧卸载液体并单侧输出,速度较快,无法从管道卸载.";
            health = 680;
            canOverdrive = false;
            requirements(Category.liquid, ItemStack.with(Items.silicon, 20, Items.tungsten, 20));
        }};
        列位混合墙 = new 列位混合墙("列位混合墙") {{
            armor = 10;
            health = 3000;
            size = 2;
            group = BlockGroup.walls;
            flashHit = true;
            requirements(Category.defense, ItemStack.with(泰特希合金, 80, 列位石, 150));
        }};
    }
}
