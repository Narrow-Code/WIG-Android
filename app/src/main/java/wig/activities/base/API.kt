package wig.activities.base

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wig.api.BorrowerService
import wig.api.LocationService
import wig.api.OwnershipService
import wig.api.ScannerService
import wig.api.UserService
import wig.models.requests.CheckoutRequest
import wig.models.requests.EditLocationRequest
import wig.models.requests.EditOwnershipRequest
import wig.models.requests.NewOwnershipRequest
import wig.models.requests.SearchRequest
import wig.models.responses.CheckoutResponse
import wig.models.responses.CommonResponse
import wig.models.responses.CreateBorrowerResponse
import wig.models.responses.GetBorrowersResponse
import wig.models.responses.GetCheckedOutItemsResponse
import wig.models.responses.InventoryResponse
import wig.models.responses.LocationResponse
import wig.models.responses.OwnershipResponse
import wig.models.responses.ScanResponse
import wig.models.responses.SearchLocationResponse
import wig.models.responses.SearchOwnershipResponse

open class API : AppCompatActivity() {
    private val scannerService = ScannerService.create()
    private val ownershipService = OwnershipService.create()
    private val locationService = LocationService.create()
    private val borrowerService = BorrowerService.create()
    private val userService = UserService.create()

    protected suspend fun scanBarcode(barcode: String): ScanResponse = withContext(Dispatchers.IO){
        val posts = scannerService.scan(barcode)
        posts
    }

    protected suspend fun checkQR(qr: String): CommonResponse = withContext(Dispatchers.IO){
        val posts = scannerService.checkQR(qr)
        posts
    }

    protected suspend fun scanQRLocation(qr: String): LocationResponse = withContext(Dispatchers.IO){
        val posts = scannerService.scanQRLocation(qr)
        posts
    }

    protected suspend fun scanQROwnership(qr: String): OwnershipResponse = withContext(Dispatchers.IO) {
        val posts = scannerService.scanQROwnership(qr)
        posts
    }

    protected suspend fun setItemLocation(ownershipUID: String, locationQR: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.setLocation(ownershipUID, locationQR)
        posts
    }

    protected suspend fun createNewLocation(name: String, locationQR: String): LocationResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.createLocation(name, locationQR)
        posts
    }

    protected suspend fun createNewOwnershipNoItem(newOwnershipRequest: NewOwnershipRequest): OwnershipResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.createOwnershipNoItem(newOwnershipRequest)
        posts
    }

    protected suspend fun changeQuantity(changeType: String, amount: Int, ownershipUID: String): OwnershipResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.changeQuantity(changeType, amount, ownershipUID)
        posts
    }

    protected suspend fun unpackLocation(locationUID: String): InventoryResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.unpackLocation(locationUID)
        posts
    }

    protected suspend fun getBorrowers(): GetBorrowersResponse = withContext(Dispatchers.IO){
        val posts = borrowerService.getBorrowers()
        posts
    }

    protected suspend fun createBorrowers(name: String): CreateBorrowerResponse = withContext(
        Dispatchers.IO){
        val posts = borrowerService.createBorrower(name)
        posts
    }

    protected suspend fun checkout(borrowerUID: String, ownerships: CheckoutRequest): CheckoutResponse = withContext(
        Dispatchers.IO){
        val posts = borrowerService.checkout(borrowerUID,ownerships)
        posts
    }

    protected suspend fun checkIn(ownerships: CheckoutRequest): CheckoutResponse = withContext(
        Dispatchers.IO){
        val posts = borrowerService.checkIn(ownerships)
        posts
    }

    protected suspend fun searchOwnership(searchRequest: SearchRequest): SearchOwnershipResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.searchOwnership(searchRequest)
        posts
    }

    protected suspend fun editOwnership(editOwnershipRequest: EditOwnershipRequest, uid: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.editOwnership(editOwnershipRequest, uid)
        posts
    }

    protected suspend fun getCheckedOutItems(): GetCheckedOutItemsResponse = withContext(Dispatchers.IO){
        val posts = borrowerService.getCheckedOutItems()
        posts
    }

    protected suspend fun searchLocation(searchRequest: SearchRequest): SearchLocationResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.searchLocation(searchRequest)
        posts
    }

    protected suspend fun locationEdit(editLocationRequest: EditLocationRequest, locationUID: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.locationEdit(editLocationRequest, locationUID)
        posts
    }

    protected suspend fun returnInventory(): InventoryResponse = withContext(Dispatchers.IO){
        val posts = locationService.returnInventory()
        posts
    }

    protected suspend fun ping(hostname: String, port: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = userService.ping(hostname, port)
        posts
    }
}