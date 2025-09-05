import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Locale
import java.util.TreeMap


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
    var lastAdjust: LocalDateTime? = null;
    var log: TreeMap<LocalDateTime, String?>;
    var na = "Not Specified";

    init {
        brand = b.uppercase(Locale.getDefault())
        model = if(m.isEmpty()) na else m.capitalize();
        type = ty.uppercase(Locale.getDefault())
        caliber = if(c.isEmpty()) na else c;
        theoreticAccuracy = if(t.isEmpty()) na else t;
        moreInfo = if(mi.isEmpty()) na else mi;
        log = TreeMap()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addLog(date: LocalDateTime, action: String?) {
        log[date] = action
    }

    fun removeLastEntry(): Int {
        val k = log.lastKey()
        return if (log.remove(k) == null) -1 else 0
    }

    override fun toString(): String{
        return brand+" "+model;
    }
}
