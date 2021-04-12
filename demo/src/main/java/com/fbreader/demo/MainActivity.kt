package com.fbreader.demo

import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fbreader.common.FBReaderHelper
import org.geometerplus.android.fbreader.FBReader
import org.geometerplus.android.fbreader.FBReaderApplication

class MainActivity : AppCompatActivity() {

    val ROOT_PATH = Environment.getExternalStorageDirectory().path
    private var fbReaderHelper: FBReaderHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FBReaderApplication.init(this)
        fbReaderHelper = FBReaderHelper(this)
        findViewById<View>(R.id.btn).setOnClickListener {
            fbReaderHelper!!.bindToService {
                val book = fbReaderHelper!!.collection.getBookByFile("${ROOT_PATH}/test.epub")
                FBReader.openBook(this@MainActivity, book, null)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fbReaderHelper!!.bindToService(null)
    }

    override fun onPause() {
        fbReaderHelper!!.unBind()
        super.onPause()
    }
}