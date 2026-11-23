package eu.gricom.basic.tokenizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * NormalizerQuotedBracketTest.java
 * <p>
 * Description: A square bracket inside a string literal is text. It must not change the spaces of the string.
 * Bug fixed in Phase 7: PRINT "a" ; "[" ; "b" printed a[";"b.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public class NormalizerQuotedBracketTest {

    @Test
    public void testSpacesInsideStringAfterBracketAreKept() {
        String strResult = Normalizer.normalize("PRINT \"[ a ] b\"");

        assertEquals("PRINT \"[ a ] b\"", strResult);
    }

    @Test
    public void testIndexBracketOutsideStringStillRemovesSpaces() {
        String strResult = Normalizer.normalize("A$[ 1 ]");

        assertEquals("A$[1]", strResult);
    }
}
