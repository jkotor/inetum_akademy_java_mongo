package com.inetum.academy.embedded;

import com.inetum.academy.embedded.data.SampleTolkienData;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InMemoryCrudTest {

    private MongodFactory mongo;
    private MongoClient client;

    private MongoCollection<Document> heroes;

    @SneakyThrows
    @BeforeEach
    void before() {
        mongo = new MongodFactory();
        client = mongo.newMongo();
        heroes = client.getDatabase("Tolkien").getCollection("heroes");
        heroes.insertMany(SampleTolkienData.takeBilboFrodoLegolas());
    }

    @SneakyThrows
    @AfterEach
    void after() {
        mongo.shutdown();
    }

    @Test
    void presentHeroes() {
        printHeroes();
    }

    // Operacje potrzebne przy update: $set, $unset,

    @Test
    void updateShouldAddProperty() {
        // dodać Frodo property wskazujące na specjalną rolę "powiernik pierścienia" - "ring-bearer"
        // wylistować bohaterów i zrobić asercję
        Document update = new Document();
        update.append("$set", new Document("special-role", "ring-bearer"));
        Bson filter = Filters.eq("name", "Frodo");

        //when
        heroes.updateOne(filter, update);

        //then
        printHeroes();
        Document currentFrodo = heroes.find(filter).first();
        assertEquals("ring-bearer", currentFrodo.get("special-role"));
    }

    @Test
    void updateShouldChangeProperty() {
        // zmienić Legolasowi broń, aby teraz posiadał oprócz łuku również sztylet - "dagger"
        // wylistować bohaterów i zrobić asercję
        // dodać Frodo property wskazujące na specjalną rolę "powiernik pierścienia" - "ring-bearer"
        // wylistować bohaterów i zrobić asercję
        Document update = new Document("$set",
                                       new Document("weapon", asList("bow", "dagger"))
        );
        Bson filter = Filters.eq(3);    // Legolas ma _id == 3

        //when
        heroes.updateOne(filter, update);

        //then
        printHeroes();
        Document currentLegolas = heroes.find(filter).first();
        assertEquals(asList("bow", "dagger"), currentLegolas.get("weapon"));
    }

    @Test
    void updateShouldRemoveProperty() {
        // odebrać Legolasowi broń, aby w mongo nie było tego atrybutu
        // wylistować bohaterów i zrobić asercję
        Document update = new Document("$unset", new Document("weapon", null));
        Bson filter = Filters.eq(3);

        //when
        heroes.updateOne(filter, update);

        //then
        printHeroes();
        Document currentLegolas = heroes.find(filter).first();
        assertFalse(currentLegolas.containsKey("weapon"));
    }

    @Test
    void shouldDeleteSingleDocument() {
        // usunąć Frodo
        // zrobić asercję na dokument po _id
        Bson filter = Filters.eq("name", "Frodo");

        //when
        heroes.deleteOne(filter);

        //then
        MongoCursor<Document> iterator = heroes.find(filter).iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void shouldDeleteMultipleDocuments() {
        // usunąć jedną operacją wszystkich hobbitów
        // zrobić asercję na dokumenty po _id
        Bson filter = Filters.eq("race", "Hobbit");

        //when
        heroes.deleteMany(filter);

        //then
        Bson idsOneTwo = Filters.in("_id", 1, 2);
        MongoCursor<Document> iterator = heroes.find(idsOneTwo).iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void shouldUpdateMultipleDocuments() {
        // minął rok, podbić jedną operacją wszystkim bohaterom wiek o 1
        // zrobić odpowiednie asercje
        Document incAgeUpdate = new Document("$inc", new Document("age", 1));
        Bson allDocs = Filters.exists("_id");

        //when
        heroes.updateMany(allDocs, incAgeUpdate);

        //then
        printHeroes();
        assertAge("Bilbo", 100);
        assertAge("Frodo", 34);
        assertAge("Legolas", 151);
    }

    private void assertAge(String heroName, int expectedAge) {
        Bson filter = Filters.eq("name", heroName);
        Document hero = heroes.find(filter).first();
        assertEquals(expectedAge, hero.get("age"));
    }

    private void printHeroes() {
        out.println("--- Current heroes ---");
        heroes.find().spliterator().forEachRemaining(hero -> {
            out.println("  " + hero.toJson());
        });
        out.println("----------------------");
    }

}
