# 🕐 TrustedTime API Sample

Android app demonstrating Google's TrustedTime API for detecting device time manipulation.

<table>
<tr>
<td><img src="https://github.com/user-attachments/assets/a4e380d1-d04d-4c24-a5af-3e8979e5cc7e" height="500"></td>
</tr>
</table>


---

## ✨ Features

- 🔒 **TrustedTime API Integration** – Secure time from Google's servers  
- ⚠️ **Time Tampering Detection** – Detects manipulated device time  
- 📊 **Millisecond Precision** – Accurate time difference calculations  
- 🚨 **Smart Warnings** – Configurable tampering thresholds  
- 🎨 **Material 3 UI** – Modern Jetpack Compose interface  

---

## 🛠️ Tech Stack

- #Kotlin  
- #JetpackCompose  
- #Material3  
- #TrustedTimeAPI  
- #GooglePlayServices  

---

## 📚 Dependencies

Make sure to add the necessary dependencies in your `build.gradle` file:

```kotlin
  implementation("com.google.android.gms:play-services-time:16.0.1")
// TrustedTime API is bundled with Play Services
