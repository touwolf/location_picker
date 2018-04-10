import Flutter
import UIKit
import GoogleMaps

public class SwiftLocationPickerPlugin: NSObject, FlutterPlugin {
  static var controller: UIViewController?
     static var call: FlutterMethodCall?
     static var result: FlutterResult?

     public static func register(with registrar: FlutterPluginRegistrar) {
         let channel = FlutterMethodChannel(name: "location_picker_plugin", binaryMessenger: registrar.messenger())
         let instance = SwiftLocationPickerPlugin()
         let app = UIApplication.shared
         let keyWindow = app.delegate?.window
         let rootViewController = keyWindow??.rootViewController
         self.controller = rootViewController
         registrar.addMethodCallDelegate(instance, channel: channel)
     }

     public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
         if call.method == "getPlatformVersion"
         {
             result("iOS " + UIDevice.current.systemVersion)
         }
         else if call.method == "locationPicker"
         {
             SwiftLocationPickerPlugin.call = call
             SwiftLocationPickerPlugin.result = result
             let args : [String : AnyObject?]  = SwiftLocationPickerPlugin.call?.arguments as! [String : AnyObject?]
             GMSServices.provideAPIKey(args["apiKey"] as! String)
             let nav = UINavigationController(rootViewController: LocationPickerViewController())
             SwiftLocationPickerPlugin.controller?.present(nav, animated: true, completion: nil)
         }
         else
         {
             result(FlutterMethodNotImplemented)
         }
     }
}
