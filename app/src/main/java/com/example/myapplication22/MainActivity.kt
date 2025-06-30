package com.example.myapplication22

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication22.ui.theme.MyApplication22Theme
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplication22Theme {
                var serverResponse by remember { mutableStateOf("로딩 중...") }

                // 통신 요청 실행
                LaunchedEffect(Unit) {
                    serverResponse = fetchPingFromServer()
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = serverResponse,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // 백엔드 서버에서 ping 응답 받기
    private suspend fun fetchPingFromServer(): String = withContext(Dispatchers.IO) {
        try {
            val url = URL("http://10.0.2.2:5050/ping") // 에뮬레이터에서 로컬 호스트 접속
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            return@withContext if (connection.responseCode == 200) {
                connection.inputStream.bufferedReader().readText()
            } else {
                "서버 에러: ${connection.responseCode}"
            }
        } catch (e: Exception) {
            return@withContext "예외 발생: ${e.message}"
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "서버 응답: $name", modifier = modifier.padding(16.dp))
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplication22Theme {
        Greeting("pong")
    }
}
