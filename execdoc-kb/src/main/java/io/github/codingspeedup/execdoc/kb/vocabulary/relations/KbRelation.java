package io.github.codingspeedup.execdoc.kb.vocabulary.relations;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbElement;
import lombok.Getter;
import lombok.Setter;

@KbFunctor
public abstract class KbRelation implements KbElement {

    private final String[] members;
    @Getter
    @Setter
    private String kbId;

    protected KbRelation(String kbId, String... members) {
        this.kbId = kbId;
        this.members = members;
    }

    public static int getArity(Class<? extends KbRelation> relationType) {
        if (KbRelation2.class.isAssignableFrom(relationType)) {
            return 2;
        }
        throw new UnsupportedOperationException("class " + relationType.getName());
    }

    public String getMember(int index) {
        return members[index];
    }

    public void setMember(int index, String kbId) {
        members[index] = kbId;
    }

    public int getArity() {
        return members.length;
    }
}
