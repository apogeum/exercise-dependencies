package com.salesforce.tests.dependency;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

/**
 * Place holder for your unit tests
 */
public class CommandParsingTest {

    @Test
    public void commandDependParsing(){
        Optional<Command> c = CommandParser.build().parse("DEPEND TELNET TCPIP NETCARD");
        assertTrue(c.isPresent());
        assertTrue(c.get() instanceof DependCommand);

        assertEquals("TELNET", ((DependCommand)c.get()).componentKey());
        assertTrue(((DependCommand)c.get()).dependencyKeysSet().contains("TCPIP"));
        assertTrue(((DependCommand)c.get()).dependencyKeysSet().contains("NETCARD"));
    }

    @Test
    public void commandInstallParsing(){
        Optional<Command> c = CommandParser.build().parse("INSTALL NETCARD");
        assertTrue(c.isPresent());
        assertTrue(c.get() instanceof InstallCommand);

        assertEquals("NETCARD", ((InstallCommand)c.get()).componentKey);
    }

    @Test
    public void commandRemoveParsing(){
        Optional<Command> c = CommandParser.build().parse("REMOVE NETCARD");
        assertTrue(c.isPresent());
        assertTrue(c.get() instanceof RemoveCommand);
        assertEquals("NETCARD", ((RemoveCommand)c.get()).component);
    }

    @Test
    public void commandListParsing(){
        Optional<Command> c = CommandParser.build().parse("LIST");
        assertTrue(c.isPresent());
        assertTrue(c.get() instanceof ListCommand);
    }

}
