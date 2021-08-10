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
import com.yunwoon.clientproejct.cleanStore.CleanStoreInterface
import com.yunwoon.clientproejct.cleanStore.RetrofitCleanStoreClient
import com.yunwoon.clientproejct.cleanStore.models.CleanStoreResponse
import com.yunwoon.clientproejct.databinding.ActivityCleanstoreBinding
import com.yunwoon.clientproejct.databinding.ItemCleanstoreBinding
import retrofit2.Response

data class CleanStoreItem(val name:String, val ownerName:String, val address:String, val category:String, val image:Int)

class CleanStoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCleanstoreBinding
    private val cleanStoreArrayList = ArrayList<CleanStoreItem>()
    private lateinit var cleanStoreAdapter:CleanStoreAdapter
    private val cleanStoreImageArray : Array<Int> = arrayOf(R.drawable.store_01, R.drawable.store_02,
        R.drawable.store_03, R.drawable.store_04, R.drawable.store_05)

    private val city : String = "서울특별시"
    private val citydo : String = "마포구"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCleanstoreBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow) // 뒤로가기 버튼 아이콘 커스텀

        binding.cleanStoreListView.isNestedScrollingEnabled = true

        // 안전식당 목록 받아오기
        getCleanStoreData()
    }

    // API 호출 메서드
   private fun getCleanStoreData() {
        val cleanStoreInterface = RetrofitCleanStoreClient.sRetrofit.create(CleanStoreInterface::class.java)
        cleanStoreInterface.getCleanStore(city, citydo).enqueue(object : retrofit2.Callback<CleanStoreResponse>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: retrofit2.Call<CleanStoreResponse>,
                response: Response<CleanStoreResponse>
            ) {
                if(response.isSuccessful) {
                    val result = response.body() as CleanStoreResponse

                    // 리스트뷰에 아이템 뿌려주기
                    for(i in 0..4) {
                        cleanStoreArrayList.add(CleanStoreItem(result.Grid_20200713000000000605_1.row[i].RELAX_RSTRNT_NM,
                                                          result.Grid_20200713000000000605_1.row[i].RELAX_RSTRNT_REPRESENT,
                                                          result.Grid_20200713000000000605_1.row[i].RELAX_ADD1,
                                                          result.Grid_20200713000000000605_1.row[i].RELAX_GUBUN_DETAIL,
                                                          cleanStoreImageArray[i]))
                    }
                    cleanStoreAdapter = CleanStoreAdapter(cleanStoreArrayList, this@CleanStoreActivity)
                    binding.cleanStoreListView.adapter = cleanStoreAdapter
                } else {
                    Log.d("CleanStoreActivity", "getCleanStoreData - onResponse : Error code ${response}")
                }
            }

            override fun onFailure(call: retrofit2.Call<CleanStoreResponse>, t: Throwable) {
                Log.d("CleanStoreActivity", t.message?:"통신오류")
            }
        })
    }

    class CleanStoreAdapter(private val cleanStoreArrayList: ArrayList<CleanStoreItem>, context: Context) : BaseAdapter() {
        private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        private lateinit var binding: ItemCleanstoreBinding

        override fun getCount(): Int = cleanStoreArrayList.size

        override fun getItem(p0: Int): Any = cleanStoreArrayList[p0]

        override fun getItemId(p0: Int): Long = p0.toLong()

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            binding = ItemCleanstoreBinding.inflate(inflater, p2, false)

            binding.storeImageView.setImageResource(cleanStoreArrayList[p0].image)
            binding.storeNameTextView.text = cleanStoreArrayList[p0].name
            binding.storeOwnerTextView.text = cleanStoreArrayList[p0].ownerName
            binding.storeAddressTextView.text = cleanStoreArrayList[p0].address
            binding.categoryTextView.text = cleanStoreArrayList[p0].category

            return binding.root
        }
    }
}