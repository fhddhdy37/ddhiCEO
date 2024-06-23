package com.tuk.ddhiceo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tuk.ddhiceo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
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
                ft.replace(R.id.main_frame, Frag2()).commitNow()
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