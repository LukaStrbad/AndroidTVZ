package hr.tvz.android.mvpstrbad

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.tvz.android.mvpstrbad.model.Picture
import hr.tvz.android.mvpstrbad.model.PictureDao

@Database(entities = [Picture::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pictureDao(): PictureDao
}