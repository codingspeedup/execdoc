package io.github.codingspeedup.execdoc.kb.vocabulary.relations;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.kb.vocabulary.KbElement;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

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

    @SneakyThrows
    public static int getArity(Class<? extends KbRelation> relationType) {
        return relationType.getConstructor().newInstance().getArity();
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
