package com.yunwoon.clientproejct

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.appbar.AppBarLayout
import com.yunwoon.clientproejct.databinding.ActivityStoreBinding
import com.yunwoon.clientproejct.databinding.ItemMenuBinding
import com.yunwoon.clientproejct.dialog.CalldialogFragment
import kotlinx.android.synthetic.main.activity_store.*
import kotlin.math.abs

data class MenuItem(val menu: String, val menuDetail: String, val menuImage: Int)

class StoreActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener{
    private lateinit var binding: ActivityStoreBinding
    private lateinit var customAdapter: CustomAdapter
    private val menuItemArrayList = ArrayList<MenuItem>()

    private var CALL_REQUEST_CODE = 1

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var menuIconDrawable: Drawable
    private lateinit var whiteFilter: PorterDuffColorFilter
    private lateinit var blackFilter: PorterDuffColorFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        val view = binding.root

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        setContentView(view)
        setSupportActionBar(binding.toolbar)

        setListView()

        binding.callTextView.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_REQUEST_CODE)
            }
            callAlertDlg()
        }

        binding.nestedScrollView.fullScroll(View.FOCUS_UP)
        binding.nestedScrollView.scrollTo(0,0)

        whiteFilter = PorterDuffColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        blackFilter = PorterDuffColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)

        appBarLayout = binding.appBarLayout
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.searchmenu, menu)

        if (menu != null)
            menuIconDrawable = menu.getItem(0).icon
        menuIconDrawable.mutate()

        appBarLayout.addOnOffsetChangedListener(this)
        return true
    }

    // 스크롤에 따른 appBarLayout 테마 변경 (흰/검)
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (appBarLayout != null) {
            when {
                //  State Expanded
                verticalOffset == 0 -> {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)
                    menuIconDrawable.colorFilter = whiteFilter
                    window.decorView.systemUiVisibility = 0
                }
                //  State Collapsed
                abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
                    menuIconDrawable.colorFilter = blackFilter
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }

    fun callAlertDlg(){
        val dialog = CalldialogFragment()
        dialog.show(supportFragmentManager, "CalldialogFragment")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CALL_REQUEST_CODE -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "권한 승인", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "권한 승인 거부", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setListView() {
        menuItemArrayList.add(MenuItem("딸기 우유크림 듬뿍 도넛","달콤한 글레이즈가 토핑된 쉘도넛에 딸기우유크림을 가득 충진", R.drawable.krispy_menu_01))
        menuItemArrayList.add(MenuItem("로투스 비스코프 도넛","로투스 크림과 스프레드,바삭한 쿠키까지 가득 토핑한 쉘도넛", R.drawable.krispy_menu_02))
        menuItemArrayList.add(MenuItem("초코와플넛","와플모양의 오리지널도넛에 진한 초코맛이 어우러진 달콤한 디저트", R.drawable.krispy_menu_03))
        // menuItemArrayList.add(MenuItem("썸머 스위밍튜브","바다 위 떠있는 빨간 튜브를 표현한 수박캔디맛 도넛", R.drawable.krispy_menu_04))

        customAdapter = CustomAdapter(menuItemArrayList, this)
        binding.menuListView.adapter = customAdapter
    }

    class CustomAdapter(private val menuItemArrayList: ArrayList<MenuItem>, context: Context) : BaseAdapter() {

        private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        private lateinit var binding: ItemMenuBinding

        override fun getCount(): Int = menuItemArrayList.size

        override fun getItem(p0: Int): Any = menuItemArrayList[p0]

        override fun getItemId(p0: Int): Long = p0.toLong()

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            binding = ItemMenuBinding.inflate(inflater, p2, false)

            binding.menuTextView.text = menuItemArrayList[p0].menu
            binding.menuDetailTextView.text = menuItemArrayList[p0].menuDetail
            binding.menuImageView.setImageResource(menuItemArrayList[p0].menuImage)

            return binding.root
        }
    }
}