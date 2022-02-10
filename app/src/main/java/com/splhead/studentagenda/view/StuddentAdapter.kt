package com.splhead.studentagenda.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splhead.studentagenda.databinding.StudentItemBinding
import com.splhead.studentagenda.model.Student

class StudentAdapter(private val onStudentItemClicked: (Student) -> Unit) :
    ListAdapter<Student, StudentAdapter.StudentViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            StudentItemBinding
                .inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
        )
    }

    fun getStudentAt(position: Int): Student {
        return this.getItem(position)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = getItem(position)
        holder.itemView.setOnClickListener {
            onStudentItemClicked(currentStudent)
        }
        holder.bind(currentStudent)
    }

    inner class StudentViewHolder(private var binding: StudentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentStudent: Student) {
            currentStudent.also { student ->
                binding.tvName.text = student.name
                binding.tvEmail.text = student.email
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Student> =
            object : DiffUtil.ItemCallback<Student>() {
                override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
                    return oldItem.name == newItem.name &&
                            oldItem.email == newItem.email &&
                            oldItem.phone == newItem.phone &&
                            oldItem.birthday == newItem.birthday
                }
            }
    }
}
