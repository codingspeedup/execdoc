package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;

import java.util.List;

@KbFunctor
public interface BpControlElement extends BpComputationalObject {

    default BpDatatype getType() {
        throw new UnsupportedOperationException();
    }

    default void setType(BpDatatype type) {
        throw new UnsupportedOperationException();
    }

    default List<? extends BpAbstractCodeElement> getCodeElement() {
        throw new UnsupportedOperationException();
    }

}
