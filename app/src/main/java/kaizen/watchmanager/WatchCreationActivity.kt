package kaizen.watchmanager

import Watch
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.google.android.material.textfield.TextInputEditText
import java.io.Serializable

class WatchCreationActivity : AppCompatActivity() {
    lateinit var brandI: TextInputEditText;
    lateinit var modelI: TextInputEditText;
    lateinit var typeI: TextInputEditText;
    lateinit var caliberI: TextInputEditText;
    lateinit var theoreticAccuracyI: TextInputEditText;
    lateinit var moreInfoI: TextInputEditText;
    lateinit var addWatch: Button;
    lateinit var statusText: TextView;
    lateinit var cancel: Button;
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
        statusText = findViewById(R.id.statusTxt);
        theoreticAccuracyI = findViewById(R.id.taInput);
        cancel = findViewById(R.id.cancelButton);

        statusText.visibility = View.INVISIBLE;

        addWatch.setOnClickListener{
            var w = createWatch();
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

    fun createWatch(): Watch? {
        var w: Watch;
        var brand = brandI.text.toString();
        var model = modelI.text.toString();
        var type = typeI.text.toString();
        var caliber = caliberI.text.toString();
        var moreInfo = moreInfoI.text.toString();
        var theoreticAccuracy = theoreticAccuracyI.text.toString();
        var na = "Not specified";

        if(brand.isEmpty() || model.isEmpty()){
            showStatus("Error. You must specify the brand and model", Color.RED);
            return null;
        }

        statusText.visibility = View.INVISIBLE;

        type = if (type.isEmpty()) na else type;
        caliber = if (caliber.isEmpty()) na else caliber;
        moreInfo = if (moreInfo.isEmpty()) na else moreInfo;
        theoreticAccuracy = if (theoreticAccuracy.isEmpty()) na else theoreticAccuracy;
        w = Watch(brand,model,type,caliber,theoreticAccuracy,moreInfo);

        showStatus("Watch created successfully!",Color.GREEN);
        return w;
    }

    fun showStatus(msg: String, color: Int){
        statusText.setTextColor(color);
        statusText.text = msg;
        statusText.visibility = View.VISIBLE;
    }
}