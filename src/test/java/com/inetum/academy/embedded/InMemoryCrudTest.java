package com.inetum.academy.embedded;

import com.inetum.academy.embedded.data.SampleTolkienData;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.SneakyThrows;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;

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

    }

    @Test
    void updateShouldChangeProperty() {
        // zmienić Legolasowi broń, aby teraz posiadał oprócz łuku również sztylet - "dagger"
        // wylistować bohaterów i zrobić asercję

    }

    @Test
    void updateShouldRemoveProperty() {
        // odebrać Legolasowi broń, aby w mongo nie było tego atrybutu
        // wylistować bohaterów i zrobić asercję

    }

    @Test
    void shouldDeleteSingleDocument() {
        // usunąć Frodo
        // zrobić asercję na dokument po _id

    }

    @Test
    void shouldDeleteMultipleDocuments() {
        // usunąć jedną operacją wszystkich hobbitów
        // zrobić asercję na dokumenty po _id

    }

    @Test
    void shouldUpdateMultipleDocuments() {
        // minął rok, podbić jedną operacją wszystkim bohaterom wiek o 1
        // zrobić odpowiednie asercje

    }

    private void printHeroes() {
        out.println("--- Current heroes ---");
        heroes.find().spliterator().forEachRemaining(hero -> {
            out.println("  " + hero.toJson());
        });
        out.println("----------------------");
    }

}
