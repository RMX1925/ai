# WhatsApp Auto-Reply App with Gemini AI

This Android app automatically replies to WhatsApp notifications containing the keyword `/ask` using the Gemini AI model.

## Table of Contents

- [Requirements](#requirements)
- [Setup](#setup)
- [Build and Run](#build-and-run)
- [Features](#features)
- [Documentation](#documentation)
- [License](#license)

## Requirements

1. Latest stable version of Android Studio.
2. API key from Google AI Studio. Follow the instructions on the [setup page](https://makersuite.google.com/app/apikey) to obtain an API key.

## Setup

1. Clone this repository or import the project from Android Studio.
2. Add your API Key to the `local.properties` file in this format:

    ```txt
    apiKey=YOUR_API_KEY
    ```

## Build and Run

1. Open the project in Android Studio.
2. Sync the project with Gradle files.
3. Run the app on an Android device or emulator.

## Features

- **Auto-Reply to WhatsApp Notifications**: The app listens for WhatsApp notifications containing the keyword `/ask` and replies using the Gemini AI model.
- **Text Generation**: Utilizes the Gemini AI model to generate text responses.
- **Accessibility Service**: Uses an accessibility service to read and respond to notifications.


## Documentation

You can find the quick start documentation for the Android Generative AI API [here](https://googledevai.google.com/tutorials/android_quickstart).

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.
