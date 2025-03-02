package mee.parser;

import arc.files.Fi;
import arc.struct.Seq;
import arc.util.Log;
import mee.MEE;
import mindustry.Vars;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.type.ErrorContent;

import java.util.Locale;

import static mindustry.Vars.content;

public class Contents {
    public static MeeContentParser parser = new MeeContentParser();

    public static void load() {
        class LoadRun {
            final ContentType type;
            final Fi file;

            public LoadRun(ContentType type, Fi file) {
                this.type = type;
                this.file = file;
            }

        }

        Seq<LoadRun> runs = new Seq<>();
        Fi root = Vars.mods.getMod(MEE.class).root.child("meeContent");
        if (root.exists()) {
            for (ContentType type : ContentType.all) {
                String lower = type.name().toLowerCase(Locale.ROOT);
                Fi folder = root.child(lower + (lower.endsWith("s") ? "" : "s"));//units,items....
                if (folder.exists()) {
                    for (Fi file : folder.findAll(f->f.extension().equals("json") || f.extension().equals("hjson"))) {
                        runs.add(new LoadRun(type, file));
                    }
                }
            }
        }


        //确保mod内容是在适当的顺序
        for (LoadRun l : runs) {
            Content current = content.getLastAdded();
            try {
                //这将绑定内容，但不会完全加载
                Content loaded = parser.parse(l.file.nameWithoutExtension(), l.file.readString("UTF-8"), l.file, l.type);

                Log.debug("[@] Loaded '@'.", MEE.mee, (loaded instanceof UnlockableContent u ? u.localizedName : loaded));
            } catch (Throwable e) {
                if (current != content.getLastAdded() && content.getLastAdded() != null) {
                    parser.markError(content.getLastAdded(), l.file, e);
                } else {
                    ErrorContent error = new ErrorContent();
                    parser.markError(error, l.file, e);
                }
            }
        }

        //这样就完成了对内容字段的解析
        parser.finishParsing();
    }
}
