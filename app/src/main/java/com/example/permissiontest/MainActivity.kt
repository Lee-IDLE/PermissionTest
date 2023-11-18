package com.example.permissiontest

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private val cameraResultLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted ->
            if(isGranted){
                Toast.makeText(this, "Permission granted for camera.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Permission denied for camera.", Toast.LENGTH_LONG).show();
            }

        }

    private val cameraAndLocationResultLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value

                if(isGranted){
                    if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this, "Permission granted for location", Toast.LENGTH_LONG).show()
                    }else if(permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(this, "Permission granted for coarse", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    private var btnCameraPermission : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // shouldShowRequestPermissionRationale: 사용자가 이전에 해당 권한을 거부한 적이 있는지, 다시 표시하지 않음을
        // 선택했는지 여부를 알려준다.
        btnCameraPermission = findViewById<Button>(R.id.btnCameraPermission)
        btnCameraPermission?.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showRationaleDialog("Permssion Demo requires camera access", "Camera cannot be used because Camera access is denied")
            }else{
                //cameraResultLauncher.launch(Manifest.permission.CAMERA)
                cameraAndLocationResultLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,                 // 카메라
                        Manifest.permission.ACCESS_FINE_LOCATION,   // 정확한 위치
                        Manifest.permission.ACCESS_COARSE_LOCATION  // 대략적인 위치
                    )
                )
            }
        }
    }

    private fun showRationaleDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}