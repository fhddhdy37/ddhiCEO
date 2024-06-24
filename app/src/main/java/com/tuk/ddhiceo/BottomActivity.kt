package com.tuk.ddhiceo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tuk.ddhiceo.databinding.ActivityBottomBinding
import com.tuk.ddhiceo.databinding.ActivityMainBinding

class BottomActivity : AppCompatActivity() {

    private var mBinding: ActivityBottomBinding? = null
    private val binding get() = mBinding!!
    private val ACTICITY_RESULT_CODE = 849

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBinding = ActivityBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavi.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_menuboard -> {
                    setFrag(0)
                }
                R.id.action_qrcode -> {
                    setFrag(1)
                }
                R.id.action_storeim -> {
                    setFrag(2)
                }
                else -> false
            }
        }

        val targetFragment = intent.getIntExtra("TARGET_FRAGMENT", 0)
        setFrag(targetFragment)
    }

    private fun setFrag(fragNum: Int): Boolean {
        val ft = supportFragmentManager.beginTransaction()

        when (fragNum) {
            0 -> {
                ft.replace(R.id.main_frame, Frag1()).commitNow()
            }
            1 -> {
                //ft.replace(R.id.main_frame, Frag2()).commitNow()
                var intent = Intent(this, QRScannerActivity::class.java)
                startActivityForResult(intent, ACTICITY_RESULT_CODE)
            }
            2 -> {
                ft.replace(R.id.main_frame, Frag3()).commitNow()
            }
        }
        return true
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}