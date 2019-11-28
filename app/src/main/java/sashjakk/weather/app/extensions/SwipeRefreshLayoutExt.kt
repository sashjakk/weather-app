package sashjakk.weather.app.extensions

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
val SwipeRefreshLayout.onRefresh: Flow<Unit>
    get() = callbackFlow {
        setOnRefreshListener { offer(Unit) }
        awaitClose { setOnRefreshListener(null) }
    }