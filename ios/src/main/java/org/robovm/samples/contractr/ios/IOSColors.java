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
import org.robovm.apple.uikit.UIColor;
import org.robovm.samples.contractr.core.ui.Colors;
import org.robovm.samples.contractr.ios.viewcontrollers.*;

import javax.inject.Singleton;
import java.lang.reflect.Method;

/**
 * Colors used by ContractR.
 */
public class IOSColors {
    public static final UIColor MAIN = toUIColor(Colors.MAIN);
    public static final UIColor START_WORK = toUIColor(Colors.START_WORK);
    public static final UIColor STOP_WORK = toUIColor(Colors.STOP_WORK);
    private static final UIColor[] CLIENTS = {
            toUIColor(Colors.CLIENT1),
            toUIColor(Colors.CLIENT2),
            toUIColor(Colors.CLIENT3),
            toUIColor(Colors.CLIENT4),
            toUIColor(Colors.CLIENT5),
            toUIColor(Colors.CLIENT6),
            toUIColor(Colors.CLIENT7),
            toUIColor(Colors.CLIENT8),
            toUIColor(Colors.CLIENT9),
    };

    public static final UIColor getClientColor(int index) {
        if (index < 0 || index >= CLIENTS.length) {
            return UIColor.gray();
        }
        return CLIENTS[index];
    }

    private static UIColor toUIColor(Colors c) {
        return UIColor.fromRGBA(c.getRed(), c.getGreen(), c.getBlue(), 1.0);
    }
}
