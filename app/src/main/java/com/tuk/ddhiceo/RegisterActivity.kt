package com.tuk.ddhiceo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mEtEmail: EditText
    private lateinit var mEtPwd: EditText
    private lateinit var mEtPwdConfirm: EditText
    private lateinit var mBtnRegister: Button
    private lateinit var mApi: Button
    private val API_REQUEST_CODE = 639
    private var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DDHI")

        mEtEmail = findViewById(R.id.et_email)
        mEtPwd = findViewById(R.id.et_pwd)
        mEtPwdConfirm = findViewById(R.id.et_pwd_confirm)
        mBtnRegister = findViewById(R.id.btn_register)
        mApi = findViewById(R.id.api)

        mApi.setOnClickListener {
            val intent = Intent(this, CheckBusinessActivity::class.java)
            startActivityForResult(intent, API_REQUEST_CODE)
        }

        mBtnRegister.setOnClickListener {
            var strEmail = mEtEmail.getText().toString()
            var strPwd = mEtPwd.getText().toString()
            var strPwd2 = mEtPwdConfirm.text.toString()

            if(strPwd != strPwd2){
                Toast.makeText(this, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                    .addOnCompleteListener(this)
                    { tast ->
                        if (tast.isSuccessful && flag) {
                            var firebaseUser: FirebaseUser? = mFirebaseAuth.getCurrentUser()
                            var account = UserAccount()
                            if (firebaseUser != null) {
                                account.idToken = firebaseUser.uid
                                account.emailId = firebaseUser.email.toString()
                                account.password = strPwd
                                mDatabaseRef.child("UserAccount").child(firebaseUser.uid)
                                    .setValue(account)
                            }

                            Toast.makeText(this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                            finish()

                        } else {
                            Toast.makeText(this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == API_REQUEST_CODE){
            flag = true
        }
        else{
            flag = false
        }
        Log.d("result", "${flag}")
    }
}