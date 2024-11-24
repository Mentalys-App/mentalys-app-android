# Mentalys App

Mentalys is a mental health detection app designed to provide insights and support for mental well-being.

## Prerequisites

1. **Android Studio**: Use **Android Studio - Ladybug** (or later versions).  
   - The app uses Android Core Library version **1.15.0**, which requires **compileSdkVersion 35**.  
   - If you need to use **compileSdkVersion 34**, downgrade the Android Core Library to version **1.12.0** in the `build.gradle` file.  
   - There's no strict requirement to update your Android Studio, as long as the version supports the desired SDK settings.

2. **Android SDK**: Ensure the following SDK settings in `build.gradle`:
   - `compileSdkVersion` set to **35**
   - `minSdkVersion` set to **35**

## Getting Started

### Step 1: Clone the Repository

Clone this repository to your local machine using:

```bash
git clone https://github.com/Mentalys-App/mentalys-app-android.git
```

### Step 2: Open the Project in Android Studio

1. Open Android Studio.
2. Go to **File > Open** and select the directory where you cloned the repository.
3. Allow Android Studio to set up the project and install any required dependencies.

### Step 3: Add Your Gemini API Key

The app uses the Gemini API for its functionality. Follow these steps to include your API key:

1. Open the `local.properties` file located in the root of the project directory.
2. Add your Gemini API key using the following format:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   ```
3. Save the file.

The API key will automatically be loaded into the app using the project's Gradle configuration. Ensure you do not commit the `local.properties` file to version control to keep your key secure.

### Step 4: Sync and Build the Project

1. Sync the project with Gradle by clicking **Sync Now** or by selecting **File > Sync Project with Gradle Files**.
2. If there are SDK version issues, confirm that:
   - `compileSdkVersion` and `targetSdkVersion` are set to **35** in `build.gradle`.
   - `minSdkVersion` is updated to **35** in `build.gradle`.

### Step 5: Run the App

1. Connect an Android device with **Developer Options** and **USB Debugging** enabled, or set up an Android Emulator in Android Studio.
2. Click on the **Run** button or press `Shift + F10` to build and run the app on your selected device.
