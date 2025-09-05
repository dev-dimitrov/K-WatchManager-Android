import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Locale
import java.util.TreeMap


class Watch(
    b: String,
    var model: String,
    ty: String,
    var caliber: String,
    var theoreticAccuracy: String,
    var moreInfo: String
) :
    Serializable {
    var brand: String
    var type: String
    var lastAdjust: LocalDateTime? = null
    var log: TreeMap<LocalDateTime, String?>
    var newLastAdjust: LocalDateTime? = null
    var newLog: TreeMap<LocalDateTime, String>? = null

    init {
        brand = b.uppercase(Locale.getDefault())
        type = ty.uppercase(Locale.getDefault())

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
