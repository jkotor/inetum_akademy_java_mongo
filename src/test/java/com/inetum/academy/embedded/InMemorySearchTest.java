package com.inetum.academy.embedded;

import com.inetum.academy.embedded.data.SampleTolkienData;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import lombok.SneakyThrows;
import org.bson.BsonType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

public class InMemorySearchTest {

    private MongodFactory mongo;
    private MongoClient client;

    private MongoCollection<Document> heroes;

    @SneakyThrows
    @BeforeEach
    void before() {
        mongo = new MongodFactory();
        client = mongo.newMongo();
        heroes = client.getDatabase("Tolkien").getCollection("heroes");
        heroes.insertMany(SampleTolkienData.takeAllHeroes());
    }

    @SneakyThrows
    @AfterEach
    void after() {
        mongo.shutdown();
    }

    @Test
    void presentAllHeroes() {
        FindIterable<Document> allHeroes = heroes.find();
        listDocuments(allHeroes);
    }

    @Test
    void findAllHumans() {
        // Znaleźć wszystkich ludzi
        // Bson, Filters.eq()
        Bson filter = Filters.eq("race", "Man");
        //when
        FindIterable<Document> documents = heroes.find(filter);
        Set<Document> humans = stream(documents.spliterator(), false).collect(toSet());
        //then
        Assertions.assertEquals(4, humans.size());
        Assertions.assertIterableEquals(            //można zrobić dużo ładniej przy użyciu 'assertJ'
                asSet("Boromir", "Aragorn", "Saruman", "Gandalf"),
                humans.stream().map(doc -> doc.get("name")).collect(toSet()));
        listDocuments(documents);
    }

    @Test
    void findAllMagicUsers() {
        // Znaleźć wszystkich czarodziejów (posługują się magią)
        Bson filter = Filters.exists("magic");
        //when
        FindIterable<Document> documents = heroes.find(filter);
        Set<Document> magicians = stream(documents.spliterator(), false).collect(toSet());
        //then
        Assertions.assertEquals(3, magicians.size());
        Assertions.assertIterableEquals(
                asSet("Saruman", "Gandalf", "Elrond"),
                magicians.stream().map(doc -> doc.get("name")).collect(toSet()));
        listDocuments(documents);
    }

    @Test
    void findAllMagicUsersWithoutElfs() {
        // Znaleźć wszystkich czarodziejów nie będących elfami
        // Filters.and()
        Bson filter = Filters.and(Filters.exists("magic"),
                                  Filters.ne("race", "Elf"));
        //when
        FindIterable<Document> documents = heroes.find(filter);
        Set<Document> nonElfMagicians = stream(documents.spliterator(), false).collect(toSet());
        //then
        Assertions.assertEquals(2, nonElfMagicians.size());
        Assertions.assertIterableEquals(
                asSet("Saruman", "Gandalf"),
                nonElfMagicians.stream().map(doc -> doc.get("name")).collect(toSet()));
        listDocuments(documents);
    }

    @Test
    void findAllYoungHeroesSortedByAge() {
        // Znaleźć wszystkich bohaterów, których wiek jest mniejszy niż 100.
        // Wiek musi być liczbowy
        Bson filter = Filters.and(Filters.type("age", BsonType.INT32),
                                  Filters.lt("age", 100));
        Bson byAge = Sorts.ascending("age");
        //when
        FindIterable<Document> documents = heroes.find(filter).sort(byAge);
        //then
        List<Object> herosNames = stream(documents.spliterator(), false)
                .map(hero -> hero.get("name"))
                .collect(toList());
        Assertions.assertIterableEquals(asList("Frodo", "Boromir", "Aragorn", "Bilbo"), herosNames);
        listDocuments(documents);
    }

    @Test
    void findAllWeaponUsersWithProjection() {
        // Znaleźć bohaterów posługujących się bronią
        // w wyniku wylistować jedynie pola "name", "weapons" oraz "_id" które jest domyślne
        // Projections.include
        Bson filter = Filters.exists("weapon");
        Bson include = Projections.include("name", "weapon");
        //when
        FindIterable<Document> documents = heroes.find(filter).projection(include);
        //then
        listDocuments(documents);
    }

    @Test
    void findAllWeaponUsersWithProjectionWithoutId() {
        // Znaleźć bohaterów posługujących się bronią
        // w wyniku wylistować jedynie pola "name", "weapons", ale bez "_id"
        // Projections.exclude, Projections.fields
        Bson filter = Filters.exists("weapon");
        Bson projection = Projections.fields(Projections.include("name", "weapon"),
                                                      Projections.excludeId());
        //when
        FindIterable<Document> documents = heroes.find(filter).projection(projection);
        //then
        listDocuments(documents);
    }

    @Test
    void findUsingSkipAndLimit() {
        // Zaprojektować samemu test z użyciem skip, limit oraz dowolnymi innymi kryteriami
        Bson filter = Filters.exists("weapon");
        Bson projection = Projections.fields(Projections.include("name", "weapon"),
                                                      Projections.excludeId());
        Bson byName = Sorts.ascending("name");
        //when
        FindIterable<Document> documents = heroes.find(filter)
                .projection(projection)
                .sort(byName)
                .skip(1)
                .limit(2);
        //then
        listDocuments(documents);
        List<Object> herosNames = stream(documents.spliterator(), false)
                .map(hero -> hero.get("name"))
                .collect(toList());
        Assertions.assertIterableEquals(asList("Boromir", "Elrond"), herosNames);
    }

    private Set<String> asSet(String ... strings) {
        return new HashSet<>(asList(strings));
    }

    private void listDocuments(FindIterable<Document> heroes) {
        out.println("--- Retrieved heroes ---");
        heroes.spliterator().forEachRemaining(hero -> {
            out.println("  " + hero.toJson());
        });
        out.println("----------------------");
    }

}
