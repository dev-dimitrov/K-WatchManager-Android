package kaizen.watchmanager

import android.content.Intent
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
import com.google.android.material.textfield.TextInputEditText
import java.io.Serializable;
import Watch;
import androidx.activity.addCallback

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class WatchModActivity : AppCompatActivity() {
    lateinit var brandI: TextInputEditText;
    lateinit var modelI: TextInputEditText;
    lateinit var typeI: TextInputEditText;
    lateinit var caliberI: TextInputEditText;
    lateinit var theoreticAccuracyI: TextInputEditText;
    lateinit var moreInfoI: TextInputEditText;
    lateinit var addWatch: Button;
    lateinit var statusText: TextView;
    lateinit var cancel: Button;
    lateinit var watch: Watch;
    lateinit var title: TextView;
    lateinit var clear: Button;
    var firstPressed = true;
    var firstPressedLogs = true;
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge();
        setContentView(R.layout.activity_watch_modification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        watch = intent.getSerializableExtra("WATCH", Watch::class.java)!!;
        setupLayout();

        onBackPressedDispatcher.addCallback {
            var intent = Intent(); // create a new intent
            intent.putExtra("WATCH",watch as Serializable); // assign the new watch created to the key "WATCH"
            setResult(RESULT_OK, intent); // Set the result with the intent
            finish(); // Exit this activity
        }

        addWatch.setOnClickListener{
            var w = modifyWatch()!!;
            // Log.i("DAVIDO-INFO",w.fullInfo());
            if(w != null){
                var intent = Intent(); // create a new intent
                intent.putExtra("WATCH",w as Serializable); // assign the new watch created to the key "WATCH"
                setResult(RESULT_OK, intent); // Set the result with the intent
                finish(); // Exit this activity
            }
        }

        cancel.setOnClickListener{
            if(firstPressed){
                MainActivity.showStatus("Press again to delete the watch",Color.RED,statusText);
                firstPressed = false;
            }
            else{
                var intent = Intent();
                intent.putExtra("WATCH",watch as Serializable);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }

        clear.setOnClickListener{
            if(firstPressedLogs){
                MainActivity.showStatus("Press again to delete the logs",Color.RED,statusText);
                firstPressedLogs = false;
            }
            else{
                watch.clearLog();
                MainActivity.showStatus("Logs cleared, this cannot be undone",Color.RED, statusText);
            }

        }
    }

    fun setupLayout(){
        brandI = findViewById(R.id.brandInput);
        brandI.setText(watch.brand);

        modelI = findViewById(R.id.modelInput);
        modelI.setText(watch.model);

        typeI = findViewById(R.id.tyInput);
        typeI.setText(watch.type);

        caliberI = findViewById(R.id.caliberInput);
        caliberI.setText(watch.caliber);

        moreInfoI = findViewById(R.id.moreInfoInput);
        moreInfoI.setText(watch.moreInfo);

        theoreticAccuracyI = findViewById(R.id.taInput);
        theoreticAccuracyI.setText(watch.theoreticAccuracy);

        addWatch = findViewById(R.id.addWatchButton);
        statusText = findViewById(R.id.statusTxt);

        cancel = findViewById(R.id.cancelButton);
        statusText.visibility = View.INVISIBLE;
        title = findViewById(R.id.textView);
        clear = findViewById(R.id.clearButton);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun modifyWatch(): Watch? {
        var w: Watch;
        var brand = brandI.text.toString().trim();
        var model = modelI.text.toString().trim();
        var type = typeI.text.toString().trim();
        var caliber = caliberI.text.toString().trim();
        var moreInfo = moreInfoI.text.toString().trim();
        var theoreticAccuracy = theoreticAccuracyI.text.toString().trim();

        if(brand.isEmpty() || model.isEmpty()){
            MainActivity.showStatus("You must specify the brand and model!", Color.RED,statusText);
            return null;
        }

        statusText.visibility = View.INVISIBLE;
        w = Watch(brand,model,type,caliber,moreInfo,theoreticAccuracy);
        w.log = watch.log;
        w.lastAdjust = watch.lastAdjust;
        MainActivity.showStatus("Watch successfully modified!", Color.GREEN,statusText);
        return w;
    }

}