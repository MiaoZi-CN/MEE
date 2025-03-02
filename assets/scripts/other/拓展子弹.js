//非常感谢 晓伟 提供的代码指导！！

//radius:吸取范围
//force:拉扯力度
//damage:伤害
//execute:处决目标阈值
//mag:舒张范围(1-mag到1+mag)
//scl:正弦函数用于控制周期的系数
exports.BlackHoleBulletType = (radius, force, damage, execute,mag,scl,height,backHeight) => {
    return extend(BasicBulletType, {
        shrinkY:0,
        collides: false,
        absorbable: false,
        hittable: false,
        reflectable: false,
        update(b) {
            this.super$update(b);
            Units.nearbyEnemies(b.team,b.x, b.y, radius, cons(unit => {
                const dst = 1 - b.dst(unit) / radius;

                unit.impulseNet(Tmp.v3.set(b).sub(unit).nor().scl(force * (1 + dst)));
                unit.damageContinuousPierce(damage * (1 + dst) / 60);
                if (unit.health / unit.maxHealth < execute) {
                    unit.kill();
                }
            }));
            Vars.indexer.allBuildings(b.x, b.y, radius, cons(build => {

                if (build.team != b.team) {
                    const dst = 1 - b.dst(build) / radius;
                    build.damageContinuousPierce(damage * (1 + dst) * b.type.buildingDamageMultiplier / 60);
                }
            }));
        },
        draw(b) {
            this.super$draw(b);
            //二次函数，用于开始的暴涨和最后的骤跌
            let m = Mathf.pow(b.time, 2) * -0.016 + 0.016 * b.lifetime * b.time;
            //正弦，用于舒张与收缩
            let n = Math.sin(b.time / 60 * scl) * mag + height;
            let bn = Math.sin(b.time / 60 * scl) * mag + backHeight;

            if (b.backRegion != null) {
                Draw.z(120);
                Draw.color(b.type.backColor);
                Draw.rect(b.backRegion, b.x, b.y, m > bn ? bn : m, m > bn ? bn : m, b.rotation());
            }
            
            Draw.z(120);
            Draw.color(b.type.frontColor);
            Draw.rect(b.frontRegion, b.x, b.y, m > n ? m : n, m > n ? m : n, b.rotation());

            Lines.stroke(2);
            Lines.circle(b.x, b.y, radius);
            Draw.reset();
            
        }
    })
}

//radius:吸取范围
//scl:伤害比例
//massScl:吸取所占质量比例
exports.DrillBulletType = (radius,scl,massScl) => {
    return extend(BasicBulletType, {
        update(b) {
            this.super$update(b);
            Units.nearbyEnemies(b.team, b.x, b.y, radius, cons(u => {
                u.impulseNet(Tmp.v3.trns(b.rotation(), b.type.speed * Math.pow(1 - b.drag, b.time / 60) * u.mass() * massScl));
                if (b.pierceArmor) u.damageContinuousPierce(b.damage * scl);
                else u.damageContinuous(b.damage * scl);
            }))
        }
    })
}

exports.LineBulletType = (radius) => {
    return extend(BulletType, {
        update(b) {
            this.super$update(b);
            let target;
            let realAimX = b.aimX < 0 ? b.x : b.aimX;
            let realAimY = b.aimY < 0 ? b.y : b.aimY;
            if (b.aimTile != null && b.aimTile.build != null && b.aimTile.build.team != b.team && b.type.collidesGround && !b.hasCollided(b.aimTile.build.id)) {
                target = b.aimTile.build;
            } else {
                target = Units.closestTarget(b.team, realAimX, realAimY, radius,
                    e => e != null && e.checkTarget(b.type.collidesAir, b.type.collidesGround) && !b.hasCollided(e.id),
                    t => t != null && b.type.collidesGround && !b.hasCollided(t.id));
            }

            if (target != null) {
                let angle = new Vec2().set(target).sub(b).angle();
                b.vel.set(new Vec2().set(target).sub(b));
                Damage.collideLine(b, b.team, b.type.hitEffect, b.x, b.y, angle, radius);
                Damage.collideLine(b, b.team, b.type.hitEffect, b.x, b.y, angle + 180, radius);
            }
        }
    })
}


