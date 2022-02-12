package xp;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.poc.jhipster.JHipsterExecdocManager;
import io.github.codingspeedup.execdoc.poc.jhipster.Workplace;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.generators.JdlGenerator;
import io.github.codingspeedup.execdoc.toolbox.files.TextFile;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.JHipsterBlueprint;

import java.io.File;

public class JdlGeneratorXp {

    public static void main(String[] args) {
        Workplace.initialize();
        Blueprint<?> bp = new JHipsterBlueprint(JHipsterExecdocManager.getLocation());
        System.out.println(bp.compileKb().listTheory());
        System.out.println("\n----------\n\n");
        JdlGenerator codeGen = new JdlGenerator(bp);
        String jdl = codeGen.getJdl();
        new TextFile(new File(bp.getFile(), "generated.jdl")).writeStringToContent(jdl);
        System.out.println(jdl);
    }

}