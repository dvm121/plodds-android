package com.viorel.plodds

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

  private lateinit var webView: WebView
  private lateinit var swipe: SwipeRefreshLayout

  private val startUrl = "https://viorel.infinityfree.me/PLodds/index.html"

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    swipe = findViewById(R.id.swipe)
    webView = findViewById(R.id.webview)

    swipe.setOnRefreshListener { webView.reload() }

    webView.settings.apply {
      javaScriptEnabled = true
      domStorageEnabled = true
      loadWithOverviewMode = true
      useWideViewPort = true
      builtInZoomControls = false
      displayZoomControls = false
    }

    webView.webChromeClient = WebChromeClient()
    webView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean = false
      override fun onPageFinished(view: WebView, url: String) { swipe.isRefreshing = false }
      override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
        swipe.isRefreshing = false
      }
    }

    if (savedInstanceState == null) webView.loadUrl(startUrl) else webView.restoreState(savedInstanceState)

    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        if (webView.canGoBack()) webView.goBack() else finish()
      }
    })
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    webView.saveState(outState)
  }
}
