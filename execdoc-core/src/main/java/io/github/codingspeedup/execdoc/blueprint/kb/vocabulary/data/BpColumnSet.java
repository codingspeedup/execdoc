package io.github.codingspeedup.execdoc.blueprint.kb.vocabulary.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.vocabulary.code.BpItemUnit;

import java.util.List;

@KbFunctor
public interface BpColumnSet extends BpDataContainer {

    List<? extends BpItemUnit> getItemUnit();

}
