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
import android.util.Log

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
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge();
        setContentView(R.layout.activity_watch_creation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        watch = intent.getSerializableExtra("WATCH", Watch::class.java)!!;
        setupLayout();



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
            finish();
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
        title.text = "Watch modification";
        addWatch.text = "Modify";
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun modifyWatch(): Watch? {
        var w: Watch;
        var brand = brandI.text.toString();
        var model = modelI.text.toString();
        var type = typeI.text.toString();
        var caliber = caliberI.text.toString();
        var moreInfo = moreInfoI.text.toString();
        var theoreticAccuracy = theoreticAccuracyI.text.toString();

        if(brand.isEmpty() || model.isEmpty()){
            showStatus("Error. You must specify the brand and model", Color.RED);
            return null;
        }

        statusText.visibility = View.INVISIBLE;
        w = Watch(brand,model,type,caliber,moreInfo,theoreticAccuracy);
        w.log = watch.log;
        w.lastAdjust = watch.lastAdjust;
        showStatus("Watch successfully modified!", Color.GREEN);
        return w;
    }

    fun showStatus(msg: String, color: Int){
        statusText.setTextColor(color);
        statusText.text = msg;
        statusText.visibility = View.VISIBLE;
    }
}