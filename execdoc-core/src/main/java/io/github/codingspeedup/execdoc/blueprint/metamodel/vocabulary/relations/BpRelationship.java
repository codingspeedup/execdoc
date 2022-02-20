package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.relations;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import io.github.codingspeedup.execdoc.kb.vocabulary.relations.KbRelation2;

@KbFunctor
public class BpRelationship extends KbRelation2 {

    public BpRelationship() {
        super();
    }

    public BpRelationship(String kbId, String fromKbId, String toKbId) {
        super(kbId, fromKbId, toKbId);
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
