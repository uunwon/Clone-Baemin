package com.yunwoon.clientproejct

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.yunwoon.clientproejct.center.CenterInterface
import com.yunwoon.clientproejct.center.RetrofitCenterClient
import com.yunwoon.clientproejct.center.models.CenterResponse
import com.yunwoon.clientproejct.databinding.ActivityCenterBinding
import com.yunwoon.clientproejct.databinding.ItemCenterBinding
import com.yunwoon.clientproejct.sharedPreference.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class CenterItem(val name:String, val address:String, val lat:String, val lng:String)

class CenterActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityCenterBinding
    private lateinit var addressText: String

    private val centerArrayList = ArrayList<CenterItem>()
    private lateinit var centerAdapter: CenterAdapter

    private val key = BuildConfig.DATA_CENTER_SECRET_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCenterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white) // 뒤로가기 버튼 아이콘 커스텀

        binding.centerListView.isNestedScrollingEnabled = true

        getCenterData(savedInstanceState)
    }

    // API 호출 메서드
    private fun getCenterData(savedInstanceState: Bundle?) {
        val centerInterface = RetrofitCenterClient.sRetrofit.create(CenterInterface::class.java)
        centerInterface.getCenter(1,284, key).enqueue(object : Callback<CenterResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CenterResponse>,
                response: Response<CenterResponse>
            ) {
                if(response.isSuccessful) {
                    val result = response.body() as CenterResponse
                    var index = 0
                    var i = 0

                    while(index < 5) {
                        if(addressText == result.data[i].sido) {
                            centerArrayList.add(CenterItem(result.data[i].centerName,result.data[i].address,
                                result.data[i].lat, result.data[i].lng))
                            index++
                        }
                        i++
                    }

                    centerAdapter = CenterAdapter(centerArrayList, this@CenterActivity,
                        this@CenterActivity, savedInstanceState)
                    binding.centerListView.adapter = centerAdapter
                } else {
                    Log.d("CenterActivity", "getCenterData - onResponse : Error code ${response}")
                }
            }

            override fun onFailure(call: Call<CenterResponse>, t: Throwable) {
                Log.d("CenterActivity", t.message?:"통신오류")
            }
        })
    }

    class CenterAdapter(private val centerArrayList: ArrayList<CenterItem>, context: Context,
                        private val callback: OnMapReadyCallback, private val savedInstanceState: Bundle?
    ) : BaseAdapter() {
        private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        private lateinit var binding: ItemCenterBinding

        override fun getCount(): Int = centerArrayList.size

        override fun getItem(p0: Int): Any = centerArrayList[p0]

        override fun getItemId(p0: Int): Long = p0.toLong()

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            binding = ItemCenterBinding.inflate(inflater, p2, false)
            MyApplication.prefs.putInt("p0", p0)

            binding.centerNameTextView.text = centerArrayList[p0].name
            binding.centerAddressTextView.text = centerArrayList[p0].address
            binding.indexTextView.text = (p0 + 1).toString()
            binding.centerMapView.onCreate(savedInstanceState)
            binding.centerMapView.getMapAsync(callback)

            return binding.root
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val p0 = MyApplication.prefs.getInt("p0", 0)
        val myLocation = LatLng(centerArrayList[p0].lat.toDouble(), centerArrayList[p0].lng.toDouble())
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }

    override fun onResume() {
        super.onResume()

        binding.addressTextView.text = MyApplication.prefs.getString("road", "서울특별시 마포구 연남동 568-26 연남토마")
        val splitArray = MyApplication.prefs.getString("road", "서울특별시 마포구 연남동 568-26 연남토마").split(" ")
        addressText = splitArray[0] // 도 단위로 받아옴
    }
}
