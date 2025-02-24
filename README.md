# Mentalys App

<div align="center">
  <img src="https://github.com/user-attachments/assets/c8bdda3a-e41e-4163-997e-dcd587402900" 
  alt="Mentalys Logo" width="120"/>
  <h3>AI-Powered Mental Health Detection</h3>
</div>

---

Mentalys is a comprehensive mental health detection app designed to provide insights and support for mental well-being using advanced AI technology.

## 🧠 Key Features

Our app integrates **three powerful AI detection methods** to provide a holistic assessment of mental health:

- **📝 Handwriting Analysis AI** - Detect emotional states and potential mental health concerns through handwriting patterns
- **🗣️ Voice Analysis AI** - Analyze voice patterns, tone, and speech characteristics to identify potential signs of depression, anxiety, and stress
- **📋 AI-Enhanced Questionnaire** - Complete scientifically validated mental health assessments with intelligent analysis

## 📱 Requirements

- Android 7.0 (API level 24) or higher
- 100MB+ free storage space
- Internet connection for AI processing

## 👩‍💻 For Developers

### Prerequisites

#### 1. Android Studio
- Use **Android Studio - Ladybug** or a later version
- The app requires **compileSdkVersion 35** for Android Core Library version **1.15.0**
- If you prefer **compileSdkVersion 34**, downgrade the Android Core Library to version **1.12.0** in the `build.gradle` file

#### 2. Android SDK
Ensure your `build.gradle` contains:
- `compileSdkVersion 35`
- `minSdkVersion 24`

#### 3. GitHub Personal Access Token
- To ensure the **Liquid Swiper** dependency works correctly, you'll need to configure authentication for GitHub Packages
- Add your **GitHub user ID** (`gpr.user`) and **GitHub personal access token** (`gpr.key`) with the `read:packages` permission

### Getting Started

#### Step 1: Clone the Repository
```bash
git clone https://github.com/Mentalys-App/mentalys-app-android.git
```

#### Step 2: Configure GitHub Authentication for Liquid Swiper
1. Open the `local.properties` file in the root directory of the project
2. Add the following lines with your **GitHub credentials**:
   ```properties
   gpr.user=your_github_user_id
   gpr.key=your_github_personal_access_token
   ```
3. Save the file
   > **Note:** Ensure your personal access token has the `read:packages` permission

#### Step 3: Add Build Properties
1. Make sure your `local.properties` file includes the necessary properties for build configuration:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   MAPS_API_KEY=your_maps_api_key_here
   FREE_SOUND_API_KEY=your_free_sound_api_key_here
   ```
2. For a full list of properties, refer to the build configuration section in [app/build.gradle.kts](https://github.com/Mentalys-App/mentalys-app-android/blob/master/app/build.gradle.kts) to see all required `buildConfigField` entries

> **Important:** Even though **Free Sound** is not currently in use, you must still provide the `FREE_SOUND_API_KEY` because the code references it during the build process

#### Step 4: Open and Sync the Project
1. Open Android Studio
2. Select **File > Open** and locate the cloned repository
3. Allow Android Studio to configure the project and download required dependencies
4. Sync the project with Gradle:
   - Click **Sync Now**, or
   - Select **File > Sync Project with Gradle Files**

#### Step 5: Configure SDK Versions (if needed)
If there are SDK version issues, verify the following settings in `build.gradle`:
```groovy
compileSdkVersion 35
targetSdkVersion 35
minSdkVersion 24
```

#### Step 6: Build and Run the App
1. Connect a physical Android device with **Developer Options** and **USB Debugging** enabled, or set up an Android Emulator in Android Studio
2. Build and run the app:
   - Click the **Run** button, or
   - Use the shortcut `Shift + F10`

### Troubleshooting
- **Gradle Sync Errors:** Ensure you have correctly added `gpr.user` and `gpr.key` in `local.properties`
- **Liquid Swiper Issues:** Verify your GitHub personal access token has the `read:packages` permission
- **Build Errors:** Confirm all required keys and properties are set in `local.properties`

## 🎬 Demo

<div align="center"> <a href="https://youtu.be/cnL0X4vDjjU?si=E8J829xOeYCFZXYn" target="_blank"> <img src="https://img.youtube.com/vi/cnL0X4vDjjU/0.jpg" alt="Mentalys App Demo Video" width="480"> </a> <p><b>👆 Click to watch the demo video</b></p> </div>

## 📊 Bangkit Academy Capstone Project

This app was developed as a capstone project for **Bangkit Academy Batch 2 2024**, integrating mobile development, cloud engineering, and machine learning.

## 📄 License

Mentalys App is released under the [MIT License](https://github.com/Mentalys-App/mentalys-app-android?tab=MIT-1-ov-file).

## 📞 Support

For support or questions, please [open an issue](https://github.com/Mentalys-App/mentalys-app-android/issues).

---

<div align="center">
  <p>Made with ❤️ by the Mentalys Team</p>
</div>