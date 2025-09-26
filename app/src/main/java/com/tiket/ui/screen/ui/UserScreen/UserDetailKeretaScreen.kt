package com.tiket.ui.screen.ui.UserScreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsRailway
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiket.local.entity.Transportasi
import com.tiket.ui.screen.formatCurrency
import com.tiket.ui.screen.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailKeretaUi(
    userId: Int,
    modifier: Modifier = Modifier,
    kereta: Transportasi,
    onBackClick: () -> Unit = {},
    onBookingSuccess: (Int, Int, Double) -> Unit
) {
    var jumlahTiket by remember { mutableStateOf("1") }
    var uangDibayar by remember { mutableStateOf("") }

    val jumlah = jumlahTiket.toIntOrNull() ?: 1
    val totalHarga = kereta.harga * jumlah
    val kembalian = uangDibayar.toDoubleOrNull()?.minus(totalHarga) ?: 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detail Perjalanan Kereta",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF388E3C) // Warna hijau untuk kereta
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE8F5E8),
                            Color(0xFFC8E6C9)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Detail Kereta
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.DirectionsRailway,
                                contentDescription = "Kereta",
                                modifier = Modifier.size(40.dp),
                                tint = Color(0xFF388E3C)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    kereta.nama,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )
                                Text(
                                    kereta.maskapai,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF666666)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("🚆 Jenis", style = MaterialTheme.typography.bodySmall, color = Color(0xFF388E3C))
                                Text(kereta.jenis, fontWeight = FontWeight.Medium)
                            }
                            Column {
                                Text("💰 Harga", style = MaterialTheme.typography.bodySmall, color = Color(0xFF388E3C))
                                Text(formatCurrency(kereta.harga), fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("📍 Stasiun Keberangkatan", style = MaterialTheme.typography.bodySmall, color = Color(0xFF388E3C))
                                Text(kereta.asal, fontWeight = FontWeight.Medium)
                            }
                            Column {
                                Text("🎯 Stasiun Tujuan", style = MaterialTheme.typography.bodySmall, color = Color(0xFF388E3C))
                                Text(kereta.tujuan, fontWeight = FontWeight.Medium)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("⏰ Waktu Berangkat", style = MaterialTheme.typography.bodySmall, color = Color(0xFF388E3C))
                        Text(formatDate(kereta.waktuBerangkat), fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Form Booking
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            "Form Pemesanan Tiket",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF388E3C),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = jumlahTiket,
                            onValueChange = { jumlahTiket = it },
                            label = { Text("Jumlah Tiket") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = uangDibayar,
                            onValueChange = { uangDibayar = it },
                            label = { Text("Uang Dibayar") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Summary
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE8F5E8), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Harga:", fontWeight = FontWeight.Medium)
                                Text(formatCurrency(totalHarga), fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Kembalian:", fontWeight = FontWeight.Medium)
                                Text(formatCurrency(kembalian),
                                    fontWeight = FontWeight.Bold,
                                    color = if (kembalian >= 0) Color(0xFF4CAF50) else Color(0xFFF44336))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val transaksiId = (1..1000).random()
                                onBookingSuccess(transaksiId, jumlah, totalHarga)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            enabled = uangDibayar.isNotEmpty() && kembalian >= 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF388E3C)
                            )
                        ) {
                            Text("Pesan Tiket Sekarang", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}