package sashjakk.weather.app.tools

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val SwipeRefreshLayout.onRefresh: Flow<Unit>
    get() = callbackFlow {
        setOnRefreshListener { offer(Unit) }
        awaitClose { setOnRefreshListener(null) }
    }