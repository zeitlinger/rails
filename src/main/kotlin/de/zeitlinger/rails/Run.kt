package de.zeitlinger.rails

import java.util.*

class Run(val homeStations: List<Station>, trainRuns: List<TrainRun>, usedTracks: Set<Track>) {
    private val trainRuns = ArrayList<TrainRun>()
    private val usedTracks: MutableSet<Track>

    val value: Int
        get() {
            var value = 0
            for (trainRun in trainRuns) {
                for (station in trainRun.stations) {
                    value += station.value
                }
            }
            return value
        }

    constructor(run: Run) : this(run.homeStations, run.getTrainRuns(), run.getUsedTracks()) {}

    init {
        for (trainRun in trainRuns) {
            this.trainRuns.add(TrainRun(trainRun.train, trainRun.stations))
        }
        this.usedTracks = HashSet(usedTracks)
    }

    fun getTrainRuns(): List<TrainRun> {
        return trainRuns
    }

    fun getUsedTracks(): Set<Track> {
        return usedTracks
    }

    fun getTrainRun(train: Train): TrainRun {
        for (trainRun in trainRuns) {
            if (trainRun.train === train) {
                return trainRun
            }
        }
        throw IllegalArgumentException("train run not found")
    }

    fun addUsedConnection(connection: Connection) {
        usedTracks.addAll(connection.tracks)
    }

    override fun toString(): String {
        return value.toString() + "=" + trainRuns
    }
}
