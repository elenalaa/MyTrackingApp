@file:Suppress("DEPRECATION")

package com.example.mytrackingapp.ui.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mytrackingapp.R
import com.example.mytrackingapp.databinding.FragmentWeatherBinding
import org.jetbrains.annotations.Nullable
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


//@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class WeatherFragment : Fragment(R.layout.fragment_weather) {

    val CITY: String = "helsinki,fi"
    val API: String = "ea24a5db5d70a7fa2d93a248d0fd9029"

    private var _binding: FragmentWeatherBinding? = null
    private var loader: ProgressBar? = null
    private var relativeLayout: RelativeLayout? = null
    private var errorText: TextView? = null

    val binding get() = _binding!!

    //@SuppressLint("StringFormatInvalid")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        weatherTask().execute()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.address.text.toString()
        binding.updatedAt.toString()
        binding.description.toString()
        binding.temp.toString()
        binding.tempMin.toString()
        binding.tempMax.toString()
        binding.wind.toString()
        binding.pressure.toString()
        binding.humidity.toString()

       // loader!!.visibility = View.GONE
       // errorText!!.visibility = View.VISIBLE

        loader = binding.loader
        relativeLayout = binding.mainContainer
        //relativeLayout!!.visibility = View.GONE
        errorText = binding.errorText
        //errorText?.visibility = View.GONE
    }

    companion object {
        fun newInstance(): WeatherFragment = WeatherFragment()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

@javax.annotation.Nullable
@SuppressLint("StaticFieldLeak")
inner class weatherTask() : AsyncTask<String, Void, String>() {
    override fun onPreExecute() {
        super.onPreExecute()

    }

    override fun doInBackground(vararg p0: String?): String? {

        val response: String? = try {
            URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                .readText(Charsets.UTF_8)

        }
        catch (e: Exception) {
            null
        }
        Log.d("response", response.toString())
        return response
    }
    @Nullable
    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        try {
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            val sys = jsonObj.getJSONObject("sys")
            var wind = jsonObj.getJSONObject("wind")
            val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
            val updatedAt: Long = jsonObj.getLong("dt")
            var updatedAtText =
                "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAt * 1000))
            var temp = main.getString("temp") + "C"
            Log.d("temp", temp)
            var tempMin = "Min Temp: " + main.getString("temp_min") + "C"
            Log.d("tempMin", tempMin)
            var tempMax = "Max Temp: " + main.getString("temp_max") + "C"
            Log.d("tempMax", tempMax)
            var pressure = main.getString("pressure")
            var humidity = main.getString("humidity")
            var windSpeed = wind.getString("speed")
            val weatherDescription = weather.getString("description")
            var address = jsonObj.getString("name") + ", " + sys.getString("country")

            address = binding.address.text.toString()
            updatedAtText = binding.updatedAt.toString()
            //description = binding.description?.text.toString()
            temp = binding.temp.text.toString()
            Log.d("temp", temp)
            tempMin = binding.tempMin.text.toString()
            Log.d("tempMin", tempMin)
            tempMax = binding.tempMax.text.toString()
            Log.d("tempMax", tempMax)
            //wind = windSpeed.toString()
            //pressure = (view)?.findViewById<TextView>(R.id.pressure)?.text.toString()
            //humidity = (view)?.findViewById<TextView>(R.id.humidity)?.text.toString()

            loader!!.visibility = View.GONE
            errorText!!.visibility = View.VISIBLE

        } catch (e: Exception) {
            loader!!.visibility = View.GONE
            errorText!!.visibility = View.VISIBLE
        }
    }
}

}