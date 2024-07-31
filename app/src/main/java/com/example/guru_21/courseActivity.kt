package com.example.guru_21

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.guru_21.databinding.ActivityCourseBinding
import com.google.android.libraries.places.api.Places
import com.example.guru_21.BuildConfig
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class courseActivity : AppCompatActivity() {

    lateinit var dbManager: MyDatabaseHelper
    lateinit var sqlitedb: SQLiteDatabase

    private lateinit var binding: ActivityCourseBinding
    lateinit var btnGoaddMyCourse: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnGoaddMyCourse=findViewById(R.id.btnGoaddMyCourse)

        val dbManager = MyDatabaseHelper(this, "CourseDB", null, 1)

        val edtPlaceName = binding.edtgoogleplacename
        val edtPlaceAddress = binding.edtgoogleplaceaddress
        val edtPlaceNumber = binding.edtgoogleplacenumber
        val edtPlaceCost = binding.edtgoogleplacecost
        val edtPlaceMemo = binding.edtgoogleplacememo
        val btnInit = binding.btninit


        // 구글 장소검색자동완성 api
        // Define a variable to hold the Places API key.
        val apiKey = BuildConfig.PLACES_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No API key")
            finish()
            return
        }

        // Initialize the SDK
        Places.initialize(applicationContext, apiKey)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Get the place ID
                val placeId = place.id ?: return

                // Specify the fields to return.
                val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER)

                // Construct a request object, passing the place ID and fields array.
                val request = FetchPlaceRequest.newInstance(placeId, placeFields)

                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response: FetchPlaceResponse ->
                        val place = response.place
                        Log.i("PlaceDetails", "Place found: ${place.name}, Address: ${place.address}, Phone: ${place.phoneNumber}")

                        binding.edtgoogleplacename.setText(place.name)
                        binding.edtgoogleplaceaddress.setText(place.address)
                        binding.edtgoogleplacenumber.setText(place.phoneNumber)

                    }
                    .addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            Log.e("PlaceDetails", "Place not found: ${exception.message}")
                            val statusCode = exception.statusCode
                            // Handle error with the given status code
                            when (statusCode) {
                                // Add handling for specific status codes if needed
                                else -> Log.e("PlaceDetails", "Status code: $statusCode")
                            }
                        }
                    }
            }

            override fun onError(status: Status) {
                // Handle any errors that occur.
                Log.e("PlaceSelection", "An error occurred: $status")
            }
        })

        // 사진 버튼 이벤트
        binding.btnpicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        binding.btninit.setOnClickListener {

        }

        // 저장 버튼
        btnInit.setOnClickListener {

            val placeAddress = edtPlaceAddress.text.toString()
            val placeName = edtPlaceName.text.toString()
            val placeNumber = edtPlaceNumber.text.toString()
            val placeCost = edtPlaceCost.text.toString()
            val placeMemo = edtPlaceMemo.text.toString()

            sqlitedb = dbManager.writableDatabase
            val sql = "INSERT INTO Course (place_name, place_addr, place_num, place_cost, memo) VALUES (" +
                    "'$placeName', '$placeAddress', '$placeNumber', $placeCost, '$placeMemo');"
            sqlitedb.execSQL(sql)
            sqlitedb.close()

        }

        // 가져오기 버튼
        btnGoaddMyCourse.setOnClickListener{

            val placeAddress = edtPlaceAddress.text.toString()
            val placeName = edtPlaceName.text.toString()
            val placeNumber = edtPlaceNumber.text.toString()

            val intent = Intent(this, addMyCourseActivity::class.java)
                intent.putExtra("SendName",placeName)
                intent.putExtra("SendAddr",placeAddress)
                intent.putExtra("SendNum", placeNumber)
            startActivity(intent)
        }
    }

    // 갤러리 사진 가져오기
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {

        // 결과 OK
        if(it.resultCode == RESULT_OK && it.data != null) {
            val uri = it.data!!.data

            Glide.with(this)
                .load(uri)
                .into(binding.imagepicture)
        }

    }

}
