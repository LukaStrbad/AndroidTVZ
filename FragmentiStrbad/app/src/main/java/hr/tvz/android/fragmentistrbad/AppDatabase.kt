package hr.tvz.android.fragmentistrbad

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.tvz.android.fragmentistrbad.model.Picture
import hr.tvz.android.fragmentistrbad.model.PictureDao

@Database(entities = [Picture::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pictureDao(): PictureDao
}