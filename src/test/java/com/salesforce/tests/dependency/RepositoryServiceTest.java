package com.salesforce.tests.dependency;

import io.vavr.collection.Set;
import io.vavr.control.Try;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RepositoryServiceTest {
    @Test
    public void shouldInstall(){
        RepositoryService s = RepositoryService.empty();
        s.addDependency("A", "B");
        s.addDependency("B", "C");

        Try<Set<String>> firstAttempt = s.install("A");
        assertTrue(firstAttempt.isSuccess());
        assertTrue(firstAttempt.get().contains("B"));
        assertTrue(firstAttempt.get().contains("C"));

        assertTrue(s.isInstalled("A"));
        assertTrue(s.isInstalled("B"));
        assertTrue(s.isInstalled("C"));

        Try<Set<String>> secondAttempt = s.install("A");
        assertTrue(secondAttempt.isFailure());
        assertEquals("A is already installed", secondAttempt.getCause().getMessage());

        Try<Set<String>> removeAttempt = s.remove("A");
        assertTrue(removeAttempt.isSuccess());
        assertFalse(s.isInstalled("A"));
        assertFalse(s.isInstalled("B"));
        assertFalse(s.isInstalled("C"));
    }


    @Test
    public void shouldNotRemoveB(){
        RepositoryService s = RepositoryService.empty();
        s.addDependency("A", "B");

        Try<Set<String>> firstAttempt = s.install("A");
        assertTrue(firstAttempt.isSuccess());
        assertTrue(firstAttempt.get().contains("B"));


        Try<Set<String>> installB = s.install("B");
        assertTrue(installB.isFailure());
        assertEquals("B is already installed", installB.getCause().getMessage());

        Try<Set<String>> removeAttempt = s.remove("A");
        assertTrue(removeAttempt.isSuccess());
        assertFalse(removeAttempt.get().contains("B"));

        assertFalse(s.isInstalled("A"));
        assertTrue(s.isInstalled("B"));
    }


}
