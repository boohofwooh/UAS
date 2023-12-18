import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.uas.TripDao
import com.example.uas.wTrip

@Database(entities = [wTrip::class], version = 2, exportSchema = false)
abstract class TripRoomDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile
        private var INSTANCE: TripRoomDatabase? = null

        fun getInstance(context: Context): TripRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripRoomDatabase::class.java, "clazz_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}
