package cloud.wig.android.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import cloud.wig.android.api.items.ItemService
import cloud.wig.android.api.items.dto.PostScanResponse
import cloud.wig.android.databinding.MainScannerBinding
import cloud.wig.android.datastore.StoreToken
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
class Scanner : AppCompatActivity() {
    // Set variables
    private lateinit var binding: MainScannerBinding
    private lateinit var codeScanner: CodeScanner
    private var pageView = "items"
    private val handler = Handler()
    private val service = ItemService.create()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginPage", "onCreate method is executing")

        // Set page orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Disable back press
        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {} })

        // Set bindings for Signup Page and Open
        binding = MainScannerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set up camera permissions and start camera
        setupPermissions()
        codeScanner()

        // Set on click listeners
        binding.binsButton.setOnClickListener{switchToBinsView()}
        binding.itemsButton.setOnClickListener{switchToItemsView()}
        binding.shelvesButton.setOnClickListener {switchToShelvesView()}
        binding.icSettings.setOnClickListener{logout()}
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.QR_CODE)


            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    // newText.text = it.text

                    if(it.barcodeFormat != BarcodeFormat.QR_CODE){
                        scanBarcodeAPICall(it.text)
                        codeScanner.startPreview()
                    }
                }
            }


            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialization error: ${it.message}")
                }
            }
        }

        binding.scannerView.setOnClickListener {
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
            android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
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
                } else {
                    //successful
                }
            }
        }
    }

    //TODO REMOVE THIS IS FOR TESTING LOG IN AND OUT UNTIL SETTINGS PAGE IS ADDED
    private fun logout() {
        // Delete token & UID
        lifecycleScope.launch {
            val storeToken = StoreToken(this@Scanner)
            storeToken.saveToken("")
        }
        val intent = Intent(this@Scanner, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun switchToBinsView() {
        binding.tableItemsTitles.visibility = View.INVISIBLE
        binding.tableShelvesTitles.visibility = View.INVISIBLE
        binding.itemsTable.visibility = View.INVISIBLE
        binding.shelvesTable.visibility = View.INVISIBLE
        binding.tableBinsTitles.visibility = View.VISIBLE
        binding.binsTable.visibility = View.VISIBLE

        pageView = "bins"
    }

    private fun switchToItemsView() {
        binding.tableBinsTitles.visibility = View.INVISIBLE
        binding.tableShelvesTitles.visibility = View.INVISIBLE
        binding.binsTable.visibility = View.INVISIBLE
        binding.shelvesTable.visibility = View.INVISIBLE
        binding.tableItemsTitles.visibility = View.VISIBLE
        binding.itemsTable.visibility = View.VISIBLE

        pageView = "items"
    }

    private fun switchToShelvesView() {
        binding.tableItemsTitles.visibility = View.INVISIBLE
        binding.tableBinsTitles.visibility = View.INVISIBLE
        binding.binsTable.visibility = View.INVISIBLE
        binding.itemsTable.visibility = View.INVISIBLE
        binding.tableShelvesTitles.visibility = View.VISIBLE
        binding.shelvesTable.visibility = View.VISIBLE

        pageView = "shelves"
    }

    private fun populateItems(postScanResponse: PostScanResponse){
        val tableLayout = binding.itemsTableLayout
        val row = TableRow(this@Scanner)

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameTextView = TextView(this@Scanner)
        nameTextView.text = postScanResponse.ownership[0].Item.item_name.substring(0 until 20.coerceAtMost(postScanResponse.ownership[0].Item.item_name.length))
        nameTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )

        val locationTextView = TextView(this@Scanner)
        locationTextView.text = postScanResponse.ownership[0].Location.location_name.substring(0 until 18.coerceAtMost(postScanResponse.ownership[0].Location.location_name.length))
        locationTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        locationTextView.gravity = Gravity.CENTER

        val quantityTextView = TextView(this@Scanner)
        quantityTextView.text = postScanResponse.ownership[0].item_quantity.toString()
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

        codeScanner.stopPreview()

        handler.postDelayed({
            // Enable scanning after 5 seconds
            codeScanner.startPreview()
        }, 1500)

    }

    private fun scanBarcodeAPICall(barcode: String) {
        lifecycleScope.launch {
            try {
                val posts = withContext(Dispatchers.IO) {
                    Log.d("API CALL", "Coroutine started")
                    service.postScan(barcode)
                }
                if (posts.success) {
                    Log.d("API CALL", "Posts success")
                    populateItems(posts)
                } else {
                    // TODO handle what else
                }
            } catch (e: Exception) {
                // TODO handle exception, maybe network issue popup?
            }
        }
    }

}