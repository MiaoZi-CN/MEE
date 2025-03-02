package mee.content;

import arc.audio.Sound;
import arc.files.Fi;
import mee.MEE;
import mindustry.Vars;

public class MEESounds {
    public static Sound 瘤液喷吐;

    static {
        瘤液喷吐 = new Sound(findOgg("瘤液喷吐.ogg"));
    }

    public static Fi findOgg(String name) {
        return Vars.mods.getMod(MEE.class).root.child("sounds").child(name);
    }
}
