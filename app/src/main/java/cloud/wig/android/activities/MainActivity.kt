package cloud.wig.android.activities

import android.util.Log
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cloud.wig.android.MainScanner

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate method is executing")

        // TODO have check if user is logged in by stored authentication token
        val isLoggedIn = true

        // If user is not logged in, start login page
        if (isLoggedIn) {
            Log.d("MainActivity", "User is logged in, starting MainScanner")
            val intent = Intent(this, MainScanner::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d("MainActivity", "User is not logged in, starting LoginPage")
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}