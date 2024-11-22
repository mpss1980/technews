package br.com.alura.technewsupdated.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.alura.technewsupdated.R

class NewsViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_viewer)
    }
}