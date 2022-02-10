package com.splhead.studentagenda.view

import android.app.SearchManager
import android.content.Context
import android.content.Context.*
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.splhead.studentagenda.R
import com.splhead.studentagenda.StudentAgendaApplication
import com.splhead.studentagenda.databinding.FragmentStudentListBinding
import com.splhead.studentagenda.model.Student
import com.splhead.studentagenda.viewmodel.StudentViewModel
import com.splhead.studentagenda.viewmodel.StudentViewModelFactory


class StudentListFragment : Fragment() {

    private val studentViewModel: StudentViewModel by activityViewModels {
        StudentViewModelFactory(
            (activity?.application as StudentAgendaApplication).repository
        )
    }

    private lateinit var adapter: StudentAdapter

    private lateinit var _binding: FragmentStudentListBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        adapter = StudentAdapter {
            val action =
                StudentListFragmentDirections.actionStudentListFragmentToSaveStudentFragment(it.id)
            this.findNavController().navigate(action)
        }

        binding.rvAllStudents.layoutManager = LinearLayoutManager(this.context)
        binding.rvAllStudents.adapter = adapter

        studentViewModel.getAllStudents().observe(this.viewLifecycleOwner) {
            students -> adapter.submitList(students)
            shouldShowEmptyState(students)
        }

        setupDeleteOnMoving()

        binding.fabAddStudent.setOnClickListener {
            val action =
                StudentListFragmentDirections.actionStudentListFragmentToSaveStudentFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun setupDeleteOnMoving() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                studentViewModel.delete(
                    adapter.getStudentAt(
                        viewHolder.absoluteAdapterPosition
                    )
                )
                Toast.makeText(this@StudentListFragment.context,
                    getString(R.string.student_deleted), Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(binding.rvAllStudents)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        setupSearch(menu)
    }

    private fun setupSearch(menu: Menu) {

        val searchView = SearchView((context as MainActivity).supportActionBar?.themedContext ?: context)
            // menu.findItem(R.id.action_search).actionView as SearchView
        menu.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.queryHint = getString(R.string.query_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(typedText: String): Boolean {
                studentViewModel.findByName(typedText).observe(
                    this@StudentListFragment
                ) { students -> adapter.submitList(students) }
                return false
            }
        })
    }

    private fun shouldShowEmptyState(studentsList: List<Student>) {
        binding.includeEmpty.emptyState.visibility = if (studentsList.isEmpty()) View.VISIBLE
        else View.GONE
    }
}