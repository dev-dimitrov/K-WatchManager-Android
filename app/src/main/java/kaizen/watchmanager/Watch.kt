import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Locale
import java.util.TreeMap

class Watch(
    b: String,
    private var model: String,
    ty: String,
    private var caliber: String,
    private var theoreticAccuracy: String,
    private var moreInfo: String
) :
    Serializable {
    private var brand: String
    private var type: String
    var lastAdjust: LocalDateTime? = null
    var log: TreeMap<LocalDateTime, String?>
    var newLastAdjust: LocalDateTime? = null
    var newLog: TreeMap<LocalDateTime, String>? = null

    init {
        brand = b.uppercase(Locale.getDefault())
        type = ty.uppercase(Locale.getDefault())

        log = TreeMap()
    }

    fun setBrand(b: String) {
        brand = b.uppercase(Locale.getDefault())
    }

    fun setModel(m: String) {
        model = m.uppercase(Locale.getDefault())
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
