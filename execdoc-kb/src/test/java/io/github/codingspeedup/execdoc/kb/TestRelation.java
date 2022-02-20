package io.github.codingspeedup.execdoc.kb;

import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import io.github.codingspeedup.execdoc.kb.vocabulary.relations.KbRelation2;
import lombok.Getter;
import lombok.Setter;

@KbFunctor
public class TestRelation extends KbRelation2 {

    @Getter
    @Setter
    @KbFunctor
    private String description;

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
