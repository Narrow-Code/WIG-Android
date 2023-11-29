package wig.utils

import wig.models.Ownership

object OwnershipManager {
    private val ownerships = mutableListOf<Ownership>()

    fun addOwnership(ownership: Ownership) {
        ownerships.add(ownership);
    }

    fun removeOwnership(uid: Int) {
        val ownershipToRemove = ownerships.find {it.ownershipUID == uid}
        ownershipToRemove?.let { ownerships.remove(it)}
    }

    fun getAllOwnerships(): List<Ownership> {
        return ownerships.toList()
    }

}