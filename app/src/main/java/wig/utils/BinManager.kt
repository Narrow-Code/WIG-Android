package wig.utils

import wig.models.Location

object BinManager {
    private val bins = mutableListOf<Location>()

    fun addBin(bin: Location) {
        bins.add(bin);
    }

    fun removeBin(uid: Int) {
        val ownershipToRemove = bins.find {it.locationUID == uid}
        ownershipToRemove?.let { bins.remove(it)}
    }

    fun getAllBins(): List<Location> {
        return bins.toList()
    }

    fun removeAllBins() {
        bins.clear()
    }

}