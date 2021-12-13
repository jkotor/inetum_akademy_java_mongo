package com.inetum.academy.embedded;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import lombok.SneakyThrows;

import java.io.IOException;

public class MongodFactory {
    private static MongodExecutable mongodExecutable;
    private static MongodProcess mongodProcess;

    public MongodFactory() {
        launchEmbedMongo();
    }

    public void launchEmbedMongo() {
        if (mongodProcess != null && mongodProcess.isProcessRunning()) {
            return;
        }
        try {
            IMongodConfig mongodConfig = new
                    MongodConfigBuilder().version(Version.Main.V4_0).build();
            MongodStarter starter = MongodStarter.getInstance(
                    new RuntimeConfigBuilder()
                            .defaults(Command.MongoD)
                            .processOutput(ProcessOutput.getDefaultInstanceSilent())
                            .build()
            );
            mongodExecutable = starter.prepare(mongodConfig);
            mongodProcess = this.mongodExecutable.start();
        } catch (IOException e) {
        }
    }

    @SneakyThrows
    public MongoClient newMongo() throws MongoException {
        return new MongoClient(new ServerAddress(mongodProcess.getConfig().net().getBindIp(), mongodProcess.getConfig().net().getPort()));
    }

    public void shutdown() {
        mongodProcess.stop();
        mongodExecutable.stop();
    }

}
