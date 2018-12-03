package com.salesforce.tests.dependency;

import java.util.Optional;
import java.util.Set;

public class ListCommand implements Command{

    private ListCommand(){
    }

    public static Optional<Command> build(String[] splitted) {
        if (splitted.length != 1) {
            return Optional.empty();
        }

        return Optional.of(new ListCommand());
    }

    @Override
    public void execute(RepositoryService service) {
        System.out.println("LIST");
        service.installed().forEach(System.out::println);
    }
}
