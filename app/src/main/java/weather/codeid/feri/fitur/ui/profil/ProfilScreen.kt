package weather.codeid.feri.fitur.ui.profil

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import weather.codeid.feri.data.local.roomdb.entity.User
import weather.codeid.feri.fitur.viewmodels.AuthViewModel
import weather.codeid.feri.utils.Constant

@ExperimentalComposeUiApi
@Composable
fun profilScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {
    authViewModel.getUser()
    var user by remember {
        authViewModel.user
    }
    var firstname by remember {
        mutableStateOf("")
    }
    var lastname by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var alamat by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var showPassword by remember {
        mutableStateOf(false)
    }
    var msgSuccess by remember {
        authViewModel.msgSuccess
    }
    user?.let {
        firstname = it.firstName.toString()
        lastname = it.lastName.toString()
        email = it.email.toString()
        alamat = it.alamat.toString()
        password = it.password.toString()
    }
    val (firstFocus, lastFocus, emailFocus, alamatFocus, passwodFocus) = FocusRequester.createRefs()
    val keyboard = LocalSoftwareKeyboardController.current
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Profil") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "",
                    )
                }
            },
            backgroundColor = Color.White
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (msgSuccess.isNotEmpty()) Card(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Column(Modifier.padding(16.dp)) { Text(text = msgSuccess) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = firstname,
                onValueChange = { firstname = it },
                label = { Text(text = "Nama Depan") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .focusOrder(firstFocus) { down = lastFocus },
                keyboardActions = KeyboardActions(onNext = { lastFocus.requestFocus() }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = lastname,
                onValueChange = { lastname = it },
                label = { Text(text = "Nama Belakang") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .focusOrder(lastFocus) { down = emailFocus },
                keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                singleLine = true,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .focusOrder(emailFocus) { down = alamatFocus },
                keyboardActions = KeyboardActions(onNext = { alamatFocus.requestFocus() }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = alamat,
                onValueChange = { alamat = it },
                label = { Text(text = "Alamat") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .focusOrder(alamatFocus) { down = passwodFocus },
                keyboardActions = KeyboardActions(onNext = { passwodFocus.requestFocus() }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .focusOrder(passwodFocus),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    val image = if (showPassword)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    IconButton(onClick = { showPassword = !showPassword })
                    { Icon(imageVector = image, "") }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    keyboard?.hide()
                    authViewModel.updateProfil(
                        User(
                            id = user?.id,
                            firstName = firstname,
                            lastName = lastname,
                            email = email,
                            alamat = alamat,
                            password = password
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .height(48.dp)
            ) {
                Text(text = "Simpan")
            }
        }
    }
}