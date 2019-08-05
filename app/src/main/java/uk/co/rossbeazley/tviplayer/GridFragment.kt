package uk.co.rossbeazley.tviplayer

import android.app.Activity
import java.util.Timer
import java.util.TimerTask

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseFragment
import androidx.core.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.navigation.Navigation

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget

/**
 * Loads a grid of cards with movies to browse.
 *
 * https://ibl.api.bbci.co.uk/ibl/v1/categories/comedy/programmes?per_page=200
 *
 * https://ibl.api.bbci.co.uk/ibl/v1/categories/
 */
class GridFragment : BrowseSupportFragment() {

    private val mHandler = Handler()
    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private var mBackgroundTimer: Timer? = null
    private var mBackgroundUri: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

        category = arguments?.getSerializable(MainActivity.ITEM) as Category

        programsRepository = ProgramsRepository(activity!!)
        episodesRepository = EpisodesRepository(activity!!)

        prepareBackgroundManager()

        setupUIElements()

        loadRows()

        setupEventListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: " + mBackgroundTimer?.toString())
        mBackgroundTimer?.cancel()
    }

    private fun prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(activity)
        if(mBackgroundManager.isAttached) {

        } else {
            mBackgroundManager.attach(activity!!.window)
        }
        mDefaultBackground = ContextCompat.getDrawable(activity!!, R.drawable.default_background)
        mMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    private fun setupUIElements() {
        title = category.title
        // over title
        headersState = BrowseFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(activity!!, R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(activity!!, R.color.search_opaque)
    }

    private lateinit var category: Category
    private lateinit var programsRepository: ProgramsRepository
    private lateinit var episodesRepository: EpisodesRepository


    private fun loadRows() {

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        programsRepository.programs(category.id) {

            val rows = mutableListOf<EpisodeListRow>()
            it.forEach { p ->
                val episodeItems = episodeItems(p)
                rows+=episodeItems
            }
            rowsAdapter.addAll(0,rows)
        }

        adapter = rowsAdapter
    }

    private fun episodeItems(programme: Program): EpisodeListRow {
        val cardPresenter = EpisodeCardPresenter()
        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        episodesRepository.programEpisodes(programme.id) {
            listRowAdapter.addAll(0, it)
        }
        val header = HeaderItem(0, programme.title)
        val listRow = EpisodeListRow(programme.id, header, listRowAdapter)
        return listRow
    }

    class EpisodeListRow(val pid : String, header: HeaderItem?, adapter: ObjectAdapter?) : ListRow(header, adapter)

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(activity, "Implement your own in-app search", Toast.LENGTH_LONG)
                .show()
        }

        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {

            if (item is Episode) {

                val itemBundle = Bundle()
                itemBundle.putSerializable(MainActivity.ITEM,item)

                Navigation.findNavController(itemViewHolder.view).navigate(R.id.action_gridFragment_to_playbackVideoFragment,itemBundle)
            }

        }
    }


    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?, item: Any?,
            rowViewHolder: RowPresenter.ViewHolder, row: Row
        ) {
            if(item is Episode) {
                mBackgroundUri = item.backgroundImageUrl
                startBackgroundTimer()
            }
        }
    }

    private fun updateBackground(uri: String?) {
        val width = mMetrics.widthPixels
        val height = mMetrics.heightPixels
        Glide.with(activity)
            .load(uri)
            .centerCrop()
            .error(mDefaultBackground)
            .into<SimpleTarget<GlideDrawable>>(
                object : SimpleTarget<GlideDrawable>(width, height) {
                    override fun onResourceReady(
                        resource: GlideDrawable,
                        glideAnimation: GlideAnimation<in GlideDrawable>
                    ) {
                        mBackgroundManager.drawable = resource
                    }
                })
        mBackgroundTimer?.cancel()
    }

    private fun startBackgroundTimer() {
        mBackgroundTimer?.cancel()
        mBackgroundTimer = Timer()
        mBackgroundTimer?.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY.toLong())
    }

    private inner class UpdateBackgroundTask : TimerTask() {

        override fun run() {
            mHandler.post { updateBackground(mBackgroundUri) }
        }
    }


    companion object {
        private val TAG = "MainFragment"

        private val BACKGROUND_UPDATE_DELAY = 200
        private val GRID_ITEM_WIDTH = 200
        private val GRID_ITEM_HEIGHT = 200
        private val NUM_ROWS = 6
        private val NUM_COLS = 15
    }
}

