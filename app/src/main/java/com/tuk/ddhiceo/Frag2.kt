package com.tuk.ddhiceo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback

class Frag2 : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_REQUEST_CODE = 167
    private val ACTIVITY_RESULT_CODE = 849

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag2, container, false)

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
        } else {
            scan(view)
        }

        return view
    }

    private fun scan(view: View) {
        val scannerView: CodeScannerView = view.findViewById(R.id.scanner_view)
        val codeText: TextView = view.findViewById(R.id.code_text)

        codeScanner = CodeScanner(requireContext(), scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    codeText.text = it.text
                    val intent = Intent(activity, SetCouponActivity::class.java)
                    intent.putExtra("couponKey", it.text)
                    startActivityForResult(intent, ACTIVITY_RESULT_CODE)
                }
            }

            errorCallback = ErrorCallback {
                requireActivity().runOnUiThread {
                    codeText.text = "Scan Error ${it.message}"
                }
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                view?.let { scan(it) }
            } else {
                Toast.makeText(context, "카메라 권한 거부됨", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ACTIVITY_RESULT_CODE) {
            data?.let {
                if (data.getBooleanExtra("MULTI_FINISH", false)) {
                    activity?.finish()
                }
            }
        }
    }
}