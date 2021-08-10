package com.yunwoon.clientproejct

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.yunwoon.clientproejct.address.AddressInterface
import com.yunwoon.clientproejct.address.RetrofitAddressClient
import com.yunwoon.clientproejct.address.models.AddressResponse
import com.yunwoon.clientproejct.cleanStore.CleanStoreInterface
import com.yunwoon.clientproejct.cleanStore.RetrofitCleanStoreClient
import com.yunwoon.clientproejct.cleanStore.models.CleanStoreResponse
import com.yunwoon.clientproejct.databinding.ActivitySearchaddressBinding
import com.yunwoon.clientproejct.databinding.ItemAddressBinding
import com.yunwoon.clientproejct.sharedPreference.MyApplication
import retrofit2.Response

data class AddressItem(val road:String, val parcel:String, val zipcode: String)
class SearchAddressActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchaddressBinding
    private val addressItemArrayList = ArrayList<AddressItem>()
    private lateinit var addressAdapter : AddressAdapter
    private lateinit var searchText:String
    private val key = "0A09C551-ED40-31C2-82B1-190160155472"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchaddressBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow) // 뒤로가기 버튼 아이콘 커스텀

        binding.addressListView.isNestedScrollingEnabled = true

        if(intent.hasExtra("address")) {
            searchText = intent.getStringExtra("address")
            binding.addressEditText.setText(searchText)
            getAddressData(searchText)
        } else {
            Toast.makeText(this, "전달된 주소가 없습니다.", Toast.LENGTH_SHORT).show()
        }

        // 작성 중 텍스트 지우기
        binding.deleteImageButton.setOnClickListener {
            binding.addressEditText.text.clear()
        }

        // 주소 검색
        binding.searchImageButton.setOnClickListener {
            searchText = binding.addressEditText.text.toString()

            if(searchText.isNotEmpty()) {
                getAddressData(searchText)
            }
            else
                Toast.makeText(applicationContext, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
        }

        // 리스트뷰 아이템 클릭 이벤트
        binding.addressListView.setOnItemClickListener { adapterView, view, i, l ->
            saveDialog(adapterView, view, i, l)
        }
    }

    // 주소 저장 다이얼로그 띄우기
    private fun saveDialog(adapterView: AdapterView<*>, view: View, position:Int, l:Long) {
        var builder = AlertDialog.Builder(this)
        val road = addressItemArrayList[position].road
        val parcel = addressItemArrayList[position].parcel

        builder.setMessage("현 주소로 설정하시겠습니까?")
        builder.setIcon(R.mipmap.ic_baemin)

        var listener = object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when (p1) {
                    Dialog.BUTTON_POSITIVE -> {
                        MyApplication.prefs.putString("road", road)
                        MyApplication.prefs.putString("parcel", parcel)
                        // Toast.makeText(applicationContext, "$position 번 주소 저장됨",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        builder.setPositiveButton("확인",listener)
        builder.setNegativeButton("취소", listener)

        builder.show()
    }

    // API 호출 메서드
    private fun getAddressData(searchText : String) {
        val addressInterface = RetrofitAddressClient.sRetrofit.create(AddressInterface::class.java)
        addressInterface.getAddress("search",15, searchText, "address", "road", "json", key).enqueue(object : retrofit2.Callback<AddressResponse>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: retrofit2.Call<AddressResponse>,
                response: Response<AddressResponse>
            ) {
                if(response.isSuccessful) {
                    val result = response.body() as AddressResponse
                    addressItemArrayList.clear()

                    // 리스트뷰에 아이템 뿌려주기
                    for(i in 0..14) {
                        addressItemArrayList.add(AddressItem(result.response.result.items[i].address.road,
                                                            result.response.result.items[i].address.parcel,
                                                            result.response.result.items[i].address.zipcode))
                    }
                    addressAdapter = AddressAdapter(
                        addressItemArrayList,
                        this@SearchAddressActivity
                    )
                    binding.addressListView.adapter = addressAdapter
                    addressAdapter.notifyDataSetChanged()
                } else {
                    Log.d("SearchAddressActivity", "getAddressData - onResponse : Error code ${response}")
                }
            }

            override fun onFailure(call: retrofit2.Call<AddressResponse>, t: Throwable) {
                Log.d("SearchAddressActivity", t.message?:"통신오류")
            }
        })
    }

    class AddressAdapter(private val addressItemArrayList:ArrayList<AddressItem>, context: Context) : BaseAdapter() {
        private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        private lateinit var binding:ItemAddressBinding

        override fun getCount(): Int = addressItemArrayList.size

        override fun getItem(p0: Int): Any = addressItemArrayList[p0]

        override fun getItemId(p0: Int): Long = p0.toLong()

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            binding = ItemAddressBinding.inflate(inflater, p2, false)

            binding.parcelTextView.text = addressItemArrayList[p0].parcel
            binding.roadTextView.text = addressItemArrayList[p0].road
            binding.zipcodeTextView.text = addressItemArrayList[p0].zipcode

            return binding.root
        }
    }
}