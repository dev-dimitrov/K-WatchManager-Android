package kaizen.watchmanager

import Watch
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class WatchScreenActivity : AppCompatActivity() {
    lateinit var brandTxt: TextView;
    lateinit var modelTxt: TextView;
    lateinit var adjustBtn: Button;
    lateinit var accBtn: Button;
    lateinit var watchAtt: TextView;
    lateinit var w: Watch;
    lateinit var statusTxt: TextView;
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
        statusTxt.visibility = View.VISIBLE;
        w = intent.getSerializableExtra("WATCH", Watch::class.java)!!; // Receives the watch from the main activity
        drawWatchInfo();
        adjustBtn.setOnClickListener{
            adjustWatch();
        }


    }

    fun asignObjectId(){
        brandTxt = findViewById(R.id.brandText);
        modelTxt = findViewById(R.id.modelText);
        adjustBtn = findViewById(R.id.adjustButton);
        brandTxt = findViewById(R.id.brandText);
        accBtn = findViewById(R.id.checkButton);
        watchAtt = findViewById(R.id.attText);
        statusTxt = findViewById(R.id.statusText);
    }

    fun drawWatchInfo(){
        brandTxt.text = w.brand;
        modelTxt.text = w.model;
        watchAtt.text = "Last Adjustment: "+w.lastAdjust+"\nMovement type: "+w.type+"\nCaliber: "+w.caliber+"\nTheoretic accuracy: "+w.theoreticAccuracy+"\nMore information: "+w.moreInfo;
    }


    fun adjustWatch(){
        w.lastAdjust = LocalDateTime.now().format(Watch.formatter);
        statusTxt.setTextColor(Color.GREEN);
        statusTxt.text = "Successfully adjusted!!";
        drawWatchInfo();
    }

}