package com.example.mytrackingapp


//import kotlinx.android.synthetic.main.activity_main.*

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.mytrackingapp.moredbclasses.Permissions.hasLocationPermission


class MainActivity : AppCompatActivity() {

    val CITY: String = "helsinki,fi"
    val API: String = "ea24a5db5d70a7fa2d93a248d0fd9029"

    // var selectedFragment: Fragment? = null

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        weatherTask().execute()
*/
        navController = findNavController(R.id.navHostFragment)

        if (hasLocationPermission(this)) {
            navController.navigate(R.id.action_permissionFragment_to_mapTrackingFragment)
        }

    }
}

    /*fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.flFragment, fragment)
            .addToBackStack(null).commit()
    }*/

    /*private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener() { item ->

            when (item.itemId) {
                R.id.trackFragment -> {
                    selectedFragment = TrackFragment.newInstance()
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.navHostFragment, selectedFragment as TrackFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.arFragment-> {
                    selectedFragment = ArFragment.newInstance()
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.navHostFragment, selectedFragment as ArFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    return@OnNavigationItemSelectedListener true
                }

                *//*R.id.statisticsFragment -> {
                    selectedFragment = <MapTrackingFragment.newInstance()
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.navHostFragment, selectedFragment as MapTrackingFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    return@OnNavigationItemSelectedListener true
                }*//*
            }
            false
        }
*/

    /*inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

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

                //findViewById<TextView>(R.id.address).text = address
                //findViewById<TextView>(R.id.updated_at).text = updatedAtText
                //findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                //findViewById<TextView>(R.id.temp).text = temp
                //findViewById<TextView>(R.id.temp_min).text = tempMin
                //findViewById<TextView>(R.id.temp_max).text = tempMax
                //findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                //findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                //findViewById<TextView>(R.id.wind).text = windSpeed
                //findViewById<TextView>(R.id.pressure).text = pressure
                //findViewById<TextView>(R.id.humidity).text = humidity


                //findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                //findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }
*/









