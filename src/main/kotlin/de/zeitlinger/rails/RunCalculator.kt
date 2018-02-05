package de.zeitlinger.rails

fun calculateBestRun(run: Run, tried: MutableSet<Run>, board: Board): Run {
    if (!tried.add(run)) {
        return run
    }

    val runs: List<Run> = run.trainRuns.flatMap { trainRun ->
        val train = trainRun.train

        val stations = trainRun.stations
        if (stations.isEmpty()) {
            run.homeStations.map { homeStation ->
                run.withStation(train, homeStation, ListPosition.TAIL, listOf())
            }
        } else {
            ListPosition.values().flatMap { listPosition ->
                val start = listPosition[stations]
                board.connections[start].orEmpty().flatMap { connection ->
                    val destination = connection.getDestination(start)

                    destination
                            .takeIf { canAddStation(connection, destination, stations, run.usedTracks, train.cities) }
                            ?.let { run.withStation(train, destination, listPosition, connection.tracks) }
                            .let { listOfNotNull(it) }
                }
            }
        }
    }
    return (runs.map { calculateBestRun(it, tried, board) } + listOf(run)).maxBy { it.value }!!
}

private fun Run.withStation(train: Train, station: Station, listPosition: ListPosition = ListPosition.TAIL, usedTracks: List<Track> = listOf()): Run {
    val trainRun = getTrainRun(trainRuns, train)
    val stations = trainRun.stations.run { toMutableList() }
            .apply { listPosition.add(this, station) }
            .apply {
                if (first().name > last().name) {
                    reverse() //avoid symmetry
                }
            }

    return copy(trainRuns = trainRuns.replace(trainRun, trainRun.copy(stations = stations)), usedTracks = this.usedTracks + usedTracks)
}

fun <T> Iterable<T>.replace(old: T, new: T): List<T> =
        map { if (it == old) new else it }

private fun canAddStation(connection: Connection, destination: Station, trainStations: List<Station>, usedTracks: Set<Track>, cities: Int): Boolean =
        (usedTracks.none { connection.tracks.contains(it) }
                && !trainStations.contains(destination)
                && trainStations.size < cities)

