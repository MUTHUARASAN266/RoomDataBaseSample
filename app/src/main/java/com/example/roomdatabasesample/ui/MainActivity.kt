package com.example.roomdatabasesample.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabasesample.MyViewModelFactory
import com.example.roomdatabasesample.OnUserDeleteListener
import com.example.roomdatabasesample.adapter.UserAdapter
import com.example.roomdatabasesample.dao.UserDao
import com.example.roomdatabasesample.databinding.ActivityMainBinding
import com.example.roomdatabasesample.datastore.UserDatabase
import com.example.roomdatabasesample.model.UserData
import com.example.roomdatabasesample.repo.UserRepo
import com.example.roomdatabasesample.viewmodel.UserViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userDao: UserDao
    private lateinit var viewmodel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private val TAG = "MainActivity"
    private val mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = UserDatabase.getDatabase(applicationContext).userDao()

        // Initialize ViewModel with a factory
        viewmodel = ViewModelProvider(
            this,
            MyViewModelFactory(UserRepo(userDao))
        )[UserViewModel::class.java]

        binding.apply {
            userAdapter = UserAdapter(this@MainActivity)
            recycleview.layoutManager = LinearLayoutManager(this@MainActivity)
            recycleview.setHasFixedSize(true)
            recycleview.setItemViewCacheSize(6)
            recycleview.adapter = userAdapter  // Set the adapter here

            btnSave.setOnClickListener {
                val userId = editTextId.text.toString()
                val userName = editTextName.text.toString()
                val age = editTextAge.text.toString()
                validation(userId, userName, age)
            }
            btnView.setOnClickListener {
                viewmodel.getData.observe(this@MainActivity, Observer { newData ->
                    try {
                        if (newData.isEmpty()) {
                            Toast.makeText(this@MainActivity, "Data is empty", Toast.LENGTH_SHORT)
                                .show()
                            Log.e(TAG, "ViewData -> Data is empty")
                        } else {
                            userAdapter.setData(newData)
                            Log.e(TAG, "ViewData -> Data is $newData")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "An error occurred: $e")
                    }
                })
            }

            userAdapter.setOnUserDeleteListener(object : OnUserDeleteListener {
                override fun onDeleteUser(userId: Int) {
                    viewmodel.deleteByUserId(userId)
                    Log.e(TAG, "onDeleteUser: $userId")
                }
            })
        }

        GlobalScope.launch(Dispatchers.IO){
            MobileAds.initialize(this@MainActivity) {}
            val context = applicationContext
            try {
                val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)

                val advertisingId = adInfo.id
                val isLimitAdTrackingEnabled = adInfo.isLimitAdTrackingEnabled
                Log.e(TAG, "advertisingId: $advertisingId")
                Log.e(TAG, "isLimitAdTrackingEnabled: $isLimitAdTrackingEnabled")
                // Do something with the advertising ID and tracking preference
                val requestConfiguration = RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf(advertisingId))
                    .build()
                MobileAds.setRequestConfiguration(requestConfiguration)

            } catch (e: Exception) {
                // Handle any exceptions (e.g., Google Play Services not available)
                Log.e(TAG, "Google Play Services not available: $e")
            }
        }
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.

            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

    }

    private fun validation(userId: String, userName: String, age: String) {
        if (userId.isEmpty() || userName.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, "Please enter all the values", Toast.LENGTH_SHORT).show()
        } else {
            storeData(userId.toInt(), userName, age.toInt())
        }
    }

    private fun storeData(userId: Int, userName: String, age: Int) {
        val data = UserData(userId, userName, age)
        viewmodel.insert(data)
    }
}
