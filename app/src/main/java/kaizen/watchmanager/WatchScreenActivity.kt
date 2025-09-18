package kaizen.watchmanager

import Watch
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.google.android.material.textfield.TextInputEditText
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class WatchScreenActivity : AppCompatActivity() {
    lateinit var brandTxt: TextView;
    lateinit var modelTxt: TextView;
    lateinit var adjustBtn: Button;
    lateinit var accBtn: Button;
    lateinit var watchAtt: TextView;
    lateinit var w: Watch;
    lateinit var statusTxt: TextView;
    lateinit var input: TextInputEditText;
    lateinit var web: WebView;
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
        statusTxt.visibility = View.INVISIBLE;
        w = intent.getSerializableExtra("WATCH", Watch::class.java)!!; // Receives the watch from the main activity
        drawWatchInfo();
        adjustBtn.setOnClickListener{
            adjustWatch();
        }

        onBackPressedDispatcher.addCallback (this){
            var i = Intent();
            i.putExtra("WATCH", w as Serializable);
            setResult(RESULT_OK, i);
            finish();
        }


       // setupWebView("https://www.time.is");
    }

    fun asignObjectId(){
        brandTxt = findViewById(R.id.brandText);
        modelTxt = findViewById(R.id.modelText);
        adjustBtn = findViewById(R.id.adjustButton);
        brandTxt = findViewById(R.id.brandText);
        accBtn = findViewById(R.id.checkButton);
        watchAtt = findViewById(R.id.attText);
        statusTxt = findViewById(R.id.statusText);
        input = findViewById(R.id.inputText);
        web = findViewById(R.id.webv);
    }

    fun drawWatchInfo(){
        brandTxt.text = w.brand;
        modelTxt.text = w.model;
        watchAtt.text = "Last Adjustment: "+w.lastAdjust+"\nMovement type: "+w.type+"\nCaliber: "+w.caliber+"\nTheoretic accuracy: "+w.theoreticAccuracy+"\nMore information: "+w.moreInfo;
    }


    fun adjustWatch(){
        var msg = "";
        if(input.text.toString().isBlank()){
            w.lastAdjust = LocalDateTime.now().format(Watch.formatter);
            statusTxt.setTextColor(Color.GREEN);
            msg = "Successfully adjusted!!";
        }
        else{
            var strDate = input.text.toString();
            try{
                var dateTime = LocalDateTime.parse(strDate, Watch.formatter);
                w.lastAdjust = strDate;
                statusTxt.setTextColor(Color.GREEN);
                msg = "Successfully adjusted!!";
            }
            catch(ex: DateTimeParseException){
                statusTxt.setTextColor(Color.RED);
                msg = "Error while parsing the date...";
            }
        }

        statusTxt.text = msg;
        statusTxt.visibility = View.VISIBLE;

        drawWatchInfo();
    }


    fun setupWebView(url: String){
        web.settings.javaScriptEnabled = true; // enabling js
        web.webViewClient = WebViewClient(); // Load web inside the app.
        web.loadUrl(url);
    }
}