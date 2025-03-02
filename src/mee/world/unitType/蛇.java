package mee.world.unitType;

import arc.func.Func;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.geom.Vec2;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mee.content.MEEFx;
import mee.content.MEESounds;
import mee.entity.MEERegister;
import mindustry.ai.types.GroundAI;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.entities.Puddles;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.units.AIController;
import mindustry.entities.units.UnitController;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.type.Weapon;
import mindustry.type.unit.ErekirUnitType;

import static mindustry.Vars.world;

@SuppressWarnings("unchecked")
public class 蛇 {
    public static 蛇body body;
    public static 蛇end end;
    public static 蛇head head;


    public 蛇head load(String name) {
        //创建头部 体节 和尾巴
        //实际上尾巴和体积是同一个类型 我为了让尾巴贴图不一样所以新写了个类型
        body = new 蛇body(name + "-body");
        end = new 蛇end(name + "-end");
        head = new 蛇head(name);
        //音效
        body.deathSound = end.deathSound = head.deathSound = Sounds.plantBreak;
        //关闭死亡飞行单位尸体
        body.createWreck = end.createWreck = head.createWreck = false;
        //关闭死亡燃烧痕迹
        body.createScorch = end.createScorch = head.createScorch = false;
        //统一速度 方便链接
        end.speed = body.speed = head.speed;
        return head;
    }

    public class 蛇body extends ErekirUnitType {

        public 蛇body(String name) {
            super(name);
            hitSize = 20;
            deathExplosionEffect = Fx.none;
            //声明实体
            constructor = (Prov<? extends Unit>) MEERegister.map.get("饕餮-body");
            //ai控制器
            controller = (Func<Unit, UnitController>) param -> new 蛇LegsUnitAI();
            logicControllable = false;//不应当被逻辑控制
            playerControllable = false;//不应当被玩家控制
            rotateMoveFirst = false;
            baseRotateSpeed = 10;
            //体节不应该被单位上线控制 应由头部控制
            useUnitCap = false;
            //你攻击时不应该看着目标  因为你应该永远看着你前面的腿 武器自己可以旋转以攻击对象
            faceTarget = false;
            //体节武器
            weapons.add(new Weapon("mee-" + "饕餮-wea") {{
                x -= 8;
                mirror = false;
                rotate = true;
                reload = 30f;
                bullet = new BasicBulletType(6, 34, "mee-呕吐子弹") {
                    @Override
                    public void hit(Bullet b, float x, float y) {
                        Puddles.deposit(world.tileWorld(x, y), Liquids.neoplasm, 20);
                        super.hit(b, x, y);
                    }

                    {
                        smokeEffect = MEEFx.shootSmallSmoke;
                        shootEffect = MEEFx.colorSpark;
                        hitEffect = MEEFx.hitLiquid;
                        shootSound = MEESounds.瘤液喷吐;
                    }
                };
            }});
        }
    }

    public class 蛇end extends ErekirUnitType {
        //尾巴属性
        public 蛇end(String name) {
            super(name);
            deathExplosionEffect = Fx.none;
            hitSize = 20;
            constructor = EntityMapping.nameMap.get(name);
            controller = (Func<Unit, UnitController>) param -> new 蛇LegsUnitAI();
            logicControllable = false;//不应当被逻辑控制
            playerControllable = false;//不应当被玩家控制
            rotateMoveFirst = true;
            baseRotateSpeed = 10;
            useUnitCap = false;
        }
    }

    public static class 蛇head extends ErekirUnitType {

        public int lengthSnake = 5;

        public 蛇head(String name) {
            super(name);
            deathExplosionEffect = Fx.none;
            constructor = EntityMapping.nameMap.get(name);
            flying = false;
            controller = (Func<Unit, UnitController>) param -> new GroundAI();
            omniMovement = false;// 它不应该能向每个方向移动  因为它是头部所以他不能倒退
            targetAir = false;//头部没有武器所以不能攻击
            targetGround = false;//头部没有武器所以不能攻击
            canAttack = false;//没有武器
            rotateMoveFirst = true;//头部旋转时应该面朝其方向
            baseRotateSpeed = 0;//无意义  无需在意
            rotateSpeed = 2f;//头部旋转速度 此值应该尽可能的低 如果旋转快了它会面朝自己的体节
            hitSize = 20;
            speed = 1.5f;//速度应当慢 快了会导致体节不协调
            health = 12000;
        }

    }

