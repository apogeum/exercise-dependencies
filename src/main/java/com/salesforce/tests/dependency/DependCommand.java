package com.salesforce.tests.dependency;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import java.util.Optional;

public class DependCommand implements Command {

    private final String componentKey;
    private final Set<String> depencencyKeys;

    public DependCommand(String component, Set<String> deps) {
        componentKey = component;
        depencencyKeys = deps;
    }


    public static Optional<Command> build(String[] splitted) {
        if (splitted.length < 3){
            return Optional.empty();
        }

        Set<String> deps = HashSet.empty();

        String component = splitted[1];
        for (int i = 2; i<splitted.length; i++){
            deps = deps.add(splitted[i]);
        }

        return Optional.of(new DependCommand(component, deps));
    }


    public String componentKey() {
        return componentKey;
    }

    public Set<String> dependencyKeysSet() {
        return depencencyKeys;
    }

    @Override
    public void execute(RepositoryService service) {
        System.out.print(String.format("DEPEND %s", componentKey));
        for (String d : depencencyKeys){
            System.out.print(" " + d);
        }
        System.out.println();
        service.addDependency(componentKey, depencencyKeys).onFailure(t -> System.out.println(t.getMessage()));
    }
}
