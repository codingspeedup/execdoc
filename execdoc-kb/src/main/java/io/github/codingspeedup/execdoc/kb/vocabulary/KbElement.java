package io.github.codingspeedup.execdoc.kb.vocabulary;

import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.kb.KbFunctor;

@KbFunctor
public interface KbElement {

    String getKbId();

    void setKbId(String kbId);

    default void kbStore(Kb kb) {
    }

    default void kbRetrieve(Kb kb) {
    }

}
