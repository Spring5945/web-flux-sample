package org.springframework.web.reactive.samples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {
    Mono<Person> findOne(int id);

    Flux<Person> findAll();

    Mono<Void> createOne(Mono<Person> person);
}
