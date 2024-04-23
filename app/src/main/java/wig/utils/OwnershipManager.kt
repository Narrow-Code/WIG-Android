package wig.utils

import wig.models.Location
import wig.models.Ownership

object OwnershipManager {
    private val ownerships = mutableListOf<Ownership>()

    fun addOwnership(ownership: Ownership) {
        ownerships.add(ownership);
    }

    fun removeOwnership(uid: String) {
        val ownershipToRemove = ownerships.find { it.ownershipUID == uid }
        ownershipToRemove?.let { ownerships.remove(it) }
    }

    fun getAllOwnerships(): List<Ownership> {
        return ownerships.toList()
    }

    fun removeAllOwnerships() {
        ownerships.clear()
    }

    fun getOwnership(uid: String): Ownership? {
        return ownerships.find { it.ownershipUID == uid }
    }

    fun setOwnershipLocation(ownershipUID: String, location: Location) {
        val ownership = ownerships.find { it.ownershipUID == ownershipUID }
        if (ownership != null) {
            ownership.location = location
        }
    }

    fun setOwnershipName(ownershipUID: String, name: String) {
        val ownership = ownerships.find { it.ownershipUID == ownershipUID }
        if (ownership != null) {
            ownership.customItemName = name
        }
    }

    fun ownershipExists(ownershipUID: String): Boolean {
        for (ownership in ownerships) {
            if (ownership.ownershipUID == ownershipUID) {
                return true
            }
        }
        return false
    }

}