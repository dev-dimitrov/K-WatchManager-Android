package kaizen.watchmanager

import Watch
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var addBtn: ImageButton;
    lateinit var list: ListView;
    var arrayList = ArrayList<Watch>();
    lateinit var adapter: ArrayAdapter<Watch>;
    lateinit var watch: Watch;
    lateinit var statusTxt: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addBtn = findViewById(R.id.addButton);
        list = findViewById(R.id.listView);
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        statusTxt = findViewById(R.id.mainStatusText);
        statusTxt.visibility = View.INVISIBLE;
        list.adapter = adapter;
        loadWatches();
        wipeData();
        addBtn.setOnClickListener({
            addWatch();
        });

        list.setOnItemClickListener{parent, view, position, id ->
            var selectedWatch = arrayList.get(position.toInt());
            var intent = Intent(this,WatchScreenActivity::class.java);
            intent.putExtra("WATCH", selectedWatch as Serializable);
            launcher.launch(intent);
        }
    }

    fun addWatch() {
        var intent = Intent(this, WatchCreationActivity::class.java)
        launcher.launch(intent);
    }

    fun loadWatches(){

        try{
            var f = File(this.filesDir, "watches.bin")
            var o = ObjectInputStream(FileInputStream(f));
            try{
                arrayList = o.readObject() as ArrayList<Watch>;
                showStatus("Loaded successfully", Color.GREEN);
            }
            catch(ex: IOException){

            }
            finally {
                o.close();
            }
        }
        catch(ex: FileNotFoundException){
            ex.printStackTrace()
        }

    }

    fun saveWatches(){
        var f = File(this.filesDir, "watches.bin");
        var o = ObjectOutputStream(FileOutputStream(f));
        try{
            o.writeObject(arrayList);
        }
        catch(ex: IOException){
            ex.printStackTrace();
        }
        finally {
            o.close();
        }
    }

    fun wipeData(){
        var f = File(this.filesDir, "watches.bin");
        var o = ObjectOutputStream(FileOutputStream(f));
        try{
            o.writeObject(ArrayList<Watch>());
            showStatus("Data wiped, remove method to keep data", Color.GREEN);
        }
        catch(ex: IOException){
            ex.printStackTrace();
        }
        finally {
            o.close();
        }
    }


    // This will execute as a callback, when an activity sends back a object
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newWatch = result.data?.getSerializableExtra("WATCH")!! as Watch;
            arrayList.add(newWatch); // adding the brand new watch
            saveWatches();
            adapter.notifyDataSetChanged(); // Notify that the data changes and updates the listview
        }
    }

    public fun showStatus(msg: String, color: Int){
        statusTxt.setTextColor(color);
        statusTxt.text = msg;
        statusTxt.visibility = View.VISIBLE;
    }
}