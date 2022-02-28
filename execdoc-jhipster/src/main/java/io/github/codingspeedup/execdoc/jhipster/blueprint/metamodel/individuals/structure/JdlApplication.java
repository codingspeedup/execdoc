package io.github.codingspeedup.execdoc.jhipster.blueprint.metamodel.individuals.structure;

import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.SpringNames;
import io.github.codingspeedup.execdoc.jhipster.blueprint.sheets.AppSheet;
import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.structure.CSubsystem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@KbFunctor
public class JdlApplication implements CSubsystem {

    @KbFunctor(value = SpringNames.CONFIG_FUNCTOR, T1 = String.class, T2 = JdlValue.class)
    private final Map<String, JdlValue> config = new HashMap<>();

    @Getter
    @KbFunctor(value = SpringNames.ENTITY_FUNCTOR, T1 = String.class)
    private final Set<String> entities = new HashSet<>();

    @Getter
    @Setter
    private String kbId;

    @Getter
    @Setter
    @KbFunctor(BpNames.NAME_FUNCTOR)
    private String name;

    public JdlApplication(AppSheet appSheet) {
        setKbId(BpNames.getAtom(appSheet.getSheet()));
        setName(appSheet.getInstanceName());
    }

    public void putConfig(String key, JdlValue value) {
        config.put(key, value);
    }

    public JdlValue getConfig(String key) {
        return config.get(key);
    }

}
