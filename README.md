# Mentalys App

Mentalys is a mental health detection app designed to provide insights and support for mental well-being.

---

## **Prerequisites**

### 1. **Android Studio**
   - Use **Android Studio - Ladybug** or a later version.
   - The app requires **compileSdkVersion 35** for Android Core Library version **1.15.0**.  
     If you prefer **compileSdkVersion 34**, downgrade the Android Core Library to version **1.12.0** in the `build.gradle` file.
   - While updating Android Studio is recommended, it's not mandatory as long as your version supports the required SDK settings.

### 2. **Android SDK**
   Ensure your `build.gradle` contains:
   - `compileSdkVersion 35`
   - `minSdkVersion 24`

### 3. **GitHub Personal Access Token**
   - To ensure the **Liquid Swiper** dependency works correctly, you'll need to configure authentication for GitHub Packages.
   - Add your **GitHub user ID** (`gpr.user`) and **GitHub personal access token** (`gpr.key`) with the `read:packages` permission.  

---

## **Getting Started**

### Step 1: Clone the Repository
Clone this repository to your local machine:
```bash
git clone https://github.com/Mentalys-App/mentalys-app-android.git
```

### Step 2: Configure GitHub Authentication for Liquid Swiper
To enable access to the **Liquid Swiper** dependency:

1. Open the `local.properties` file in the root directory of the project.
2. Add the following lines with your **GitHub credentials**:
   ```properties
   gpr.user=your_github_user_id
   gpr.key=your_github_personal_access_token
   ```
3. Save the file.

   > **Note:** Ensure your personal access token has the `read:packages` permission.

---

### Step 3: Add Build Properties

1. Make sure your `local.properties` file includes the necessary properties for build configuration. Here's an example of some required properties:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   MAPS_API_KEY=your_maps_api_key_here
   FREE_SOUND_API_KEY=your_free_sound_api_key_here
   ```
2. For a full list of properties, refer to the build configuration section in your project:
   - Open `[app/build.gradle.kts]([url](https://github.com/Mentalys-App/mentalys-app-android/blob/master/app/build.gradle.kts))` to see all the required `buildConfigField` entries.

> **Important:** Even though **Free Sound** is not currently in use, you must still provide the `FREE_SOUND_API_KEY` because the code references it during the build process.

---

### Step 4: Open and Sync the Project
1. Open Android Studio.
2. Select **File > Open** and locate the cloned repository.
3. Allow Android Studio to configure the project and download required dependencies.
4. Sync the project with Gradle:
   - Click **Sync Now**, or
   - Select **File > Sync Project with Gradle Files**.

---

### Step 5: Configure SDK Versions (if needed)
If there are SDK version issues, verify the following settings in `build.gradle`:

- **Module-level `build.gradle`:**
  ```groovy
  compileSdkVersion 35
  targetSdkVersion 35
  minSdkVersion 24
  ```

---

### Step 6: Build and Run the App
1. Connect a physical Android device with **Developer Options** and **USB Debugging** enabled, or set up an Android Emulator in Android Studio.
2. Build and run the app:
   - Click the **Run** button, or
   - Use the shortcut `Shift + F10`.

---

## **Troubleshooting**

- **Gradle Sync Errors:**
  Ensure you have correctly added `gpr.user` and `gpr.key` in `local.properties`.
- **Liquid Swiper Issues:**
  Verify your GitHub personal access token has the `read:packages` permission.
- **Build Errors:**
  Confirm all required keys and properties are set in `local.properties`.

---

## **License**
Mentalys App is released under the [MIT License](https://github.com/Mentalys-App/mentalys-app-android?tab=MIT-1-ov-file).

For support or questions, please [open an issue](https://github.com/Mentalys-App/mentalys-app-android/issues).

---

