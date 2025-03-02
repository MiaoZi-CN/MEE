package mee.world.blocks;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.content.Items;
import mindustry.entities.TargetPriority;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.*;

public class 曲镜 extends Block {
    public int shapeSides = 4;

    public float shapeRadius = 5, shapeRotateSpeed = 0.8f, range = 200, force = 140;

    public Color shapeColor = Color.valueOf("ea8878"), waveColor = shapeColor;
    //偏转范围

    public 曲镜(String name) {
        super(name);
        buildType = 曲镜Build::new;
        hasPower = true;
        solid = true;
        update = true;
        destructible = true;
        hasConsumers = true;
        envEnabled = Env.any;
        canOverdrive=false;
        group = BlockGroup.none;
        priority = TargetPriority.base;
        buildVisibility = BuildVisibility.shown;
        health = 1050;
        size = 4;
        consumePower(28);
        requirements(Category.effect, ItemStack.with(Items.tungsten, 800, Items.phaseFabric, 240, Items.surgeAlloy, 240));
    }

    @Override
    public void setStats() {
        stats.add(Stat.range, range / 8, StatUnit.blocks);
        super.setStats();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        Drawf.dashCircle(x * 8 + this.offset, y * 8 + this.offset, range, Pal.accent);
    }

    public class 曲镜Build extends Building {
        public Seq<Bullet> bullets = new Seq<>();

        public boolean shouldConsume() {
            return bullets.size > 0;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            bullets.clear();
            if (potentialEfficiency > 0) {
                Groups.bullet.intersect(this.x - range, this.y - range, range * 2, range * 2, b->{
                    if (b.team != this.team && b.type.absorbable && b.within(this, range)) {
                        bullets.add(b);
                    }
                });
                bullets.each(b->{
                    //子弹运动矢量——方向,速度 方向上加随机以提高偏转概率
                    Vec2 vec1 = new Vec2().trns(b.rotation(), b.type.speed);
                    //炮塔推力方向
                    Vec2 vec2 = new Vec2().set(b).sub(this).nor().scl(force / b.damage/2);
                    b.vel.rotateTo(vec1.add(vec2).angle(), vec1.add(vec2).len());
                });
            }
        }

        @Override
        public void draw() {
            super.draw();
            Draw.z(Layer.effect);
            Draw.color(shapeColor);
            Fill.poly(this.x, this.y, shapeSides, shapeRadius * this.potentialEfficiency, Time.time * shapeRotateSpeed);
            Draw.color();
        }

        @Override
        public void drawConfigure() {
            super.drawConfigure();
            Drawf.dashCircle(this.x, this.y, range, Pal.accent);
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            Drawf.dashCircle(this.x, this.y, range, waveColor);
        }
    }
}

