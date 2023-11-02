package ofek.yariv.kmovies.utils.managers

import ofek.yariv.kmovies.model.data.TimeFrame

class TimeFrameManager {
    private var timeFrame = TimeFrame.DAY
    fun changeTimeFrame() {
        timeFrame = if (timeFrame == TimeFrame.DAY) TimeFrame.WEEK else TimeFrame.DAY
    }
    fun getTimeFrame() = timeFrame
}