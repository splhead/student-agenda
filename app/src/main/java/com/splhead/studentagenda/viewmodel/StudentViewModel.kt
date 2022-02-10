package com.splhead.studentagenda.viewmodel

import androidx.lifecycle.*
import com.splhead.studentagenda.data.StudentDao
import com.splhead.studentagenda.data.repository.StudentRepository
import com.splhead.studentagenda.model.Student
import kotlinx.coroutines.launch

class StudentViewModel(private val studentRepository: StudentRepository) : ViewModel() {
    private var allStudents = studentRepository.getAllStudents().asLiveData()


    fun getAllStudents() = allStudents

    fun findByName(name: String): LiveData<List<Student>> = studentRepository
        .findByName(name).asLiveData()

    fun findById(id: Int): LiveData<Student> = studentRepository.findById(id).asLiveData()

    private fun insert(student: Student) {
        viewModelScope.launch {
            studentRepository.insert(student)
        }
    }

    private fun update(student: Student) {
        viewModelScope.launch {
            studentRepository.update(student)
        }
    }

    fun delete(student: Student) {
        viewModelScope.launch {
            studentRepository.delete(student)
        }
    }

    fun isEntryValid(name: String, email: String, phone: String, birthday: String): Boolean {
        if (name.isBlank() || email.isBlank() || phone.isBlank() || birthday.isBlank()) {
            return false
        }
        return true
    }

    fun addNewStudent(name: String, email: String, phone: String, birthday: String) {
        val newStudent = getNewStudentEntry(name, email, phone, birthday)
        insert(newStudent)
    }

    fun updateStudent(id: Int, name: String, email: String, phone: String, birthday: String) {
        val updatedStudent = getUpdatedStudentEntry(id, name, email, phone, birthday)
        update(updatedStudent)
    }

    private fun getNewStudentEntry(
        name: String,
        email: String,
        phone: String,
        birthday: String
    ): Student {
        return Student(
            name = name,
            email = email,
            phone = phone,
            birthday = birthday
        )
    }

    private fun getUpdatedStudentEntry(
        id: Int,
        name: String,
        email: String,
        phone: String,
        birthday: String
    ): Student {
        return Student(
            id = id,
            name = name,
            email = email,
            phone = phone,
            birthday = birthday
        )
    }
}

class StudentViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}