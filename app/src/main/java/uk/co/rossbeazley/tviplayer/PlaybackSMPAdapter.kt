package uk.co.rossbeazley.tviplayer

import androidx.leanback.media.PlayerAdapter
import uk.co.bbc.smpan.SMP
import uk.co.bbc.smpan.SMPObservable
import uk.co.bbc.smpan.media.errors.SMPError
import uk.co.bbc.smpan.playercontroller.media.MediaPosition
import uk.co.bbc.smpan.playercontroller.media.MediaProgress

class PlaybackSMPAdapter(val smp: SMP) : PlayerAdapter(), SMPObservable.PlayerState.Playing,
                                                          SMPObservable.PlayerState.Loading

{

    override fun leavingLoading() {
        callback?.onBufferingStateChanged(this, false)
    }

    override fun loading() {
        callback?.onBufferingStateChanged(this, true)
    }

    var inPlaying = false
    var hasPrepared = false

    override fun playing() {
        inPlaying = true
        if(!hasPrepared) {
            hasPrepared = true
            callback?.onPreparedStateChanged(this)
        }
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
        smp.addErrorStateListener( object : SMPObservable.PlayerState.Error {
            override fun leavingError() {

            }

            override fun error(p0: SMPError?) {
                callback?.onError(this@PlaybackSMPAdapter, 0, p0?.message())
            }
        })
        smp.addEndedListener { callback?.onPlayCompleted(this) }
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
