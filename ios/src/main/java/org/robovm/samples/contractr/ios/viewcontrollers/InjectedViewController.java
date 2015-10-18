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
package org.robovm.samples.contractr.ios.viewcontrollers;

import org.robovm.apple.foundation.NSCoder;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.*;
import org.robovm.apple.uikit.UIBarButtonItem.OnClickListener;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.samples.contractr.ios.ContractRApp;

import java.lang.reflect.Method;

/**
 * Abstract {@link UIViewController} which handles dependency injection using Dagger.
 */
@CustomClass("InjectedViewController")
public abstract class InjectedViewController extends UIViewController {

    public InjectedViewController() {
        super();
        ContractRApp.getComponent().inject(this);
    }

    public InjectedViewController(NSCoder coder) {
        super(coder);
        ContractRApp.getComponent().inject(this);
    }
}
