package org.springframework.web.reactive.samples;

import com.google.common.collect.Maps;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public class NickPersonRepository implements PersonRepository {
    final static Map<Integer, Person> personMap = Maps.newConcurrentMap();
    static {
        personMap.put(1,new Person(1,"nick",25));
        personMap.put(2,new Person(2,"edison",36));
    }

    @Override
    public Mono<Person> findOne(int id) {
        return Mono.justOrEmpty(personMap.get(id));
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.fromIterable(personMap.values());
    }

    @Override
    public Mono<Void> createOne(Mono<Person> personMono) {
        return personMono.doOnNext(person -> personMap.put(person.getId(), person)).thenEmpty(Mono.empty());
    }
}
