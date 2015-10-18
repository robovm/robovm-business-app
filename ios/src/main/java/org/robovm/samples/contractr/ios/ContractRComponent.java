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

import dagger.Component;
import org.robovm.samples.contractr.ios.viewcontrollers.ClientsViewController;
import org.robovm.samples.contractr.ios.viewcontrollers.EditClientViewController;
import org.robovm.samples.contractr.ios.viewcontrollers.EditTaskViewController;
import org.robovm.samples.contractr.ios.viewcontrollers.SelectTaskViewController;
import org.robovm.samples.contractr.ios.viewcontrollers.WorkViewController;

import javax.inject.Singleton;
import java.lang.reflect.Method;

/**
 * Dagger {@link Component}. This needs an {@code inject(...)} for each type of object that will be dependency injected.
 */
@Singleton
@Component(modules = ContractRModule.class)
public interface ContractRComponent {
    void inject(ClientsViewController controller);
    void inject(EditClientViewController controller);
    void inject(EditTaskViewController controller);
    void inject(SelectTaskViewController controller);
    void inject(org.robovm.samples.contractr.ios.viewcontrollers.TasksViewController controller);
    void inject(WorkViewController controller);

    default void inject(Object o) {
        try {
            for (Class<?> c = o.getClass(); c != Object.class; c = c.getSuperclass()) {
                try {
                    Method m = ContractRComponent.class.getMethod("inject", c);
                    m.invoke(this, o);
                    return;
                } catch (NoSuchMethodException e) {
                    // Just try the next ancestor class
                }
            }
        } catch (Throwable t) {
            throw new Error("Failed to inject dependencies into a " + o.getClass().getName(), t);
        }
        // If we end up here it means no inject(...) method was found.
        throw new Error("Failed to inject dependencies into a " + o.getClass().getName()
                + ". No " + ContractRComponent.class.getName() + ".inject(...) method found for "
                + "it or any of its ancestor classes.");
    }
}
