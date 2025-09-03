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
    private var model: String,
    ty: String,
    private var caliber: String,
    private var theoreticAccuracy: String,
    private var moreInfo: String
) :
    Parcelable {
    private var brand: String
    private var type: String
    var lastAdjust: LocalDateTime? = null
    var log: TreeMap<LocalDateTime, String?>
    var newLastAdjust: LocalDateTime? = null
    var newLog: TreeMap<LocalDateTime, String>? = null

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(), // b
        parcel.readString().toString(), // model
        parcel.readString().toString(), // ty
        parcel.readString().toString(), // caliber
        parcel.readString().toString(), // theoreticAccuracy
        parcel.readString().toString()  // moreInfo
    ) {
        brand = brand.uppercase(Locale.getDefault())
        type = type.uppercase(Locale.getDefault())
    }


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

    override fun describeContents(): Int {
       return 0;
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(brand);
        p0.writeString(model);
        p0.writeString(type);
        p0.writeString(theoreticAccuracy);
        p0.writeString(caliber);
        p0.writeString(moreInfo);
    }

    companion object CREATOR : Parcelable.Creator<Watch> {
        override fun createFromParcel(parcel: Parcel): Watch {
            return Watch(parcel)
        }

        override fun newArray(size: Int): Array<Watch?> {
            return arrayOfNulls(size)
        }
    }

}
