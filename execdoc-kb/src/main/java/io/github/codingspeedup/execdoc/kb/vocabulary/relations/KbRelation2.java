package io.github.codingspeedup.execdoc.kb.vocabulary.relations;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;

@KbFunctor
public class KbRelation2 extends KbRelation {

    public KbRelation2() {
        this(null, null, null);
    }

    public KbRelation2(String kbId, String kbId1, String kbId2) {
        super(kbId, kbId1, kbId2);
    }

    public String getFrom() {
        return getMember(0);
    }

    public void setFrom(String kbId) {
        setMember(0, kbId);
    }

    public void setFrom(KbConcept entity) {
        setFrom(entity.getKbId());
    }

    public String getTo() {
        return getMember(1);
    }

    public void setTo(String kbId) {
        setMember(1, kbId);
    }

    public void setTo(KbConcept entity) {
        setTo(entity.getKbId());
    }

}
