package com.splhead.studentagenda.data

import androidx.room.*
import com.splhead.studentagenda.model.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert
    suspend fun insert(student: Student)

    @Update
    suspend fun update(student: Student)

    @Delete
    suspend fun delete(student: Student)

    @Query("SELECT * FROM student WHERE name LIKE '%' || :name || '%'")
    fun findByName(name: String): Flow<List<Student>>

    @Query("SELECT * FROM student ORDER BY name ASC")
    fun getAll(): Flow<List<Student>>

    @Query("SELECT * FROM student WHERE id = :id")
    fun getStudent(id: Int): Flow<Student>
}