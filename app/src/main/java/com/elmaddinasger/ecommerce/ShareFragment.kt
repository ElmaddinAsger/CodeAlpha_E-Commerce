package com.elmaddinasger.ecommerce

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import androidx.core.content.FileProvider
import com.elmaddinasger.ecommerce.databinding.FragmentShareBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.Date
import java.util.Locale
import android.graphics.*
import android.media.ExifInterface
import java.io.IOException

class ShareFragment : Fragment() {
    private lateinit var binding: FragmentShareBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedBitmap: Bitmap? = null

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShareBinding.inflate(inflater,container,false)
        registerLauncher()
        cameraLauncher ()
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddImageFromGallery.setOnClickListener {
            selectImage(binding.root)
        }
        binding.btnTakeAPhoto.setOnClickListener {
            openCamera()
        }
    }


    private fun cameraLauncher () {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val uri = photoUri // `val` olarak sabitle
                uri?.let {
                    val bitmap = uriToBitmap(it)
                    binding.imgProductImage.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "com.elmaddinasger.ecommerce.fileprovider", // Burada package adı tam olmalı!
            photoFile
        )
        photoUri?.let {
            cameraLauncher.launch(it)
        }

    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // EXIF verisini oku
            val exif = ExifInterface(requireContext().contentResolver.openInputStream(uri)!!)
            val rotation = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            // Gerekirse döndür
            if (rotation != 0) {
                val matrix = Matrix()
                matrix.postRotate(rotation.toFloat())
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
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