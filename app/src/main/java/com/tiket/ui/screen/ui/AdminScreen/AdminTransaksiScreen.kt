package com.tiket.ui.screen.ui.AdminScreen
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.tiket.local.entity.Transaksi
import com.tiket.local.entity.Transportasi
import com.tiket.local.entity.User
import com.tiket.ui.screen.buttonColorsSecondary
import com.tiket.ui.screen.formatCurrency
import com.tiket.ui.screen.textFieldColors
import com.tiket.viewmodel.TransaksiVm
import com.tiket.viewmodel.TransaksiVmFactory
import com.tiket.viewmodel.TransportasiVm
import com.tiket.viewmodel.TransportasiVmFactory
import com.tiket.viewmodel.UserVm
import com.tiket.viewmodel.UserVmFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTransaksiUi(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onBackToHome: () -> Unit = {}
) {
    val transaksiVm: TransaksiVm = viewModel(factory = TransaksiVmFactory(context))
    val transportasiVm: TransportasiVm = viewModel(factory = TransportasiVmFactory(context))
    val userVm: UserVm = viewModel(factory = UserVmFactory(context))

    var searchQuery by remember { mutableStateOf("") }
    var selectedTransaksi by remember { mutableStateOf<Transaksi?>(null) }

    // Load data saat pertama kali dibuka
    LaunchedEffect(Unit) {
        transaksiVm.loadAll()
        transportasiVm.loadAll()
        userVm.loadAllUsers()
    }

    val transaksiList by transaksiVm.transaksiList.collectAsState()
    val transportasiList by transportasiVm.transportasiList.collectAsState()
    val userList by userVm.userList.collectAsState()

    val totalPendapatan = transaksiList.sumOf { it.totalHarga }
    val totalTransaksi = transaksiList.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackToHome) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                        Text(
                            "Laporan Transaksi",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
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
                // Summary Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Transaksi Card
                    SummaryCard(
                        title = "Total Transaksi",
                        value = totalTransaksi.toString(),
                        icon = Icons.Default.Receipt,
                        iconColor = Color(0xFF2196F3),
                        backgroundColor = Color(0xFFE3F2FD),
                        modifier = Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "Total Pendapatan",
                        value = formatCurrency(totalPendapatan),
                        icon = Icons.Default.Receipt,
                        iconColor = Color(0xFF4CAF50),
                        backgroundColor = Color(0xFFE8F5E8),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Section
                SearchSectionTransaksi(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onClear = {
                        searchQuery = ""
                        transaksiVm.loadAll()
                        Toast.makeText(context, "Pencarian direset", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (transaksiList.isEmpty()) {
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
                                contentDescription = "No Data",
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFF1E88E5)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Belum ada transaksi",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF666666)
                            )
                            Text(
                                "Transaksi akan muncul setelah user melakukan booking",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                } else {
                    // Filter transaksi berdasarkan search
                    val filteredTransaksi = if (searchQuery.isNotEmpty()) {
                        transaksiList.filter { transaksi ->
                            val user = userList.find { it.id == transaksi.userId }
                            val transportasi = transportasiList.find { it.id == transaksi.transportasiId }

                            transaksi.kodeBooking.contains(searchQuery, ignoreCase = true) ||
                                    user?.username?.contains(searchQuery, ignoreCase = true) == true ||
                                    transportasi?.nama?.contains(searchQuery, ignoreCase = true) == true
                        }
                    } else {
                        transaksiList
                    }

                    if (filteredTransaksi.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Tidak ditemukan transaksi yang sesuai",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF666666)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(filteredTransaksi) { transaksi ->
                                val user = userList.find { it.id == transaksi.userId }
                                val transportasi = transportasiList.find { it.id == transaksi.transportasiId }

                                TransaksiCard(
                                    transaksi = transaksi,
                                    user = user,
                                    transportasi = transportasi,
                                    onViewDetails = {
                                        selectedTransaksi = transaksi
                                    },
                                    onDelete = {
                                        transaksiVm.delete(transaksi)
                                        Toast.makeText(context, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Detail Dialog
        selectedTransaksi?.let { transaksi ->
            val user = userList.find { it.id == transaksi.userId }
            val transportasi = transportasiList.find { it.id == transaksi.transportasiId }

            TransaksiDetailDialog(
                transaksi = transaksi,
                user = user,
                transportasi = transportasi,
                onDismiss = { selectedTransaksi = null }
            )
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666),
                        fontSize = 14.sp
                    )
                    Text(
                        value,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF333333),
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(32.dp),
                    tint = iconColor
                )
            }
        }
    }
}

@Composable
fun SearchSectionTransaksi(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClear: () -> Unit
) {
    Card(
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
                "🔍 Pencarian Transaksi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color(0xFF1E88E5)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Cari kode booking, username, atau transportasi...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF1E88E5))
                },
                singleLine = true,
                colors = textFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onClear,
                    colors = buttonColorsSecondary()
                ) {
                    Text("Hapus Filter")
                }
            }
        }
    }
}

@Composable
fun TransaksiCard(
    transaksi: Transaksi,
    user: User?,
    transportasi: Transportasi?,
    onViewDetails: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header dengan kode booking dan total harga
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

            // Info User
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("👤 User", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E88E5))
                    Text(
                        user?.username ?: "User tidak ditemukan",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        user?.email ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Info Transportasi
            transportasi?.let { transport ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("🚗 Transportasi", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E88E5))
                        Text(
                            transport.nama,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "${transport.jenis} • ${transport.asal} → ${transport.tujuan}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF666666)
                        )
                    }
                }
            } ?: run {
                Text(
                    "Data transportasi tidak ditemukan",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF44336)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Detail Transaksi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("📦 Jumlah", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E88E5))
                    Text("${transaksi.jumlah} tiket")
                }
                Column {
                    Text("🆔 Transaksi ID", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E88E5))
                    Text("#${transaksi.id}")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onViewDetails,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = "View Details", tint = Color(0xFF2196F3))
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFF44336))
                }
            }
        }
    }
}

@Composable
fun TransaksiDetailDialog(
    transaksi: Transaksi,
    user: User?,
    transportasi: Transportasi?,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("📋 Detail Transaksi", color = Color(0xFF1E88E5))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Info Transaksi
                DetailRow("Kode Booking", transaksi.kodeBooking)
                DetailRow("Transaksi ID", "#${transaksi.id}")
                DetailRow("Jumlah Tiket", "${transaksi.jumlah} tiket")
                DetailRow("Total Harga", formatCurrency(transaksi.totalHarga))

                Spacer(modifier = Modifier.height(8.dp))

                // Info User
                Text("👤 Informasi User", style = MaterialTheme.typography.titleSmall, color = Color(0xFF1E88E5))
                DetailRow("Username", user?.username ?: "Tidak ditemukan")
                DetailRow("Email", user?.email ?: "Tidak ditemukan")
                DetailRow("User ID", user?.id?.toString() ?: "Tidak ditemukan")

                Spacer(modifier = Modifier.height(8.dp))

                // Info Transportasi
                Text("🚗 Informasi Transportasi", style = MaterialTheme.typography.titleSmall, color = Color(0xFF1E88E5))
                transportasi?.let { transport ->
                    DetailRow("Nama", transport.nama)
                    DetailRow("Jenis", transport.jenis)
                    DetailRow("Rute", "${transport.asal} → ${transport.tujuan}")
                    DetailRow("Harga per Tiket", formatCurrency(transport.harga))
                } ?: run {
                    Text("Data transportasi tidak ditemukan", color = Color(0xFFF44336))
                }
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium, color = Color(0xFF666666))
        Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
    }
}
