package com.tiket.ui.screen.ui.UserScreen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiket.local.entity.Transaksi
import com.tiket.local.entity.Transportasi
import com.tiket.ui.screen.formatCurrency
import com.tiket.ui.screen.formatDate
import com.tiket.viewmodel.TransaksiVm
import com.tiket.viewmodel.TransaksiVmFactory
import com.tiket.viewmodel.TransportasiVm
import com.tiket.viewmodel.TransportasiVmFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHistoryBookingUi(
    modifier: Modifier = Modifier,
    userId: Int,
    context: Context = LocalContext.current,
    onBackClick: () -> Unit = {}
) {
    val transaksiVm: TransaksiVm = viewModel(factory = TransaksiVmFactory(context))
    val transportasiVm: TransportasiVm = viewModel(factory = TransportasiVmFactory(context))

    // Load data saat screen dibuka
    LaunchedEffect(userId) {
        transaksiVm.loadByUserId(userId)
        transportasiVm.loadAll()
    }

    val transaksiList by transaksiVm.transaksiList.collectAsState()
    val transportasiList by transportasiVm.transportasiList.collectAsState()

    // Filter transaksi berdasarkan userId
    val userTransactions = transaksiList.filter { it.userId == userId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "History Booking",
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
                // Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Total Booking",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666)
                            )
                            Text(
                                "${userTransactions.size} transaksi",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E88E5)
                            )
                        }
                        Icon(
                            Icons.Default.History,
                            contentDescription = "History",
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF1E88E5)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (userTransactions.isEmpty()) {
                    // Empty state
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
                                Icons.Default.Receipt,
                                contentDescription = "No History",
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFF1E88E5)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Belum ada history booking",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF666666)
                            )
                            Text(
                                "Booking tiket pesawat atau kereta terlebih dahulu",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(userTransactions) { transaksi ->
                            // Find transportasi data for this transaction
                            val transportasi = transportasiList.find { it.id == transaksi.transportasiId }

                            HistoryBookingCard(
                                transaksi = transaksi,
                                transportasi = transportasi
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryBookingCard(
    transaksi: Transaksi,
    transportasi: Transportasi?
) {
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
            // Header dengan kode booking
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Kode: ${transaksi.kodeBooking}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5)
                )
                Text(
                    formatCurrency(transaksi.totalHarga),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Info transportasi
            transportasi?.let { transport ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            transport.nama,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "${transport.jenis} • ${transport.asal} → ${transport.tujuan}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Detail transaksi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Jumlah Tiket",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1E88E5)
                        )
                        Text(
                            "${transaksi.jumlah} tiket",
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column {
                        Text(
                            "Status",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1E88E5)
                        )
                        Text(
                            "Berhasil",
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            } ?: run {
                // Jika data transportasi tidak ditemukan
                Text(
                    "Data transportasi tidak tersedia",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFF44336)
                )
            }
        }
    }
}