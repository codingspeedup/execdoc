package io.github.codingspeedup.execdoc.jhipster.generators;

import io.github.codingspeedup.execdoc.jhipster.blueprint.JHipsterBlueprint;
import io.github.codingspeedup.execdoc.kb.Kb;

import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.files.TextFile;
import io.github.codingspeedup.execdoc.toolbox.processes.OsProcess;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SolutionGenerator {

    public static final Logger LOGGER = LoggerFactory.getLogger(SolutionGenerator.class);

    private final JHipsterBlueprint bp;
    private final Kb kb;
    private final JHipsterGenCfg genCfg;
    private final Folder appFolder;
    private final TextFile defaultJdl;
    private final OsProcess os = new OsProcess();

    @SneakyThrows
    public SolutionGenerator(JHipsterBlueprint bp, JHipsterGenCfg genCfg) {
        this.bp = bp;
        this.kb = bp.compileKb();
        this.genCfg = genCfg;
        if (genCfg.getDestinationFolder().exists() && genCfg.isForce()) {
            FileUtils.deleteQuietly(genCfg.getDestinationFolder());
            LOGGER.info("Deleted " + genCfg.getDestinationFolder().getCanonicalPath());
        }
        appFolder = Folder.of(genCfg.getDestinationFolder());
        defaultJdl = new TextFile(new File(appFolder, "default.jdl"));
    }

    public synchronized void generateApp() {
        defaultJdl.writeStringToContent(new JdlGenerator(kb).getJdl());
        os.execute(appFolder, "jhipster", "jdl", defaultJdl.getName());
        OsUtility.open(appFolder);
    }

}
