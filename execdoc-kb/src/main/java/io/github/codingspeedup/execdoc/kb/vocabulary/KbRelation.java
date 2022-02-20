package io.github.codingspeedup.execdoc.kb.vocabulary;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@KbFunctor
public abstract class KbRelation implements KbElement {

    @Getter(AccessLevel.PROTECTED)
    private final String[] members;

    @Getter
    @Setter
    private String kbId;

    protected KbRelation(String kbId, String... members) {
        this.kbId = kbId;
        this.members = members;
    }
}
