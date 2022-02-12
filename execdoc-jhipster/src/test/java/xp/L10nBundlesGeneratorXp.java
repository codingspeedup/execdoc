package xp;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.blueprint.generators.L10nBundlesGenerator;
import io.github.codingspeedup.execdoc.poc.jhipster.JHipsterExecdocManager;
import io.github.codingspeedup.execdoc.poc.jhipster.Workplace;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.JHipsterBlueprint;

public class L10nBundlesGeneratorXp {

    public static void main(String[] args) {
        Workplace.initialize();
        Blueprint<?> bp = new JHipsterBlueprint(JHipsterExecdocManager.getLocation());
        System.out.println(bp.compileKb().listTheory());
        System.out.println(bp);
        System.out.println("\n----------\n\n");
        L10nBundlesGenerator codeGen = new L10nBundlesGenerator(bp);
        System.out.println(codeGen.getLabels());
    }

}
