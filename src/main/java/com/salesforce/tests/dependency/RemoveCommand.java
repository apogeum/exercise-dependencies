package com.salesforce.tests.dependency;

import io.vavr.collection.Set;
import io.vavr.control.Try;

import java.util.Optional;

public class RemoveCommand implements Command{
    public final String component;

    private RemoveCommand(String component) {
        this.component = component;
    }

    public static Optional<Command> build(String[] splitted) {
        if (splitted.length != 2) {
            return Optional.empty();
        }
        return Optional.of(new RemoveCommand(splitted[1]));
    }

    @Override
    public void execute(RepositoryService service) {
        System.out.println("REMOVE " + component);
        Try<Set<String>> removeAttempt = service.remove(component);
        if (removeAttempt.isFailure()){
            System.out.println(removeAttempt.getCause().getMessage());
        } else {
            if (removeAttempt.get().nonEmpty()){
                removeAttempt.get().forEach(s -> System.out.println("Removing " + s));
            }
            System.out.println("Removing " + component);
        }
    }
}
