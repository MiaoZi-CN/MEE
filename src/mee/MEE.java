package mee;

import mee.content.*;
import mee.parser.Contents;
import mee.ui.MEEDialog;
import mindustry.Vars;
import mindustry.mod.Mod;
import mindustry.mod.Mods;

public class MEE extends Mod {
    public static final String mee = "mee";
    public static Mods.LoadedMod mod;

    @Override
    public void init() {
        MEEDialog.load();
        Vars.renderer.minZoom = 0.5f;
        Vars.renderer.maxZoom = 150;
    }

    @Override
    public void loadContent() {
        mod = Vars.mods.getMod(this.getClass());
        MEEStatus.load();
        MEEItems.load();
        MEELiquids.load();
        MEEBlocks.load();
        MEEUnitTypes.load();
        MEEPlanets.laod();
        MEESectorPreset.load();
        MEETechTrees.laod();
        Contents.load();
    }
}
