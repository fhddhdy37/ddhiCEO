package com.tuk.ddhiceo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.tuk.ddhiceo.databinding.ActivityQrscannerBinding

class QRScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_REQUEST_CODE = 167
    private val ACTICITY_RESULT_CODE = 849

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //권한 체크
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
            scan()
        }else{
            //QRCODE 스캔
            scan()
        }

        bind.resetBtn.setOnClickListener {
            //초기화 확인하고 실행
            if(::codeScanner.isInitialized){
                codeScanner.startPreview()
            }
        }
    }

    //QRCODE 스캔
    private fun scan() {
        //초기화
        val scannerView: CodeScannerView = findViewById(R.id.scanner_view)

        //QRCODE 보여줄 텍스트뷰
        val codeText: TextView = findViewById(R.id.code_text)

        //스캐너 초기화
        codeScanner = CodeScanner(this, scannerView)

        //스캐너 셋팅
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS //포맷

            autoFocusMode = AutoFocusMode.SAFE //포커스
            isAutoFocusEnabled = true //자동포커스 활성화
            isFlashEnabled = false //플래쉬

            //QRCODE 확인되면 실행
            decodeCallback = DecodeCallback {
                runOnUiThread {

                    //텍스트에 QRCODE 보여주기
                    codeText.text = it.text
                    val intent = Intent(this@QRScannerActivity, SetCouponActivity::class.java)
                    intent.putExtra("couponKey", it.text)
                    startActivityForResult(intent, ACTICITY_RESULT_CODE)
                }
            }
            //에러 발생 시 실행
            codeScanner.errorCallback = ErrorCallback {
                runOnUiThread {
                    codeText.text = "Scan Error ${it.message}"
                }
            }
        }

        //스캔뷰 클랙 이벤트
        scannerView.setOnClickListener {
            //초기화 확인하고 실행
            if(::codeScanner.isInitialized){
                codeScanner.startPreview()
            }
        }
    }

    //권한요청 처리결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "카메라 권한 부여됨",
                    Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "카메라 권한 거부됨",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    //액티비티 재실행 되면 실행됨
    override fun onResume() {
        super.onResume()
        //초기화 확인하고 실행
        if(::codeScanner.isInitialized){
            codeScanner.startPreview()
        }
    }

    //액티비티 정지되면 실행됨
    override fun onPause() {
        super.onPause()
        //초기화 확인하고 실행
        if(::codeScanner.isInitialized){
            codeScanner.releaseResources()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == ACTICITY_RESULT_CODE){
            data?.let{
                if(data.getBooleanExtra("MULTI_FINISH", false)){
                    finish()
                }
            }
        }
    }
}