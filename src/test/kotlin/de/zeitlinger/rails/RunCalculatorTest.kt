package de.zeitlinger.rails

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class RunCalculatorTest {

    @Test
    fun calculateRun() {
        val lines = RunCalculatorTest::class.java.classLoader.getResource("gameState.txt").readText(Charsets.UTF_8).lines().toMutableList()

        val stations = LinkedHashMap<String, Station>()
        for (list in lines.removeAt(0).split(',').chunked(2)) {
            val name = list[0]
            stations[name] = Station(name, Integer.parseInt(list[1]))
        }

        val homeStations = ArrayList<Station>()
        for (name in lines.removeAt(0).split(',')) {
            homeStations.add(stations[name]!!)
        }

        val trainRuns = ArrayList<TrainRun>()
        for (value in lines.removeAt(0).split(',')) {
            val cities = value.toInt()
            trainRuns.add(TrainRun(Train(cities), listOf()))
        }

        val connections = mutableMapOf<Station, MutableList<Connection>>()

        for (line in lines) {
            val strings = line.split(',')
            val start = stations[strings[0]]!!
            val end = stations[strings[2]]!!
            val connection = Connection(
                    Track(start, strings[1]),
                    Track(end, strings[3]))

            connections.getOrPut(start) { mutableListOf() }.add(connection)
            connections.getOrPut(end) { mutableListOf() }.add(connection)
        }

        assertThat(calculateBestRun(Run(homeStations, trainRuns, setOf()), mutableSetOf(), Board(connections)).value).isEqualTo(420)
    }
}