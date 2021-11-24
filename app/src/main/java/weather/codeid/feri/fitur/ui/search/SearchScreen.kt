package weather.codeid.feri.fitur.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import weather.codeid.feri.R
import weather.codeid.feri.fitur.viewmodels.WeatherViewModel
import weather.codeid.feri.utils.Constant
import weather.codeid.feri.utils.roundOffDecimal
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun searchScreen(
    navController: NavController,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    var text by remember {
        mutableStateOf("")
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
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Pencarian") },
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
        Column(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize(0.9f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        if (text.length > 2) weatherViewModel.search(text)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Cari cuaca kotamu...") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (isLoading) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterHorizontally),
                                color = Color.White,
                                strokeWidth = 5.dp
                            )
                        }
                    }
                }
                if (msgError.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(msgError)
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(onClick = {
                                weatherViewModel.search(text)
                            }) {
                                Text("Coba lagi")
                            }
                        }
                    }
                }
                if (!isLoading && msgError.isEmpty()) {
                    if (text.length > 2) {
                        data?.let {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = MaterialTheme.colors.primary
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    it.message?.let { Text(text = it) }
                                    it.name?.let {
                                        Text(text = it + ", " + data?.sys?.country.orEmpty())
                                    }
                                    it.weather?.getOrNull(0)?.let {
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = it.description.orEmpty()
                                                    .capitalize(Locale.current),
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
                                    it.wind?.let {
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
                                    it.main?.let {
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
                                    it.main?.let {
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
                                    it.clouds?.let {
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
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_group_12843),
                            contentDescription ="",
                            modifier = Modifier
                                .fillMaxSize(0.6f)
                                .align(Alignment.CenterHorizontally)
                                .weight(0.7f)
                        )
                    }
                }
            }
        }
    }
}