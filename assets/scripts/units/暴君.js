const bulletType = require("other/拓展子弹");

const 暴君 = new ErekirUnitType("暴君");
暴君.constructor = prov(() => extend(UnitTypes.conquer.constructor.get().class, {}));

let 融合子弹 = Object.assign(bulletType.
FocusBulletType(480,0.10,300), {
    sprite: "埃里克尔更多设备-抹除子弹",
    height: 72,
    width: 24,
    trailColor: Color.valueOf("ffdfb0"),
    trailLength: 16,
    trailWidth: 4,

    speed: 12,
    lifetime: 60,
    reflectable: false,
    absorbable:false,

    damage: 0,
    pierce: true,
    pierceArmor: true,
    pierceBuilding: true,
    shootEffect: Object.assign(new WaveEffect(), {
        lifetime: 20,
        sizeFrom: 480,
        sizeTo: 80,
        strokeFrom: 4,
        strokeTo: 0,
        colorFrom: Color.valueOf("ffdfb0"),
        colorTo: Color.valueOf("ffdfb0"),
    }),
    smokeEffect: Object.assign(new ParticleEffect(), {
        lifetime: 60,
        cone: 8,
        particles:12,
        sizeFrom: 12,
        sizeTo: 8,
        baseLength: 0,
        length:160,
        colorFrom: Color.valueOf("ffdfb0"),
        colorTo: Color.valueOf("ffdfb000"),
    }),
    hitEffect: Object.assign(new ParticleEffect(), {
        lifetime: 30,
        line: true,
        particles: 8,
        cone: 36,

        baseLength: 0,
        length: 40,
        strokeFrom: 2,
        strokeTo: 0,
        lenFrom: 12,
        lenTo: 10,
        colorFrom: Color.valueOf("ffdfb0"),
        colorTo: Color.valueOf("ffdfb0"),
    })
})
const 暴君主武 = extend(Weapon, "埃里克尔更多设备-暴君武器",{
    mirror: false,
    rotate: true,
    rotationLimit: 30,
    rotateSpeed: 1.6,
    x: 0,
    y: 0,
    shootY:24,
    reload: 480,
    shake:42,
    bullet: 融合子弹,
    shoot: Object.assign(new ShootBarrel(), {
        barrels: [
            -10, 0, 0,
            10,0,0
        ]
    })
    //shootSound:"暴君主炮-shoot"
})
暴君主武.parts.add(
    Object.assign(new ShapePart(), {
        circle: true,
        hollow: true,
        layer:110,
        radius: 480,
        radiusTo: 4,
        stroke: 4,
        strokeTo:0,
        color: Color.valueOf("ffdfb0"),
        colorTo: Color.valueOf("ffdfb0"),
        progress:DrawPart.PartProgress.reload
    })
);

let 修复子弹 = Object.assign(bulletType.RepairOwnBulletType(0.0052), {
    sprite: "埃里克尔更多设备-抹除子弹",
    height: 18,
    width: 10,
    trailColor: Color.valueOf("ffdfb0"),
    trailLength: 6,
    trailWidth: 2.4,

    speed: 12,
    lifetime: 40,
    reflectable: false,

    damage: 80,
    pierce: true,
    pierceCap:4,
    pierceArmor: true,
    pierceBuilding:true,
})
const 暴君副武 = extend(Weapon, "埃里克尔更多设备-暴君副炮",{
    rotate:true,
    reload: 10,
    x: 20,
    y:32,
    rotateSpeed:2,
    bullet:修复子弹
})

暴君.weapons.add(暴君主武);
暴君.weapons.add(暴君副武);
exports.暴君 = 暴君;