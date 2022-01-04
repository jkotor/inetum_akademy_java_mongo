package com.inetum.academy.embedded;


import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;

import javax.print.Doc;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

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
        MongoDatabase db = client.getDatabase("akademia");
        db.createCollection("users");
        db.createCollection("addresses");

        System.out.print("Collections: ");
        for (String collection : db.listCollectionNames()) {
            System.out.print(collection + "  ");
        }
        System.out.println();
    }

    @Test
    void insertSampleDocuments() {
        // wstawić do kolekcji "users" użytkownika - Dokument z dwoma polami "name" i "age" oraz polem "_id"=1
        // wyświetlić zawartość kolekcji
        // wstawić kolejnego użytkownika z "_id"=2 i dowolnymi "name" i "age"
        // wyświetlić zawartość kolekcji
        //    Document(), Document.append(), MongoCollection.insertOne/Many(), MongoCollection.find()
        // [opcjonalnie] zrobić odpowiednie asercje
        MongoCollection<Document> users = client.getDatabase("akademia").getCollection("users");

        Document user1 = new Document();
        user1.append("_id", 1);
        user1.append("name", "Jakub");
        user1.append("age", 30);
        Document user2 = new Document();
        user2.append("_id", 2);
        user2.append("name", "Andrzej");
        user2.append("age", 25);

        //when
        users.insertOne(user1);
        users.insertOne(user2);

        //then
        printCollection(users, "users");
        FindIterable<Document> found = users.find();
        Assertions.assertIterableEquals(found, Arrays.asList(user1, user2));
    }

    @Test
    void shouldThrowWhenDuplicatingID() {
        // wstawić do kolekcji "users" 2 użytkowników z tym samym "_id" i dowolnymi innymi polami
        // obsłużyć asercję na zgłaszany wyjątek, np używając org.junit.jupiter.api.Assertions.assertThrows()

        // given
        MongoCollection<Document> users = client.getDatabase("akademia").getCollection("users");

        Document user1 = new Document();
        user1.append("_id", 1);
        user1.append("name", "Jakub");
        user1.append("age", 30);
        Document user2 = new Document();
        user2.append("_id", 1);
        user2.append("name", "Andrzej");
        user2.append("age", 25);

        users.insertOne(user1);

        //when - then
        Assertions.assertThrows(MongoWriteException.class,
                                () -> users.insertOne(user2));
        printCollection(users, "users");
    }

    @Test
    void shouldGenerateIdsDuringInsertion() {
        // wstawić do kolekcji "users" 2 użytkowników z dowolnymi polami "name", "age", ale bez "_id"
        // wyświetlić zawartość kolekcji (co się stało?)
        // dorobić asercje na zaobserwowaną sytuację
        MongoCollection<Document> users = client.getDatabase("akademia").getCollection("users");

        Document user1 = new Document();
        user1.append("name", "Jakub");
        user1.append("age", 30);
        Document user2 = new Document();
        user2.append("name", "Andrzej");
        user2.append("age", 25);

        //when
        users.insertOne(user1);
        users.insertOne(user2);

        //then
        FindIterable<Document> found = users.find();
        List<Document> foundUsers = StreamSupport.stream(found.spliterator(), false).collect(toList());
        Assertions.assertEquals(2, foundUsers.size());
        for (Document user : foundUsers) {
            Assertions.assertNotNull(user.get("_id"));
        }
        printCollection(users, "users");
    }

    private void printCollection(MongoCollection<Document> collection, String collectionName) {
        System.out.println("--- Collection '" + collectionName + "' ---");
        collection.find().spliterator().forEachRemaining(doc -> {
            System.out.println("  " + doc);
        });
        System.out.println("----------------------");
    }


}
