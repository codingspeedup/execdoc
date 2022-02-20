package io.github.codingspeedup.execdoc.kb.vocabulary;

import io.github.codingspeedup.execdoc.kb.KbFunctor;

@KbFunctor
public class BpRelationship extends KbRelation {

    public BpRelationship() {
        super(null, new String[2]);
    }

    public BpRelationship(String kbId, String fromKbId, String toKbId) {
        super(kbId, fromKbId, toKbId);
    }

    public String getFrom() {
        return getMembers()[0];
    }

    public void setFrom(String kbId) {
        getMembers()[0] = kbId;
    }

    public void setFrom(KbConcept entity) {
        setFrom(entity.getKbId());
    }

    public String getTo() {
        return getMembers()[1];
    }

    public void setTo(String kbId) {
        getMembers()[1] = kbId;
    }

    public void setTo(KbConcept entity) {
        setTo(entity.getKbId());
    }

}
