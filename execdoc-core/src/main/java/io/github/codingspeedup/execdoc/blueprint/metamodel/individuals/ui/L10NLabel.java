package io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.ui;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpConceptCell;
import io.github.codingspeedup.execdoc.blueprint.metamodel.IsNamed;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.ui.CAbstractUiElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@KbFunctor
public class L10NLabel extends BpConceptCell implements CAbstractUiElement, IsNamed {

    public static final String DEFAULT_LANGUAGE_KEY = "";

    @Getter
    @KbFunctor(value = BpNames.L10N_FUNCTOR, T1 = String.class, T2 = String.class)
    private final Map<String, String> l10n = new HashMap<>();

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    public L10NLabel(Cell cell) {
        super(cell);
    }

    public String getTranslation() {
        return l10n.get(DEFAULT_LANGUAGE_KEY);
    }

    public String getTranslation(String languageKey) {
        String translation = l10n.get(DEFAULT_LANGUAGE_KEY);
        return translation == null ? getTranslation() : translation;
    }

}
