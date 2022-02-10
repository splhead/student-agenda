package com.splhead.studentagenda.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student" )
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val birthday: String,
)
