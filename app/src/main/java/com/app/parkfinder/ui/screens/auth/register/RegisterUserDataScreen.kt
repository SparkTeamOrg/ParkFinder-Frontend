package com.app.parkfinder.ui.screens.auth.register

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.app.parkfinder.R
import com.app.parkfinder.ui.theme.ParkFinderTheme

@Composable
fun RegisterUserDataScreen(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onBackClick: () -> Unit,
    validateUserName: (String) -> Boolean,
    validatePhoneNumber: (String) -> Boolean,
    onNextClick: () -> Unit,
    openImagePicker: () -> Unit,
    profileImage: Uri?,
    onProfileImageChange: (Uri?) -> Unit
) {
    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF293038), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.park_finder_logo),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Dummy",
                tint = Color.Transparent,
                modifier = Modifier.size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = stringResource(id = R.string.register_user_data_setup_your_account),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier.size(180.dp)
        ){
            ProfileImage(profileImage)
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
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Upload Image",
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                        tint = Color.White
                    )
                }
            }
            if(profileImage != null) {
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
                            .clickable { onProfileImageChange(null) }
                    ) {
                        Icon(
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
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .background(Color(36, 45, 64).copy(alpha = 0.4f))
                .padding(16.dp)
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.register_user_data_personal_information),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(36, 45, 64),
                        unfocusedBorderColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "personIcon",
                            tint = if(nameError) Color.Red else Color.White
                        )
                    },
                    placeholder = {
                        if (nameError) {
                            Text(
                                text = stringResource(id = R.string.error_invalid_full_name_format),
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    isError = nameError,
                    value = fullName,
                    onValueChange = {
                        onFullNameChange(it)
                        nameError = false
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.register_user_data_full_name),
                            color = if (nameError) Color.Red else Color.White)
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(36, 45, 64),
                        unfocusedBorderColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "phoneIcon",
                            tint = if(phoneError) Color.Red else Color.White
                        )
                    },
                    placeholder = {
                        if (phoneError) {
                            Text(
                                text = stringResource(id = R.string.error_invalid_phone_number_format),
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    isError = phoneError,
                    value = phoneNumber,
                    onValueChange = {
                        onPhoneNumberChange(it)
                        phoneError = false
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.register_user_data_phone_number),
                            color = if (phoneError) Color.Red else Color.White)
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        Button(
            onClick = {
                nameError = !validateUserName(fullName)
                phoneError = !validatePhoneNumber(phoneNumber)
                if(!nameError && !phoneError){
                    onNextClick()
                }
                else{
                    if(nameError)
                        onFullNameChange("")
                    if(phoneError)
                        onPhoneNumberChange("")
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0FCFFF),
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(id = R.string.common_next)
            )
        }
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

@Preview(showBackground = true)
@Composable
fun RegisterUserDataScreenPreview() {
    ParkFinderTheme {
        RegisterUserDataScreen(
            fullName = "",
            onFullNameChange = {},
            phoneNumber = "",
            onPhoneNumberChange = {},
            onBackClick = {},
            validateUserName = { true },
            validatePhoneNumber = { true },
            onNextClick = {},
            openImagePicker = {},
            profileImage = null,
            onProfileImageChange = {}
        )
    }
}