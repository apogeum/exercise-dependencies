package com.salesforce.tests.dependency;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;



public class ArtefactRegistry {

    //these two maps are mutable
    private Map<String, Set<String>> dependencies;
    private Map<String, Set<String>> dependants;

    public ArtefactRegistry(){
        dependencies = new HashMap<>();
        dependants = new HashMap<>();
    }

    public static ArtefactRegistry empty() {
        return new ArtefactRegistry();
    }


    public Try<String> register(String id, String ... d){
        Set<String> deps = HashSet.of(d);
        return register(id, deps);
    }

    public Try<String> register(String id, Set<String> deps) {
        if (dependencies.containsKey(id)){
            return Try.failure(new Exception(id + " is already installed"));
        }

        Option<String> opt = introducesCycle(id, deps);
        if (opt.isDefined()){
            return Try.failure(new Exception(opt.get() + " depends on " + id + ", ignoring command"));
        }

        dependencies.put(id, deps);

        for (String d : deps){
            if (dependants.containsKey(d)){
                dependants.put(d, dependants.get(d).add(id));
            } else {
                dependants.put(d, HashSet.of(id));
            }
        }

//        System.out.println("registered:" + id);
//        System.out.println(Arrays.toString(dependencies.entrySet().toArray()));
//        System.out.println(Arrays.toString(dependants.entrySet().toArray()));


        return Try.success(id);
    }


    // given A -> B -> C
    // add B -> A (cycle)
    // add C -> A (cycle)

    private Option<String> introducesCycle(String id, Set<String> deps) {
        for (String d : deps){
            if (dependants.getOrDefault(id, HashSet.empty()).contains(d)){
                return Option.of(d);
            }

            for (String t : dependants.getOrDefault(id, HashSet.empty())){
                Option<String> o = introducesCycle(t, deps);
                if (o.isDefined()){
                    return o;
                }
            }
        }
        return Option.none();
    }

    public Set<String> getDependencies(String a) {
        return dependencies.getOrDefault(a, HashSet.empty());
    }

    public Set<String> getDependants(String c) {
        return dependants.getOrDefault(c, HashSet.empty());
    }
}
