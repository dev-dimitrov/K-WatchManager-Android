package kaizen.watchmanager

import Watch
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class WatchScreenActivity : AppCompatActivity() {
    lateinit var brandTxt: TextView;
    lateinit var modelTxt: TextView;
    lateinit var adjustBtn: Button;
    lateinit var accBtn: Button;
    lateinit var watchAtt: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_watch_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignObjectId();
        var selectedWatch: Watch;

        selectedWatch = intent.getSerializableExtra("WATCH", Watch::class.java)!!; // Receives the watch from the main activity
        showWatchInfo(selectedWatch);
    }

    fun asignObjectId(){
        brandTxt = findViewById(R.id.brandText);
        modelTxt = findViewById(R.id.modelText);
        adjustBtn = findViewById(R.id.adjustButton);
        brandTxt = findViewById(R.id.brandText);
        accBtn = findViewById(R.id.checkButton);
        watchAtt = findViewById(R.id.attText);
    }

    fun showWatchInfo(w: Watch){
        brandTxt.text = w.brand;
        modelTxt.text = w.model;
        watchAtt.text = "Movement type: "+w.type+"\nCaliber: "+w.caliber+"\nTheoretic accuracy: "+w.theoreticAccuracy+"\nMore information: "+w.moreInfo;
    }

}