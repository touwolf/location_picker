#import "LocationPickerPlugin.h"
#import <location_picker/location_picker-Swift.h>

@implementation LocationPickerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftLocationPickerPlugin registerWithRegistrar:registrar];
}
@end
