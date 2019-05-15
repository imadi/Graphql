package com.adi.graphql.repository;

import com.adi.graphql.models.Person;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepository {

    private static List<Person> personList;

    static {
        personList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            personList.add(new Person(String.valueOf(i), "person" + i));
        }
    }

    public Flux<Person> getPersons() {
        Flux<Person> personFlux = Flux.fromIterable(personList);
        return Flux.zip(personFlux, Flux.interval(Duration.ofSeconds(1))).map(Tuple2::getT1); //Added 1 second delay to simulate 1 sec.
    }

}
