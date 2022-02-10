package com.splhead.studentagenda.view

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.splhead.studentagenda.R
import com.splhead.studentagenda.StudentAgendaApplication
import com.splhead.studentagenda.databinding.FragmentSaveStudentBinding
import com.splhead.studentagenda.extensions.format
import com.splhead.studentagenda.model.Student
import com.splhead.studentagenda.viewmodel.StudentViewModel
import com.splhead.studentagenda.viewmodel.StudentViewModelFactory
import java.util.*

class SaveStudentFragment : Fragment() {
    private val studentViewModel: StudentViewModel by activityViewModels {
        StudentViewModelFactory(
            (activity?.application as StudentAgendaApplication).repository
        )
    }

    private val navigationArgs: SaveStudentFragmentArgs by navArgs()
    lateinit var student: Student

    private lateinit var _binding: FragmentSaveStudentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDateInput()
        setupCancel()

        binding.toolbar.setNavigationOnClickListener {
            navigateToBack()
        }

        binding.tietPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        val id = navigationArgs.studentId
        if (id > 0) {
            studentViewModel.findById(id).observe(this.viewLifecycleOwner) { selectedStudent ->
                student = selectedStudent
                bind(student)

                binding.bSaveStudent.setOnClickListener {
                    updateStudent()
                }
            }

        } else {
            binding.bSaveStudent.setOnClickListener {
                addNewStudent()
            }
        }
    }

    private fun setupCancel() {
        binding.bCancelSaveStudent.setOnClickListener {
            navigateToBack()
        }
    }

    private fun addNewStudent() {
        if (isEntryValid()) {
            studentViewModel.addNewStudent(
                binding.tietName.text.toString(),
                binding.tietEmail.text.toString(),
                binding.tietPhone.text.toString(),
                binding.tietBirthday.text.toString()
            )
            showMessage(getString(R.string.new_student_added))
            navigateToBack()
        } else {
            showMessage(getString(R.string.all_fields_required))
        }
    }

    private fun updateStudent() {
        if (isEntryValid()) {
            studentViewModel.updateStudent(
                navigationArgs.studentId,
                binding.tietName.text.toString(),
                binding.tietEmail.text.toString(),
                binding.tietPhone.text.toString(),
                binding.tietBirthday.text.toString()
            )
            showMessage(getString(R.string.user_updated))
            navigateToBack()
        } else {
            showMessage(getString(R.string.all_fields_required))
        }
    }

    private fun navigateToBack() {
        val action =
            SaveStudentFragmentDirections.actionSaveStudentFragmentToStudentListFragment()
        findNavController().navigate(action)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }

    private fun isEntryValid(): Boolean {
        return studentViewModel.isEntryValid(
            binding.tietName.text.toString(),
            binding.tietEmail.text.toString(),
            binding.tietPhone.text.toString(),
            binding.tietBirthday.text.toString()
        )
    }

    private fun setupDateInput() {
        binding.tietBirthday.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.birthday_datepicker_title))
                .build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tietBirthday.setText(Date(it + offset).format())
            }
            activity?.let { it1 -> datePicker.show(it1.supportFragmentManager, "DATE_PICKER_TAG") }
        }
    }

    private fun bind(student: Student) {
        binding.apply {
            tietName.setText(student.name)
            tietEmail.setText(student.email)
            tietPhone.setText(student.phone)
            tietBirthday.setText(student.birthday)
        }
    }
}