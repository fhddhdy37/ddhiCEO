package com.tuk.ddhiceo

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.tuk.ddhiceo.databinding.ActivityCheckBusinessBinding
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CheckBusinessActivity : AppCompatActivity() {

    private val baseUrl = "https://api.odcloud.kr/api/nts-businessman/v1/"
    private val encodeKey = "SM%2By3IUAeEUhKQKcYvJLUphzvNTj20ZtrId%2BLt09CM2KN8GHa9KmFwSah403v%2BozraNflikVe0DCFidXYwNiKw%3D%3D"
    private val decodeKey = "SM+y3IUAeEUhKQKcYvJLUphzvNTj20ZtrId+Lt09CM2KN8GHa9KmFwSah403v+ozraNflikVe0DCFidXYwNiKw=="
    private val API_REQUEST_CODE = 639

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityCheckBusinessBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val bNo = bind.bNo
        val bNm = bind.bNm
        val startDt = bind.startDt
        val pNm = bind.pNm
        val address = bind.bAdr
        val register = bind.register

        register.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(BusinessApiService::class.java)
            val business = Business(
                b_no = bNo.text.toString(),
                b_nm = bNm.text.toString(),
                start_dt = startDt.text.toString(),
                p_nm = pNm.text.toString()
            )
            val requestBody = BusinessRequestBodyData(listOf(business))
            val gson = Gson()
            val dlg = AlertDialog.Builder(this)

            service.postData(decodeKey, "JSON", requestBody).enqueue(object: Callback<BusinessApiResponse> {
                override fun onResponse(call: Call<BusinessApiResponse>, response: Response<BusinessApiResponse>) {
                    val apiResponse = response.body()
                    val responseBody = gson.toJson(apiResponse)
                    if(response.isSuccessful){
                        if(apiResponse!!.data[0]!!.valid == "01"){
                            dlg.setMessage("사업자 확인이 완료되었습니다.")
                            dlg.setPositiveButton("OK",
                                DialogInterface.OnClickListener{dialog, which ->
                                    setResult(API_REQUEST_CODE)
                                    finish()
                                })
                            dlg.show()

                        }
                        else{
                            Log.d("responseFail", "${response.code()} ${response.message()}\n${responseBody.toString()}")
                            dlg.setMessage("사업자 확인에 실패했습니다.\n정보를 다시 확인해주세요.")
                            dlg.setPositiveButton("OK", null)
                            dlg.show()
                        }
                    }
                    else{
                        Log.d("responseFail", "${response.code()} ${response.message()}")
                        dlg.setMessage("사업자 확인에 실패했습니다.\n관리자에게 문의해주세요.\n에러 코드: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<BusinessApiResponse>, t: Throwable) {
                    Log.d("onFailure", "${t.message}")
                    dlg.setMessage("사업자 확인에 실패했습니다.\n관리자에게 문의해주세요.\n에러 코드: ${t.message}")
                }
            })
        }
    }
}