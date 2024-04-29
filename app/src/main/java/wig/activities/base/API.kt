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
import wig.models.requests.LocationEditRequest
import wig.models.requests.OwnershipEditRequest
import wig.models.requests.OwnershipCreateRequest
import wig.models.requests.SearchRequest
import wig.models.responses.borrowerCheckedOutResponse
import wig.models.responses.CommonResponse
import wig.models.responses.borrowerCreateResponse
import wig.models.responses.borrowerGetAllResponse
import wig.models.responses.borrowerGetInventoryResponse
import wig.models.responses.InventoryResponse
import wig.models.responses.LocationResponse
import wig.models.responses.OwnershipResponse
import wig.models.responses.ScannerBarcodeResponse
import wig.models.responses.LocationSearchResponse
import wig.models.responses.ownershipSearchResponse

// API holds all of the API calls within Coroutine functions
open class API : AppCompatActivity() {
    private val scannerService = ScannerService.create()
    private val ownershipService = OwnershipService.create()
    private val locationService = LocationService.create()
    private val borrowerService = BorrowerService.create()
    private val userService = UserService.create()

    // borrowerGetAll retrieves all borrowers associated with the user
    protected suspend fun borrowerGetAll(): borrowerGetAllResponse = withContext(Dispatchers.IO){
        val posts = borrowerService.borrowerGetAll()
        posts
    }

    // borrowerCreate creates a new borrower
    protected suspend fun borrowerCreate(name: String): borrowerCreateResponse = withContext(
        Dispatchers.IO){
        val posts = borrowerService.borrowerCreate(name)
        posts
    }

    // borrowerCheckout checks out a list of Ownerships to a Borrower
    protected suspend fun borrowerCheckout(borrowerUID: String, ownerships: CheckoutRequest): borrowerCheckedOutResponse = withContext(
        Dispatchers.IO){
        val posts = borrowerService.borrowerCheckout(borrowerUID,ownerships)
        posts
    }

    // borrowerCheckIn returns a list of Ownerships from Borrowers
    protected suspend fun borrowerCheckIn(ownerships: CheckoutRequest): borrowerCheckedOutResponse = withContext(
        Dispatchers.IO){
        val posts = borrowerService.borrowerCheckIn(ownerships)
        posts
    }

    // borrowerGetInventory returns all Borrowed ownerships
    protected suspend fun borrowerGetInventory(): borrowerGetInventoryResponse = withContext(Dispatchers.IO){
        val posts = borrowerService.borrowerGetInventory()
        posts
    }

    // ownershipCreate creates a new Ownership
    protected suspend fun ownershipCreate(ownershipCreateRequest: OwnershipCreateRequest): OwnershipResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.ownershipCreate(ownershipCreateRequest)
        posts
    }

    // ownershipQuantity changes or sets the quantity of an Ownership
    protected suspend fun ownershipQuantity(changeType: String, amount: Int, ownershipUID: String): OwnershipResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.ownershipQuantity(changeType, amount, ownershipUID)
        posts
    }

    // ownershipSetLocation sets the location of an Ownership
    protected suspend fun ownershipSetLocation(ownershipUID: String, locationQR: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.ownershipSetLocation(ownershipUID, locationQR)
        posts
    }

    // ownershipSearch searches for an Ownership based on Name and Tags
    protected suspend fun ownershipSearch(searchRequest: SearchRequest): ownershipSearchResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.ownershipSearch(searchRequest)
        posts
    }

    // ownershipEdit edits the fields of an Ownership
    protected suspend fun ownershipEdit(ownershipEditRequest: OwnershipEditRequest, uid: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.ownershipEdit(ownershipEditRequest, uid)
        posts
    }


    // scannerBarcode takes a barcode and retrieves existing Ownerships or creates a new one if none exist
    protected suspend fun scannerBarcode(barcode: String): ScannerBarcodeResponse = withContext(Dispatchers.IO){
        val posts = scannerService.scannerBarcode(barcode)
        posts
    }

    // scanQROwnership takes a QR code and returns its Ownership
    protected suspend fun scanQROwnership(qr: String): OwnershipResponse = withContext(Dispatchers.IO) {
        val posts = scannerService.scanQROwnership(qr)
        posts
    }

    // scannerCheckQR checks if a QR code is in use by a Location, Ownership or is New
    protected suspend fun scannerCheckQR(qr: String): CommonResponse = withContext(Dispatchers.IO){
        val posts = scannerService.scannerCheckQR(qr)
        posts
    }

    // scannerQRLocation takes a QR code and returns its location
    protected suspend fun scannerQRLocation(qr: String): LocationResponse = withContext(Dispatchers.IO){
        val posts = scannerService.scannerQRLocation(qr)
        posts
    }

    // locationCreate creates a new Location
    protected suspend fun locationCreate(name: String, locationQR: String): LocationResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.locationCreate(name, locationQR)
        posts
    }

    // locationUnpack retrieves all inventory with a location
    protected suspend fun locationUnpack(locationUID: String): InventoryResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.locationUnpack(locationUID)
        posts
    }

    // locationSearch searches for Locations based on Name and Tags
    protected suspend fun locationSearch(searchRequest: SearchRequest): LocationSearchResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.locationSearch(searchRequest)
        posts
    }

    // locationEdit edits the fields of a Location
    protected suspend fun locationEdit(locationEditRequest: LocationEditRequest, locationUID: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.locationEdit(locationEditRequest, locationUID)
        posts
    }

    // locationGetInventory returns users entire inventory
    protected suspend fun locationGetInventory(): InventoryResponse = withContext(Dispatchers.IO){
        val posts = locationService.locationGetInventory()
        posts
    }

    // ping performs a health check on a self hosted server to validate existence
    protected suspend fun ping(hostname: String, port: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = userService.ping(hostname, port)
        posts
    }
}