package com.splhead.studentagenda.data.repository

import android.app.Application
import com.splhead.studentagenda.data.StudentDao
import com.splhead.studentagenda.data.StudentRoomDatabase
import com.splhead.studentagenda.model.Student
import kotlinx.coroutines.flow.Flow

class StudentRepository(
    application: Application
) {
    private var allStudents: Flow<List<Student>>
    private var studentDao: StudentDao

    init {
        val database = StudentRoomDatabase.getDatabase(application)
        studentDao = database.studentDao()
        allStudents = studentDao.getAll()
    }

    suspend fun insert(student: Student) = studentDao.insert(student)

    suspend fun update(student: Student) = studentDao.update(student)

    suspend fun delete(student: Student) = studentDao.delete(student)

    fun getAllStudents() = allStudents

    fun findByName(name: String) = studentDao.findByName(name)

    fun findById(id: Int) = studentDao.getStudent(id)
}