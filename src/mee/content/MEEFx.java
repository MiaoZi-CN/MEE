package mee.content;

import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import mindustry.content.Liquids;
import mindustry.entities.Effect;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

public class MEEFx {
    public static Effect colorSpark, hitLiquid, shootSmallSmoke;

    static {
        colorSpark = new Effect(21f, e->{
            color(Liquids.neoplasm.color, Liquids.neoplasm.color, e.fin());
            stroke(e.fout() * 1.1f + 0.5f);
            randLenVectors(e.id, 5, 27f * e.fin(), e.rotation, 9f, (x, y)->{
                lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 5f + 0.5f);
            });
        });
        hitLiquid = new Effect(16, e->{
            color(Liquids.neoplasm.color);
            randLenVectors(e.id, 5, 1f + e.fin() * 15f, e.rotation, 60f, (x, y)->{
                Fill.circle(e.x + x, e.y + y, e.fout() * 2f);
            });
        });
        shootSmallSmoke = new Effect(20f, e->{
            color(Liquids.neoplasm.color, Liquids.neoplasm.color, e.fin());

            randLenVectors(e.id, 5, e.finpow() * 6f, e.rotation, 20f, (x, y)->{
                Fill.circle(e.x + x, e.y + y, e.fout() * 1.5f);
            });
        });
    }
}
