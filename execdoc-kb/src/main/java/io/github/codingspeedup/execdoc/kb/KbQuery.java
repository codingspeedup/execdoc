package io.github.codingspeedup.execdoc.kb;

import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import io.github.codingspeedup.execdoc.kb.vocabulary.relations.KbRelation;
import it.unibo.tuprolog.core.Struct;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Set;

public interface KbQuery
{
    Set<String> findFunctors(String ofAtom);

    Set<String> findFunctors(String ofAtom, int index);

    KbResult solve(boolean asList, Struct goal);

    KbResult solve(boolean asList, Object... goal);

    KbResult solveList(Object functor, Object... args);

    KbResult solveOnce(Object functor, Object... args);

    <E extends KbConcept> Set<String> solveConcepts(Class<E> entityType);

    <E extends KbConcept> E solveConcept(Class<E> entityType, String kbId);

    <E extends KbConcept> E solveConcept(Class<E> entityType);

    <R extends KbRelation> Set<Triple<String, String, String>> solveRelation(Class<R> relationType);

    <R extends KbRelation> R solveRelation(Class<R> relationType, String kbId);

}