    public static class 蛇LegsUnit extends LegsUnit {
        //所有腿都需要头部
        public 蛇headLegsUnit head;
        //退出游戏保存头部ID
        public int headid = -1;
        //他前面是哪个腿
        public 蛇LegsUnit parent;
        //前面腿 的id
        public int parentid = -1;
        //他后面跟哪个腿
        public 蛇LegsUnit child;
        //后面腿id
        public int childid = -1;

        //清除wet状态,对你没有意义 无需深究
        @Override
        public void clearStatuses() {
            statuses.each((s) -> {
                if (s.effect == StatusEffects.wet) {
                    statuses.remove(s);
                }
            });
        }

        //读取id
        @Override
        public void read(Reads read) {
            super.read(read);
            headid = read.i();
            parentid = read.i();
            childid = read.i();
        }

        //写入id
        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(head == null ? -1 : head.id);
            write.i(parent == null ? -1 : parent.id);
            write.i(child == null ? -1 : child.id);
        }

        public 蛇LegsUnit() {
        }

        //类id 用于实体生成
        @Override
        public int classId() {
            return MEERegister.getId(this.getClass());
        }

        //载入世界完成后用ID查找对象,并绑定
        @Override
        public void update() {
            if (headid != -1) {
                head = (蛇headLegsUnit) Groups.unit.getByID(headid);
            }
            if (parentid != -1) {
                parent = (蛇LegsUnit) Groups.unit.getByID(parentid);
            }
            if (childid != -1) {
                child = (蛇LegsUnit) Groups.unit.getByID(childid);
            }
            super.update();
            clearStatuses();
        }

