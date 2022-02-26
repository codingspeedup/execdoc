package io.github.codingspeedup.execdoc.blueprint.metamodel;

import io.github.codingspeedup.execdoc.kb.Kb;

public class BpKb extends Kb {

    public BpKb() {
        super(new BpTermBuilder());
    }
}
