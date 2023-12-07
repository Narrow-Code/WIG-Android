package wig.utils

import wig.models.Location
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

    fun removeAllOwnerships() {
        ownerships.clear()
    }

    fun getOwnership(uid: Int): Ownership? {
        return ownerships.find {it.ownershipUID == uid}
    }

    fun setOwnershipLocation(ownershipUID: Int, location: Location) {
        val ownership = ownerships.find {it.ownershipUID == ownershipUID}
        if (ownership != null) {
            ownership.location = location
        }

    }

}