package mee.world.bulletType;

import arc.graphics.Color;
import arc.math.Interp;
import arc.math.geom.Vec2;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.effect.WaveEffect;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.graphics.Pal;

public class 磁力子弹Type extends BasicBulletType {
    public 磁力子弹Type(float range) {
        this.range = range;
    }

    {
        sprite = "circle";
        damage = 50;
        lifetime = 600;
        speed = 1.2f;
        shrinkY = 0;
        collides = false;
        absorbable = false;
        frontColor = Color.valueOf("fcf387");
        backColor = Color.valueOf("fcf387");
        trailColor = Pal.surge;
        trailWidth = 1;
        trailLength = 12;
        trailChance = 1;
        trailEffect = new ParticleEffect() {{
            lifetime = 50;
            cone = 360;
            particles = 1;
            line = true;
            baseLength = 0;
            length = 8;
            strokeFrom = 0.8f;
            strokeTo = 0;
            lenFrom = 5;
            lenTo = 3;
            colorFrom = Pal.bulletYellow;
            colorTo = Pal.bulletYellow;
        }};
        hitEffect = new ParticleEffect() {{
            lifetime = 20;
            cone = 360;
            particles = 8;
            baseLength = 0;
            length = 20;
            sizeFrom = 6;
            sizeTo = 2;
            colorFrom = Pal.bulletYellow;
            colorTo = Pal.bulletYellow;
        }};
        chargeEffect = new MultiEffect(new WaveEffect() {{
            lifetime = 80;
            sizeFrom = 40;
            sizeTo = 3;
            strokeFrom = 0;
            strokeTo = 2;
            colorFrom = Color.valueOf("fcf387");
            colorTo = Color.valueOf("fcf387");
            interp = Interp.pow3Out;
        }}, new ParticleEffect() {{
            lifetime = 80;
            particles = 1;
            baseLength = 0;
            length = 0;
            sizeFrom = 0;
            sizeTo = 5;
            colorFrom = Color.valueOf("fcf387");
            colorTo = Color.valueOf("fcf387");
            interp = Interp.pow3In;
        }}, new ParticleEffect() {{
            line = true;
            lifetime = 80;
            particles = 12;
            baseLength = 40;
            length = -38;
            lenFrom = 2;
            lenTo = 4;
            strokeFrom = 0.2f;
            strokeTo = 1.2f;
            colorFrom = Color.valueOf("fcf387");
            colorTo = Color.valueOf("fcf387");
            interp = Interp.pow3In;
        }});
    }


    float range;

    @Override
    public void update(Bullet b) {
        super.update(b);
        Groups.bullet.intersect(b.x - range, b.y - range, range * 2, range * 2, bullet->{
            if (b != bullet) {
                //偏转目标的速度矢量
                Vec2 vec1 = new Vec2().trns(bullet.rotation(), bullet.type.speed);
                //该子弹的偏转矢量
                Vec2 vec2 = new Vec2().set(b).sub(bullet).nor().scl(bullet.type.speed * 2 * b.dst(bullet) / range);
                if (bullet.type instanceof 磁力子弹Type) {
                    bullet.vel.rotateTo(vec1.add(vec2).angle() + 180, vec1.add(vec2).len());
                } else {
                    bullet.vel.rotateTo(vec1.add(vec2).angle(), vec1.add(vec2).len());
                }
                bullet.time = 0;
            }
        });
    }
}