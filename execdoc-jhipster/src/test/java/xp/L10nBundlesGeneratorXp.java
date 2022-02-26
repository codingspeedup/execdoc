package xp;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.generators.L10nBundlesGenerator;
import io.github.codingspeedup.execdoc.jhipster.JHipsterBlueprintManager;
import io.github.codingspeedup.execdoc.jhipster.Workplace;
import io.github.codingspeedup.execdoc.jhipster.blueprint.JHipsterBlueprint;

public class L10nBundlesGeneratorXp {

    public static void main(String[] args) {
        Workplace.initialize();
        Blueprint<?> bp = new JHipsterBlueprint(JHipsterBlueprintManager.getLocation());
        System.out.println(bp.compileKb().listTheory());
        System.out.println(bp);
        System.out.println("\n----------\n\n");
        L10nBundlesGenerator codeGen = new L10nBundlesGenerator(bp.compileKb());
        System.out.println(codeGen.getLabels());
    }

}
