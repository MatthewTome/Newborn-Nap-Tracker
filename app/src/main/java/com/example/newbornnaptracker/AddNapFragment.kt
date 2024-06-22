package com.example.newbornnaptracker

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.newbornnaptracker.databinding.FragmentAddNapBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class AddNapFragment : Fragment() {

    companion object {
        private var hasShownSnackbar = false
    }

    private var _binding: FragmentAddNapBinding? = null
    private val binding get() = _binding!!
    private val babyName = "Dawson"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show the Snackbar message when the fragment is created
        showDescriptionSnackbar(view)

        // Set the initial date for the calendar
        binding.calendarView.date = System.currentTimeMillis()

        // Set the OnDateChangeListener to handle date selection
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Add an event to the calendar
            val eventTitle = "$babyName's Nap"
            val eventStartTime = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 10, 0)
            }
            val eventEndTime = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 11, 0)
            }

            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, eventTitle)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventStartTime.timeInMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEndTime.timeInMillis)

            startActivity(intent)
        }
    }

    private fun showDescriptionSnackbar(view: View) {
        if (!hasShownSnackbar) {
            val snackbar = Snackbar.make(
                view,
                getString(R.string.add_nap_description),
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Got it") {
                hasShownSnackbar = true
            }

            // Customize the Snackbar
            val snackbarView = snackbar.view
            snackbarView.background = GradientDrawable().apply {
                cornerRadius = resources.getDimension(R.dimen.snackbar_corner_radius)
            }

            val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.maxLines = 5

            snackbar.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}