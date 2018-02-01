package de.zeitlinger.rails

import java.util.*

class RunCalculator {

    fun calculateBestRun(run: Run): Run? {
        var best: Run? = null

        for (trainRun in run.getTrainRuns()) {
            val stations = trainRun.stations
            if (stations.isEmpty()) {
                for (station in run.homeStations) {
                    val next = Run(run)
                    next.getTrainRun(trainRun.train).stations.add(station)
                    best = getBestRun(best, next)
                }
                //the rest is placed in the next iteration
                return best
            }

            if (!trainRun.isComplete) {
                for (listPosition in ListPosition.values()) {
                    best = addStation(run, best, trainRun, listPosition)
                }

                if (!trainRun.isComplete) {
                    //the rest is placed in the next iteration
                    return best
                }
            }
        }

        return run
    }

    private fun addStation(run: Run, best: Run?, trainRun: TrainRun, listPosition: ListPosition): Run? {
        var best = best
        var complete = true

        val station = listPosition[trainRun.stations]
        for (connection in station.getConnections()) {
            val destination = connection.getDestination(station)
            if (canAddStation(run, trainRun, connection, destination)) {
                complete = false

                val next = Run(run)
                listPosition.add(next.getTrainRun(trainRun.train).stations, destination)
                next.addUsedConnection(connection)
                best = getBestRun(best, next)
            }
        }

        trainRun.isComplete = complete

        return best
    }

    private fun canAddStation(run: Run, trainRun: TrainRun, connection: Connection, destination: Station): Boolean {
        return (run.getUsedTracks().none { connection.tracks.contains(it) }
                && !trainRun.stations.contains(destination)
                && trainRun.stations.size < trainRun.train.cities)
    }

    private fun getBestRun(oldBest: Run?, next: Run): Run? {
        val newBest = calculateBestRun(next)

        val oldValue = oldBest?.value ?: 0
        val newValue = newBest?.value ?: 0

        return if (oldValue >= newValue) oldBest else newBest
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val lines = RunCalculator::class.java.classLoader.getResource("gameState.txt").readText(Charsets.UTF_8).lines().toMutableList()

            val stations = LinkedHashMap<String, Station>()
            for (list in lines.removeAt(0).split(',').chunked(2)) {
                val name = list[0]
                stations[name] = Station(name, Integer.parseInt(list.get(1)))
            }

            val homeStations = ArrayList<Station>()
            for (name in lines.removeAt(0).split(',')) {
                homeStations.add(stations[name]!!)
            }

            val trainRuns = ArrayList<TrainRun>()
            for (value in lines.removeAt(0).split(',')) {
                trainRuns.add(TrainRun(Train(Integer.parseInt(value)), listOf()))
            }

            for (line in lines) {
                val strings = line.split(',')
                val start = stations[strings[0]]!!
                val end = stations[strings[2]]!!
                val connection = Connection(
                        Track(start, strings[1]),
                        Track(end, strings[3]))

                start.addConnection(connection)
                end.addConnection(connection)
            }

            println(RunCalculator().calculateBestRun(Run(homeStations, trainRuns, setOf()))!!.value)
        }
    }
}
