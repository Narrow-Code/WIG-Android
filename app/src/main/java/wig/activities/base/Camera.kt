package wig.activities.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.launch

private const val CAMERA_REQUEST_CODE = 101

open class Camera : Settings()  {
    protected lateinit var codeScanner: CodeScanner
    protected open suspend fun scanSuccess(code: String, barcodeFormat: BarcodeFormat){}

    // codeScanner is used to set up the scanners functionality and settings
    protected fun codeScanner() {
        // Initialize CodeScanner
        codeScanner = CodeScanner(this, scannerBinding.scannerView)

        codeScanner.apply {
            // Initialize CodeScanner settings
            camera = CodeScanner.CAMERA_BACK
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
            formats = listOf(
                BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E,
                BarcodeFormat.EAN_13,
                BarcodeFormat.EAN_8,
                BarcodeFormat.QR_CODE
            )

            // Define callback on scan
            decodeCallback = DecodeCallback {
                performVibration(this@Camera)
                playScanSound(this@Camera)
                lifecycleScope.launch {
                    scanSuccess(it.text, it.barcodeFormat)
                }
            }

            // Define error callback
            errorCallback = ErrorCallback {
                runOnUiThread {
                    // Add error handling if needed
                }
            }
        }

        // Start scanner preview when ScannerView is clicked
        scannerBinding.scannerView.setOnClickListener {
            onResume()
        }
    }

    // onResume is used to resume the camera
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    // onPause is used to pause the camera
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    // setupPermissions is used to check and request camera permissions if not granted
    protected fun setupPermissions() {
        val permission : Int = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }
    }

    // Override function to handle permission request result
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