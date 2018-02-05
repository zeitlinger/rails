package de.zeitlinger.rails

data class Run(val homeStations: List<Station>, val trainRuns: List<TrainRun>, val usedTracks: Set<Track>) {
    val value: Int
        get() {
            return trainRuns.flatMap { it.stations }.sumBy { it.value }
        }

    override fun toString(): String {
        return value.toString() + "=" + trainRuns
    }
}

fun getTrainRun(list: List<TrainRun>, train: Train): TrainRun {
    for (trainRun in list) {
        if (trainRun.train === train) {
            return trainRun
        }
    }
    throw IllegalArgumentException("train run not found")
}

