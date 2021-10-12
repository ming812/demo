package com.example.weatherdemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import com.example.weatherdemo.adapter.CityListListAdapter
import com.example.weatherdemo.adapter.DiffCallback
import com.example.weatherdemo.adapter.EmptyVM
import com.example.weatherdemo.databinding.FragmentXmlBinding
import com.example.weatherdemo.model.City
import com.example.weatherdemo.model.CityRepository
import com.example.weatherdemo.model.CityRoomDatabase
import com.example.weatherdemo.model.SearchKey
import com.example.weatherdemo.viewModel.CityViewModelFactory
import com.example.weatherdemo.viewModel.WeatherViewModel
import kotlinx.coroutines.*


class XmlFragment : Fragment() {

    private lateinit var binding : FragmentXmlBinding
    private lateinit var cityListAdapter : CityListListAdapter

    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { CityRoomDatabase.getDatabase(requireContext(), applicationScope) }
    val repository by lazy { CityRepository(database.cityDao()) }

    private val viewModel by viewModels<WeatherViewModel>{CityViewModelFactory(application = requireActivity().application,repository)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel.init(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentXmlBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.updateKeyWord(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    val list = viewModel.historyPageData.value.apply {
                        if(this.isNullOrEmpty())
                            listOf(EmptyVM())
                        else
                            this
                    }
                    cityListAdapter.submitList(list)
                }
                return true
            }

        })
        binding.recyclerView.apply {
            cityListAdapter = CityListListAdapter(
                DiffCallback(),
                click = {
                    GlobalScope.launch(Dispatchers.IO) {
//                        cityListAdapter.submitList(null)
                        val currentTimestamp = System.currentTimeMillis()
                        viewModel.updateHistoryRecord(City(it.id , it.name ,it.lat , it.lon, currentTimestamp ))
                        viewModel.callWeatherData(SearchKey(it.id, it.lat ,it.lon , it.name))
//                        Log.e("OnClickListener" , "result = $result")
                    }
                },
                clear = {
                    GlobalScope.launch(Dispatchers.IO) {
                        Log.e("clear" , "enter")
                        viewModel.clearHistoryRecord()
                    }
                }
            )
            this.adapter = cityListAdapter
        }

//        binding.recyclerView

        viewModel.apply {

            cityResultList.observe(this@XmlFragment.viewLifecycleOwner , {
                Log.e("cityResultList" , "result = $it")
                val data = if(it.isNullOrEmpty()) listOf(EmptyVM()) else it
                cityListAdapter.submitList(data)
            })


            historyPageData.observe(this@XmlFragment.viewLifecycleOwner , {
            Log.e("historyPageData" , "result = $it")
                val data = if(it.isNullOrEmpty()) listOf(EmptyVM()) else it
//                Log.e("cityListAdapter" , "currentList = ${cityListAdapter.currentList}")
            cityListAdapter.submitList(data){
                binding.recyclerView.scrollToPosition(0)
            }
        })
        }
    }
}