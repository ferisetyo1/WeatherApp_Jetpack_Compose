package weather.codeid.feri.fitur.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
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
fun loginScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var showPassword by remember {
        mutableStateOf(false)
    }
    val (emailFocus, passwodFocus) = FocusRequester.createRefs()
    val keyboard = LocalSoftwareKeyboardController.current
    val isSuccess by remember {
        authViewModel.isSuccess
    }
    if (isSuccess) navController.navigate(Constant.Route.homeScreen) {
        popUpTo(Constant.Route.registerScreen) { inclusive = true }
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Hello, bro!",
                fontSize = 24.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(
                        Alignment.CenterHorizontally
                    )
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .focusOrder(emailFocus) { down = passwodFocus },
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
                    authViewModel.signin(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .height(48.dp)
            ) {
                Text(text = "Masuk")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = buildAnnotatedString {
                append("Belum punya akun? ")
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("daftar")
                }
            }, modifier = Modifier
                .clickable {
                    navController.navigate(Constant.Route.registerScreen) {
                        popUpTo(Constant.Route.registerScreen) { inclusive = true }
                    }
                }
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
            )
        }
    }
}