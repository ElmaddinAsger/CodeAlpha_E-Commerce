package com.elmaddinasger.ecommerce

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.elmaddinasger.ecommerce.databinding.FragmentShareBinding
import com.google.android.material.snackbar.Snackbar

class ShareFragment : Fragment() {
    private lateinit var binding: FragmentShareBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShareBinding.inflate(inflater,container,false)
        registerLauncher()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddImageFromGallery.setOnClickListener {
            selectImage(binding.root)
        }
    }

    private fun selectImage(view: View) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //Android 33+ -> READ_MEDIA_IMAGES
            permissionCall(view,Manifest.permission.READ_MEDIA_IMAGES)

        } else {
            //Android 32- -> READ_EXTERNAL_STORAGE
            permissionCall(view,Manifest.permission.READ_EXTERNAL_STORAGE)

        }

    }

    private fun permissionCall (view: View, permission: String) {
        if (ContextCompat.checkSelfPermission(requireContext(),permission) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),permission)){

                Snackbar.make(view,"Permission needed for gallery.", Snackbar.LENGTH_INDEFINITE).setAction("Give Permissions",View.OnClickListener {
                    //request permission
                    permissionLauncher.launch(permission)
                }).show()
            } else {
                permissionLauncher.launch(permission)
                //request permission
            }
        } else {
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
            //intent
        }
    }

    private fun registerLauncher () {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null ) {
                    val imageData = intentFromResult.data
                    imageData?.let { image ->
                        try {
                            if (Build.VERSION.SDK_INT >= 28){
                                val source = ImageDecoder.createSource(requireContext().contentResolver,image)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                            } else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,image)
                            }
                            binding.imgProductImage.setImageBitmap(selectedBitmap)
                        } catch (e: Exception) {

                        }
                    }

                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result) {
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(requireContext(),"Permission needed!", Toast.LENGTH_LONG).show()
            }
        }
    }



}