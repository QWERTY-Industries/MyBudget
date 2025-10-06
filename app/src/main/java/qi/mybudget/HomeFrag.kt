package qi.mybudget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import qi.mybudget.databinding.FragmentHomeBinding // Import the binding class for the fragment

class HomeFrag : Fragment() {

    // Declare binding variables
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access buttons directly through the binding object (no more findViewById)
        binding.btnAnalysis.setOnClickListener {
            findNavController().navigate(R.id.action_homeFrag2_to_analysisFrag2)
        }

        binding.btnReporter.setOnClickListener {
            findNavController().navigate(R.id.action_homeFrag2_to_reporterFrag)
        }

        binding.btnOverview.setOnClickListener {
            findNavController().navigate(R.id.action_homeFrag2_to_overviewFrag)
        }

        // Note: Your "Side Menu" button with the id "button" is not yet used.
        // You can add a listener for it here if you need to.
        // binding.button.setOnClickListener { ... }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding object when the view is destroyed to avoid memory leaks
        _binding = null
    }
}