        //死亡生成瘤液
        @Override
        public void kill() {
            Puddles.deposit(tileOn(), Liquids.neoplasm, hitSize * 50);
            super.kill();
        }
    }

    //腿 和头的ai控制器
    public class 蛇LegsUnitAI extends AIController {
        @Override
        public void updateMovement() {
            蛇LegsUnit unit1 = (蛇LegsUnit) unit;
            //他前面有腿那就跟着腿走
            if (unit1.parent != null) {
                Vec2 c = new Vec2(unit1.x, unit1.y);
                Vec2 p = new Vec2(unit1.parent.x, unit1.parent.y);
                //计算向量
                p.sub(c);
                //瞬移 一般用于被方块卡住位置拉扯距离大于2.5格 进行传送
                if (Math.abs(p.x) > 25 || Math.abs(p.y) > 25) {
                    unit1.x += p.x;
                    unit1.y += p.y;
                }
                //该腿永远面朝前面的腿
                unit.lookAt(unit1.parent);
                //向前面的腿移动  unit1.hitSize()是间隔  防止实体挤压碰撞
                moveTo(unit1.parent, unit1.hitSize(), 0);
            } else if (unit1.head != null) {
                //他前面有头 说明前面没有腿了那就跟着头走
                Vec2 c = new Vec2(unit1.x, unit1.y);
                Vec2 p = new Vec2(unit1.head.x, unit1.head.y);
                //计算头部和当前位置的向量
                p.sub(c);
                if (Math.abs(p.x) > 25 || Math.abs(p.y) > 25) {
                    unit1.x += p.x;
                    unit1.y += p.y;
                }
                //和上面同理 只不过换成头了
                unit.lookAt(unit1.head);
                moveTo(unit1.head, unit1.hitSize(), 0);
            }
        }
    }

    //头部的实体
    public static class 蛇headLegsUnit extends LegsUnit {
        //蛇的所有腿和尾巴 由头部生成
        //该值记录这个头部是否已经生成过腿和尾巴 用于初始化
        boolean child = false;
        //头后边跟着腿
        蛇LegsUnit child1;
        //腿 的实体id用于载入游戏绑定
        int child1ID = -1;
        //他有多少个腿 初始化便会生成这个数量的腿
        int childs = 0;
        //腿部生成偏移 总不能让一堆腿生成在一个点上吧 每生成一个腿往后偏移此值 游戏内显示为一竖列
        float offy;
        //哦这个单位有一个 秒杀头部前方单位的能力 这个向量是用来计算头部前方的一片区域的位置
        Vec2 xy = new Vec2(0, 16);

        //类 id保存 用于实体生成
        @Override
        public int classId() {
            return MEERegister.getId(this.getClass());
        }
        //载入游戏读取 id
        @Override
        public void read(Reads read) {
            super.read(read);
            child1ID = read.i();
            child = read.bool();
        }
        //退出游戏保存id
        //当然是否生成过腿和尾部也会被保留  总不能让它重新载入游戏再次生成一次吧
        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(child1 == null ? -1 : child1.id);
            write.bool(child);
        }
        //死亡生成液体
        @Override
        public void kill() {
            Puddles.deposit(tileOn(), Liquids.neoplasm, hitSize * 50);
            super.kill();
        }
        //秒杀头部单位能力 秒杀后 所有体节获取生命 因为要计算血量所以要传入被击杀的单位和自己的头部
        public void chcs(蛇LegsUnit unit, Unit kill) {
            if (unit == null) {
                return;
            }
            unit.health = Math.min(unit.health + kill.health / 3, unit.maxHealth);
            chcs(unit.child, kill);
        }
        //递归队伍赋值 以头部为标准把尾部所有的体节的队伍赋值为自己的队伍 总不能体节些是其他队伍的
        public void teams(蛇LegsUnit unit) {
            if (unit != null) {
                unit.team = team;
                if (unit.child != null) teams(unit.child);
            }
            //等于null直接结束该方法因为它已经没有尾巴了
        }

        @Override
        public void update() {
            //循环队伍赋值
            teams(child1);
            //没有体节尝试从全局中查找体节 用于载入游戏初始化
            if (child1 == null) child1 = (蛇LegsUnit) Groups.unit.getByID(child1ID);
            //计算头部秒杀的位置
            xy.setAngle(rotation);
            //遍历头部秒杀位置的所有单位
            Groups.unit.intersect(x + xy.x - 5, y + xy.y - 5, 5 * 2f, 5 * 2f, (u) -> {
                if (u.team != team && u.hitSize <= hitSize * 0.6) {
                    //计算血量最大值
                    health = Math.min(health + u.health / 3, maxHealth);
                    //给所有体节恢复生命
                    chcs(child1, u);
                    //然后击杀
                    u.kill();
                }
            });
            //没有生成过 体节 开始生成
            if (!child) {
                offy += 20;//y偏移
                蛇LegsUnit spawn = (蛇LegsUnit) body.spawn(team, x, y + offy);
                spawn.elevation = 0;
                spawn.head = this;
                child1 = spawn;
                addChild(spawn);
            }
            super.update();
        }
        //递归生成体节
        public void addChild(蛇LegsUnit unit) {
            offy += 20;
            //已经生成了多少个体节
            childs++;
            蛇head type1 = (蛇head) type;
            //大于等于要生成体节时  直接生成尾部  结束生成
            if (childs >= type1.lengthSnake) {
                child = true;
                //spaw就是生成实体的方法
                蛇LegsUnit end1 = (蛇LegsUnit) end.spawn(team, x, y + offy);
                end1.elevation = 0;
                //给他的儿子和他的父亲赋值 （破阵）
                end1.parent = unit;
                unit.child = end1;
                return;
            }
            //否则继续生成体节
            蛇LegsUnit spawn = (蛇LegsUnit) body.spawn(team, x, y + offy);
            spawn.elevation = 0;
            unit.child = spawn;
            if (childs > 0) {
                spawn.parent = unit;
            }
            //递归生成
            addChild(spawn);
        }
    }
}

