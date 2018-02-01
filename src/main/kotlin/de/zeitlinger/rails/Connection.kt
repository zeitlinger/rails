package de.zeitlinger.rails

class Connection(private val start: Track, private val end: Track) {

    val tracks: List<Track>
        get() = listOf(start, end)

    fun getDestination(station: Station): Station {
        if (start.station == station) {
            return end.station
        }
        if (end.station == station) {
            return start.station
        }
        throw IllegalArgumentException("station not in connection")
    }
}
