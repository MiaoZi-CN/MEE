package mee.content;

import arc.graphics.Color;
import arc.math.Interp;
import mee.type.MEEStatusEffect;
import mee.world.stat.MEEStat;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;
import mindustry.world.meta.Stat;

public class MEEStatus {
    public static StatusEffect 破甲, 裂解, 瘤恢复, 酸蚀, 速攻, 瘤增强, 血色环计时器;

    public static void load() {
        血色环计时器 = new MEEStatusEffect("血色环计时器") {{
            damage = 10;
            effectChance = 0;
            show = false;
        }};
        瘤增强 = new MEEStatusEffect("瘤增强") {{
            speedMultiplier = 2;
            reloadMultiplier = 2;
            effectChance = 1;
            effect = new ParticleEffect() {{
                particles = 30;
                lifetime = 50;
                sizeFrom = 0;
                sizeTo = 4;
                length = 2;
                colorFrom = Color.valueOf("9E172CFF");
                colorTo = Color.valueOf("9E172CFF");
            }};
        }};
        速攻 = new MEEStatusEffect("速攻") {{
            damageMultiplier = 0.5f;
            healthMultiplier = 2;
            speedMultiplier = 2;
            reloadMultiplier = 4;
            damage = -2;
            permanent = false;
            reactive = false;
            color = Color.valueOf("FF0000");
            effectChance = 1;
        }};
        酸蚀 = new MEEStatusEffect("酸蚀") {{
            damage = 0.6f;
            speedMultiplier = 0.6f;
            effectChance = 1;
            effect = new ParticleEffect() {{
                particles = 1;
                lifetime = 50;
                sizeFrom = 3;
                sizeTo = 0;
                baseLength = 0;
                length = 20;
                colorFrom = Color.valueOf("59c2a200");
                colorTo = Color.valueOf("59c2a2");
            }};
        }};
        瘤恢复 = new MEEStatusEffect("瘤恢复") {{
            damage = -10;
            effectChance = 1;
            effect = new ParticleEffect() {{
                particles = 30;
                lifetime = 50;
                sizeFrom = 0;
                sizeTo = 4;
                length = 2;
                colorFrom = Color.valueOf("9E172CFF");
                colorTo = Color.valueOf("9E172C00");
            }};
        }};
        破甲 = new MEEStatusEffect("破甲") {
            {
                effectChance = 0;
            }

            @Override
            public void setStats() {
                stats.add(MEEStat.armorBreak, 0.005 * 60 + "/s");
                super.setStats();
            }

            @Override
            public void update(Unit unit, float time) {
                if (unit.armor > 0) {
                    unit.armor -= 0.005f;
                }
                super.update(unit, time);
            }
        };
        裂解 = new MEEStatusEffect("裂解") {
            @Override
            public void setStats() {
                stats.add(Stat.damage, 2.4f + "%/s");
                super.setStats();
            }

            @Override
            public void update(Unit unit, float time) {
                float v = unit.maxHealth * 0.024f / 60;
                if (unit.health > v) {
                    unit.health -= v;
                }
                super.update(unit, time);
            }

            {
                color = Color.valueOf("8bbeb0");
                healthMultiplier = 0.8f;
                speedMultiplier = 0.8f;
                effectChance = 0.1f;
                effect = new MultiEffect(new ParticleEffect() {{
                    interp = Interp.circleOut;
                    sizeInterp = Interp.pow10Out;
                    line = true;
                    cone = 360;
                    baseLength = 2;
                    length = 24;
                    lenFrom = 12;
                    lenTo = 8;
                    strokeFrom = 2;
                    strokeTo = 0;
                    colorFrom = Color.valueOf("8bbeb0");
                    colorTo = Color.valueOf("8bbeb0");
                }}, new ParticleEffect() {{
                    interp = Interp.circleOut;
                    sizeInterp = Interp.pow10Out;
                    baseLength = 0;
                    length = 0;
                    sizeFrom = 8;
                    sizeTo = 0;
                    colorFrom = Color.valueOf("8bbeb0");
                    colorTo = Color.valueOf("8bbeb0");
                }});
            }
        };
    }
}
