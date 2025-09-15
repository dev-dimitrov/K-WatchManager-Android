import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    var lastAdjust = "";
    var log = "";
    var na = "Not Specified";

    init {
        brand = b.uppercase(Locale.getDefault())
        model = if(m.isEmpty()) na else m.capitalize();
        type = ty.uppercase(Locale.getDefault())
        caliber = if(c.isEmpty()) na else c;
        theoreticAccuracy = if(t.isEmpty()) na else t;
        moreInfo = if(mi.isEmpty()) na else mi;
    }


    override fun toString(): String{
        return brand+" "+model;
    }


    override fun equals(other: Any?): Boolean {
        var o = other as Watch;
        return this.brand.equals(o.brand) && this.model.equals(o.model);
    }

    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}
