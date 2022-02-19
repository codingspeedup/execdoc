package io.github.codingspeedup.execdoc.blueprint.kb.vocabulary.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;

import java.util.List;

@KbFunctor
public interface BpEnumeratedType extends BpDatatype {

    List<? extends BpValue> getValue();

}
