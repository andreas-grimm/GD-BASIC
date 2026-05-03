package eu.gricom.basic.memoryManager;

import eu.gricom.basic.statements.Statement;
import eu.gricom.basic.tokenizer.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProgramTest {

    private Program _oProgram;

    @BeforeEach
    public void setUp() {
        _oProgram = new Program();
    }

    @Test
    public void testLoadAndGetters() {
        String name = "test.bas";
        String source = "10 PRINT \"Hello\"";
        _oProgram.load(name, source);

        assertEquals(name, _oProgram.getProgramName());
        assertEquals(source, _oProgram.getProgram());
    }

    @Test
    public void testSetProgram() {
        String source = "20 GOTO 10";
        _oProgram.setProgram(source);
        assertEquals(source, _oProgram.getProgram());
    }

    @Test
    public void testTokens() {
        List<Token> tokens = new ArrayList<>();
        // Note: Token and Statement might be interfaces or classes. 
        // We'll use null or mock if possible, but let's see if we can use real ones or just nulls for now.
        _oProgram.setTokens(tokens);
        assertEquals(tokens, _oProgram.getTokens());
    }

    @Test
    public void testStatements() {
        List<Statement> statements = new ArrayList<>();
        _oProgram.setStatements(statements);
        assertEquals(statements, _oProgram.getStatements());
    }

    @Test
    public void testPreRunStatements() {
        List<Statement> preRunStatements = new ArrayList<>();
        _oProgram.setPreRunStatements(preRunStatements);
        assertEquals(preRunStatements, _oProgram.getPreRunStatements());
    }

    @Test
    public void testEqualsPositive() {
        Program p1 = new Program();
        p1.load("test", "source");
        p1.setTokens(new ArrayList<>());
        p1.setStatements(new ArrayList<>());
        p1.setPreRunStatements(new ArrayList<>());

        Program p2 = new Program();
        p2.load("test", "source");
        p2.setTokens(new ArrayList<>());
        p2.setStatements(new ArrayList<>());
        p2.setPreRunStatements(new ArrayList<>());

        assertTrue(p1.equals(p2));
    }

    @Test
    public void testEqualsNegativeName() {
        Program p1 = new Program();
        p1.load("test1", "source");
        
        Program p2 = new Program();
        p2.load("test2", "source");

        assertFalse(p1.equals(p2));
    }

    @Test
    public void testEqualsNegativeSource() {
        Program p1 = new Program();
        p1.load("test", "source1");
        
        Program p2 = new Program();
        p2.load("test", "source2");

        assertFalse(p1.equals(p2));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(_oProgram.equals(null));
    }

    @Test
    public void testEqualsDifferentTokens() {
        Program p1 = new Program();
        p1.load("test", "source");
        List<Token> t1 = new ArrayList<>();
        // p1._aoTokens remains null if not set

        Program p2 = new Program();
        p2.load("test", "source");
        p2.setTokens(new ArrayList<>());

        assertFalse(p1.equals(p2));
    }
}
