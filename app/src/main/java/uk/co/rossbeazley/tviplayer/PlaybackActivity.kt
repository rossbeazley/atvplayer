package uk.co.rossbeazley.tviplayer

import android.R
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/** Loads [PlaybackVideoFragment]. */
class PlaybackActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val playbackVideoFragment = PlaybackVideoFragment()
            val serializableExtra = intent.getSerializableExtra(MainActivity.ITEM)
            val bundle = Bundle()
            bundle.putSerializable(MainActivity.ITEM, serializableExtra)
            playbackVideoFragment.arguments = bundle
            supportFragmentManager.beginTransaction()

                .replace(R.id.content, playbackVideoFragment)
                .commit()
        }
    }
}
