package wig.utils

import wig.models.Location

object LocationManager {
    private val locations = mutableListOf<Location>()

    fun addLocation(location: Location) {
        locations.add(location);
    }

    fun removeLocation(uid: String) {
        val ownershipToRemove = locations.find {it.locationUID == uid}
        ownershipToRemove?.let { locations.remove(it)}
    }

    fun getAllLocations(): List<Location> {
        return locations.toList()
    }

    fun getAllLocationNames(): List<String> {
        val names = mutableListOf<String>()
        for (name in locations.toList()) {
            names.add(name.locationName)
        }
        return names
    }

    fun removeAllLocations() {
        locations.clear()
    }

    fun locationExists(locationUID: String): Boolean {
        for (location in locations) {
            if (location.locationUID == locationUID) {
                return true
            }
        }
        return false
    }
}