package de.zeitlinger.rails

enum class ListPosition {
    TAIL {
        override operator fun <T> get(list: List<T>): T {
            return list[0]
        }

        override fun <T> add(list: MutableList<T>, element: T) {
            list.add(0, element)
        }
    },
    HEAD {
        override operator fun <T> get(list: List<T>): T {
            return list[list.size - 1]
        }

        override fun <T> add(list: MutableList<T>, element: T) {
            list.add(element)
        }
    };

    abstract operator fun <T> get(list: List<T>): T

    abstract fun <T> add(list: MutableList<T>, element: T)

}
