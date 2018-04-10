import 'dart:async';

import 'package:flutter/services.dart';

class LocationPicker{

  static String apiKey = "";

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

  LocationPicker({
    this.titleText = "Pick your location",
    this.closeIfLocationDenied = false,
    this.useGeoCoder = true,
    this.initialLat = -33.86,
    this.initialLong = 151.20,
    this.enableMyLocation = true,
    this.androidAppBarColor = '#F44336',
    this.androidTitleAndBackButtonColor = '#FFFFFF',
    this.androidSelectButtonColor = '#F44336',
    this.androidNavBarColor = '#F44336',
    this.androidStatusBarColor = '#B71C1C',
    this.iosBackButtonText = 'Cancel',
    this.iosSelectButtonText = 'Select',
  });

  static void initApiKey(String key)
  {
    LocationPicker.apiKey = key;
  }

  static const MethodChannel _channel =
  const MethodChannel('location_picker_plugin');

  Future<Map<dynamic, dynamic>> get showLocationPicker async {
    if(LocationPicker.apiKey.isEmpty)
    {
      print("PLEASE SET MAPS API KEY");
      return new Map();
    }
    else
    {
      final Map<dynamic, dynamic> result = await _channel.invokeMethod('locationPicker', _buildArguments());
      return result;
    }
  }

  Map<dynamic, dynamic> _buildArguments(){

    Map<dynamic, dynamic> args = new Map();

    args['apiKey'] = LocationPicker.apiKey;
    args['titleText'] = titleText;
    args['closeIfLocationDenied'] = closeIfLocationDenied;
    args['useGeoCoder'] = useGeoCoder;
    args['initialLat'] = initialLat;
    args['initialLong'] = initialLong;
    args['enableMyLocation'] = enableMyLocation;
    args['androidAppBarColor'] = androidAppBarColor;
    args['androidTitleAndBackButtonColor'] = androidTitleAndBackButtonColor;
    args['androidSelectButtonColor'] = androidSelectButtonColor;
    args['androidNavBarColor'] = androidNavBarColor;
    args['androidStatusBarColor'] = androidStatusBarColor;
    args['iosBackButtonText'] = iosBackButtonText;
    args['iosSelectButtonText'] = iosSelectButtonText;

    return args;
  }
}
