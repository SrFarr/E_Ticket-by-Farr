package com.tiket.ui.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiket.local.entity.Transportasi
import com.tiket.viewmodel.TransportasiVm
import com.tiket.viewmodel.TransportasiVmFactory
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPesawatUi(
    userId: Int,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onItemClick: (Transportasi) -> Unit = {}
) {
    val transportasiVm: TransportasiVm = viewModel(
        factory = TransportasiVmFactory(context)
    )

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        transportasiVm.loadAll()
    }

    // Filter hanya pesawat
    val pesawatList = transportasiVm.transportasiList.collectAsState().value.filter { it.jenis == "Pesawat" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Penerbangan Tersedia",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5)
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
                            Color(0xFFE3F2FD),
                            Color(0xFFBBDEFB)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Cari tujuan...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (pesawatList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.AirplanemodeActive,
                                contentDescription = "No Data",
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFF1E88E5)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Tidak ada penerbangan tersedia",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                } else {
                    // Filter berdasarkan search
                    val filteredList = if (searchQuery.isNotEmpty()) {
                        pesawatList.filter {
                            it.tujuan.contains(searchQuery, ignoreCase = true) ||
                                    it.asal.contains(searchQuery, ignoreCase = true)
                        }
                    } else {
                        pesawatList
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredList) { pesawat ->
                            PesawatCard(
                                pesawat = pesawat,
                                onClick = { onItemClick(pesawat) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PesawatCard(
    pesawat: Transportasi,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AirplanemodeActive,
                        contentDescription = "Pesawat",
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF2196F3)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            pesawat.nama,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Text(
                            formatCurrency(pesawat.harga),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF1E88E5),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("📍 Rute", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E88E5))
                    Text("${pesawat.asal} → ${pesawat.tujuan}",
                        fontWeight = FontWeight.Medium)
                }

                Column {
                    Text("⏰ Berangkat", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E88E5))
                    Text(formatDate(pesawat.waktuBerangkat),
                        fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}