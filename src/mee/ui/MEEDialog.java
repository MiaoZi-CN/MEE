package mee.ui;

import arc.Core;
import arc.flabel.FLabel;
import arc.util.Align;
import mee.MEE;
import mindustry.ui.dialogs.BaseDialog;

public class MEEDialog {
    public static void load() {
        BaseDialog dialogo = new BaseDialog("MEE");
        dialogo.cont.table(table->{
            table.image(Core.atlas.find("logo")).labelAlign(Align.center).row();
            String meeTable = Core.bundle.get("meeTable","");
            table.add(meeTable).labelAlign(Align.center).row();
            dialogo.show();
        });
        dialogo.buttons.button("@close", dialogo::hide).size(210, 64);
    }
}
