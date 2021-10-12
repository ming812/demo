package com.example.weatherdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController



class LaunchFragment : Fragment() {

    private val xmlAction = XmlFragmentDirections.actionGlobalXmlFragment()
    private val composeAction = ComposeFragmentDirections.actionGlobalComposeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        view.findViewById<AppCompatButton>(R.id.btn_goToXml).apply {
            setOnClickListener {
                navController.navigate(xmlAction)
            }
        }
        view.findViewById<AppCompatButton>(R.id.btn_goToCompose).apply {
            setOnClickListener {
                navController.navigate(composeAction)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launch, container, false)
    }

}