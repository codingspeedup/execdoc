package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;

import java.util.List;

@KbFunctor
public interface CControlElement extends CComputationalObject {

    default CDatatype getType() {
        throw new UnsupportedOperationException();
    }

    default void setType(CDatatype type) {
        throw new UnsupportedOperationException();
    }

    default List<? extends CAbstractCodeElement> getCodeElement() {
        throw new UnsupportedOperationException();
    }

}
