package com.adi.graphql.resolvers;

import com.adi.graphql.models.Person;
import com.adi.graphql.repository.PersonRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonSubscriptionResolver implements GraphQLSubscriptionResolver, GraphQLQueryResolver {

    @Autowired
    private PersonRepository personRepository;

    public Publisher<Person> persons() {
        return personRepository.getPersons();
    }

    public List<Person> person() {
        return personRepository.getPersonList();
    }

}
