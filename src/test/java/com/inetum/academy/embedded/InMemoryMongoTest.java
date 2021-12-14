package com.inetum.academy.embedded;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryMongoTest {

    private MongodFactory mongo;
    private MongoClient client;

    @SneakyThrows
    @BeforeEach
    protected void before() {
        mongo = new MongodFactory();
        client = mongo.newMongo();
    }

    @SneakyThrows
    @AfterEach
    protected void after() {
        mongo.shutdown();
    }

    @Test
    void listDatabases() {
        // Wylistowac wszystkie bazy danych
        MongoCursor<String> iterator = client.listDatabaseNames().iterator();
        System.out.print("Existing databases: ");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

    @Test
    void createDatabaseAndCollection() {
        // utworzyć baze danych "akademia" a w środku 2 kolekcje 'users' i 'addresses', wyświetlić wszystkie kolekcje
        //    MongoClient.getDatabase(), MongoDatabase.createCollection(), MongoDatabase.listCollectionNames()

    }

    @Test
    void insertSampleDocuments() {
        // wstawić do kolekcji "users" użytkownika - Dokument z dwoma polami "name" i "age" oraz polem "_id"=1
        // wyświetlić zawartość kolekcji
        // wstawić kolejnego użytkownika z "_id"=2 i dowolnymi "name" i "age"
        // wyświetlić zawartość kolekcji
        //    Document(), Document.append(), MongoCollection.insertOne/Many(), MongoCollection.find()
        // [opcjonalnie] zrobić odpowiednie asercje

    }

    @Test
    void shouldThrowWhenDuplicatingID() {
        // wstawić do kolekcji "users" 2 użytkowników z tym samym "_id" i dowolnymi innymi polami
        // obsłużyć asercję na zgłaszany wyjątek, np używając org.junit.jupiter.api.Assertions.assertThrows()

        // given

        // when

        // then

    }

    @Test
    void shouldGenerateIdsDuringInsertion() {
        // wstawić do kolekcji "users" 2 użytkowników z dowolnymi polami "name", "age", ale bez "_id"
        // wyświetlić zawartość kolekcji (co się stało?)
        // dorobić asercje na zaobserwowaną sytuację

        // given

        // when

        // then

    }


}
