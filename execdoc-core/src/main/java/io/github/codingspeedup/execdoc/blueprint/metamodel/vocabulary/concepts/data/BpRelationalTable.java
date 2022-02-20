package io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.data;

import io.github.codingspeedup.execdoc.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.metamodel.IsOwned;

@KbFunctor
public interface BpRelationalTable extends BpColumnSet, IsOwned {

}
