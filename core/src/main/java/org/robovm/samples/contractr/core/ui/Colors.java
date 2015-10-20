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
package org.robovm.samples.contractr.core.ui;

/**
 * Colors used by ContractR.
 */
public enum Colors {
    MAIN(0x5D015E),
    START_WORK(0x9fc827),
    STOP_WORK(0xce3428),
    CLIENT1(0x6392D7), // light blue
    CLIENT2(0xA1CA27), // light green
    CLIENT3(0xE98D20), // orange
    CLIENT4(0x9575BE), // light purple blue
    CLIENT5(0x4EA283), // light blue green
    CLIENT6(0x5DAF95), // light olive
    CLIENT7(0xFAE400), // yellow
    CLIENT8(0xE96C5A), // light pink
    CLIENT9(0xC478A4); // purple red

    private final int value;

    private Colors(int value) {
        this.value = value;
    }

    public double getRed() {
        return ((value >> 16) & 0xff) / 255.0;
    }

    public double getGreen() {
        return ((value >> 8) & 0xff) / 255.0;
    }

    public double getBlue() {
        return (value & 0xff) / 255.0;
    }
}
