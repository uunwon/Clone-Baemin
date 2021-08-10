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
class CenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCenterBinding
    private lateinit var addressText: String

    private val centerArrayList = ArrayList<CenterItem>()
    private lateinit var centerAdapter: CenterAdapter
    private val key = "BDjQIk0hhjDzdEDbxWaxssyEkO+eo/VsCAO+LUrVKBsO+8QSXAZ0PrEZvnNjFiL/oin4KcihJhQtj2TP2nWl1w=="

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

        getCenterData()
    }

    override fun onResume() {
        super.onResume()

        binding.addressTextView.text = MyApplication.prefs.getString("road", "서울시 마포구 연남동 568-26 연남토마")
        val splitArray = MyApplication.prefs.getString("road", "서울시 마포구 연남동 568-26 연남토마").split(" ")
        addressText = splitArray[0] // 도 단위로 받아옴
        Toast.makeText(applicationContext, "$addressText 여기요", Toast.LENGTH_SHORT).show()
    }

    // API 호출 메서드
    private fun getCenterData() {
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

                    centerAdapter = CenterAdapter(centerArrayList, this@CenterActivity)
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

    class CenterAdapter(private val centerArrayList: ArrayList<CenterItem>, context: Context) : BaseAdapter() {
        private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        private lateinit var binding: ItemCenterBinding
        override fun getCount(): Int = centerArrayList.size

        override fun getItem(p0: Int): Any = centerArrayList[p0]

        override fun getItemId(p0: Int): Long = p0.toLong()

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            binding = ItemCenterBinding.inflate(inflater, p2, false)

            binding.centerNameTextView.text = centerArrayList[p0].name
            binding.centerAddressTextView.text = centerArrayList[p0].address
            binding.indexTextView.text = (p0 + 1).toString()

            return binding.root
        }
    }
}