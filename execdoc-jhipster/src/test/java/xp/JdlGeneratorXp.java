package xp;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.jhipster.JHipsterBlueprintManager;
import io.github.codingspeedup.execdoc.jhipster.Workplace;
import io.github.codingspeedup.execdoc.jhipster.blueprint.JHipsterBlueprint;
import io.github.codingspeedup.execdoc.jhipster.generators.JdlGenerator;
import io.github.codingspeedup.execdoc.toolbox.files.TextFile;

import java.io.File;

public class JdlGeneratorXp {

    public static void main(String[] args) {
        Workplace.initialize();
        Blueprint<?> bp = new JHipsterBlueprint(JHipsterBlueprintManager.getLocation());
        System.out.println(bp.compileKb().listTheory());
        System.out.println("\n----------\n\n");
        JdlGenerator codeGen = new JdlGenerator(bp.compileKb());
        String jdl = codeGen.getJdl();
        new TextFile(new File(bp.getWrappedFile(), "generated.jdl")).writeStringToContent(jdl);
        System.out.println(jdl);
    }

}