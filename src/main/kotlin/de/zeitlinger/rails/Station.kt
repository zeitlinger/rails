package de.zeitlinger.rails

import java.util.*

class Station(val name: String, val value: Int) {
    private val connections = ArrayList<Connection>()

    fun getConnections(): List<Connection> {
        return connections
    }

    fun addConnection(connection: Connection) {
        connections.add(connection)
    }

    override fun toString(): String {
        return name
    }
}
