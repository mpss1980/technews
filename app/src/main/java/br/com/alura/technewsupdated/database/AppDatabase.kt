package br.com.alura.technewsupdated.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.alura.technewsupdated.database.dao.NewsDAO
import br.com.alura.technewsupdated.model.News

private const val DATABASE_NAME = "news.db"

@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val newsDAO: NewsDAO

    companion object {

        private lateinit var db: AppDatabase
        fun getInstance(context: Context): AppDatabase {
            if (::db.isInitialized) return db

            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME,
            ).build()

            return db
        }
    }
}