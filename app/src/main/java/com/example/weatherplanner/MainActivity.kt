package com.example.weatherplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import com.example.weatherplanner.databinding.ActivityMainBinding
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//067c6c034649846e8005daeb9bdbe811
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        fetchweatherdata("Rewa")
        searchcity()
    }

    private fun searchcity() {
        val searchview = binding.searchView
        searchview.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchweatherdata(query)
                }
                return true;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true;
            }

        })
    }

    private fun fetchweatherdata(s: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterFace::class.java)
        val response = retrofit.getweatherData("$s","067c6c034649846e8005daeb9bdbe811","metric")
        response.enqueue(object: Callback<WeatherApi>{
            override fun onResponse(
                call: retrofit2.Call<WeatherApi>,
                response: Response<WeatherApi>
            ) {
               val responsebody = response.body()
                if(response.isSuccessful && responsebody!=null){
                    val temp = responsebody.main.temp.toString()
                    val humidity = responsebody.main.humidity
                    val sunset = responsebody.sys.sunset.toLong()
                    val sunrise = responsebody.sys.sunrise.toLong()
                    val windspeed = responsebody.wind.speed
                    val sealevel = responsebody.main.pressure
                    val condtion = responsebody.weather.firstOrNull()?.main?:"unknown"
                    val mxtemp = responsebody.main.temp_max
                    val mntemp = responsebody.main.temp_min
                                          binding.TemId.text = "$temp °C"
                    binding.TemperatureId.text = "$humidity %"
                    binding.LottieTextId.text = condtion
                    binding.MaxTempId.text = "Max Temp: $mxtemp °C"
                    binding.minTempId.text =  "Min Temp: $mntemp °C"

                    binding.condition.text  = condtion
                    binding.WindSpeedId.text = "$windspeed m/s"
                    binding.SunriseId.text =    "${time(sunrise)}"
                    binding.SunsetId.text = "${time(sunset)}"
                    binding.SeaLevelId.text = "$sealevel hPa"
                    binding.condition.text = condtion
                    binding.DayId.text =dayName(System.currentTimeMillis())
                        binding.DateId.text =dateName()
                        binding.LocationId.text ="$s"
                    changeBackground(condtion)


                }
            }

            override fun onFailure(call: retrofit2.Call<WeatherApi>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun changeBackground(condtion: String) {
        when(condtion){
            "Haze" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloudanimation)

            }
            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_image)
                binding.lottieAnimationView.setAnimation(R.raw.sunny)
            }
            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloudanimation)
            }
            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain","Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rainy_lottie)
            }
            "Snow","Light Snow","Heavy Snow", "Moderate Snow","Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }else->{
            binding.root.setBackgroundResource(R.drawable.sunny_image)
            binding.lottieAnimationView.setAnimation(R.raw.sunny)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun dateName():String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timestamp: Long):String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }
    fun dayName(timestamp:Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}