package com.example.mobitail.retailer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.mobitail.R
import com.example.mobitail.retailer.adapterClasses.SettingsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class RetailSettingsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var generalSettings: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retail_settings)

        generalSettings = findViewById(R.id.generalSettings)

        val itemList: ArrayList<SettingsAdapter.SettingsItem> = arrayListOf(
            SettingsAdapter.SettingsItem("Location", R.layout.rv_listitem_type_1, R.drawable.categories, RetailDashboardActivity::class.java),
            SettingsAdapter.SettingsItem("Language", R.layout.rv_listitem_type_1, R.drawable.wallets, RetailDashboardActivity::class.java),
            SettingsAdapter.SettingsItem("Appearance", R.layout.rv_listitem_type_1, R.drawable.purchases, RetailDashboardActivity::class.java)
        )

        val adapter = SettingsAdapter(this, itemList)
        generalSettings.adapter = adapter













        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.action_settings
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_accounts -> {
                    val intent = Intent(this, RetailAccountsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                R.id.action_dashboard -> {
                    val intent = Intent(this, RetailDashboardActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                R.id.action_products -> {
                    val intent = Intent(this, ProductsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                R.id.action_settings -> {
                    val intent = Intent(this, RetailSettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                else -> false
            }
        }
    }
}