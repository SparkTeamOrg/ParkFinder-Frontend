package com.app.parkfinder.ui.screens.main

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.UserDto
import com.app.parkfinder.logic.view_models.ProfileViewModel
import java.util.logging.Logger
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.style.TextAlign
import com.app.parkfinder.utilis.validateUserName

@Composable
fun ProfileScreen(
    logout : ()->Unit,
    user: UserDto,
    currentImageUrl: Uri?,
    openImagePicker: () -> Unit = {},
    removeImage: () -> Unit = {},
    navigateToVehicleInfo: () -> Unit = {},
    startFpmNotificationService: () -> Unit = {},
    stopFpmNotificationService: () -> Unit = {},
    navigateToHelpCenter: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel(),
    updateUserName: (String) -> Unit
    ) {
    var showModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Box(
            modifier = Modifier.size(180.dp)
        ){
            currentImageUrl?.let { uri ->
                Logger.getLogger("ProfileScreen").info("Image with uri: $uri")
                ProfileImage(uri)
            } ?: run {
                Logger.getLogger("ProfileScreen").info("No image")
                ProfileImage(null)
            }
            Box(
                modifier = Modifier
                    .padding(bottom = 17.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(width = 3.dp, color = Color.White, shape = CircleShape)
                        .background(Color(0xFF0FCFFF))
                        .clickable{ openImagePicker() }
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Upload Image",
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                        tint = Color.White
                    )
                }
            }
            if(currentImageUrl != null) {
                Box(
                    modifier = Modifier
                        .padding(end = 42.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(width = 3.dp, color = Color.White, shape = CircleShape)
                            .background(Color.Red)
                            .clickable { removeImage() }
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Image",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center),
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = user.Fullname,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Email
        Text(
            text = user.Email,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = "Edit",
            fontSize = 14.sp,
            color = Color(0xFF00AEEF),
            modifier = Modifier.clickable{ showModal = true  }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            //FPM
            ToggleSwitch(
                isChecked = profileViewModel.isFpmOn,
                onCheckedChange = { isChecked ->
                    profileViewModel.isFpmOn = isChecked
                    if(isChecked)
                        startFpmNotificationService()
                    else {
                        stopFpmNotificationService()
                    }
                }
            )

            // Menu Items
            MenuItem(icon = Icons.Default.Wallet, title = "Balance")
            MenuItem(icon = Icons.Default.DirectionsCar, title = "Vehicle info", handleClick = navigateToVehicleInfo)
            MenuItem(icon = Icons.Default.StackedBarChart, title = "Statistics")
            MenuItem(icon = Icons.Default.Favorite, title = "Favourites")
            MenuItem(icon = Icons.AutoMirrored.Filled.HelpOutline, title = "Help Center", handleClick = navigateToHelpCenter)
        }
        Spacer(modifier = Modifier.weight(1f))

        // Log Out
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { logout() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,  // Replace with your logout icon resource
                contentDescription = "Log Out",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Log Out",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }

    EditNameDialog(onDismiss = { showModal = false }, updateUserName = updateUserName, currentName = user.Fullname, showModal)
}

@Composable
fun MenuItem(icon: ImageVector, title: String, notificationCount: Int? = null, handleClick: (()->Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (handleClick != null) {
                    handleClick()
                }
            }
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.W700,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        if (notificationCount != null && notificationCount > 0) {
            Box(
                modifier = Modifier
                    .background(Color.Red, shape = CircleShape)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$notificationCount",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ProfileImage(profileImage: Uri?) {
    val imagePainter = rememberAsyncImagePainter(
        model = profileImage ?: R.drawable.default_profile_picture
    )
    Image(
        painter = imagePainter,
        contentDescription = "Profile Image",
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .border(4.dp, Color(0xFF0FCFFF), CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ToggleSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = if (isChecked) "FPM On" else "FPM Off", color = Color.White,fontWeight = FontWeight.W700)
        Spacer(modifier = Modifier.fillMaxWidth())
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun EditNameDialog(
    onDismiss: () -> Unit,
    updateUserName: (String) -> Unit,
    currentName: String,
    showModal: Boolean
) {
    var fullName by remember { mutableStateOf(currentName) }

    if(showModal) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        tint = Color.White,
                        contentDescription = "Edit"
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Set a new name",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFF151A24),
                            unfocusedBorderColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Both first and last name must start with an uppercase letter and contain only lowercase alphabetic characters.",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 15.dp),
                        textAlign = TextAlign.Justify
                    )
                }
            },
            containerColor = Color(0xFF151A24),
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        updateUserName(fullName)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xFF0FCFFF),
                        disabledContentColor = Color.White.copy(alpha = 0.3f),
                        disabledContainerColor = Color(0xFF0FCFFF).copy(alpha = 0.3f)
                    ),
                    enabled = (fullName != currentName && validateUserName(fullName))
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        fullName = currentName
                   },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color.Red
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}