package io.github.codingspeedup.execdoc.kb.vocabulary;

import io.github.codingspeedup.execdoc.kb.BpKb;
import io.github.codingspeedup.execdoc.kb.KbFunctor;

@KbFunctor
public interface KbElement {

    String getKbId();

    void setKbId(String kbId);

    default void kbStore(BpKb bpKb) {
    }

    default void kbRetrieve(BpKb bpKb) {
    }

}
