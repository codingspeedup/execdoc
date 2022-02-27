package io.github.codingspeedup.execdoc.generators.spec;

import io.github.codingspeedup.execdoc.toolbox.documents.xml.XmlDocument;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;

import java.io.File;

public interface MavenProjectSpec extends ResourceSpec {

    File getResourceFile();

    default XmlDocument getMasterPomXml() {
        return new XmlDocument(new File(getResourceFile(), "pom.xml"));
    }

    default Folder getSrcMainJava() {
        return Folder.of(new File(getResourceFile(), "src/main/java"));
    }

    default Folder getSrcMainResources() {
        return Folder.of(new File(getResourceFile(), "src/main/resources"));
    }

    default Folder getSrcTestJava() {
        return Folder.of(new File(getResourceFile(), "src/test/java"));
    }

    default Folder getSrcTestResources() {
        return Folder.of(new File(getResourceFile(), "src/test/resources"));
    }

    String getPackageName();

}
