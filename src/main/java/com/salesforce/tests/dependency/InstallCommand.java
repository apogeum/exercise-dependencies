package com.salesforce.tests.dependency;

import io.vavr.collection.Set;
import io.vavr.control.Try;

import java.util.Optional;

public class InstallCommand implements Command {

    public final String componentKey;

    private InstallCommand(String componentKey) {

        this.componentKey = componentKey;
    }

    public static Optional<Command> build(String[] splitted) {
        if (splitted.length != 2) {
            return Optional.empty();
        }

        return Optional.of(new InstallCommand(splitted[1]));
    }

    @Override
    public void execute(RepositoryService service) {
        System.out.println("INSTALL " + componentKey);
        Try<Set<String>> res = service.install(componentKey);
        if (res.isFailure()){
            System.out.println(res.getCause().getMessage());
        } else {
            if (res.get().nonEmpty()) {
                res.get().forEach(s -> System.out.println("Installing " + s));
            }
            System.out.println("Installing " + componentKey);
        }

    }
}
