package com.jumpstart.locationpicker;

import android.app.Activity;
import android.content.Intent;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * LocationPickerPlugin
 */
public class LocationPickerPlugin implements MethodCallHandler {
  /**
   * Plugin registration.
   */
  static MethodCall call;
  static Result result;
  private Activity activity;

  private LocationPickerPlugin(Activity activity)
  {
    this.activity = activity;
  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "location_picker_plugin");

    channel.setMethodCallHandler(new LocationPickerPlugin(registrar.activity()));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("locationPicker")) {
      LocationPickerPlugin.call = call;
      LocationPickerPlugin.result = result;
      Intent intent = new Intent(activity, LocationPickerActivity.class);
      activity.startActivity(intent);
    } else {
      result.notImplemented();
    }
  }
}
