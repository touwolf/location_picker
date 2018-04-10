# location_picker

A location picker plugin for Flutter.
 
## What it does

As the name implies, this is a component for getting a desired location from the user. 
It handles location permissions requests, a native map view and it return a map with all the data.
It also uses reversed GeoCoding for info associated with the selected location.     

<img src="https://raw.githubusercontent.com/touwolf/location_picker/master/android.jpg" width="300">    <img src="https://raw.githubusercontent.com/touwolf/location_picker/master/ios.jpeg" width="300">


## Example

 ```dart
        //NECESSARY
        LocationPicker.initApiKey('YOUR API KEY');
        
        LocationPicker picker = new LocationPicker();
        
        try 
        {
          result = await picker.showLocationPicker;
        } 
          on PlatformException 
        {
          //HANDLE ERROR
        }
        
        setState(() {_locationPickerData = result.toString();});
 ```

### Configuration

The constructor
    
    new LocationPicker();
    
Takes the following optional parameters for configuration:

```dart
      String titleText;
      bool closeIfLocationDenied;
      bool useGeoCoder;
      double initialLat;
      double initialLong;
      bool enableMyLocation;
    
      //ANDROID ONLY
      String androidAppBarColor;
      String androidNavBarColor;
      String androidStatusBarColor;
      String androidSelectButtonColor;
      String androidTitleAndBackButtonColor;
    
      //IOS ONLY
      String iosBackButtonText;
      String iosSelectButtonText;
```

## Importing 

1. Add this line to your pubspec.yaml

        location_picker: ^0.0.1

## Installing

1. Get your Google Maps Api Key.

### Android

1. Request permissions in your manifest

    ```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    ``` 

2. Add your Android API Key in your manifest inside the application tag

    ```xml
    <meta-data
     android:name="com.google.android.geo.API_KEY"
     android:value="YOUR API KEY"/>
    ```
     
### IOS

1. Request permissions in your project's info.plist

    ```xml
   <key>NSLocationWhenInUseUsageDescription</key>
   <string>Your message to the user</string>
  	```



## Getting Started

For help getting started with Flutter, view our online
[documentation](https://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).
