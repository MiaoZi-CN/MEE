package mee.world.blocks;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import mee.world.stat.MEEStat;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Groups;
import mindustry.graphics.Drawf;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.meta.Stat;

public class 石墨防御墙 extends Wall {
    public float 电击D = 1;
    public float 熔岩 = 0;
    public float rangle = 1;

    public 石墨防御墙(String name) {
        super(name);
        consumesPower = false;
        outputsPower = true;
        hasPower = true;
        buildType = 石墨防御墙Build::new;
        update = true;
        consumePowerBuffered(10);
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("power");
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(MEEStat.自爆范围, rangle);
        stats.add(MEEStat.自爆状态, (table)->{
            extracted(StatusEffects.electrified, table, 5);
            table.add(" 通电时:");
            extracted(StatusEffects.shocked, table, 3);
            extracted(StatusEffects.melting, table, 熔岩);
        });
        stats.add(Stat.damage, 电击D);
    }

    private void extracted(UnlockableContent e, Table table, float time) {
        table.image(e.uiIcon).size(3 * 8).padRight(4).right().scaling(Scaling.fit).top();
        table.add(e.localizedName).padRight(10).left().top();
        table.add(time + " 秒");
    }

    public class 石墨防御墙Build extends WallBuild {
        @Override
        public void killed() {
            Groups.unit.intersect(x - rangle, y - rangle, 8 * rangle * 2, 8 * rangle * 2, (u)->{
                if (u.team != team) {
                    u.damage(电击D);
                    if (power.graph.getPowerBalance() > 0) {
                        u.apply(StatusEffects.shocked, 3 * 60);
                        u.apply(StatusEffects.melting, 熔岩 * 60);
                    }
                    u.apply(StatusEffects.electrified, 5 * 60);
                }
            });
            Fx.smokeCloud.at(x, y, Color.valueOf("7C8DBA"));
            super.killed();
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            Draw.color(Color.red);
            Drawf.dashSquareBasic(x, y, rangle * 8);
            Draw.color();
        }
    }
}
