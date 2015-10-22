/*
 * Copyright (C) 2014 RoboVM AB
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
package org.robovm.samples.contractr.core;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.listener.Handler;
import org.robovm.samples.contractr.core.service.ClientManager;
import org.robovm.samples.contractr.core.service.TaskManager;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * Model for {@link Client} objects. Supports the use cases the controllers in
 * the different GUIs will need. Event handling is based on the
 * {@link MBassador} event bus.
 */
public class ClientModel {
    private final ClientManager clientManager;
    private final TaskManager taskManager;
    private final MBassador<Object> bus;

    private Client selectedClient;

    /**
     * Creates a new {@link ClientModel} backed by the specified
     * {@link ClientManager}.
     */
    public ClientModel(ClientManager clientManager, TaskManager taskManager) {
        this.clientManager = Objects.requireNonNull(clientManager, "clientManager");
        this.taskManager = Objects.requireNonNull(taskManager, "taskManager");
        this.bus = new MBassador<Object>(new BusConfiguration()
                .addFeature(Feature.SyncPubSub.Default())
                .addFeature(Feature.AsynchronousHandlerInvocation.Default())
                .addFeature(Feature.AsynchronousMessageDispatch.Default()));
    }

    /**
     * Subscribes to events fired by this {@link ClientModel}. Use MBassador's
     * {@link Handler} annotation to mark methods in the listener as listener
     * methods.
     */
    public void subscribe(Object listener) {
        bus.subscribe(listener);
    }

    /**
     * Unsubscribes a listener previously subscribed using {@link #subscribe(Object)}.
     */
    public void unsubscribe(Object listener) {
        bus.unsubscribe(listener);
    }

    /**
     * Returns the currently selected {@link Client} or {@code null} if no
     * {@link Client} has been selected.
     */
    public Client getSelectedClient() {
        return selectedClient;
    }

    /**
     * Selects a new {@link Client}. Pass {@code null} to deselect the currently
     * selected {@link Client}. Fires a {@link SelectedClientChangedEvent} if
     * the selected {@link Client} has changed.
     */
    public void selectClient(Client newClient) {
        Client oldClient = this.selectedClient;
        this.selectedClient = newClient;
        if (!Objects.equals(oldClient, newClient)) {
            bus.publish(new SelectedClientChangedEvent(oldClient, newClient));
        }
    }

    /**
     * Creates a new {@link Client}. The new {@link Client} will not be added to
     * the underlying storage until {@link #save(Client)} is called.
     */
    public Client create() {
        return clientManager.create();
    }

    /**
     * Returns the total number of available {@link Client}s.
     */
    public int count() {
        return clientManager.count();
    }

    /**
     * Returns the {@link Client} at the specified index.
     */
    public Client get(int index) {
        return clientManager.get(index);
    }

    /**
     * Returns the index of the specified {@link Client} or -1 if it is unknown.
     */
    public int indexOf(Client client) {
        return clientManager.indexOf(client);
    }
    
    /**
     * Saves the specified {@link Client} in the underlying storage. Fires
     * {@link ClientSavedEvent}.
     */
    public void save(Client client) {
        clientManager.save(client);
        bus.publish(new ClientSavedEvent(client));
    }

    /**
     * Deletes the specified {@link Client} from the underlying storage. Fires
     * {@link ClientDeletedEvent} if the {@link Client} existed in the storage
     * and was deleted. Also fires {@link SelectedClientChangedEvent} if the
     * selected {@link Client} is deleted.
     */
    public void delete(Client client) {
        if (clientManager.delete(client)) {
            if (client.equals(selectedClient)) {
                selectClient(null);
            }
            bus.publish(new ClientDeletedEvent(client));
        }
    }

    /**
     * Returns the total number of seconds worked for the specified {@link Client}.
     */
    public int getTotalSecondsElapsed(Client client) {
        int seconds = 0;
        for (Task task : taskManager.getForClient(client, false)) {
            seconds += task.getSecondsElapsed();
        }
        return seconds;
    }

    /**
     * Returns a textual representation of the total time elapsed for this
     * {@link Client} suitable for display in a UI.
     */
    public String getTotalTimeElapsed(Client client) {
        int seconds = getTotalSecondsElapsed(client);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    /**
     * Returns the total amount earned on work done for the specified {@link Client}.
     */
    public BigDecimal getTotalAmountEarned(Client client) {
        return client.getHourlyRate().multiply(BigDecimal.valueOf(getTotalSecondsElapsed(client) / 3600.0));
    }

    /**
     * Returns the total amount earned on work done for the specified {@link Client} as a string formatted using the
     * specified {@link Locale}.
     */
    public String getTotalAmountEarned(Client client, Locale locale) {
        return NumberFormat.getCurrencyInstance(locale).format(getTotalAmountEarned(client));
    }

    /**
     * Event fired when the selected {@link Client} changes.
     */
    public static class SelectedClientChangedEvent {
        private final Client oldClient;
        private final Client newClient;

        SelectedClientChangedEvent(Client oldClient, Client newClient) {
            this.oldClient = oldClient;
            this.newClient = newClient;
        }

        public Client getOldClient() {
            return oldClient;
        }

        public Client getNewClient() {
            return newClient;
        }
    }

    /**
     * Event fired when a {@link Client} has been saved.
     */
    public static class ClientSavedEvent {
        private final Client client;

        ClientSavedEvent(Client client) {
            this.client = client;
        }

        public Client getClient() {
            return client;
        }
    }

    /**
     * Event fired when a {@link Client} has been deleted.
     */
    public static class ClientDeletedEvent {
        private final Client client;

        ClientDeletedEvent(Client client) {
            this.client = client;
        }

        public Client getClient() {
            return client;
        }
    }
}
