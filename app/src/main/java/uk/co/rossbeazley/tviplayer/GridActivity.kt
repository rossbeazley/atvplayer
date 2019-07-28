/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package uk.co.rossbeazley.tviplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Loads [MainFragment].
 */
class GridActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
    }
}


fun Activity.navigateToPlayback(item: Episode) {
    val intent = Intent(this, PlaybackActivity::class.java)
    intent.putExtra(MainActivity.ITEM, item)

    startActivity(intent)


//        val playbackVideoFragment = PlaybackVideoFragment()
//        val serializableExtra = intent.getSerializableExtra(MainActivity.ITEM)
//        val bundle = Bundle()
//        bundle.putSerializable(MainActivity.ITEM, serializableExtra)
//        playbackVideoFragment.arguments = bundle
//        this.fragmentManager.beginTransaction()
//            .replace(android.R.id.content, playbackVideoFragment)
//            .commit()
}