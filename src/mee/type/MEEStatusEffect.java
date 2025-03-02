package mee.type;

import mindustry.type.StatusEffect;

public class MEEStatusEffect extends StatusEffect {

    public MEEStatusEffect(String name) {
        super(name);
    }

    @Override
    public boolean isHidden() {
        return !show;
    }
}
