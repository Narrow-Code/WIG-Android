package cloud.wig.android

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.os.Handler
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cloud.wig.android.databinding.MainScannerBinding
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import mockdata.Mocker


private const val CAMERA_REQUEST_CODE = 101

@Suppress("DEPRECATION")
class MainScanner : AppCompatActivity() {
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

    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

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
            CAMERA_REQUEST_CODE)
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
        val tableLayout = binding.itemsTableLayout

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
        binding.tableItemsTitles.visibility = View.INVISIBLE
        binding.binsTable.visibility = View.INVISIBLE
        binding.shelvesTable.visibility = View.INVISIBLE

        binding.tableShelvesTitles.visibility = View.VISIBLE
        binding.shelvesTable.visibility = View.VISIBLE

        pageView = "shelves"
    }

    private fun populateMockItems()
    {
        val tableLayout = binding.itemsTableLayout
        val dataArray = arrayOf(mocker.getItem(), mocker.getLocation(), mocker.getQuantity().toString())
        val row = TableRow(this@MainScanner)

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameTextView = TextView(this@MainScanner)
        nameTextView.text = dataArray[0]
        nameTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )

        val locationTextView = TextView(this@MainScanner)
        locationTextView.text = dataArray[1]
        locationTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        locationTextView.gravity = Gravity.CENTER

        val quantityTextView = TextView(this@MainScanner)
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
        val row = TableRow(this@MainScanner)

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameTextView = TextView(this@MainScanner)
        nameTextView.text = dataArray[0]
        nameTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )

        val quantityTextView = TextView(this@MainScanner)
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
        val row = TableRow(this@MainScanner)

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val nameTextView = TextView(this@MainScanner)
        nameTextView.text = dataArray[0]
        nameTextView.layoutParams = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )

        val quantityTextView = TextView(this@MainScanner)
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


}