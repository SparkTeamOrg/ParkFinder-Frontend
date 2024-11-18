package com.app.parkfinder.utilis

import android.graphics.Rect
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

object ImageUtils {
    fun openImagePicker(pickMedia: ActivityResultLauncher<PickVisualMediaRequest>) {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun openCropper(uri: Uri, cropImage: ActivityResultLauncher<CropImageContractOptions>) {
        val options = CropImageContractOptions(
            uri,
            CropImageOptions(
                imageSourceIncludeGallery = true,
                cropShape = CropImageView.CropShape.RECTANGLE,
                fixAspectRatio = true,
                aspectRatioX = 1,
                aspectRatioY = 1,
                canChangeCropWindow = true,
                centerMoveEnabled = true,
                initialCropWindowRectangle = Rect(100, 100, 900, 900),
                activityBackgroundColor = 0xFF151A24.toInt(),
                toolbarColor = 0xFF1B1B1B.toInt(),
                toolbarBackButtonColor = 0xFFFFFF,
                toolbarTintColor = 0xFFFFFF,
                cropMenuCropButtonTitle = "Done",
                minCropWindowWidth = 800,
                minCropWindowHeight = 800,
                maxCropResultWidth = 800,
                maxCropResultHeight = 800
            )
        )
        cropImage.launch(options)
    }
}