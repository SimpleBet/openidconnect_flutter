package com.openidconnect.openidconnect

import android.app.Activity
import android.os.Bundle

class CallbackActivity: Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val url = intent?.data
    val scheme = url?.scheme

    if (scheme != null) {
      val resultCallback = OpenidconnectPlugin.callbacks[scheme]
      OpenidconnectPlugin.callbacks.remove(scheme)
      resultCallback?.success(url.toString())
    }

    finish()
  }
}