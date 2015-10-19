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
package org.robovm.samples.contractr.ios;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAttributedString;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.*;
import org.robovm.samples.contractr.ios.viewcontrollers.RootViewController;

/**
 * App entry point.
 */
public class ContractRApp extends UIApplicationDelegateAdapter {

    public static final UIColor HIGHLIGHT_COLOR = 
            UIColor.fromRGBA(0x93 / 255.0, 0xc6 / 255.0, 0x23 / 255.0, 1.0);

    private static ContractRComponent component;

    public static ContractRComponent getComponent() {
        return component;
    }

    @Override
    public boolean didFinishLaunching(UIApplication application,
            UIApplicationLaunchOptions launchOptions) {

        /* Render tab bar images from the Ionicons TTF font. */
        RootViewController rootViewController = (RootViewController) getWindow().getRootViewController();
        UIFont ioniconsFont = UIFont.getFont("Ionicons", 30.0);
        UIImage workIconImage = createIconImage(ioniconsFont, '\uf1e1');
        UIImage reportsIconImage = createIconImage(ioniconsFont, '\uf2b5');
        UIImage clientsIconImage = createIconImage(ioniconsFont, '\uf1bf');
        UIImage tasksIconImage = createIconImage(ioniconsFont, '\uf16c');
        NSArray<UIViewController> viewControllers = rootViewController.getViewControllers();
        viewControllers.get(0).getTabBarItem().setImage(workIconImage);
        viewControllers.get(1).getTabBarItem().setImage(reportsIconImage);
        viewControllers.get(2).getTabBarItem().setImage(clientsIconImage);
        viewControllers.get(3).getTabBarItem().setImage(tasksIconImage);

        /* Customize the colors in the UI. */
        UITabBar appearanceTabBar = UIAppearance.getAppearance(UITabBar.class);
        appearanceTabBar.setTintColor(HIGHLIGHT_COLOR);
        UINavigationBar appearanceNavigationBar = UIAppearance.getAppearance(UINavigationBar.class);
        appearanceNavigationBar.setBarStyle(UIBarStyle.Black);
        appearanceNavigationBar.setBarTintColor(HIGHLIGHT_COLOR);
        appearanceNavigationBar.setTintColor(UIColor.white());
        UITableView appearanceTableView = UIAppearance.getAppearance(UITableView.class);
        appearanceTableView.setSeparatorColor(HIGHLIGHT_COLOR);
        UISwitch appearanceSwitch = UIAppearance.getAppearance(UISwitch.class);
        appearanceSwitch.setOnTintColor(HIGHLIGHT_COLOR);

        return true;
    }

    private CGRect calculateIconDrawingRect(NSAttributedString s, CGSize imageSize) {
        CGSize iconSize = s.getSize();
        double xOffset = (imageSize.getWidth() - iconSize.getWidth()) / 2.0;
        double yOffset = (imageSize.getHeight() - iconSize.getHeight()) / 2.0;
        return new CGRect(xOffset, yOffset, iconSize.getWidth(), iconSize.getHeight());
    }
    
    private UIImage createIconImage(UIFont font, char code) {
        // Create a 30x30 image on iOS 6 and 60x60 on later iOS versions.
        double side = System.getProperty("os.version").startsWith("6.") ? 30 : 60;
        CGSize imageSize = new CGSize(side, side);
        UIGraphics.beginImageContext(imageSize, false, 0.0);
        NSAttributedStringAttributes attributes = new NSAttributedStringAttributes();
        attributes.setFont(font);
        NSAttributedString s = new NSAttributedString(Character.toString(code), attributes);
        s.draw(calculateIconDrawingRect(s, imageSize));
        UIImage image = UIGraphics.getImageFromCurrentImageContext();
        UIGraphics.endImageContext();
        return image;
    }

    public static void main(String[] args) {

        component = DaggerContractRComponent.builder()
                .contractRModule(new ContractRModule())
                .build();

        try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
            UIApplication.main(args, null, ContractRApp.class);
        }
    }

}
