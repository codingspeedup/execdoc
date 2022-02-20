package io.github.codingspeedup.execdoc.kb.vocabulary;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import lombok.Getter;
import lombok.Setter;

@KbFunctor
public abstract class KbRelation implements KbElement {

    @Getter
    @Setter
    private String kbId;

}
