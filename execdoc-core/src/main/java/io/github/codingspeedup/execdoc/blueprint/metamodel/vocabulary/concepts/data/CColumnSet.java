package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.CItemUnit;

import java.util.List;

@KbFunctor
public interface CColumnSet extends CDataContainer {

    List<? extends CItemUnit> getItemUnit();

}
