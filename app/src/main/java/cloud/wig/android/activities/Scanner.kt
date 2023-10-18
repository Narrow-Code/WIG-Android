package cloud.wig.android.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cloud.wig.android.R
import cloud.wig.android.databinding.MainScannerBinding
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import mockdata.Mocker


private const val CAMERA_REQUEST_CODE = 101

@Suppress("DEPRECATION")
class Scanner : AppCompatActivity() {
    private lateinit var binding: MainScannerBinding
    private lateinit var codeScanner: CodeScanner
    private val handler = Handler()
    private var mocker = Mocker()
    private var pageView = "items"

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginPage", "onCreate method is executing")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Portrait lock

        binding = MainScannerBinding.inflate(layoutInflater) // set bindings to login_page layout id's
        val view = binding.root
        setContentView(view) // Open login_page view

        setupPermissions()
        codeScanner()

        binding.clear.setOnClickListener {
            removeAllRowsFromTableLayout()
        }

        binding.binsButton.setOnClickListener{
            switchToBinsView()
        }

        binding.itemsButton.setOnClickListener{
            switchToItemsView()
        }

        binding.shelvesButton.setOnClickListener {
            switchToShelvesView()
        }

        binding.overlayLayout.setOnClickListener{
            removeMenuPopup()
        }

        binding.placeQueue.setOnClickListener{
            placeQueuePopup()
        }

        binding.placeQueueMenu.setOnClickListener{
            // TODO figure out how to click on all indexes
        }

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

                    when (pageView) {
                        "items" -> {
                            populateMockItems()
                        }
                        "bins" -> {
                            populateMockBins()
                        }
                        "shelves" -> {
                            populateMockShelves()
                        }
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

    private fun removeAllRowsFromTableLayout() {
        var tableLayout = binding.itemsTableLayout
        val placeQueueMenu = binding.placeQueueMenu

        when (pageView) {
            "items" -> {
                tableLayout = binding.itemsTableLayout
            }

            "bins" -> {
                tableLayout = binding.binsTableLayout
                for (i in placeQueueMenu.childCount - 1 downTo 0) {
                    val row = placeQueueMenu.getChildAt(i)
                    if (row is TableRow) {
                        val textView = row.getChildAt(0) as? TextView

                        if (textView?.text.toString().endsWith("Bin")) {
                            placeQueueMenu.removeViewAt(i)
                        }

                    }
                }
            }

            "shelves" -> {
                tableLayout = binding.shelvesTableLayout
                for (i in placeQueueMenu.childCount - 1 downTo 0) {
                    val row = placeQueueMenu.getChildAt(i)
                    if (row is TableRow) {
                        val textView = row.getChildAt(0) as? TextView

                        if (textView?.text.toString().endsWith("Shelf")) {
                            placeQueueMenu.removeViewAt(i)
                        }

                    }
                }
            }
        }

        // Remove all rows from the table layout
        while (tableLayout.childCount > 0) {
            tableLayout.removeViewAt(0)
        }
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

    private fun populateMockItems()
    {
        val tableLayout = binding.itemsTableLayout
        val dataArray = arrayOf(mocker.getItem(), mocker.getLocation(), mocker.getQuantity().toString())
        val row = TableRow(this@Scanner)

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameTextView = TextView(this@Scanner)
        nameTextView.text = dataArray[0]
        nameTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )

        val locationTextView = TextView(this@Scanner)
        locationTextView.text = dataArray[1]
        locationTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        locationTextView.gravity = Gravity.CENTER

        val quantityTextView = TextView(this@Scanner)
        quantityTextView.text = dataArray[2]
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
    private fun populateMockBins()
    {
        val tableLayout = binding.binsTableLayout
        val dataArray = arrayOf(mocker.getBin(), mocker.getQuantity().toString())
        val row = TableRow(this@Scanner)

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameTextView = TextView(this@Scanner)
        nameTextView.text = dataArray[0]
        addToPlaceQueueMenu(dataArray[0] + " - Bin")
        nameTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )

        val quantityTextView = TextView(this@Scanner)
        quantityTextView.text = dataArray[1]
        quantityTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        quantityTextView.gravity = Gravity.END

        row.addView(nameTextView)
        row.addView(quantityTextView)

        row.layoutParams = layoutParams

        tableLayout.addView(row)

        codeScanner.stopPreview()

        handler.postDelayed({
            // Enable scanning after 5 seconds
            codeScanner.startPreview()
        }, 1500)

    }

    private fun populateMockShelves()
    {
        val tableLayout = binding.shelvesTableLayout
        val dataArray = arrayOf(mocker.getShelf(), mocker.getQuantity().toString())
        val row = TableRow(this@Scanner)

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameTextView = TextView(this@Scanner)
        nameTextView.text = dataArray[0]
        addToPlaceQueueMenu(dataArray[0] + " - Shelf")
        nameTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )

        val quantityTextView = TextView(this@Scanner)
        quantityTextView.text = dataArray[1]
        quantityTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        quantityTextView.gravity = Gravity.END

        row.addView(nameTextView)
        row.addView(quantityTextView)

        row.layoutParams = layoutParams

        tableLayout.addView(row)

        codeScanner.stopPreview()

        handler.postDelayed({
            // Enable scanning after 1.5 seconds
            codeScanner.startPreview()
        }, 1500)

    }

    private fun removeMenuPopup() {
        binding.overlayLayout.visibility = View.INVISIBLE
        binding.placeQueueMenu.visibility = View.INVISIBLE
        codeScanner.startPreview()
    }

    private fun placeQueuePopup() {
        binding.placeQueueMenu.visibility = View.VISIBLE
        binding.overlayLayout.visibility = View.VISIBLE
        binding.overlayLayout.bringToFront()
        binding.placeQueueMenu.bringToFront()
        codeScanner.stopPreview()
    }

    private fun addToPlaceQueueMenu(binName: String) {
        val placeQueueMenu = binding.placeQueueMenu
        val row = TableRow(this@Scanner)

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val binTextView = TextView(this@Scanner)
        binTextView.text = binName
        // TODO change this to pass to place queue confirmation page
        // binTextView.setOnClickListener {
        //    val intent = Intent(this@MainScanner, NextActivity::class.java)
        //    intent.putExtra("BIN_NAME", binName)
        //    startActivity(intent)
        //}
        binTextView.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        binTextView.setPadding(3, 3, 3, 3) // Set padding
        binTextView.setBackgroundResource(R.drawable.menu_background)
        binTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23f) // Set text size

        row.addView(binTextView)
        row.layoutParams = layoutParams

        placeQueueMenu.addView(row)
    }
}