exports.ConnectBulletType = (range) => {
    return extend(BasicBulletType, {
        update(b) {
            this.super$update(b);
            Groups.bullet.intersect(b.x - range, b.y - range, range * 2, range * 2, cons(bullet => {
                if (b != bullet && b.team == bullet.team && bullet.type instanceof this) {
                    let vec = new Vec2().set(bullet).sub(b);
                    Damage.collideLaser(b, vec.len(), true, true, 999);
                }
            }))
        },
        draw(b) {
            this.super$draw(b);
            Groups.bullet.intersect(b.x - range, b.y - range, range * 2, range * 2, cons(bullet => {
                if (b != bullet && b.team == bullet.team && bullet.type instanceof this) {
                    Lines.stroke(2,Pal.techBlue)
                    Lines.line(b.x,b.y,bullet.x,bullet.y);
                }
            }))
        }
    })
}

exports.SpeedUpBulletType = (range,power) => {
    return extend(BasicBulletType, {
        update(b) {
            this.super$update(b);
            let target;
            let realAimX = b.aimX < 0 ? b.x : b.aimX;
            let realAimY = b.aimY < 0 ? b.y : b.aimY;
            if (b.aimTile != null && b.aimTile.build != null && b.aimTile.build.team != b.team && b.type.collidesGround && !b.hasCollided(b.aimTile.build.id)) {
                target = b.aimTile.build;
            } else {
                target = Units.closestTarget(b.team, realAimX, realAimY, range,
                    e => e != null && e.checkTarget(b.type.collidesAir, b.type.collidesGround) && !b.hasCollided(e.id),
                    t => t != null && b.type.collidesGround && !b.hasCollided(t.id));
            }
            if (target != null && b.dst(b.owner) < 480) {
                b.vel.trns(Angles.moveToward(b.rotation(), b.angleTo(target),power),b.type.speed);
                b.lifetime += Time.delta;
            }
        }
    })
}
exports.FloatingBulletType = (spawn) => {
    return extend(BasicBulletType, {
        update(b) {
            this.super$update(b);
            let aim = Vars.world.build(b.tileX(), b.tileY());
            if (aim != null || b.blockOn() instanceof StaticWall || b.floorOn().isLiquid) b.lifetime += Time.delta;
        },
        despawned(b) {
            if (b === undefined) return;
            this.super$despawned(b);
            if (Vars.world.build(b.tileX(), b.tileY()) == null && spawn != null) {
                Vars.world.tile(b.tileX(), b.tileY()).setBlock(spawn,b.team);
                if (Vars.world.build(b.tileX(), b.tileY()) == null) this.create(b, b.originX, b.originY, b.rotation() + Math.random() * 180 + 90);
            }

        }
    })
}
exports.RepairOwnBulletType = (percent) => {
    return extend(BasicBulletType, {
        handlePierce(b, initialHealth, x, y) {
            this.super$handlePierce(b, initialHealth, x, y);

            let h = b.owner;
            if (h instanceof Unit) {
                h.heal((h.maxHealth - h.health) * percent);
            }
        }
    })
}
exports.ResonantBulletType = (range, percent) => {
    return extend(BasicBulletType, {
        handlePierce(b, initialHealth, x, y) {
            this.super$handlePierce(b, initialHealth, x, y);

            Units.nearbyEnemies(b.team,x,y,cons(u => {
              Damage.damage(b.team, x, y, range , u.maxHealth * percent, false, b.type.collidesAir, b.type.collidesGround, 1, b);
            }))
        }
    })
}
exports.FocusBulletType = (radius,per,min) => {
    return extend(BasicBulletType, {
        init(b) {
            if (b === undefined) return;
            this.super$init();
            this.super$init(b);
            let sum = 0;
            Units.nearby(b.team, b.x, b.y, radius, cons(u => {
                sum += Math.min(u.maxHealth * per,min);
                u.damagePierce(u.maxHealth * per * 0.5);
            }));
            Units.nearbyEnemies(b.team, b.x, b.y, radius, cons(u => {
                sum += Math.min(u.maxHealth * per * 1.5, min);
                u.damagePierce(Math.max(u.maxHealth * per * 1.5, min));
            }));
            Vars.indexer.allBuildings(b.x, b.y, radius, cons(build => {
                if(build.team != b.team){
                  sum += Math.min(build.maxHealth * per, min);
                  build.damagePierce(Math.max(build.maxHealth * per, min));
                }
            }));
            b.damage = sum * 1.15;
            if (b.owner instanceof Healthc){
              b.owner.heal(sum*0.6);
            }
        }
    })
}
