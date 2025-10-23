package kaizen.watchmanager

import Watch
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.io.Serializable
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class WatchScreenActivity : AppCompatActivity() {
    lateinit var brandTxt: TextView;
    lateinit var modelTxt: TextView;
    lateinit var adjustBtn: Button;
    lateinit var accBtn: Button;
    lateinit var watchAtt: TextView;
    lateinit var w: Watch;
    lateinit var initalWatch: Watch;
    lateinit var statusTxt: TextView;
    lateinit var input: TextInputEditText;
    lateinit var web: WebView;
    lateinit var nowTime: LocalTime;
    lateinit var infoCont: ScrollView;
    lateinit var revertBtn: ImageButton;
    var logShowing = false;
    var formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    var defHint = "yyy-mm-dd hh:mm:ss";
    var accHint = "(hh:mm:ss) and then press the button again";
    var firstHitted = true;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_watch_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupLayout();
        statusTxt.visibility = View.INVISIBLE;
        w = intent.getSerializableExtra("WATCH", Watch::class.java)!!; // Receives the watch from the main activity
        initalWatch = Watch.copy(w);
        drawWatchInfo();
        adjustBtn.setOnClickListener{
            web.visibility = View.INVISIBLE;
            if(!firstHitted){
                firstHitted = true;
                input.text?.clear();
                input.hint = defHint;
                statusTxt.text = "Switched mode to Adjust, type now the date that was adjusted..."
            }
            else{
                adjustWatch();
            }
        }

        onBackPressedDispatcher.addCallback (this){
            var i = Intent();
            i.putExtra("WATCH", w as Serializable);
            setResult(RESULT_OK, i);
            finish();
        }

        watchAtt.setOnClickListener({
            if(!logShowing){
                var logs = w.getLog();
                watchAtt.text = if (!logs.isEmpty()) logs+"Tap HERE to see watch information" else "Empty logs\nTap HERE to see watch information";
                logShowing = true;
            }
            else{
                drawWatchInfo();
                logShowing = false;
            }

        });

        revertBtn.setOnClickListener({
            web.visibility = View.INVISIBLE;
            w = initalWatch;
            drawWatchInfo();
            statusTxt.text = "Reverted changes.";
            statusTxt.visibility = View.VISIBLE;
            statusTxt.setTextColor(Color.GREEN);
            logShowing = false;

        });

        accBtn.setOnClickListener({
            startCheckAccuracyProcess();
            firstHitted = !firstHitted;
        })
        // setupWebView("https://www.time.is");
    }

    fun setupLayout(){
        brandTxt = findViewById(R.id.brandText);
        modelTxt = findViewById(R.id.modelText);
        adjustBtn = findViewById(R.id.adjustButton);
        brandTxt = findViewById(R.id.brandText);
        accBtn = findViewById(R.id.checkButton);
        watchAtt = findViewById(R.id.attText);
        statusTxt = findViewById(R.id.statusText);
        input = findViewById(R.id.inputText);
        web = findViewById(R.id.webv);
        revertBtn = findViewById(R.id.revertButton);
        infoCont = findViewById(R.id.infoContainer);
        web.visibility = View.INVISIBLE;
    }

    fun drawWatchInfo(){
        brandTxt.text = w.brand;
        modelTxt.text = w.model;
        watchAtt.text = "Last Adjustment: "+w.lastAdjust+"\nMovement type: "+w.type+"\nCaliber: "+w.caliber+"\nTheoretic accuracy: "+w.theoreticAccuracy+"\nMore information: "+w.moreInfo+"\nTap HERE to see logs";
    }


    fun adjustWatch(){
        input.hint = defHint;
        var msg = "";
        var status = 0;
        var strDate = "";
        // if there is no input = means that was adjusted now
        if(input.text.toString().isBlank()){
            w.lastAdjust = LocalDateTime.now().format(Watch.formatter); // format to String
            strDate = w.lastAdjust; // save the date for later
        }
        else{
            strDate = input.text.toString();
            try{
                var dateTime = LocalDateTime.parse(strDate, Watch.formatter);
                w.lastAdjust = strDate;
            }
            catch(ex: DateTimeParseException){
                statusTxt.setTextColor(Color.RED);
                msg = "Wrong format for adjust watch...";
                status = 1;
            }
        }



        drawWatchInfo();
        if(status == 0){
            w.logWrite(strDate, "Adjusted.");
            statusTxt.setTextColor(Color.GREEN);
            msg = "Successfully adjusted!!";
        }

        statusTxt.text = msg;
        statusTxt.visibility = View.VISIBLE;
    }


    fun setupWebView(url: String){
        web.visibility = View.VISIBLE;
        web.settings.javaScriptEnabled = true; // enabling js
        web.webViewClient = WebViewClient(); // Load web inside the app.
        web.loadUrl(url);
    }

    fun startCheckAccuracyProcess(){
        // If the button was hit for the first time, load the web view and show the status text.
        if(firstHitted){
            setupWebView("https://www.time.is");
            nowTime = LocalTime.now();
            // Add 30s to now, to give the user time to put correctly the time
            nowTime = nowTime.plusSeconds(30);
            var nowTimeString = formatter.format(nowTime);
            statusTxt.setTextColor(Color.WHITE);
            statusTxt.setText("What time is it in your watch at "+nowTimeString+" ?");
            statusTxt.visibility = View.VISIBLE;
            input.hint = accHint;
        }
        else{
            // get time form input
            var text = input.text.toString();
            try{
                var l = LocalTime.parse(text,formatter);
                var secondsDiff = nowTime.until(l, ChronoUnit.SECONDS);
                var text = "";
                if(secondsDiff < 0){
                    text = (secondsDiff.toString());
                }
                else{
                    text = "+"+secondsDiff.toString();
                }
                var deviation = getDeviation(w.lastAdjust, secondsDiff.toDouble());
                statusTxt.text = text+"s.";
                if(deviation != null){
                    statusTxt.setText(text+"s. About "+deviation.toString()+"s/day.");
                }

            }
            catch(ex: DateTimeParseException){
                statusTxt.setTextColor(Color.RED);
                statusTxt.text = "Wrong format for checking accuracy.";
                return;
            }
            w.logWrite(Watch.formatter.format(LocalDateTime.now()),statusTxt.text.toString());
            input.hint = defHint;
        }
        input.text?.clear();
    }

    fun getDeviation(la: String, s: Double): Double?{
        var result: Double?;
        result = null;
        if(la.isNotBlank()){
            var now = LocalDateTime.now();
            var then = LocalDateTime.parse(la,Watch.formatter);
            // The days between now and the last adjustment
            var days = then.until(now,ChronoUnit.DAYS).toDouble();
            // If the last adjustment was today, do not operate..
            if(days.toInt() == 0){
                result = s;
            }
            else{
                result = s/days;
            }

        }
        return result;
    }

    //TODO make an universal statusShow method for all activities
}