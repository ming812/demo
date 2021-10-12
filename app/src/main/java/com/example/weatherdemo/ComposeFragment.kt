package com.example.weatherdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherdemo.adapter.BaseVM
import com.example.weatherdemo.adapter.CityListVM
import com.example.weatherdemo.adapter.ClearHistoryVM
import com.example.weatherdemo.adapter.RecentVM
import com.example.weatherdemo.model.City
import com.example.weatherdemo.model.CityRepository
import com.example.weatherdemo.model.CityRoomDatabase
import com.example.weatherdemo.model.SearchKey
import com.example.weatherdemo.viewModel.CityViewModelFactory
import com.example.weatherdemo.viewModel.WeatherViewModel
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class ComposeFragment : Fragment() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { CityRoomDatabase.getDatabase(requireContext(), applicationScope) }
    private val repository by lazy { CityRepository(database.cityDao()) }

    private val viewModel by viewModels<WeatherViewModel>{ CityViewModelFactory(application = requireActivity().application,repository) }

    private val recentDisplayList = listOf(
        R.string.weather ,
        R.string.description,
        R.string.sunrise,
        R.string.sunset,
        R.string.temp,
        R.string.feels_like
    )

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    val displaySearchResult = remember{
                        mutableStateOf(false)
                    }
                    val shouldDisplayLoading = remember{
                        mutableStateOf(false)
                    }
//                    ProvideWindowInsets{
                        Column(
                            modifier = Modifier
                                .statusBarsPadding()
                                .fillMaxHeight()
                                .fillMaxWidth()
                        ) {
                            SearchBar(
                                update = {
                                    displaySearchResult.value = it
                                    shouldDisplayLoading.value = it
                                }
                            )
                            Body(
                                displaySearchResult ,
                                shouldDisplayLoading
                            )
                        }
//                    }
                }
            }
        }
    }

    @Composable
    fun SearchBar(update : (Boolean) -> Unit){
            AndroidView(
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.teal_200))
                ,
                factory = {
                    SearchView(it).apply {
                        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                update(!query.isNullOrEmpty())
                                viewModel.updateKeyWord(query.orEmpty())
                                return true
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                if(newText.isNullOrEmpty()) {
                                    update(false)
                                }
                                return true
                            }

                        })
                    }
                }
            )
    }

    @Composable
    fun Body( display: MutableState<Boolean> , shouldDisplayLoading: MutableState<Boolean>){
//        val displaySearchResult = remember{
//            mutableStateOf(false)
//        }
        val searchResult = viewModel.cityResultList.observeAsState()
        val historyResult = viewModel.historyPageData.observeAsState()
        Log.e("ComposeView" , "historyResult = ${historyResult.value}")
        ProgressBar(isDisplay = shouldDisplayLoading.value)
        if(display.value){
            if(searchResult.value != null && searchResult.value!!.isNotEmpty()){
                shouldDisplayLoading.value = false
                CityResultObject(searchResult = searchResult.value!! , display)
            }else {
                EmptyView()
            }
        }else {
            Log.e("RecentView"  , "should get in")
            if(historyResult.value != null && historyResult.value!!.isNotEmpty()) {
                RecentView(historyList = historyResult.value!! , display)
            }else {
                EmptyView()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun CityResultObject(searchResult : List<CityListVM> , display : MutableState<Boolean>){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            items(searchResult){
                Box(
                    modifier = Modifier
                        .clickable {
                            cityClick(it, display)
                        }
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                ){
                    Text(
                        text = it.name,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun RecentView(historyList : List<BaseVM> , display: MutableState<Boolean>) {
        historyList.map { baseVM ->
            when (baseVM){
                is RecentVM -> {
                    Text(
                        text = baseVM.name,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.Black
                    )
                    val recentDataDetail = baseVM.weatherDetail?.current
                    val recentdataList = listOf(
                        recentDataDetail?.weather?.get(0)?.main.orEmpty(),
                        recentDataDetail?.weather?.get(0)?.description.orEmpty(),
                        convertLongToTime(recentDataDetail?.sunrise ?: 0L),
                        convertLongToTime(recentDataDetail?.sunset ?: 0L),
                        recentDataDetail?.temp,
                        recentDataDetail?.feels_like
                    )
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2),
                        content = {
                            itemsIndexed(recentDisplayList){ index , item ->
                                Card(
                                    modifier = Modifier.padding(4.dp),
                                    backgroundColor = androidx.compose.ui.graphics.Color.Transparent,
                                    elevation = 0.dp
                                ) {
                                    Text(
                                        text = stringResource(item, recentdataList[index].toString()),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    )
                    Divider()
                }
                is CityListVM -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                            .clickable {
                                cityClick(baseVM, display)
                            }
                    ) {
                        Text(
                            text = baseVM.name,
                            color = Color.Black
                        )
                    }
                }
                is ClearHistoryVM -> {
                    Column(
                        modifier =  Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(
                            onClick = {
                                GlobalScope.launch(Dispatchers.IO) {
                                    Log.e("clear", "enter")
                                    viewModel.clearHistoryRecord()
                                }
                            },
                            modifier = Modifier.background(colorResource(id = R.color.teal_200))
                        ) {
                            Text("HISTORY CLEAR")
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun EmptyView(){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No Result"
                    )
                }
            }


    @SuppressLint("SimpleDateFormat")
    fun convertLongToTime(time: Long): String {
        return if(time == 0L){
            "unknown"
        }else {
            val date = Date(time)
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            format.format(date)
        }
    }

    private fun cityClick(cityListVM: CityListVM, display: MutableState<Boolean>) {
        GlobalScope.launch(Dispatchers.IO) {

            val currentTimestamp = System.currentTimeMillis()
            viewModel.updateHistoryRecord(
                City(
                    cityListVM.id,
                    cityListVM.name,
                    cityListVM.lat,
                    cityListVM.lon,
                    currentTimestamp
                )
            )
            viewModel.callWeatherData(SearchKey(cityListVM.id, cityListVM.lat, cityListVM.lon, cityListVM.name))
            display.value = false
        }
    }
    }
