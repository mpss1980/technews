package br.com.alura.technewsupdated.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.com.alura.technewsupdated.model.New

@Dao
interface NewsDAO {

    @Query("SELECT * FROM New ORDER BY id DESC")
    fun getAll(): List<New>

    @Query("SELECT * FROM New WHERE id = :id")
    fun getById(id: Long): New?

    @Insert(onConflict = REPLACE)
    fun save(new: New)

    @Insert(onConflict = REPLACE)
    fun save(news: List<New>)

    @Delete
    fun remove(new: New)
}