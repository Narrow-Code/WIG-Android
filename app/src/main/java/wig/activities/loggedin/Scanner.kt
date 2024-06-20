package wig.activities.loggedin

import android.app.Dialog
import android.os.Bundle
import android.os.StrictMode
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.launch
import wig.R
import wig.activities.base.Camera
import wig.api.API
import wig.databinding.CreateNewBinding
import wig.models.entities.Ownership
import wig.managers.OwnershipAdapter
import wig.models.requests.OwnershipCreateRequest

class Scanner : Camera() {
    private var pageView = "items"
    private lateinit var recyclerView: RecyclerView
    private lateinit var ownershipAdapter: OwnershipAdapter
    private val ownershipList = mutableListOf<Ownership>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setScannerBindings()
        setupPermissions()
        codeScanner()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        checkForUpdates()
        setOnClickListeners()

        recyclerView = findViewById(R.id.items_table_recycler_view)
        ownershipAdapter = OwnershipAdapter(ownershipList, this, API(), this)
        recyclerView.adapter = ownershipAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setOnClickListeners() {
        scannerBinding.locationsButton.setOnClickListener { switchToLocationsView() }
        scannerBinding.itemsButton.setOnClickListener { switchToItemsView() }
        scannerBinding.topMenu.icScanner.setOnClickListener { startActivityScanner() }
        scannerBinding.topMenu.icSettings.setOnClickListener { startActivitySettings() }
        scannerBinding.topMenu.icCheckedOut.setOnClickListener { startActivityCheckedOut() }
        scannerBinding.topMenu.icInventory.setOnClickListener { startActivityInventory() }
        scannerBinding.clear.setOnClickListener { ownershipAdapter.clearOwnerships() }
        //scannerBinding.place.setOnClickListener { placeQueueButton() }
        scannerBinding.add.setOnClickListener { newEntry() }
        //scannerBinding.unpack.setOnClickListener { unpackButton() }
        //scannerBinding.checkOut.setOnClickListener { checkoutButton() }
        //scannerBinding.search.setOnClickListener { searchButton() }
    }

    override suspend fun scanSuccess(code: String, barcodeFormat: BarcodeFormat) {
        codeScanner.stopPreview()
        if (barcodeFormat != BarcodeFormat.QR_CODE) {
            val response = api.scannerBarcode(code)
            if (response.message == "429") {
                Toast.makeText(this@Scanner, "LIMIT REACHED", Toast.LENGTH_SHORT).show()
            }
            for (ownership in response.ownership){
                ownershipAdapter.addOwnership(ownership)
            }
            switchToItemsView()
            codeScanner.startPreview()
        } else {
            val response = api.scannerCheckQR(code)
            when (response.message) {
                "NEW" -> {
                    // TODO newEntry(code)
                }

                "LOCATION" -> {
                    val locationResponse = api.scannerQRLocation(code)
                    // TODO populateLocations(locationResponse.location)
                    switchToLocationsView()
                }

                "OWNERSHIP" -> {
                    val ownershipResponse = api.scanQROwnership(code)
                    // TODO populateItem(ownershipResponse.ownership)
                    switchToItemsView()
                    codeScanner.startPreview()
                }
            }
        }
    }

    private fun newEntry(qr: String? = null) {
        codeScanner.stopPreview()

        val createNewBinding: CreateNewBinding = CreateNewBinding.inflate(layoutInflater)
        val popupDialog = Dialog(this)
        popupDialog.setContentView(createNewBinding.root)
        qr?.let { createNewBinding.qrCodeEditText.setText(it) }
        popupDialog.setOnDismissListener { codeScanner.startPreview() }

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupDialog.window?.setLayout(layoutParams.width, layoutParams.height)

        createNewBinding.createButton.setOnClickListener {
            createNewButton(
                createNewBinding,
                popupDialog
            )
        }
        createNewBinding.cancelButton.setOnClickListener { popupDialog.dismiss() }

        val spinnerPosition = if (pageView == "items") 0 else 1
        createNewBinding.typeSpinner.setSelection(spinnerPosition)

        popupDialog.show()
    }

    private fun createNewButton(createNewBinding: CreateNewBinding, popup: Dialog) {
        val typeSpinner = createNewBinding.typeSpinner
        val name = createNewBinding.nameEditText.text.toString()
        val qr = createNewBinding.qrCodeEditText.text.toString()
        if (name == "" || qr == "") {
            return
        }
        lifecycleScope.launch {
            when (typeSpinner.selectedItem?.toString() ?: "") {
                "Location" -> {
                    val response = api.locationCreate(name, qr)
                    if (response.success) {
                        Toast.makeText(this@Scanner, "Location created", Toast.LENGTH_SHORT).show()
                        // TODO populateLocations(response.location)
                        popup.dismiss()
                        switchToLocationsView()
                    }
                }

                "Item" -> {
                    val request = OwnershipCreateRequest(qr, name)
                    val response = api.ownershipCreate(request)
                    if (response.success) {
                        Toast.makeText(this@Scanner, "Ownership created", Toast.LENGTH_SHORT).show()
                        ownershipAdapter.addOwnership(response.ownership)
                        popup.dismiss()
                        switchToItemsView()
                    }
                }
            }
        }
    }

    private fun switchToLocationsView() {
        scannerBinding.tableItemsTitles.visibility = View.INVISIBLE
        scannerBinding.itemsTable.visibility = View.INVISIBLE
        scannerBinding.tableLocationTitles.visibility = View.VISIBLE
        scannerBinding.locationsTable.visibility = View.VISIBLE
        scannerBinding.place.visibility = View.INVISIBLE
        scannerBinding.unpack.visibility = View.VISIBLE
        pageView = "locations"
    }

    private fun switchToItemsView() {
        scannerBinding.tableLocationTitles.visibility = View.INVISIBLE
        scannerBinding.locationsTable.visibility = View.INVISIBLE
        scannerBinding.tableItemsTitles.visibility = View.VISIBLE
        scannerBinding.itemsTable.visibility = View.VISIBLE
        scannerBinding.place.visibility = View.VISIBLE
        scannerBinding.unpack.visibility = View.INVISIBLE
        pageView = "items"
    }
}