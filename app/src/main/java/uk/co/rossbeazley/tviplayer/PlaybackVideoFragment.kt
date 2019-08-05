package uk.co.rossbeazley.tviplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.leanback.app.PlaybackSupportFragment
import androidx.leanback.app.PlaybackSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.navigation.Navigation
import uk.co.bbc.httpclient.useragent.UserAgent
import uk.co.bbc.smpan.SMP
import uk.co.bbc.smpan.SMPBuilder
import uk.co.bbc.smpan.media.PlayRequest
import uk.co.bbc.smpan.media.model.MediaContentVpid
import uk.co.bbc.smpan.media.model.MediaMetadata
import uk.co.bbc.smpan.stats.av.AVStatisticsProvider
import uk.co.bbc.smpan.stats.ui.UserInteractionStatisticsProvider

/** Handles video playback with media controls. */
class PlaybackVideoFragment : PlaybackSupportFragment() {

    private lateinit var smp: SMP

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.video, container, false)
        val placeholder = root.findViewById<FrameLayout>(R.id.placeholder)

        val stats = object : AVStatisticsProvider {}

        val pr = PlayRequest.create(
            MediaContentVpid(episode.vpid, UserAgent("beazleysAndroidTVApp", "1.0"), "iptv-all"),
            MediaMetadata.MediaType.ONDEMAND,
            MediaMetadata.MediaAvType.VIDEO,
            stats
        )
        pr.withAutoplay(true)

        smp.load(pr.build())
        smp.createMediaLayer().attachToViewGroup(placeholder)
        smp.addEndedListener {
            Navigation.findNavController(container!!).popBackStack()
        }
        return root
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        smp.stop()
    }


    private lateinit var episode: Episode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        episode = arguments?.getSerializable(MainActivity.ITEM) as Episode

        smp = SMPBuilder.create(context, UserAgent("", ""), UserInteractionStatisticsProvider.NULL).build()
        val playerGlue = PlaybackTransportControlGlue(
            activity,
            PlaybackSMPAdapter(smp)
        )
        val playbackSupportFragmentGlueHost = PlaybackSupportFragmentGlueHost(this)
        playbackSupportFragmentGlueHost.showControlsOverlay(true)
        playerGlue.host = playbackSupportFragmentGlueHost
        playerGlue.isSeekEnabled = true
        playerGlue.subtitle = episode.subtitle
        playerGlue.title = episode.title
    }
}
