package com.splhead.studentagenda

import android.app.Application
import com.splhead.studentagenda.data.StudentRoomDatabase
import com.splhead.studentagenda.data.repository.StudentRepository

class StudentAgendaApplication : Application() {
    val database: StudentRoomDatabase by lazy { StudentRoomDatabase.getDatabase(this)}
    val repository by lazy { StudentRepository(this) }
}