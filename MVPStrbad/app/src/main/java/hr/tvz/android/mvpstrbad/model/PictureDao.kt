package hr.tvz.android.mvpstrbad.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PictureDao {
    @Query("SELECT * FROM picture")
    fun getAll(): List<Picture>

    @Query("SELECT * FROM picture WHERE id = :id")
    fun getById(id: Int): Picture

    @Insert
    fun insertAll(vararg pictures: Picture)

    @Delete
    fun delete(picture: Picture)
}