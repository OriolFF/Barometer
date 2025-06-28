# Barometer App

This project is a fully functional Barometer application for Android, built to serve as a practical exercise in modern Android development with Jetpack Compose. It showcases a clean, scalable architecture and a feature-rich, custom UI.

## Purpose

The primary goal of this project is to practice and demonstrate proficiency in building a modern Android application from the ground up using **Jetpack Compose** and a **clean, MVI-based architecture**.

## Features

*   **Dual Display**: Combines a classic, custom-drawn **Analog Barometer** with a precise **Digital Readout**.
*   **Real-Time Data**: Utilizes the device's built-in pressure sensor (`TYPE_PRESSURE`) to display live atmospheric pressure.
*   **Tendency Indicator**: The analog gauge includes a secondary needle to track pressure trends over time.
*   **Optimized UI**: The UI is built entirely with Jetpack Compose, with performance optimizations such as caching static drawing elements and smooth animations for needle transitions.
*   **Robust Architecture**: Built on a solid MVI (Model-View-Intent) architecture for a clear separation of concerns.

![Analog Barometer Screenshot](/external/screen_shot_analog.png)

## Architecture & Tech Stack

This project follows modern Android architecture principles to ensure it is scalable, maintainable, and testable.

*   **Architecture**: Model-View-Intent (MVI)
*   **UI**: Jetpack Compose
*   **Dependency Injection**: Koin
*   **Asynchronous Programming**: Kotlin Coroutines & Flow
*   **Language**: Kotlin

## Key Components

The application is structured into several key components:

*   **`data` layer**: Handles the data sources, including the `RealBarometerDataSource` which interfaces with the Android SensorManager.
*   **`domain` layer**: Contains the business logic, use cases (e.g., `SubscribeBarometerUseCase`), and repository interfaces.
*   **`ui` layer**: Holds all the Jetpack Compose UI components, ViewModels, and state management.
    *   **`AnalogBarometerScreen`**: A custom-drawn, skeuomorphic analog barometer gauge.
    *   **`DigitalBarometerScreen`**: A retro-style digital display for precise pressure readings.
    *   **`BarometerViewModel`**: Manages the UI state and connects the domain layer to the UI.

## How to Run

1.  Clone the repository.
2.  Open the project in Android Studio.
3.  Build and run on an Android device or emulator that has a pressure sensor.
