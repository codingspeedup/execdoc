package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;

import java.util.List;

@KbFunctor
public interface CEnumeratedType extends CDatatype {

    List<? extends CValue> getValue();

}
