package mee.world.blocks;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import mindustry.entities.TargetPriority;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.blocks.liquid.LiquidBlock;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;

import static mindustry.Vars.content;

public class 液体卸载器 extends Block {
    public TextureRegion top1, top2, arrow1, arrow2;

    public 液体卸载器(String name) {
        super(name);
        buildType = 液体卸载器Build::new;
        update = true;
        group = BlockGroup.transportation;
        solid = true;
        configurable = true;
        saveConfig = true;
        rotate = true;
        noUpdateDisabled = true;
        envDisabled = Env.none;
        clearOnDoubleTap = true;
        priority = TargetPriority.transport;
        config(Liquid.class, (液体卸载器Build tile, Liquid liquid)->tile.sortLquid = liquid);
        configClear((液体卸载器Build tile)->tile.sortLquid = null);
    }

    @Override
    public void drawPlan(BuildPlan plan, Eachable<BuildPlan> list, boolean valid) {
        super.drawPlan(plan, list, valid);
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{region, top1, top2, arrow1, arrow2};
    }

    @Override
    public void load() {
        arrow2 = Core.atlas.find(name + "-arrow2");
        arrow1 = Core.atlas.find(name + "-arrow1");
        top1 = Core.atlas.find(name + "-top1");
        top2 = Core.atlas.find(name + "-top2");
        super.load();
    }

    public class 液体卸载器Build extends Building {


        public Building front, back;
        public Liquid sortLquid;

        @Override
        public void draw() {
            if (sortLquid != null) LiquidBlock.drawTiledFrames(size, x, y, 1, sortLquid, 1f);
            Draw.rect(region, x, y);
            if (rotation == 0 || rotation == 1) Draw.rect(top1, x, y, rotation * 90);
            if (rotation == 2 || rotation == 3) Draw.rect(top2, x, y, rotation * 90);

            Draw.rect(arrow2, x, y, rotation * 90);
            if (sortLquid == null) Draw.rect(arrow1, x, y, rotation * 90);
        }

        @Override
        public Liquid config() {
            return sortLquid;
        }

        @Override
        public void buildConfiguration(Table table) {
            ItemSelection.buildTable(block, table, content.liquids(), ()->sortLquid, this::configure, selectionRows, selectionColumns);
            super.buildConfiguration(table);
        }

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            return super.init(tile, team, shouldAdd, rotation);
        }

        @Override
        public void updateTile() {
            front = front();
            back = back();
            if (sortLquid != null && front != null && back != null && back.liquids != null && front.liquids != null) {
                back.transferLiquid(front, back.liquids.get(sortLquid), sortLquid);
            }
        }

        @Override
        public void transferLiquid(Building next, float amount, Liquid liquid) {
            super.transferLiquid(next, amount, liquid);
        }
    }
}
