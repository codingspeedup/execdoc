package io.github.codingspeedup.execdoc.blueprint.metamodel;

import io.github.codingspeedup.execdoc.kb.vocabulary.KbConcept;

public interface IsOwned {

    KbConcept getOwner();

    void setOwner(KbConcept value);

}
