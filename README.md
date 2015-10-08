# RoboVM ContractR sample

Sample app for iOS and Android which lets you add Clients, Tasks and keep
track of the time you are working on these tasks and how much money your work
is generating.

This sample gives an idea of how to share code between an iOS and Android app
using native UI in both apps. The iOS and Android projects are using a shared
core project which holds the Model part of the Model View Controller pattern.

## iOS app

The iOS app is built using native UI components and APIs available through the
Cocoa bindings in RoboVM. This project depends on the core project which holds
all domain objects and services for managing clients, tasks and the work
performed.

## Android app

The Android app is built using standard Android components such as view XMLs,
Fragments and ActionBar.
