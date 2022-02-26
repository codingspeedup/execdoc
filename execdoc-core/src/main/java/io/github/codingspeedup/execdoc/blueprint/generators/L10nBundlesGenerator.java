package io.github.codingspeedup.execdoc.blueprint.generators;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.ui.L10NLabel;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class L10nBundlesGenerator {

    private final Kb kb;

    public L10nBundlesGenerator(Blueprint<?> bp) {
        this.kb = bp.compileKb();
    }

    public Map<String, Properties> getLabels() {
        Map<String, Properties> translations = new HashMap<>();
        for (String labelId : kb.solveConcepts(L10NLabel.class)) {
            L10NLabel l10nLabel = kb.solveConcept(L10NLabel.class, labelId);
            for (Map.Entry<String, String> l10n : l10nLabel.getL10n().entrySet()) {
                Properties translation = translations.computeIfAbsent(l10n.getKey(), lang -> new Properties());
                translation.put(l10nLabel.getName(), l10n.getValue());
            }
        }
        return translations;
    }

}
