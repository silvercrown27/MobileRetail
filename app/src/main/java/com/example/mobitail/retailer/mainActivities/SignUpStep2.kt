package com.example.mobitail.retailer.mainActivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.mobitail.R
import com.google.android.material.button.MaterialButton
import org.json.JSONArray
import org.json.JSONException


class SignUpStep2 : AppCompatActivity() {
    private lateinit var countrySpinner: Spinner
    private lateinit var countySpinner: Spinner
    private lateinit var next_btn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step2)

        next_btn = findViewById(R.id.NextStep)
        countrySpinner = findViewById(R.id.countrySpinner)
        countySpinner = findViewById(R.id.countySpinner)

        // Populate the country Spinner
        countySpinner = findViewById(R.id.countySpinner)

        // Read country data from JSON file and update spinner
        readCountryDataFromJson()

        val spinner: Spinner = findViewById(R.id.spinner)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.options_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.d("Selected Item", selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        next_btn.setOnClickListener{
            var intent = Intent(this, FinalSignupstep::class.java)
            startActivity(intent)
        }
    }
    private fun readCountryDataFromJson() {
        val json: String = applicationContext.assets.open("states.json").bufferedReader().use { it.readText() }

        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val countryObject = jsonArray.getJSONObject(i)
                val countryName = countryObject.getJSONObject("name").getString("common")

                // Retrieve the counties for Jordan
                if (countryName == "Jordan") {
                    if (countryObject.has("states")) {
                        val stateArray = countryObject.getJSONArray("states")
                        val countyList = mutableListOf<String>()

                        for (j in 0 until stateArray.length()) {
                            val stateObject = stateArray.getJSONObject(j)
                            val stateName = stateObject.getString("name")

                            if (stateObject.has("counties")) {
                                val countyArray = stateObject.getJSONArray("counties")
                                for (k in 0 until countyArray.length()) {
                                    val countyName = countyArray.getString(k)
                                    val countyWithState = "$countyName, $stateName"
                                    countyList.add(countyWithState)
                                }
                            }
                        }

                        // Update the spinner with the county list
                        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countyList)
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        countySpinner.adapter = spinnerAdapter
                    } else {
                        // Handle the case when the "states" key is missing
                        // Show an error message or take appropriate action
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            // Handle the JSON parsing error
        }
    }
}