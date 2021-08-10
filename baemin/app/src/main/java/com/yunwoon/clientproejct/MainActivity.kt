package com.yunwoon.clientproejct

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.github.amlcurran.showcaseview.ShowcaseView
import com.github.amlcurran.showcaseview.targets.ActionViewTarget
import com.optimizer.tooltips.TipsManager
import com.optimizer.tooltips.animations.AnimationComposer
import com.optimizer.tooltips.animations.fading_in.FadeInAnimator
import com.optimizer.tooltips.animations.fading_out.FadeOutAnimator
import com.optimizer.tooltips.position.TipHorizontalGravity
import com.optimizer.tooltips.position.TipVerticalGravity
import com.optimizer.tooltips.tips.Tooltip
import com.yunwoon.clientproejct.databinding.ActivityMainBinding
import com.yunwoon.clientproejct.sharedPreference.MyApplication
import com.yunwoon.clientproejct.sqlite.DBHelper
import com.yunwoon.clientproejct.sqlite.MemberDB
import java.util.concurrent.LinkedBlockingQueue

const val ANIM_DURATION = 300L

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("생명주기", "Main onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 최초 실행시 데이터베이스 세팅
        dbHelper = DBHelper(this, "MemberDB", null, 1)

        // setSqlite()
        // MyApplication.prefs.clear()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu) // 뒤로가기 버튼 아이콘 커스텀

        // 시작 시, initial tip 세팅
        setInitialTip()

        // 코로나 예약접종센터 리스트로 화면 넘어가기
        binding.toolbar.setNavigationOnClickListener {
            this.startActivity(Intent(this, CenterActivity::class.java))
        }

        binding.titleAddress.setOnClickListener {
            this.startActivity(Intent(this, AddressActivity::class.java))
        }

        /* 첫 화면 광고 띄우기 */
        // val dialog = AddialogFragment()
        // dialog.show(supportFragmentManager, "DialogFragment")

        // 배달 메뉴
        binding.btn01.setOnClickListener {
            this.startActivity(Intent(this, DeliveryActivity::class.java))
        }

        // 안심식당 메뉴
        binding.cleanStoreImageButton.setOnClickListener {
            this.startActivity(Intent(this, CleanStoreActivity::class.java))
        }
    }

    private fun setInitialTip() {
        TipsManager.showTips(binding.root as ViewGroup, ContextCompat.getColor(this, com.optimizer.tooltips.R.color.black_30)) {
            val inflater = LayoutInflater.from(this)
            val menuTipView = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.item_tooltip, null, false).getRoot() as TextView
            val menuTipParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)

            menuTipView.layoutParams = menuTipParams

            menuTipView.background = ContextCompat.getDrawable(this, R.drawable.shape_tooltip_window_purple)
            menuTipView.text = "메뉴 버튼을 클릭해 우리동네 코로나 예약접종센터를 확인해보세요!\n코로나19의 빠른 회복을 기원합니다."

            val actionbarMenuTooltip = Tooltip.Builder()
                .attachTooltipView(menuTipView)
                .withEnterAnimation(AnimationComposer(FadeInAnimator()).duration(ANIM_DURATION))
                .withExitAnimation(AnimationComposer(FadeOutAnimator()).duration(ANIM_DURATION))
                .withGravity(TipVerticalGravity.BOTTOM, TipHorizontalGravity.LEFT)
                .withAnchorView(binding.toolbar)
                .build()

            LinkedBlockingQueue(listOf(actionbarMenuTooltip))
        }
    }

    fun setSqlite() {
        // 쓰기 모드로 데이터 저장소를 가져옴
        val db = dbHelper.writableDatabase

        // 열 이름이 키인 새 값 맵을 만들기
        val values = ContentValues().apply {
            put(MemberDB.MemberEntry.COLUMN_USER_EMAIL, "abc7017@gmail.com")
            put(MemberDB.MemberEntry.COLUMN_USER_PASSWORD, "1234")
            put(MemberDB.MemberEntry.COLUMN_USER_NICKNAME, "이름")
            put(MemberDB.MemberEntry.COLUMN_USER_STATUS, "N")
        }

        // 새 행을 삽입하고 새 행의 기본 키 값을 반환
        db?.insert(MemberDB.MemberEntry.TABLE_NAME, null, values)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_mypage -> {
                // 사용자 상태에 따라 My 배민 페이지 다르게 표시
                this.startActivity(Intent(this,MyBaeminActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        Log.d("생명주기", "Main onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("생명주기", "Main onResume")

        binding.titleAddress.text = MyApplication.prefs.getString("road", "서울시 마포구 연남동 568-26 연남토마")
    }

    override fun onPause() {
        super.onPause()
        Log.d("생명주기", "Main onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("생명주기", "Main onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("생명주기", "Main onDestroy")

        val db = dbHelper.writableDatabase

        val status = "N"
        val values = ContentValues().apply {
            put(MemberDB.MemberEntry.COLUMN_USER_STATUS, status)
        }

        val selection = "${MemberDB.MemberEntry.COLUMN_USER_EMAIL} LIKE ?"
        val selectionArgs = arrayOf("abc7017@gmail.com")

        db.update(MemberDB.MemberEntry.TABLE_NAME, values, selection, selectionArgs)
    }
}
