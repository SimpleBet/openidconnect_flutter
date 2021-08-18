#import "OpenidconnectPlugin.h"
#if __has_include(<openidconnect/openidconnect-Swift.h>)
#import <openidconnect/openidconnect-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "openidconnect-Swift.h"
#endif

@implementation OpenidconnectPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftOpenidconnectPlugin registerWithRegistrar:registrar];
}
@end
