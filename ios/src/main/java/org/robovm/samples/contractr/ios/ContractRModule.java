/*
 * Copyright (C) 2015 RoboVM AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.robovm.samples.contractr.ios;

import dagger.Module;
import dagger.Provides;
import org.robovm.apple.foundation.Foundation;
import org.robovm.samples.contractr.core.ClientModel;
import org.robovm.samples.contractr.core.TaskModel;
import org.robovm.samples.contractr.core.service.ClientManager;
import org.robovm.samples.contractr.core.service.ConnectionPool;
import org.robovm.samples.contractr.core.service.JdbcClientManager;
import org.robovm.samples.contractr.core.service.JdbcTaskManager;
import org.robovm.samples.contractr.core.service.SingletonConnectionPool;
import org.robovm.samples.contractr.core.service.TaskManager;

import javax.inject.Singleton;
import java.io.File;

/**
 * Dagger {@link Module} that configures the iOS version of the ContractR app.
 */
@Module
public class ContractRModule {

    /**
     * Whether dummy data should be preloaded into new databases.
     */
    public static final boolean PRELOAD_DATA = true;

    @Provides
    @Singleton
    public ConnectionPool proivdeConnectionPool() {
        try {
            Class.forName("SQLite.JDBCDriver");
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }

        /*
         * The SQLite database is kept in
         * <Application_Home>/Documents/db.sqlite. This directory is backed up
         * by iTunes. See http://goo.gl/BWlCGN for Apple's docs on the iOS file
         * system.
         */
        File dbFile = new File(System.getenv("HOME"), "Documents/db.sqlite");
        dbFile.getParentFile().mkdirs();
        Foundation.log("Using db in file: " + dbFile.getAbsolutePath());

        return new SingletonConnectionPool(
                "jdbc:sqlite:" + dbFile.getAbsolutePath());
    }

    @Provides
    @Singleton
    public ClientManager proivdeClientManager(ConnectionPool connectionPool) {
        return new JdbcClientManager(connectionPool, PRELOAD_DATA);
    }

    @Provides
    @Singleton
    public TaskManager proivdeTaskManager(ConnectionPool connectionPool, ClientManager clientManager) {
        JdbcTaskManager taskManager = new JdbcTaskManager(connectionPool, PRELOAD_DATA);
        taskManager.setClientManager((JdbcClientManager) clientManager);
        ((JdbcClientManager) clientManager).setTaskManager(taskManager);
        return taskManager;
    }

    @Provides
    @Singleton
    public ClientModel proivdeClientModel(ClientManager clientManager) {
        return new ClientModel(clientManager);
    }

    @Provides
    @Singleton
    public TaskModel proivdeTaskModel(ClientModel clientModel, TaskManager taskManager) {
        return new TaskModel(clientModel, taskManager);
    }
}
