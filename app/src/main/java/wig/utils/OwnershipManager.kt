package wig.utils

import wig.models.Ownership

object OwnershipManager {
    private val ownerships = mutableListOf<Ownership>()

    fun addOwnership(ownership: Ownership) {
        ownerships.add(ownership);
    }

    fun removeOwnership(ownership: Ownership) {
        ownerships.remove(ownership);
    }

    fun getAllOwnerships(): List<Ownership> {
        return ownerships.toList()
    }

}