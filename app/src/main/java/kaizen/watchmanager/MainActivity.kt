package kaizen.watchmanager

import Watch
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.ArrayList
import androidx.annotation.RequiresApi;

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    lateinit var addBtn: ImageButton;
    lateinit var list: ListView;
    var arrayList = ArrayList<Watch>();
    lateinit var adapter: ArrayAdapter<Watch>;
    lateinit var statusTxt: TextView;
    lateinit var title: TextView;
    var pressedWatch = false;
    var watchPos = -1;
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        statusTxt = findViewById(R.id.mainStatusText);
        title = findViewById(R.id.titleText);
        statusTxt.visibility = View.INVISIBLE;
        loadWatches();

        addBtn = findViewById(R.id.addButton);
        list = findViewById(R.id.listView);
        // Asigning an adapter to de arraylist of watches
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);



        list.adapter = adapter;

        check4emptyList();

        addBtn.setOnClickListener{
            addWatch();
        };

        list.setOnItemClickListener{parent, view, position, id ->
            pressedWatch = true;
            watchPos = position.toInt();
            var selectedWatch = arrayList.get(watchPos);
            var intent = Intent(this,WatchScreenActivity::class.java);
            intent.putExtra("WATCH", selectedWatch as Serializable);
            launcher.launch(intent);
        }

        list.setOnItemLongClickListener { parent, view, position, id ->
            pressedWatch = true; // This will update the watch, managed by the launcher callback
            var heldWatch = arrayList.get(position.toInt());
            watchPos = position.toInt();
            var intent = Intent(this,WatchModActivity::class.java);
            intent.putExtra("WATCH",heldWatch as Serializable);
            launcher.launch(intent);
            true // This is to consume the event and not trigger the click listener
        }

        title.setOnClickListener{
            var intent = Intent(this, AboutActivity::class.java)
            watchPos = -1;
            launcher.launch(intent);
        }
    }


    fun addWatch() {
        var intent = Intent(this, WatchCreationActivity::class.java)
        watchPos = -1;
        launcher.launch(intent);
    }

    fun loadWatches(){

        try{
            var f = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "watches.bin");
            var o = ObjectInputStream(FileInputStream(f));
            try{
                arrayList = o.readObject() as ArrayList<Watch>;
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
        var f = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "watches.bin");
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
        var f = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "watches.bin");
        var o = ObjectOutputStream(FileOutputStream(f));
        try{
            o.writeObject(ArrayList<Watch>());
            showStatus("Data wiped, remove method to keep data", Color.GREEN, statusTxt);
        }
        catch(ex: IOException){
            ex.printStackTrace();
        }
        finally {
            o.close();
        }
    }


    // This will execute as a callback, when an activity sends back a object
    @RequiresApi(Build.VERSION_CODES.O)
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newWatch = result.data?.getSerializableExtra("WATCH")!! as Watch;
            // Log.i("DAVIDO-INFO","MAIN -> "+newWatch.fullInfo());
            // This if protects from callback that are coming from the WatchCreationActivity
            if(pressedWatch && watchPos != -1){
                arrayList.removeAt(watchPos) // Removes the old watch
            }
            // Adds the new modified watch
            arrayList.add(newWatch); // adding the brand new watch or update the one with the new last adjustment
        }
        else if (result.resultCode == RESULT_CANCELED && watchPos != -1) {
            arrayList.removeAt(watchPos);
        }
        pressedWatch = false;
        check4emptyList();
        saveWatches();
        adapter.notifyDataSetChanged(); // Notify that the data changes and updates the listview
    }


    // Simple method to print status messages with a color

    // A method to check it the list is empty, to show a message under the list container
    private fun check4emptyList(){
        if(arrayList.isEmpty()){
            showStatus("No watches added for now.",Color.WHITE,statusTxt);
        }
        else{
            showStatus("Press and hold a watch to edit it.",Color.WHITE,statusTxt);
        }
    }
    companion object{
        fun showStatus(msg: String, color: Int, text: TextView){
            text.text = msg;
            text.setTextColor(color);
            text.visibility = View.VISIBLE;
        }
    }

}