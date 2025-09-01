package kaizen.watchmanager

import Watch
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var addBtn: ImageButton;
    lateinit var list: ListView;
    lateinit var input: TextInputEditText;
    var arrayList = ArrayList<Watch>();
    lateinit var adapter: ArrayAdapter<Watch>;
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

        list.adapter = adapter;


        addBtn.setOnClickListener({
            addWatch();
        });

        list.setOnItemClickListener{parent, view, id, position ->
            var selectedWatch = arrayList.get(position.toInt());
        }
    }

    fun addWatch() {
        var title = input.text.toString();
        var watch = Watch(title,"","","","","");
        arrayList.add(watch);
        adapter.notifyDataSetChanged();
    }
}