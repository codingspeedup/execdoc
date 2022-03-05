package io.github.codingspeedup.execdoc.spring.generators;

import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.kb.KbQuery;
import io.github.codingspeedup.execdoc.kb.KbResult;
import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import io.github.codingspeedup.execdoc.kb.vocabulary.relations.KbRelation;
import it.unibo.tuprolog.core.Struct;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Set;

public class SpringKb implements KbQuery {

    private final Kb kb;

    public SpringKb(Kb kb) {
        this.kb = kb;
    }

    @Override
    public Set<String> findFunctors(String ofAtom) {
        return kb.findFunctors(ofAtom);
    }

    @Override
    public Set<String> findFunctors(String ofAtom, int index) {
        return kb.findFunctors(ofAtom, index);
    }

    @Override
    public KbResult solve(boolean asList, Struct goal) {
        return kb.solve(asList, goal);
    }

    @Override
    public KbResult solve(boolean asList, Object... goal) {
        return kb.solve(asList, goal);
    }

    @Override
    public KbResult solveList(Object functor, Object... args) {
        return kb.solveList(functor, args);
    }

    @Override
    public KbResult solveOnce(Object functor, Object... args) {
        return kb.solveOnce(functor, args);
    }

    @Override
    public <E extends KbConcept> Set<String> solveConcepts(Class<E> entityType) {
        return kb.solveConcepts(entityType);
    }

    @Override
    public <E extends KbConcept> E solveConcept(Class<E> entityType, String kbId) {
        return kb.solveConcept(entityType, kbId);
    }

    @Override
    public <E extends KbConcept> E solveConcept(Class<E> entityType) {
        return kb.solveConcept(entityType);
    }

    @Override
    public <R extends KbRelation> Set<Triple<String, String, String>> solveRelation(Class<R> relationType) {
        return kb.solveRelation(relationType);
    }

    @Override
    public <R extends KbRelation> R solveRelation(Class<R> relationType, String kbId) {
        return kb.solveRelation(relationType, kbId);
    }

}
