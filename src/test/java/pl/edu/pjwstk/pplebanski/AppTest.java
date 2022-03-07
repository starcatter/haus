package pl.edu.pjwstk.pplebanski;

import static org.junit.Assert.assertTrue;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void TestPrologWorks()
    {
        Query q1 =
                new Query(
                        "consult",
                        new Term[] {new Atom("test.pl")}
                );
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed"));

        Query q2 =
                new Query(
                        "child_of",
                        new Term[] {new Atom("joe"),new Atom("ralf")}
                );
        System.out.println(
                "child_of(joe,ralf) is " +
                        ( q2.hasSolution() ? "provable" : "not provable" )
        );

        assert(q2.hasSolution());
    }
}
