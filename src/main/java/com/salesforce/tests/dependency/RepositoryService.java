package com.salesforce.tests.dependency;

import io.vavr.collection.*;
import io.vavr.control.Try;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepositoryService {

    private Map<String, Boolean> installed; // indicate transitive and dont remove as transitive if not transitive


    private ArtefactRegistry registry;

    private RepositoryService(){
        this.registry = ArtefactRegistry.empty();
        this.installed = HashMap.empty();
    }

    public static RepositoryService empty() {
        return new RepositoryService();
    }

    public Try<String> addDependency(String a, String ... b) {
        return registry.register(a, b);
    }

    public Try<String> addDependency(String a, Set<String> deps) {
        return registry.register(a, deps);
    }

    private Try<String> installOne(String a, Boolean transitive){
        if (installed.containsKey(a)){
            if (!transitive) {
                installed = installed.put(a, false);
            }
            return Try.failure(new Exception(a + " is already installed"));
        }
        installed = installed.put(a, transitive);
        return Try.success(a);
    }

    public Try<Set<String>> install(String a) {
        Try<String> r = installOne(a, false);
        if (r.isFailure()){
            return Try.failure(r.getCause());
        }

        Set<String> installedNow = HashSet.empty();
        Set<String> toInstall = registry.getDependencies(a);

        while (!toInstall.isEmpty()){
            for (String e : toInstall){
                toInstall = toInstall.remove(e);
                if (installOne(e, true).isSuccess()){
                    installedNow = installedNow.add(e);
                }
                toInstall = toInstall.addAll(registry.getDependencies(e));
            }
        }

        return Try.success(installedNow);
    }

    public boolean isInstalled(String a) {
        return installed.containsKey(a);
    }

    public Set<String> installed() {
        return installed.keySet();
    }

    public Try<String> removeOne(String c){
        if (!installed.containsKey(c)){
            return Try.failure(new Exception(c + " is not installed"));
        }
        if (stillNeeded(c)){
            return Try.failure(new Exception(c + " is still needed"));
        }
        installed = installed.remove(c);
        return Try.success(c);
    }


    public Try<Set<String>> remove(String c) {
        Try<String> r = removeOne(c);
        if (r.isFailure()){
            return Try.failure(r.getCause());
        }

        Set<String> removedNow = HashSet.empty();
        Queue<String> toRemove = allToRemove(c);

        while(toRemove.nonEmpty()){
            String e = toRemove.head();
            toRemove = toRemove.tail();
            // only remove if transitive dependency
            if (installed.get(e).get()) {
                r = removeOne(e);
                if (r.isSuccess()) {
                    removedNow = removedNow.add(r.get());
                }
            }
        }
        return Try.success(removedNow);
    }

    // returns items to remove in proper order
    private Queue<String> allToRemove(String c) {
        Queue<String> ret = Queue.empty();
        Queue<String> toRemove = Queue.ofAll(registry.getDependencies(c));

        // breadth first reverse
        while (toRemove.nonEmpty()){
            String e = toRemove.head();
            toRemove = toRemove.tail();
            ret = ret.append(e);
            toRemove = toRemove.appendAll(registry.getDependencies(e));
        }

        return ret;
    }

    private boolean stillNeeded(String c) {
        // it means c has no installed dependants
        return !registry.getDependants(c).intersect(installed.keySet()).isEmpty();
    }
}
