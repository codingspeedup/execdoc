package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.BpItemUnit;

import java.util.List;

@KbFunctor
public interface BpColumnSet extends BpDataContainer {

    List<? extends BpItemUnit> getItemUnit();

}
