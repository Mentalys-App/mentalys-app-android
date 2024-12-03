package com.mentalys.app.ui.youtube

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityPlayerBinding
import com.mentalys.app.utils.showToast

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var youTubeUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Restore or get the URL
        youTubeUrl = savedInstanceState?.getString(EXTRA_YOUTUBE_LINK)
            ?: intent.getStringExtra(EXTRA_YOUTUBE_LINK).toString()

        val iframeHtml = generateYouTubeIframe(youTubeUrl)

        val youtubeWebView = binding.youtubeWebView
        youtubeWebView.settings.javaScriptEnabled = true
        youtubeWebView.settings.domStorageEnabled = true
        iframeHtml?.let {
            youtubeWebView.loadData(it, "text/html", "utf-8")
        }

        showToast(this@PlayerActivity, "This is the url: $youTubeUrl")
    }

    private fun getYouTubeVideoId(url: String): String? {
        val regex = Regex("(?:https?://)?(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtu\\.be/)([a-zA-Z0-9_-]{11})")
        val matchResult = regex.find(url)
        return matchResult?.groups?.get(1)?.value
    }

    private fun generateYouTubeIframe(url: String): String? {
        val videoId = getYouTubeVideoId(url)
        return videoId?.let {
            """
            <iframe width="100%" height="100%" 
                src="https://www.youtube.com/embed/$it" 
                title="YouTube video player" 
                frameborder="0" 
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
                referrerpolicy="strict-origin-when-cross-origin" 
                allowfullscreen>
            </iframe>
            """.trimIndent()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_YOUTUBE_LINK, youTubeUrl)
    }

    companion object {
        const val EXTRA_YOUTUBE_LINK = "EXTRA_YOUTUBE_LINK"
    }

}