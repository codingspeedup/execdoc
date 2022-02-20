package io.github.codingspeedup.execdoc.kb.vocabulary.relations;

import io.github.codingspeedup.execdoc.kb.KbFunctor;

@KbFunctor
public abstract class KbRelation2 extends KbRelation {

    public KbRelation2() {
        this(null, null, null);
    }

    public KbRelation2(String kbId, String kbId1, String kbId2) {
        super(kbId, kbId1, kbId2);
    }

}
