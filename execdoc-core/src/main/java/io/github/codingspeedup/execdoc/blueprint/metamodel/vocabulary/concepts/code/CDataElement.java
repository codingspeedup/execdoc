package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.IsNamed;

@KbFunctor
public interface CDataElement extends CComputationalObject, IsNamed {

    default CDatatype getType() {
        throw new UnsupportedOperationException();
    }

    default void setType(CDatatype type) {
        throw new UnsupportedOperationException();
    }

    default Integer getSize() {
        throw new UnsupportedOperationException();
    }

    default void setSize(Integer value) {
        throw new UnsupportedOperationException();
    }

    default String getExt() {
        throw new UnsupportedOperationException();
    }

    default void setExt(String ext) {
        throw new UnsupportedOperationException();
    }

}
