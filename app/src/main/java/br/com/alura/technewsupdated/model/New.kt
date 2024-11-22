package br.com.alura.technewsupdated.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class New(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val content: String = ""
)
