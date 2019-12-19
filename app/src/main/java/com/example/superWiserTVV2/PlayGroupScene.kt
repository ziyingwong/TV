package com.example.superWiserTVV2

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar


class PlayGroupScene : Fragment() {
    lateinit var ipAdd: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        //start new timer
        var myFragmentManager = activity!!.supportFragmentManager
        DataContainer.isRunning = true
        DataContainer.counter = DataContainer.timeInterval
        DataContainer.initTimer(myFragmentManager, DataContainer.timeInterval.toLong())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var myView = inflater.inflate(R.layout.webview, container, false)
        var webView = myView.findViewById<WebView>(R.id.mywebView)
        var myFragmentManager = activity!!.supportFragmentManager

        ipAdd = resources.getString(R.string.ipAdd)
        var progressBar = myView.findViewById<ProgressBar>(R.id.progressBar)
        var url = "https://board.opa-x.com/domain/demo/board-viewer/"
//        var url = "http://${ipAdd}:3000/board-viewer/"
        webView.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE


        //webView setting
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.loadUrl(
                    "javascript:document.querySelector(\"things-app\").shadowRoot.querySelector(\"header-bar\").remove();" +
                            "document.querySelector(\"things-app\").shadowRoot.querySelector(\"main\").querySelector(\"app-board-viewer-page\").shadowRoot.querySelector(\"board-viewer\").shadowRoot.querySelector(\"mwc-fab\").remove();" +
                            "document.querySelector(\"things-app\").shadowRoot.querySelector(\"footer-bar\").remove();" +
                            "document.querySelector(\"things-app\").shadowRoot.querySelector(\"snack-bar\").remove();"
                )
                progressBar.visibility = View.INVISIBLE
                webView.visibility = View.VISIBLE
            }

            override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                return true
            }
        }
        if (DataContainer.selected == 6) {
            DataContainer.selected = 0
        } else if (DataContainer.selected < 0) {
            DataContainer.selected = 6 - 1
        }
        if (DataContainer.selected == DataContainer.playscene.size) {
            DataContainer.selected = 0
        } else if (DataContainer.selected < 0) {
            DataContainer.selected = DataContainer.playscene.size - 1
        }
        webView.loadUrl(url + DataContainer.playscene.get(DataContainer.selected).id)
//        webView.loadUrl("https://www.google.com.my")
        return myView
    }

}