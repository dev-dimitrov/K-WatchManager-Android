import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TreeMap

@RequiresApi(Build.VERSION_CODES.O)
class Watch( // constructor parameters
    b: String,
    m: String,
    ty: String,
    c: String,
    t: String,
    mi: String
) :
    Serializable { // actual attributes
    var brand: String
    var model: String;
    var type: String;
    var caliber: String;
    var theoreticAccuracy: String;
    var moreInfo: String;
    var lastAdjust = "";
    var log = LinkedHashMap<String,String>();
    var na = "Not Specified";

    init {
        brand = b.uppercase(Locale.getDefault())
        model = if(m.isEmpty()) na else m.capitalize();
        type = ty.uppercase(Locale.getDefault())
        caliber = if(c.isEmpty()) na else c;
        theoreticAccuracy = if(t.isEmpty()) na else t;
        moreInfo = if(mi.isEmpty()) na else mi;
        log = LinkedHashMap();
    }

    override fun toString(): String{
        return brand+" "+model;
    }

    fun logWrite(strDate: String, desc: String){
        log.put(strDate,desc);
    }


    fun getLog(): String{
        var keyS = log.keys;
        var s = "";
        for(k in keyS){
            s+= k+" | "+log.get(k)+"\n";
        }
        return s; // return all the logs in String.
    }

    override fun equals(other: Any?): Boolean {
        var o = other as Watch;
        return this.brand.equals(o.brand) && this.model.equals(o.model);
    }

    companion object{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        fun copy(w: Watch) : Watch{
            var newWatch = Watch(w.brand,w.model,w.type,w.caliber, w.theoreticAccuracy,w.moreInfo);
            newWatch.lastAdjust = w.lastAdjust;
            newWatch.log = LinkedHashMap(w.log);
            return newWatch;
        }
    }
}
