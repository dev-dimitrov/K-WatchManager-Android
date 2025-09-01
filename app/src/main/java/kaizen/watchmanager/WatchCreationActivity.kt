package kaizen.watchmanager

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class WatchCreationActivity : AppCompatActivity() {
    lateinit var brandI: TextInputEditText;
    lateinit var modelI: TextInputEditText;
    lateinit var typeI: TextInputEditText;
    lateinit var caliberI: TextInputEditText;
    lateinit var moreInfoI: TextInputEditText;
    lateinit var addWatch: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_watch_creation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        brandI = findViewById(R.id.brandInput);
        modelI = findViewById(R.id.modelInput);
        typeI = findViewById(R.id.tyInput);
        caliberI = findViewById(R.id.tyInput);
        moreInfoI = findViewById(R.id.moreInfoInput);
        addWatch = findViewById(R.id.addWatchButton);
    }
}