# Mentalys App

Mentalys is a mental health detection app designed to provide insights and support for mental well-being.

## Prerequisites

1. **Android Studio**: Download and install the latest version of [Android Studio](https://developer.android.com/studio), currently **Android Studio - Ladybug**.
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

### Step 3: Sync and Build the Project

1. Sync the project with Gradle by clicking **Sync Now** or by selecting **File > Sync Project with Gradle Files**.
2. If there are SDK version issues, confirm that:
   - `compileSdkVersion` and `targetSdkVersion` are set to **35** in `build.gradle`.
   - `minSdkVersion` is updated to **35** in `build.gradle`.

### Step 4: Run the App

1. Connect an Android device with **Developer Options** and **USB Debugging** enabled, or set up an Android Emulator in Android Studio.
2. Click on the **Run** button or press `Shift + F10` to build and run the app on your selected device.
