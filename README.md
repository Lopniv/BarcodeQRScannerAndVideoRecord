# BarcodeQRScannerAndVideoRecord
This app contains feature barcode and QR scanner, and video recording in-app.

1. Scanner using ML Kit and CameraX.
2. Video Recording using CameraX


Using dependecies:
```
// ViewModel and LiveData
implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0"
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"

// Barcode scanning API
implementation 'com.google.mlkit:barcode-scanning:17.0.0'

// CameraX library
implementation "androidx.camera:camera-camera2:1.1.0-alpha11"
implementation "androidx.camera:camera-lifecycle:1.1.0-alpha11"
implementation "androidx.camera:camera-core:1.1.0-alpha11"
implementation "androidx.camera:camera-video:1.1.0-alpha11"
implementation "androidx.camera:camera-view:1.0.0-alpha31"
```
Code reference:
[Camera Sample Repository](https://github.com/android/camera-samples "Camera Sample Repository")
