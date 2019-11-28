package sashjakk.weather.app.tools

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val margin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val right = if (position % 2 == 0) margin else margin / 2
        val left = if (position % 2 == 0) margin / 2 else margin

        outRect.set(left, margin, right, margin)
    }
}