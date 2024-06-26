package com.example.newbornnaptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
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
            val numNapsEditText = EditText(context).apply {
                hint = "Baby $i Number of Naps"
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
                id = View.generateViewId()
            }
            val radioGroup = RadioGroup(context).apply {
                orientation = RadioGroup.HORIZONTAL
                addView(RadioButton(context).apply {
                    text = context.getString(R.string.years)
                    id = View.generateViewId()
                })
                addView(RadioButton(context).apply {
                    text = context.getString(R.string.months)
                    id = View.generateViewId()
                })
                check(getChildAt(0).id) // Default selection to Years
            }
            binding.babiesContainer.addView(babyNameEditText)
            binding.babiesContainer.addView(babyAgeEditText)
            binding.babiesContainer.addView(numNapsEditText)
            binding.babiesContainer.addView(radioGroup)
        }
        binding.babiesContainer.visibility = View.VISIBLE
        binding.saveButton.visibility = View.VISIBLE
    }

    private fun saveBabyData() {
        val numBabies = binding.numBabiesSlider.value.toInt()
        val babyNames = mutableListOf<String>()
        val babyAges = mutableListOf<Pair<Int, String>>()
        val numNaps = mutableListOf<Int>()
        for (i in 0 until numBabies) {
            val babyNameEditText = binding.babiesContainer.getChildAt(i * 4) as EditText
            val babyAgeEditText = binding.babiesContainer.getChildAt(i * 4 + 1) as EditText
            val numNapsEditText = binding.babiesContainer.getChildAt(i * 4 + 2) as EditText
            val radioGroup = binding.babiesContainer.getChildAt(i * 4 + 3) as RadioGroup
            val selectedButtonId = radioGroup.checkedRadioButtonId
            val ageUnit = when (selectedButtonId) {
                radioGroup.getChildAt(0).id -> "Years"
                radioGroup.getChildAt(1).id -> "Months"
                else -> ""
            }
            babyNames.add(babyNameEditText.text.toString())
            val babyAge = babyAgeEditText.text.toString().toIntOrNull() ?: 0
            babyAges.add(Pair(babyAge, ageUnit))
            val naps = numNapsEditText.text.toString().toIntOrNull() ?: 0
            numNaps.add(naps)
        }
        sharedViewModel.setBabyNames(babyNames)
        sharedViewModel.setBabyAges(babyAges)
        sharedViewModel.setNumNaps(numNaps)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}