package com.devabhijeet.livestockbazaar.ui.activities

import android.graphics.Bitmap
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.models.User
import com.devabhijeet.livestockbazaar.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_show_my_qractivity.*


private lateinit var mUserid: String

class ShowMyQRActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_my_qractivity)


                showProgressDialog(R.string.please_wait.toString())


               mUserid= FirebaseAuth.getInstance().currentUser?.uid.toString()

        btn_ok_show_my_qr.visibility = View.GONE
            showUserQRCode(mUserid)



        btn_ok_show_my_qr.setOnClickListener {
            finish()
        }

    }


    private fun showUserQRCode(mUserid:String)
    {

        val writer = QRCodeWriter()

        try {

            val bitMatrix = writer.encode(mUserid, BarcodeFormat.QR_CODE,512,512)
            val width = bitMatrix.width
            val height= bitMatrix.height
            val bmp = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)

            for (x in 0 until width)
            {
                for(y in 0 until height)
                {
                    bmp.setPixel(x,y, if(bitMatrix[x,y])Color.BLACK else Color.WHITE )
                }
            }

            iv_my_qr_code.setImageBitmap(bmp)

            showErrorSnackBar("QR code generated successfully !",false)

        }
        catch (e: WriterException)
        {
            showErrorSnackBar("error occurred while generating qr code!$e",true)
        }

        hideProgressDialog()

        btn_ok_show_my_qr.visibility = View.VISIBLE

    }


}