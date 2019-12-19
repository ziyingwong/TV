package com.example.superWiserTVV2

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.example.superWiserTVV2.R
import kotlinx.android.synthetic.main.webview.view.*

class GroupScene : Activity() {
    lateinit var ipAdd: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)
        ipAdd = resources.getString(R.string.ipAdd)

        var webView = findViewById<WebView>(R.id.mywebView)
        var progessBar = findViewById<ProgressBar>(R.id.progressBar)
        webView.settings.javaScriptEnabled = true
        var id = intent.getStringExtra("id")
//        var url = resources.getString(R.string.secureAdd)+"/board-viewer/${id}"
//        var url = "http://${ipAdd}:3000/board-viewer/${id}"
        var url = "https://board.opa-x.com/domain/demo/board-viewer/${id}"
        webView.visibility = View.INVISIBLE
        progessBar.visibility = View.VISIBLE
        webView.settings.mixedContentMode = 0
        webView.settings.domStorageEnabled = true
        webView.settings.useWideViewPort = true
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.setWebViewClient(object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.e("myTag", "error :${error}")
            }

            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl(
                    "javascript:document.querySelector(\"things-app\").shadowRoot.querySelector(\"header-bar\").remove();" +
                            "document.querySelector(\"things-app\").shadowRoot.querySelector(\"main\").querySelector(\"app-board-viewer-page\").shadowRoot.querySelector(\"board-viewer\").shadowRoot.querySelector(\"mwc-fab\").remove();" +
                            "document.querySelector(\"things-app\").shadowRoot.querySelector(\"footer-bar\").remove();" +
                            "document.querySelector(\"things-app\").shadowRoot.querySelector(\"snack-bar\").remove();"
                )
                progessBar.visibility = View.INVISIBLE
                webView.visibility = View.VISIBLE


            }

        })
        Log.e("myTag", url)
        webView.loadUrl(url)

    }
}