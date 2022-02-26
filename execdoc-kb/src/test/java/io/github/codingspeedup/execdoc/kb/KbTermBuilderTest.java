package io.github.codingspeedup.execdoc.kb;

import io.github.codingspeedup.execdoc.kb.vocabulary.KbElement;
import io.github.codingspeedup.execdoc.kb.vocabulary.concepts.KbConcept;
import it.unibo.tuprolog.core.Clause;
import it.unibo.tuprolog.core.Struct;
import it.unibo.tuprolog.core.Var;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KbTermBuilderTest {

    private static final Var X = Var.of("X");
    private static final KbTermBuilder TERM_BUILDER = new KbTermBuilder() {
    };

    @Test
    void ensureKbId() {
        assertThrows(NullPointerException.class, () -> Kb.ensureKbId(null));
        KbElement bpElt = new KbElement() {
            @Getter
            @Setter
            private String kbId;
        };
        assertNull(bpElt.getKbId());
        String kbId = Kb.ensureKbId(bpElt);
        assertNotNull(kbId);
        assertEquals(kbId, bpElt.getKbId());
        assertEquals(kbId, Kb.ensureKbId(bpElt));
    }

    @Test
    void parseClauses() {
        List<Clause> clauses;

        clauses = TERM_BUILDER.parseClauses();
        assertTrue(clauses.isEmpty());
        clauses = TERM_BUILDER.parseClauses(null);
        assertTrue(clauses.isEmpty());
        clauses = TERM_BUILDER.parseClauses("", null, "\t", "\n");
        assertTrue(clauses.isEmpty());

        clauses = TERM_BUILDER.parseClauses("atom");
        assertEquals(1, clauses.size());

        clauses = TERM_BUILDER.parseClauses("hello(world)");
        assertEquals(1, clauses.size());

        clauses = TERM_BUILDER.parseClauses("hello(world)", "\n.", "foo(bar)", ".");
        assertEquals(2, clauses.size());

        clauses = TERM_BUILDER.parseClauses(TestRelation.class, "(", 12, ",", "twelve", ")");
        assertEquals(1, clauses.size());

        clauses = TERM_BUILDER.parseClauses(KbConcept.class, "(bar)");
        assertEquals(1, clauses.size());

        assertThrows(UnsupportedOperationException.class, () -> TERM_BUILDER.parseClauses(Object.class, "(", new String[]{"abc"}, ",", Collections.emptyMap(), ")"));
        assertThrows(UnsupportedOperationException.class, () -> TERM_BUILDER.parseClauses("."));
    }

    @Test
    void parseStruct() {
        assertNull(TERM_BUILDER.parseStruct());
        assertThrows(UnsupportedOperationException.class, () -> TERM_BUILDER.parseStruct("hello(world)", "\n.", "foo(bar)"));
        assertThrows(UnsupportedOperationException.class, () -> TERM_BUILDER.parseStruct("hello(world) :- foo(bar)"));
        assertEquals("foo", TERM_BUILDER.parseStruct("foo").toString());
        assertEquals("'KbConcept'(bar)", TERM_BUILDER.parseStruct(KbConcept.class, "(bar)").toString());
    }

    @Test
    void structOf_functor() {
        Pair<Struct, List<Var>> structVar;

        structVar = TERM_BUILDER.structOf(true, "foo");
        assertEquals("foo", structVar.getLeft().toString());

        structVar = TERM_BUILDER.structOf(true, KbConcept.class);
        assertEquals("'KbConcept'", structVar.getLeft().toString());

        structVar = TERM_BUILDER.structOf(true, "foo", null);
        assertEquals("foo", structVar.getLeft().toString());

        assertThrows(UnsupportedOperationException.class, () -> TERM_BUILDER.structOf(true, new Object()));
    }

    @Test
    void structOf_null() {
        Exception thrown = assertThrows(UnsupportedOperationException.class,
                () -> TERM_BUILDER.structOf(true, "foo", null, null));
        assertEquals("Undefined mapping for null", thrown.getMessage());
    }

    @Test
    void structOf_unsupported() {
        Exception thrown = assertThrows(UnsupportedOperationException.class,
                () -> TERM_BUILDER.structOf(true, "foo", new Object()));
        assertEquals("Undefined mapping for java.lang.Object", thrown.getMessage());
    }

    @Test
    void structOf_strings() {
        Pair<Struct, List<Var>> structVar;

        structVar = TERM_BUILDER.structOf(true, "foo", "a", "Z", "b", "Y", "Z");
        assertEquals("foo(a, Z_0, b, Y_0, Z_0)", structVar.getLeft().toString());
        assertEquals(3, structVar.getRight().size());

        structVar = TERM_BUILDER.structOf(false, "foo", "a", "Z", "b", "Y", "Z");
        assertEquals("foo(a, 'Z', b, 'Y', 'Z')", structVar.getLeft().toString());
        assertEquals(0, structVar.getRight().size());
    }

    @Test
    void structOf_terms() {
        Struct s = TERM_BUILDER.parseStruct("foo(bar)");
        Pair<Struct, List<Var>> structVar;
        structVar = TERM_BUILDER.structOf(true, "foo", X, s, X);
        assertEquals("foo(X_0, foo(bar), X_0)", structVar.getLeft().toString());
        assertEquals(2, structVar.getRight().size());
    }

    @Test
    void structOf_raw_primitives() {
        Pair<Struct, List<Var>> structVar;
        structVar = TERM_BUILDER.structOf(true, "foo", 1, 2L, 3F, 5.6D, false);
        assertEquals("foo(1, 2, 3.0, 5.6, false)", structVar.getLeft().toString());
    }

    @Test
    void structOf_boxed_primitives() {
        Pair<Struct, List<Var>> structVar;
        structVar = TERM_BUILDER.structOf(true, "foo", Integer.valueOf(1), Long.valueOf(2), Float.valueOf(3f), Double.valueOf(5.6), Boolean.TRUE);
        assertEquals("foo(1, 2, 3.0, 5.6, true)", structVar.getLeft().toString());
    }

    @Test
    void structOf_element() {
        KbElement bpElt = new KbElement() {
            @Getter
            @Setter
            private String kbId;
        };
        Exception thrown = assertThrows(UnsupportedOperationException.class,
                () -> TERM_BUILDER.structOf(true, "elt", bpElt));
        assertEquals("Entity kbId is not set", thrown.getMessage());
        String kbId = "$" + Kb.ensureKbId(bpElt);
        bpElt.setKbId(kbId);
        Pair<Struct, List<Var>> structVar = TERM_BUILDER.structOf(true, "elt", bpElt);
        assertEquals("elt('" + kbId + "')", structVar.getLeft().toString());
    }

}