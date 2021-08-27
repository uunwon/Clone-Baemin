package com.yunwoon.clientproejct

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.data.OAuthLoginState
import com.yunwoon.clientproejct.databinding.ActivityLoginBinding
import com.yunwoon.clientproejct.naver.AuthLogin
import com.yunwoon.clientproejct.sharedPreference.MyApplication
import com.yunwoon.clientproejct.sqlite.DBHelper
import com.yunwoon.clientproejct.sqlite.MemberDB
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper : DBHelper

    private lateinit var mOAuthLoginModule : OAuthLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mOAuthLoginModule = AuthLogin.init(this@LoginActivity)
        /* mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(
             this@LoginActivity
            ,getString(R.string.naver_client_id)
            ,getString(R.string.naver_client_secret)
            ,getString(R.string.naver_client_name)
        ) */

        dbHelper = DBHelper(this, "MemberDB", null, 1)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 생성
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        // emailEditText passwordEditText loginButton
        binding.loginButton.setOnClickListener {
            val loginStatus = login(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())

            if(loginStatus)
                loginSuccess()
            else
                Toast.makeText(applicationContext, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.naverButton.setOnClickListener {
            naverLogin()
        }
    }

    private fun naverLogout() {
        if(mOAuthLoginModule.getState(this@LoginActivity) == OAuthLoginState.OK) {
            mOAuthLoginModule.logoutAndDeleteToken(this@LoginActivity)
            Log.i("로그인_액티비티", "Login Module State : " + mOAuthLoginModule.getState(this@LoginActivity).toString())
        }
        else {
            Log.i("로그인_액티비티", "Login Module State : " + mOAuthLoginModule.getState(this@LoginActivity).toString())
        }
    }

    private fun login(email: String?, password: String?): Boolean {
        // 읽기 모드로 데이터 저장소 가져옴
        val db = dbHelper.readableDatabase

        // 리턴받고자 하는 컬럼의 array
        val projection = arrayOf(BaseColumns._ID)

        // where 조건
        val selection = "${MemberDB.MemberEntry.COLUMN_USER_EMAIL} = ? AND ${MemberDB.MemberEntry.COLUMN_USER_PASSWORD} = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(
            MemberDB.MemberEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null, // group by 조건
            null, // having 조건
            null // order by 조건
        )

        // 반환된 cursor 의 값이 있다면! 정보 있다는 거니까! 로그인 성공!
        return cursor.count > 0
    }

    private fun loginSuccess() {
        val db = dbHelper.writableDatabase

        val status = "Y"
        val values = ContentValues().apply {
            put(MemberDB.MemberEntry.COLUMN_USER_STATUS, status)
        }

        val selection = "${MemberDB.MemberEntry.COLUMN_USER_EMAIL} LIKE ?"
        val selectionArgs = arrayOf("abc7017@gmail.com")

        db.update(MemberDB.MemberEntry.TABLE_NAME, values, selection, selectionArgs)

        Log.d("데이터베이스", "사용자 로그인 상태는 $status 로 업데이트 됨")

        finish()
    }

    // 네이버 로그인 구현
    private fun naverLogin() {
        /* 로그아웃 */
        // val isSuccessDeleteToken = mOAuthLoginModule.logoutAndDeleteToken(this@LoginActivity)
        // mOAuthLoginModule.logout(this@LoginActivity)

        if(mOAuthLoginModule.getState(this@LoginActivity) == OAuthLoginState.OK){
            Log.d("로그인_액티비티", "status don't need login")
            // RequestApiTask().execute()
        }else{
            Log.d("로그인_액티비티", "status need login")
            mOAuthLoginModule.startOauthLoginActivity(this, @SuppressLint("HandlerLeak")
            object: OAuthLoginHandler(){
                override fun run(success: Boolean) {
                    Log.d("로그인_액티비티", "OAuthLoginHandler run 동작")
                    if (success) {
                        Log.d("로그인_액티비티", "success 들어옴")
                        val accessToken = mOAuthLoginModule.getAccessToken(this@LoginActivity)
                        val refreshToken = mOAuthLoginModule.getRefreshToken(this@LoginActivity)
                        val expiresAt = mOAuthLoginModule.getExpiresAt(this@LoginActivity)
                        val tokenType = mOAuthLoginModule.getTokenType(this@LoginActivity)
                        Log.i("로그인_액티비티", "Login Access Token : $accessToken")
                        Log.i("로그인_액티비티", "Login refresh Token : $refreshToken")
                        Log.i("로그인_액티비티", "Login expiresAt : $expiresAt")
                        Log.i("로그인_액티비티", "Login Token Type : $tokenType")
                        Log.i("로그인_액티비티", "Login Module State : " + mOAuthLoginModule.getState(this@LoginActivity).toString())

                        // successLogin()
                        RequestApiTask().execute()
                    } else {
                        val errorCode = mOAuthLoginModule.getLastErrorCode(this@LoginActivity).getCode()
                        val errorDesc = mOAuthLoginModule.getLastErrorDesc(this@LoginActivity)
                        Toast.makeText(this@LoginActivity, "errorCode:$errorCode, errorDesc:$errorDesc", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    fun successLogin() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("생명주기", "Login onDestroy")
    }

    inner class RequestApiTask : AsyncTask<Void, Void, String>() {

        // 작업을 시작하면 호출되며, 백그라운드 스레드에서 동장
        // execute() 메서드로 전달된 인자를 받을 수 있음
        override fun doInBackground(vararg p0: Void?): String {
            // https://openapi.naver.com/v1/nid/me
            // https://apis.naver.com/nidlogin/nid/getHashId_v2.xml
            val url = "https://openapi.naver.com/v1/nid/me"
            val at : String = mOAuthLoginModule.getAccessToken(this@LoginActivity)
            return mOAuthLoginModule.requestApi(this@LoginActivity, at, url)
        }

        // doInBackground() 메서드가 Result 타입 값을 반환하면 호출되는 메서드
        // 메인 스레드에서 동작
        // 작업의 실행결과를 UI 에 업데이트할 수 있음
        override fun onPostExecute(result: String) {
            val loginResult = JSONObject(result)

            if(loginResult.getString("resultcode").equals("00")) {
                val response : JSONObject = loginResult.getJSONObject("response")
                val name : String = response.getString("name")
                val email : String = response.getString("email")
                val mobile : String = response.getString("mobile")

                Log.d("로그인_액티비티", "name = $name, email = $email, phoneNumber = $mobile")

                val splitArray = mobile.split("-")
                val mobile1 = splitArray[0]
                val mobile2 = splitArray[1]
                val mobile3 = splitArray[2]

                MyApplication.prefs.putString("name", name)
                MyApplication.prefs.putString("email", email)
                MyApplication.prefs.putString("mobile1", mobile1)
                MyApplication.prefs.putString("mobile2", mobile2)
                MyApplication.prefs.putString("mobile3", mobile3)

                successLogin()
            }  else {
                Log.d("로그인_액티비티", "RequestApiTask onPostExecute else 처리됨, ${loginResult.getString("resultcode")}")
            }
        }
    }
}