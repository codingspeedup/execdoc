package io.github.codingspeedup.execdoc.kb.vocabulary;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbElement;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbConcept;

@KbFunctor
public interface BpRelationship extends KbElement {

    String getFrom();

    void setFrom(String kbId);

    String getTo();

    void setTo(String kbId);

    default void setFrom(KbConcept entity) { setFrom(entity.getKbId()); }

    default void setTo(KbConcept entity) { setTo(entity.getKbId()); }

}
