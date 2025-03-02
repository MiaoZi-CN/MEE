package mee.world.stat;

import arc.Core;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

public class MEEStat extends Stat {
    public static final Stat recover = new MEEStat("recover"), 自爆状态 =new MEEStat("自爆状态",StatCat.function), 自爆范围 = new MEEStat("自爆范围",StatCat.function), armorBreak = new MEEStat("armorBreak");


    public MEEStat(String name) {
        super(name);
    }

    public MEEStat(String name, StatCat category) {
        super(name, category);
    }

    @Override
    public String localized() {
        return Core.bundle.get("meeStat." + name);
    }
}

