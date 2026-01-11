package kaizen.watchmanager

import Watch
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.io.Serializable

class WatchCreationActivity : AppCompatActivity() {
    private lateinit var brandI: TextInputEditText;
    private lateinit var modelI: TextInputEditText;
    private lateinit var typeI: TextInputEditText;
    private lateinit var caliberI: TextInputEditText;
    private lateinit var theoreticAccuracyI: TextInputEditText;
    private lateinit var moreInfoI: TextInputEditText;
    private lateinit var addWatch: Button;
    private lateinit var statusText: TextView;
    private lateinit var cancel: Button;
    private lateinit var spinner: Spinner;
    @RequiresApi(Build.VERSION_CODES.O)
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
        caliberI = findViewById(R.id.caliberInput);
        moreInfoI = findViewById(R.id.moreInfoInput);
        addWatch = findViewById(R.id.addWatchButton);
        statusText = findViewById(R.id.statusTxt);
        theoreticAccuracyI = findViewById(R.id.taInput);
        cancel = findViewById(R.id.cancelButton);

        // Stuff for the combo box... In android is called "Spinner" (lmao)
        spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.caliber_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }


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

    @RequiresApi(Build.VERSION_CODES.O)
    fun createWatch(): Watch? {
        var w: Watch;
        val brand = brandI.text.toString().trim();
        val model = modelI.text.toString().trim();
        val type = typeI.text.toString().trim();
        val caliber = caliberI.text.toString().trim();
        val moreInfo = moreInfoI.text.toString().trim();
        val theoreticAccuracy = theoreticAccuracyI.text.toString().trim();

        if(brand.isEmpty() || model.isEmpty()){
            MainActivity.showStatus("You must specify the brand and model!", Color.RED,statusText);
            return null;
        }

        statusText.visibility = View.INVISIBLE;
        w = Watch(brand,model,type,caliber,theoreticAccuracy,moreInfo);
        MainActivity.showStatus("Watch created successfully!",Color.GREEN,statusText);
        return w;
    }
}