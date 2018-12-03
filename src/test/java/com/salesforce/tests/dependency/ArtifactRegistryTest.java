package com.salesforce.tests.dependency;

import io.vavr.control.Try;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

public class ArtifactRegistryTest {

    @Test
    public void shouldAvoidCircular(){
        ArtefactRegistry l = ArtefactRegistry.empty();

        assertTrue( l.register("A", "B").isSuccess());
        assertTrue( l.register("B", "C").isSuccess());
        assertTrue( l.register("C", "A").isFailure());

    }
}
