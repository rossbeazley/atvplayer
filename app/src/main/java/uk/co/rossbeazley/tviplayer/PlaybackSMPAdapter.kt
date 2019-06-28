package uk.co.rossbeazley.tviplayer

import androidx.leanback.media.PlaybackGlueHost
import androidx.leanback.media.PlayerAdapter
import uk.co.bbc.smpan.SMP
import uk.co.bbc.smpan.SMPObservable
import uk.co.bbc.smpan.playercontroller.media.MediaPosition
import uk.co.bbc.smpan.playercontroller.media.MediaProgress

class PlaybackSMPAdapter(val smp: SMP) : PlayerAdapter(), SMPObservable.PlayerState.Playing {

    var inPlaying = false
    var hasPrepared = false

    override fun playing() {
        inPlaying = true
        hasPrepared = true
        callback?.onPlayStateChanged(this)
    }

    override fun leavingPlaying() {
        inPlaying = false
        callback?.onPlayStateChanged(this)
    }

    private var mediaProgress: MediaProgress? = null

    init {
        smp.addProgressListener {
            mediaProgress = it
            callback?.onCurrentPositionChanged(this)
            callback?.onDurationChanged(this)

        }
        smp.addPlayingListener(this)
    }
    override fun pause() {
        smp.pause()
    }

    override fun play() {
        smp.play()
    }

    override fun getCurrentPosition(): Long {
        return mediaProgress?.positionInMilliseconds ?: 0
    }

    override fun getDuration(): Long {
        return mediaProgress?.endTimeInMilliseconds ?: 0
    }

    override fun isPlaying(): Boolean {
        return inPlaying
    }

    override fun isPrepared(): Boolean {
        return hasPrepared
    }

    override fun seekTo(positionInMs: Long) {
        smp.seekTo(MediaPosition.fromMilliseconds(positionInMs))
    }
}
