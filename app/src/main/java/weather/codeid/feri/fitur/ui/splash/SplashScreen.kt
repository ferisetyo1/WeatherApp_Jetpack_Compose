package weather.codeid.feri.fitur.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import weather.codeid.feri.fitur.viewmodels.AuthViewModel
import weather.codeid.feri.utils.Constant

@Composable
fun splashScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {
    val email = authViewModel.email.collectAsState(initial = "")
    LaunchedEffect(key1 = true){
        delay(1500)
        if (email.value.isEmpty()){
            navController.navigate(Constant.Route.loginScreen){
                popUpTo(Constant.Route.splashScreen){inclusive=true}
            }
        }else{
            navController.navigate(Constant.Route.homeScreen){
                popUpTo(Constant.Route.splashScreen){inclusive=true}
            }
        }
    }
    Scaffold {
        Box(modifier = Modifier.fillMaxSize()){
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 32.sp, fontWeight = FontWeight.W700)){ append("Weathers") }
                append(" by Feri")
            }, modifier = Modifier.align(Alignment.Center))
        }
    }
}

