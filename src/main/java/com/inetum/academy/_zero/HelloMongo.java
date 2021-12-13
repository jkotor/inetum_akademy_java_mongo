package com.inetum.academy._zero;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import lombok.SneakyThrows;

public class HelloMongo {

    @SneakyThrows
    public static void main(String[] args) {
        // Proces 'mongod' musi działać w systemie na odpowiednim porcie
        MongoClient client = new MongoClient("localhost", 27017);

        // Wylistowac wszystkie bazy danych
        MongoCursor<String> iterator = client.listDatabaseNames().iterator();
        System.out.print("Existing databases: ");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();

        client.close();;
    }

}
