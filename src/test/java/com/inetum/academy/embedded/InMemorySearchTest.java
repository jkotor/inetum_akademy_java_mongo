package com.inetum.academy.embedded;

import com.inetum.academy.embedded.data.SampleTolkienData;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;

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

    }

    @Test
    void findAllMagicUsers() {
        // Znaleźć wszystkich czarodziejów (posługują się magią)

    }

    @Test
    void findAllMagicUsersWithoutElfs() {
        // Znaleźć wszystkich czarodziejów nie będących elfami
        // Filters.and()


    }

    @Test
    void findAllYoungHeroesSortedByAge() {
        // Znaleźć wszystkich bohaterów, których wiek jest mniejszy niż 100.
        // Wiek musi być liczbowy

    }

    @Test
    void findAllWeaponUsersWithProjection() {
        // Znaleźć bohaterów posługujących się bronią
        // w wyniku wylistować jedynie pola "name", "weapons" oraz "_id" które jest domyślne
        // Projections.include

    }

    @Test
    void findAllWeaponUsersWithProjectionWithoutId() {
        // Znaleźć bohaterów posługujących się bronią
        // w wyniku wylistować jedynie pola "name", "weapons", ale bez "_id"
        // Projections.exclude, Projections.fields

    }

    @Test
    void findUsingSkipAndLimit() {
        // Zaprojektować samemu test z użyciem skip, limit oraz dowolnymi innymi kryteriami

    }


    private void listDocuments(FindIterable<Document> heroes) {
        out.println("--- Retrieved heroes ---");
        heroes.spliterator().forEachRemaining(hero -> {
            out.println("  " + hero.toJson());
        });
        out.println("----------------------");
    }

}
