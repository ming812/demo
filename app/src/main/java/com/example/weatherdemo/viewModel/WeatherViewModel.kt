package com.example.weatherdemo.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.weatherdemo.CityApplication
import com.example.weatherdemo.adapter.*
import com.example.weatherdemo.model.*
import com.example.weatherdemo.network.retrofit
import com.example.weatherdemo.network.weatherService
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*


class WeatherViewModel(application : Application ,private val repository: CityRepository) : AndroidViewModel(application){

   private lateinit var cityList : List<CityObject>

   init {
      viewModelScope.launch(Dispatchers.IO) {
         cityList =
            Gson().fromJson(
               getJsonDataFromAsset(application.applicationContext , "city.list.min.json"),
               Array<CityObject>::class.java
            ).toList()
      }
   }

   val keyword = MutableLiveData("")

   fun updateKeyWord(word : String){
      keyword.value = word
   }

   val service: weatherService = retrofit().create(weatherService::class.java)

   fun callWeatherData(searchKey: SearchKey){
      val response = service.getWeather(searchKey.lat, searchKey.lon).execute().body()
      recentSearchResult.postValue(
         RecentVM(
            searchKey.id,
            searchKey.lat,
            searchKey.lon,
            CityListListAdapter.TYPE_RECENT,
            searchKey.name,
            response
      ))
   }

   suspend fun updateHistoryRecord(city : City){
      repository.insert(city)
   }

   fun clearHistoryRecord(){
      repository.deleteAll()
   }

   val recentSearchResult = MutableLiveData<BaseVM>()

   private val fromDataBaseAllResult = repository.allCity
   private val fromDataBaseLastRecord = repository.lastRecord


//   val historyPageData = recentSearchResult.combineWith(fromDataBaseResult){ recent , history ->
//      Log.e("history" , "$history")
//      liveData {
//         val list = mutableListOf<BaseVM>()
//         if(recent != null)
//            list.add(recent)
//         if(history != null && history.isNotEmpty()) {
//            history.map {
//               list.add(
//                  CityListVM(
//                     id = it.id,
//                     lat = it.lat,
//                     lon = it.lon,
//                     name = it.name,
//                     viewType = CityListListAdapter.TYPE_HISTORY
//                  )
//               )
//            }
//            list.add(
//               ClearHistoryVM(title = "History Clear")
//            )
//         }
//         emit(list)
//      }
//
//   }.switchMap {
//      it
//   }
   val historyPageData = HistoryLiveData()

   val cityResultList = Transformations.switchMap(keyword){ keyword->
      liveData{
         emit(
            if(keyword.isEmpty()){
               null
            }else {
               if( ::cityList.isInitialized && cityList.isNotEmpty()) {
                  val data = cityList.filter {
                     it.name.lowercase().contains(keyword.lowercase())
                  }
                  data.map {
                     CityListVM(
                        it.id,
                        it.coord.lat,
                        it.coord.lon,
                        CityListListAdapter.TYPE_SEARCH,
                        it.name
                     )
                  }
               }else{
                  null
               }
            }
         )
      }
   }

   fun getJsonDataFromAsset(context: Context, fileName: String): String? {
      val jsonString: String
      try {
         jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
      } catch (ioException: IOException) {
         ioException.printStackTrace()
         return null
      }
      return jsonString
   }

//   val historyMediatorLiveData =  HistoryPageData()

//   inner class HistoryPageData<BaseVM , BaseVM , List<BaseVM>>(source) : Media {
//
//      override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
//        addSource(
//           recentSearchResult
//        )
//      }
//   }

   inner class HistoryLiveData : MediatorLiveData<List<BaseVM>>(){

      var job : Job? = null
      val jobTask = viewModelScope
      override fun onActive() {
         super.onActive()
         addSource()
      }

      private fun addSource(){
         removeSource(fromDataBaseLastRecord)
         addSource(fromDataBaseLastRecord){
            jobTask.launch(Dispatchers.IO) {
               load()
            }
         }
         removeSource(recentSearchResult)
         addSource(recentSearchResult){
            jobTask.launch(Dispatchers.IO) {
               load()
            }
         }
         removeSource(fromDataBaseAllResult)
         addSource(fromDataBaseAllResult){
            jobTask.launch(Dispatchers.IO) {
               load()
            }
         }
      }

      private fun load() {
         job?.cancel()
         job = jobTask.launch {
            postValue(withContext(Dispatchers.IO){
               createChild()
            })
         }
         postValue(createChild())
//         handler.removeCallbacks(loadTask)
//         handler.postDelayed(loadTask, if (tag.value == RefType.COLLECTION) 100 else 300)
      }

      private fun createChild(): List<BaseVM> {
         val list = ArrayList<BaseVM>()
         try {

            if(recentSearchResult.value != null) {
               list.add(recentSearchResult.value!!)
               Log.e("HistoryLiveData" , "recent enter again")
            }
            if(fromDataBaseAllResult.value != null && fromDataBaseAllResult.value!!.isNotEmpty()) {
               Log.e("HistoryLiveData" , "recent = ${recentSearchResult.value} , lastRecord = ${fromDataBaseLastRecord.value}")
               if(recentSearchResult.value == null && fromDataBaseLastRecord.value != null) {
                  val cache = fromDataBaseLastRecord.value!!
                  callWeatherData(
                     SearchKey(id = cache.id , name = cache.name , lat = cache.lat , lon = cache.lon)
                  )
               }
               fromDataBaseAllResult.value!!.map {
                  list.add(
                     CityListVM(
                        id = it.id,
                        lat = it.lat,
                        lon = it.lon,
                        name = it.name,
                        viewType = CityListListAdapter.TYPE_HISTORY
                     )
                  )
               }
               list.add(
                  ClearHistoryVM(title = "History Clear" , CityListListAdapter.TYPE_CLEAR)
               )
            }

         } catch (e: Exception) {
            e.printStackTrace()
         }
         return list
      }
   }

fun <T, K, R> LiveData<T>.combineWith(
   liveData: LiveData<K>,
   block: (T?, K?) -> R
): LiveData<R> {
   val result = MediatorLiveData<R>()
   result.addSource(this) {
      result.value = block(this.value, liveData.value)
   }
   result.addSource(liveData) {
      result.value = block(this.value, liveData.value)
   }
   return result
}

}

class CityViewModelFactory(private val application : Application,private val repository: CityRepository) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
         @Suppress("UNCHECKED_CAST")
         return WeatherViewModel(application,repository) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
   }
}

