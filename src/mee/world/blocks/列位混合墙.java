package mee.world.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mee.world.stat.MEEStat;
import mindustry.gen.Bullet;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.defense.Wall;

import static mee.world.stat.MEEStatCat.count;

public class 列位混合墙 extends Wall {
    public float addMaxMaxHeath = 20000;
    public float addMaxHeath = 1000;
    public float addArmor = 1;
    public float addMaxArmor = 50;
    public float addHeath = 0.1f;
    /** 蓄势 */
    public float countMax = 20;

    public 列位混合墙(String name) {
        super(name);
        update = true;
        buildType = 列位混合墙Build::new;
    }


    @Override
    public void setBars() {
        addBar("armor", (列位混合墙Build b)->new Bar(()->"护甲" + b.armor, ()->Pal.ammo, ()->1f));
        addBar("count", (列位混合墙Build b)->(new Bar(()->Core.bundle.get("bar.count") + ":" + b.count,

                ()->Color.valueOf("FFFFFF"), b::cuntf)));
        super.setBars();
    }

    @Override
    public void setStats() {
        stats.add(new MEEStat("addArmor", count), addArmor);
        stats.add(new MEEStat("addMaxHeath", count), addMaxHeath);
        stats.add(new MEEStat("maxHeath", count), addMaxMaxHeath);
        stats.addPercent(new MEEStat("countRecover", count), 0.1f);
        super.setStats();
    }

    public class 列位混合墙Build extends WallBuild {
        public float armor = 0;

        public float count = 0f;

        public float cuntf() {
            return count / countMax;
        }

        @Override
        public void updateTile() {
          if (health>maxHealth){
              health=maxHealth;
          }
        }

        @Override
        public boolean collision(Bullet bullet) {
            bullet.damage -= armor;
            if (count >= countMax) {
                xu();
                count = 0;
            } else {
                count++;
            }
            return super.collision(bullet);
        }

        public void xu() {
            if (armor < addMaxArmor) {
                armor += addArmor;
            } else {
                armor = addMaxArmor;
            }

            if (health >= maxHealth) {
                health = maxHealth;
            } else {
                health += maxHealth * addHeath;
            }
            if (maxHealth >= addMaxMaxHeath) {
                maxHealth = addMaxMaxHeath;
            } else {
                maxHealth += addMaxHeath;
            }

        }

        @Override
        public void write(Writes write) {
            write.f(armor);
            write.f(count);
            write.f(maxHealth);
            write.f(health);
            super.write(write);
        }

        @Override
        public void read(Reads read, byte revision) {
            armor = read.f();
            count = read.f();
            maxHealth = read.f();
            health = read.f();
            super.read(read, revision);
        }
    }

}
