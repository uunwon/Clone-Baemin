package com.yunwoon.clientproejct

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.yunwoon.clientproejct.databinding.ActivityLoginBinding
import com.yunwoon.clientproejct.sqlite.DBHelper
import com.yunwoon.clientproejct.sqlite.MemberDB
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper : DBHelper

    private val mOAuthLoginModule = OAuthLogin.getInstance()
    private val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            Log.d("핸들러 작동 확인" , "OAuthLoginHandler 동작")
            if (success) {
                Log.d("핸들러 작동 확인" , "OAuthLoginHandler run 성공")
                val accessToken: String = mOAuthLoginModule.getAccessToken(applicationContext)
                val refreshToken: String = mOAuthLoginModule.getRefreshToken(applicationContext)
                val expiresAt: Long = mOAuthLoginModule.getExpiresAt(applicationContext)
                val tokenType: String = mOAuthLoginModule.getTokenType(applicationContext)

                RequestApiTask(applicationContext, mOAuthLoginModule).execute()
            } else {
                Log.d("핸들러 작동 확인" , "OAuthLoginHandler run 실패")
                val errorCode: String = mOAuthLoginModule.getLastErrorCode(applicationContext).getCode()
                val errorDesc: String = mOAuthLoginModule.getLastErrorDesc(applicationContext)
                Toast.makeText(
                    applicationContext, "errorCode:" + errorCode
                            + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //  네이버 아이디로 로그인 인스턴스 초기화
        mOAuthLoginModule.init(
            this@LoginActivity
            , R.string.naver_client_id.toString()
            , R.string.naver_client_secret.toString()
            , R.string.naver_client_name.toString()
        )

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
        mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("생명주기", "Login onDestroy")
    }

    class RequestApiTask(private val context: Context, private val mOAuthLoginModule: OAuthLogin) : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Void?): String {
            // https://openapi.naver.com/v1/nid/me
            // https://apis.naver.com/nidlogin/nid/getHashId_v2.xml
            Log.d("핸들러 작동 확인" , "RequestApiTask doInBackground 에 들어옴")
            val url = "https://openapi.naver.com/v1/nid/me"
            val at : String = mOAuthLoginModule.getAccessToken(context)
            return mOAuthLoginModule.requestApi(context, at, url)
        }

        override fun onPostExecute(result: String) {
            Log.d("핸들러 작동 확인" , "RequestApiTask onPostExecute 성공")
            try{
                val loginResult = JSONObject(result)
                if(loginResult.getString("resultcode").equals("00")) {
                    val response : JSONObject = loginResult.getJSONObject("response")
                    val id : String = response.getString("id")
                    val email : String = response.getString("email")
                    Toast.makeText(context, "id = $id , email = $email", Toast.LENGTH_SHORT).show()
                }

            } catch (e : JSONException) {
                e.printStackTrace()
            }
        }

    }
}