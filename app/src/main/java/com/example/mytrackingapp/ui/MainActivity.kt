package com.example.mytrackingapp.ui


//import kotlinx.android.synthetic.main.activity_main.*

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.mytrackingapp.R
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*



class MainActivity : AppCompatActivity() {

    val CITY: String = "helsinki,fi"
    val API: String = "ea24a5db5d70a7fa2d93a248d0fd9029"
    val lon


    private lateinit var toolbar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         weatherTask().execute()

        //toolbar = supportActionBar!!
        //val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)

    }

    inner class weatherTask() : AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            response = try {
                URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            } catch (e: Exception) {
                null
            }
            return response

        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000))
                val temp = main.getString("temp") + "C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                //findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                // findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                //  findViewById<TextView>(R.id.wind).text = windSpeed
                // findViewById<TextView>(R.id.pressure).text = pressure
                // findViewById<TextView>(R.id.humidity).text = humidity


                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }
}


    /*companion object {
        private fun openFragment(mainActivity: MainActivity, fragment: Fragment) {
            val transaction = mainActivity.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }*/








