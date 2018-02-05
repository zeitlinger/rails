package de.zeitlinger.rails


data class Board(val connections: Map<Station, List<Connection>>)
data class Station(val name: String, val value: Int) {

    override fun toString(): String {
        return name
    }
}

data class Track(val station: Station, val name: String)
data class Train(val cities: Int)
data class TrainRun(val train: Train, val stations: List<Station>) {

    override fun toString(): String {
        return "${train.cities}: ${stations.map { it.name }}"
    }
}