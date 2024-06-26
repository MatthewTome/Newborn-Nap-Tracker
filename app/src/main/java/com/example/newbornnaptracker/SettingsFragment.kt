package com.example.newbornnaptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.newbornnaptracker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.numBabiesSlider.addOnChangeListener { _, value, _ ->
            val numBabies = value.toInt()
            setupBabyInputFields(numBabies)
        }

        binding.saveButton.setOnClickListener {
            saveBabyData()
        }
    }

    private fun setupBabyInputFields(numBabies: Int) {
        binding.babiesContainer.removeAllViews()
        for (i in 1..numBabies) {
            val babyNameEditText = EditText(context).apply {
                hint = "Baby $i Name"
                id = View.generateViewId()
            }
            val babyAgeEditText = EditText(context).apply {
                hint = "Baby $i Age"
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
                id = View.generateViewId()
            }
            binding.babiesContainer.addView(babyNameEditText)
            binding.babiesContainer.addView(babyAgeEditText)
        }
        binding.babiesContainer.visibility = View.VISIBLE
        binding.saveButton.visibility = View.VISIBLE
    }

    private fun saveBabyData() {
        val numBabies = binding.numBabiesSlider.value.toInt()
        val babyNames = mutableListOf<String>()
        val babyAges = mutableListOf<Int>()
        for (i in 0 until numBabies) {
            val babyNameEditText = binding.babiesContainer.getChildAt(i * 2) as EditText
            val babyAgeEditText = binding.babiesContainer.getChildAt(i * 2 + 1) as EditText
            babyNames.add(babyNameEditText.text.toString())
            babyAges.add(babyAgeEditText.text.toString().toIntOrNull() ?: 0)
        }
        sharedViewModel.setBabyNames(babyNames)
        sharedViewModel.setBabyAges(babyAges)
        sharedViewModel.setNumBabies(numBabies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}