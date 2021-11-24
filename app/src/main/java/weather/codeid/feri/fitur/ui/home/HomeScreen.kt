package weather.codeid.feri.fitur.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import weather.codeid.feri.fitur.viewmodels.AuthViewModel
import weather.codeid.feri.fitur.viewmodels.WeatherViewModel
import weather.codeid.feri.utils.Constant
import weather.codeid.feri.utils.roundOffDecimal
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalPermissionsApi
@Composable
fun homeScreen(navController: NavController) {
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }
    val loctpermission =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    val context = LocalContext.current
    val corutine = rememberCoroutineScope()
    Scaffold {
        when {
            loctpermission.allPermissionsGranted -> contentHomeScreen(navController = navController)
            loctpermission.shouldShowRationale || !loctpermission.permissionRequested -> {
                LaunchedEffect(key1 = true) {
                    corutine.launch {
                        loctpermission.launchMultiplePermissionRequest()
                    }
                }
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Anda tidak bisa melanjutkan karena belum diizinkan mengakses GPS, silahkan izinkan aplikasi sekarang."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(onClick = {
                            corutine.launch {
                                loctpermission.launchMultiplePermissionRequest()
                            }
                        }) {
                            Text("Minta Izin Sekarang")
                        }
                    }
                }
            }
            else -> {
                Card(
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Anda tidak bisa melanjutkan karena akses izin GPS anda tolak, silahkan izinkan aplikasi melalui pengaturan aplikasi."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(onClick = { context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS)) }) {
                            Text("Buka Pengaturan")
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun contentHomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    authViewModel.getUser()
    val user by remember {
        authViewModel.user
    }
    val context = LocalContext.current
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gpsloc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    val lat by remember {
        mutableStateOf(gpsloc?.latitude)
    }
    val lon by remember {
        mutableStateOf(gpsloc?.longitude)
    }
    LaunchedEffect(key1 = true) {
        weatherViewModel.getWeather(lat.toString(), lon.toString())
    }
    val msgError by remember {
        weatherViewModel.msgError
    }
    val isLoading by remember {
        weatherViewModel.isLoading
    }
    val data by remember {
        weatherViewModel.dataWeather
    }
    Column(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Selamat Datang,", fontSize = 20.sp)
            Text(
                text = user?.firstName.orEmpty().capitalize(Locale.current),
                fontSize = 24.sp,
                fontWeight = FontWeight.W700
            )
            Text(text = "(Tekan untuk melihat profil)", fontSize = 10.sp, modifier = Modifier.clickable { navController.navigate(Constant.Route.profilScreen) })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = "Cari cuaca kotamu...",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusable(false)
                    .clickable { navController.navigate(Constant.Route.searchScreen) },
                enabled = false
            )
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Column(Modifier.padding(16.dp)) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterHorizontally),
                            color = Color.White,
                            strokeWidth = 5.dp
                        )
                    }
                    if (msgError.isNotEmpty()) {
                        Text(msgError)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(onClick = {
                            weatherViewModel.getWeather(
                                lat.toString(),
                                lon.toString()
                            )
                        }) {
                            Text("Coba lagi")
                        }
                    }
                    if (!isLoading && msgError.isEmpty()) {
                        val simpleDateFormat =
                            SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                        Text(text = simpleDateFormat.format(Date()), fontSize = 11.sp)
                        Text(text = "informasi cuaca hari ini : ", fontWeight = FontWeight.W600)
                        data?.weather?.getOrNull(0)?.let {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = it.description.orEmpty().capitalize(Locale.current),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.W700,
                                )
                                it.icon?.let {
                                    Image(
                                        painter = rememberImagePainter(
                                            Constant.getImageOpenWeatherMap(it)
                                        ),
                                        contentDescription = "",
                                        modifier = Modifier.size(75.dp)
                                    )
                                }
                            }
                        }
                        data?.wind?.let {
                            it.speed?.let {
                                Row {
                                    Icon(
                                        imageVector = Icons.Outlined.Navigation,
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "${it}m/s S")
                                }
                            }
                        }
                        data?.main?.let {
                            it.temp?.let {
                                Row {
                                    Icon(
                                        imageVector = Icons.Outlined.Thermostat,
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "${(it - 273.15).roundOffDecimal()}°C")
                                }
                            }
                        }
                        data?.main?.let {
                            it.humidity?.let {
                                Row {
                                    Icon(
                                        imageVector = Icons.Outlined.Opacity,
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "${it}hPa")
                                }
                            }
                        }
                        data?.clouds?.let {
                            it.all?.let {
                                Row {
                                    Icon(
                                        imageVector = Icons.Outlined.Cloud,
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "${it} awan terlihat")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}