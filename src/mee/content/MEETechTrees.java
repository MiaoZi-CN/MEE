package mee.content;

import mindustry.content.TechTree;

import static mee.content.MEESectorPreset.*;
import static mindustry.content.TechTree.node;

public class MEETechTrees {
    public static void laod() {
        MEEPlanets.赫尔斯特.techTree = TechTree.nodeRoot("赫尔斯特", MEEPlanets.赫尔斯特, ()->{
            node(风化裂谷, ()->{
                node(晶石峡谷, ()->{
                    node(碳岩油田, ()->{
                        node(瘤蚀裂谷, ()->{
                            node(列位山区, ()->{
                            });
                            node(龙岩裂谷, ()->{
                            });
                        });
                    });
                });
            });

        });
    }
}
