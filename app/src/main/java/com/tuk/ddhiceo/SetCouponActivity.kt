package com.tuk.ddhiceo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tuk.ddhiceo.databinding.ActivitySetCouponBinding

class SetCouponActivity : AppCompatActivity() {
    private val RESULT_SUCCESS_CODE = 849

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivitySetCouponBinding.inflate(layoutInflater)
        var couponCount = bind.couponCount.text.toString().toInt()
        val dlg = AlertDialog.Builder(this@SetCouponActivity)

        bind.testText.text = intent.getStringExtra("couponKey")
        setContentView(bind.root)

        bind.minus.setOnClickListener {
            couponCount -= 1
            if(couponCount <= 1){
                couponCount = 1
            }
            bind.couponCount.text = couponCount.toString()
        }
        bind.plus.setOnClickListener {
            couponCount += 1
            if(couponCount >= 10){
                couponCount = 10
            }
            bind.couponCount.text = couponCount.toString()
        }
        bind.applyMinus.setOnClickListener {
            dlg.setMessage("${bind.couponCount.text}개의 쿠폰을 차감하시겠습니까?")
            dlg.setPositiveButton("확인"){ dialog, which ->
                returnToMenuBoard()
            }
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
        bind.applyPlus.setOnClickListener {
            dlg.setMessage("${bind.couponCount.text}개의 쿠폰을 적립하시겠습니까?")
            dlg.setPositiveButton("확인"){ dialog, which ->
                returnToMenuBoard()
            }
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
    }

    private fun returnToMenuBoard() {
        val intent = Intent(this, BottomActivity::class.java)
        intent.putExtra("TARGET_FRAGMENT", 0)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}