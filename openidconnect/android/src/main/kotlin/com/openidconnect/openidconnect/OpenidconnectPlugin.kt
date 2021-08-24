package com.openidconnect.openidconnect

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** OpenidconnectPlugin */
class OpenidconnectPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "openidconnect")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "authenticate" -> {
        val url = Uri.parse(call.argument("url"))
        val callbackUrlScheme = call.argument<String>("callbackUrlScheme")!!

        callbacks[callbackUrlScheme] = resultCallback

        val intent = CustomTabsIntent.Builder().build()
        val keepAliveIntent = Intent(context, KeepAliveService::class.java)

        intent.intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.intent.putExtra("android.support.customtabs.extra.KEEP_ALIVE", keepAliveIntent)

        intent.launchUrl(context, url)
      }
      "cleanUpDanglingCalls" -> {
        callbacks.forEach{ (_, danglingResultCallback) ->
            danglingResultCallback.error("CANCELED", "User canceled login", null)
        }
        callbacks.clear()
        resultCallback.success(null)
      }
      else -> resultCallback.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
