package de.zeitlinger.rails

class TrainRun(val train: Train, stations: List<Station>) {
    //for each recursion step, we need copy the stations from the previous step
    val stations: MutableList<Station> = stations.toMutableList()
    var isComplete = false
}
