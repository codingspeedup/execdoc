package io.github.codingspeedup.execdoc.blueprint.metamodel;

import io.github.codingspeedup.execdoc.kb.KbTermBuilder;
import it.unibo.tuprolog.core.Atom;
import it.unibo.tuprolog.core.Term;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

public class BpTermBuilder implements KbTermBuilder {

    @Override
    public Term termOf(Object arg) {
        if (arg instanceof Cell) {
            return Atom.of(BpNames.getAtom((Cell) arg));
        } else if (arg instanceof Sheet) {
            return Atom.of(BpNames.getAtom((Sheet) arg));
        }
        return KbTermBuilder.super.termOf(arg);
    }

}