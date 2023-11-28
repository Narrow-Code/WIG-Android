package wig.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import wig.api.ScannerService
import wig.api.dto.ScanResponse
import wig.utils.StoreToken
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val CAMERA_REQUEST_CODE = 101

@Suppress("DEPRECATION")
class Scanner : BaseActivity() {
    private lateinit var codeScanner: CodeScanner
    private var pageView = "items"
    private val handler = Handler()
    private val service = ScannerService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setScannerBindings()
        setupPermissions()
        codeScanner()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        scannerBinding.binsButton.setOnClickListener{ switchToBinsView() }
        scannerBinding.itemsButton.setOnClickListener{ switchToItemsView() }
        scannerBinding.shelvesButton.setOnClickListener { switchToShelvesView() }
        scannerBinding.icSettings.setOnClickListener{ logout() }
    }

    private suspend fun scanBarcode(barcode: String) = withContext(Dispatchers.IO){
        val posts = service.scan(barcode)
        if (posts.success) {
            populateItems(posts)
        } else {
            // TODO handle what else noise?
            codeScanner.startPreview()
        }
    }

    private fun populateItems(postScanResponse: ScanResponse){
        val tableLayout = scannerBinding.itemsTableLayout
        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameTextView = TextView(this)
        nameTextView.text = postScanResponse.ownership[0].item.itemName.substring(0 until 20.coerceAtMost(postScanResponse.ownership[0].item.itemName.length))
        nameTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )

        val locationTextView = TextView(this)
        locationTextView.text = postScanResponse.ownership[0].location.locationName.substring(0 until 18.coerceAtMost(postScanResponse.ownership[0].location.locationName.length))
        locationTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        locationTextView.gravity = Gravity.CENTER

        val quantityTextView = TextView(this)
        quantityTextView.text = postScanResponse.ownership[0].itemQuantity.toString()
        quantityTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        quantityTextView.gravity = Gravity.END

        row.addView(nameTextView)
        row.addView(locationTextView)
        row.addView(quantityTextView)

        row.layoutParams = layoutParams

        tableLayout.addView(row)

        handler.postDelayed({
            // Enable scanning after 5 seconds
            codeScanner.startPreview()
        }, 1500)

    }

    private fun switchToBinsView() {
        scannerBinding.tableItemsTitles.visibility = View.INVISIBLE
        scannerBinding.tableShelvesTitles.visibility = View.INVISIBLE
        scannerBinding.itemsTable.visibility = View.INVISIBLE
        scannerBinding.shelvesTable.visibility = View.INVISIBLE
        scannerBinding.tableBinsTitles.visibility = View.VISIBLE
        scannerBinding.binsTable.visibility = View.VISIBLE
        pageView = "bins"
    }

    private fun switchToItemsView() {
        scannerBinding.tableBinsTitles.visibility = View.INVISIBLE
        scannerBinding.tableShelvesTitles.visibility = View.INVISIBLE
        scannerBinding.binsTable.visibility = View.INVISIBLE
        scannerBinding.shelvesTable.visibility = View.INVISIBLE
        scannerBinding.tableItemsTitles.visibility = View.VISIBLE
        scannerBinding.itemsTable.visibility = View.VISIBLE
        pageView = "items"
    }

    private fun switchToShelvesView() {
        scannerBinding.tableItemsTitles.visibility = View.INVISIBLE
        scannerBinding.tableBinsTitles.visibility = View.INVISIBLE
        scannerBinding.binsTable.visibility = View.INVISIBLE
        scannerBinding.itemsTable.visibility = View.INVISIBLE
        scannerBinding.tableShelvesTitles.visibility = View.VISIBLE
        scannerBinding.shelvesTable.visibility = View.VISIBLE
        pageView = "shelves"
    }

    // TODO REMOVE THIS IS FOR TESTING LOG IN AND OUT UNTIL SETTINGS PAGE IS ADDED
    private fun logout() {
        // Delete token & UID
        lifecycleScope.launch {
            val storeToken = StoreToken(this@Scanner)
            storeToken.saveToken("")
        }
        startActivityLogin()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, scannerBinding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    codeScanner.stopPreview()
                    if(it.barcodeFormat != BarcodeFormat.QR_CODE){
                        lifecycleScope.launch {
                        scanBarcode(it.text)
                        }
                    }
                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread {
                }
            }
        }
        scannerBinding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission : Int = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need the camera permission to be able to use this app!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

