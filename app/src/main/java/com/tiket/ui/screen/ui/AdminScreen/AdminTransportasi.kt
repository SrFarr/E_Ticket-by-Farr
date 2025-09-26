package com.tiket.ui.screen

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiket.local.entity.Transportasi
import com.tiket.viewmodel.TransportasiVm
import com.tiket.viewmodel.TransportasiVmFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTransportasiUi(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onBackToHome: () -> Unit = {}
) {
    val transportasiVm: TransportasiVm = viewModel(
        factory = TransportasiVmFactory(context)
    )

    var showDialog by remember { mutableStateOf(false) }
    var selectedTransportasi by remember { mutableStateOf<Transportasi?>(null) }
    var searchTujuan by remember { mutableStateOf("") }
    var searchStartDate by remember { mutableStateOf("") }
    var searchEndDate by remember { mutableStateOf("") }

    val transportasiList by transportasiVm.transportasiList.collectAsState()

    LaunchedEffect(Unit) {
       transportasiVm.loadAll()
    }


    // Load data saat pertama kali dibuka
    LaunchedEffect(Unit) {
        println("Loading data transportasi...")
        transportasiVm.loadAll()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (verticalAlignment = Alignment.CenterVertically){
                        IconButton(
                            onClick = {
                                onBackToHome()
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                        Text(
                            "Manajemen Transportasi",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedTransportasi = null
                    showDialog = true
                },
                containerColor = Color(0xFF1E88E5),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Transportasi")
            }
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
                // Search Section
                SearchSection(
                    searchTujuan = searchTujuan,
                    onSearchTujuanChange = { searchTujuan = it },
                    searchStartDate = searchStartDate,
                    onSearchStartDateChange = { searchStartDate = it },
                    searchEndDate = searchEndDate,
                    onSearchEndDateChange = { searchEndDate = it },
                    onSearch = {
                        when {
                            searchTujuan.isNotEmpty() && searchStartDate.isNotEmpty() && searchEndDate.isNotEmpty() -> {
                                val startDate = parseDate(searchStartDate)
                                val endDate = parseDate(searchEndDate)
                                if (startDate != null && endDate != null) {
                                    transportasiVm.searchByTanggalAndTujuan(searchTujuan, startDate, endDate)
                                } else {
                                    Toast.makeText(context, "Format tanggal salah", Toast.LENGTH_SHORT).show()
                                }
                            }
                            searchTujuan.isNotEmpty() -> {
                                transportasiVm.searchByTujuan(searchTujuan)
                            }
                            searchStartDate.isNotEmpty() && searchEndDate.isNotEmpty() -> {
                                val startDate = parseDate(searchStartDate)
                                val endDate = parseDate(searchEndDate)
                                if (startDate != null && endDate != null) {
                                    transportasiVm.searchKeretaByTanggal(startDate, endDate)
                                } else {
                                    Toast.makeText(context, "Format tanggal salah", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else -> transportasiVm.loadAll()
                        }
                    },
                    onClear = {
                        searchTujuan = ""
                        searchStartDate = ""
                        searchEndDate = ""
                        transportasiVm.loadAll()
                        Toast.makeText(context, "Pencarian direset", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (transportasiList.isEmpty()) {
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
                                "Tidak ada data transportasi",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF666666)
                            )
                            Text(
                                "Klik tombol + untuk menambah data",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(transportasiList) { transportasi ->
                            TransportasiCard(
                                transportasi = transportasi,
                                onEdit = {
                                    selectedTransportasi = transportasi
                                    showDialog = true
                                },
                                onDelete = {
                                    transportasiVm.delete(transportasi)
                                    Toast.makeText(context, "Transportasi berhasil dihapus", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }

        // Add/Edit Dialog
        if (showDialog) {
            TransportasiDialog(
                transportasi = selectedTransportasi,
                onDismiss = { showDialog = false },
                onSave = { transportasi ->
                    try {
                        if (selectedTransportasi == null) {
                            transportasiVm.insert(transportasi)
                            Toast.makeText(context, "Transportasi berhasil ditambah", Toast.LENGTH_SHORT).show()
                        } else {
                            transportasiVm.update(transportasi)
                            Toast.makeText(context, "Transportasi berhasil diupdate", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun SearchSection(
    searchTujuan: String,
    onSearchTujuanChange: (String) -> Unit,
    searchStartDate: String,
    onSearchStartDateChange: (String) -> Unit,
    searchEndDate: String,
    onSearchEndDateChange: (String) -> Unit,
    onSearch: () -> Unit,
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
                "🔍 Pencarian Transportasi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color(0xFF1E88E5)
            )

            OutlinedTextField(
                value = searchTujuan,
                onValueChange = onSearchTujuanChange,
                label = { Text("Tujuan") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF1E88E5))
                },
                singleLine = true,
                colors = textFieldColors()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchStartDate,
                    onValueChange = onSearchStartDateChange,
                    label = { Text("Tanggal Mulai (dd/MM/yyyy)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = searchEndDate,
                    onValueChange = onSearchEndDateChange,
                    label = { Text("Tanggal Akhir (dd/MM/yyyy)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = textFieldColors()
                )
            }

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
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onSearch,
                    colors = buttonColorsPrimary()
                ) {
                    Text("Cari")
                }
            }
        }
    }
}

@Composable
fun TransportasiCard(
    transportasi: Transportasi,
    onEdit: () -> Unit,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (transportasi.jenis == "Pesawat") Icons.Default.AirplanemodeActive else Icons.Default.Train,
                        contentDescription = transportasi.jenis,
                        modifier = Modifier.size(32.dp),
                        tint = if (transportasi.jenis == "Pesawat") Color(0xFF2196F3) else Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            transportasi.nama,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Text(
                            "${transportasi.jenis} • ${formatCurrency(transportasi.harga)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666)
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF2196F3))
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFF44336))
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
                    Text("${transportasi.asal} → ${transportasi.tujuan}",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333))
                }

                Column {
                    Text("⏰ Keberangkatan", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E88E5))
                    Text(formatDate(transportasi.waktuBerangkat),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportasiDialog(
    transportasi: Transportasi?,
    onDismiss: () -> Unit,
    onSave: (Transportasi) -> Unit
) {
    var nama by remember { mutableStateOf(transportasi?.nama ?: "") }
    var jenis by remember { mutableStateOf(transportasi?.jenis ?: "Pesawat") }
    var maskapai by remember { mutableStateOf(transportasi?.maskapai ?: "") }
    var asal by remember { mutableStateOf(transportasi?.asal ?: "") }
    var tujuan by remember { mutableStateOf(transportasi?.tujuan ?: "") }
    var waktuBerangkat by remember { mutableStateOf(transportasi?.waktuBerangkat ?: System.currentTimeMillis()) }
    var harga by remember { mutableStateOf(transportasi?.harga?.toString() ?: "") }

    var expanded by remember { mutableStateOf(false) }
    val jenisOptions = listOf("Pesawat", "Kereta")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (transportasi == null) "✈️ Tambah Transportasi" else "✏️ Edit Transportasi",
                color = Color(0xFF1E88E5)
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Transportasi") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = textFieldColors()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = jenis,
                        onValueChange = {},
                        label = { Text("Jenis Transportasi") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        singleLine = true,
                        colors = textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        jenisOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    jenis = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = maskapai,
                    onValueChange = { maskapai = it },
                    label = { Text("Maskapai") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = textFieldColors()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = asal,
                        onValueChange = { asal = it },
                        label = { Text("Kota Asal") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = textFieldColors()
                    )

                    OutlinedTextField(
                        value = tujuan,
                        onValueChange = { tujuan = it },
                        label = { Text("Kota Tujuan") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = textFieldColors()
                    )
                }

                OutlinedTextField(
                    value = formatDate(waktuBerangkat),
                    onValueChange = { },
                    label = { Text("Waktu Berangkat") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    singleLine = true,
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = harga,
                    onValueChange = { harga = it },
                    label = { Text("Harga Tiket") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = textFieldColors()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val hargaValue = harga.toDoubleOrNull() ?: 0.0
                    val newTransportasi = Transportasi(
                        id = transportasi?.id ?: 0,
                        nama = nama,
                        maskapai = maskapai,
                        jenis = jenis,
                        asal = asal,
                        tujuan = tujuan,
                        waktuBerangkat = waktuBerangkat,
                        harga = hargaValue
                    )
                    onSave(newTransportasi)
                },
                enabled = nama.isNotEmpty() && asal.isNotEmpty() && tujuan.isNotEmpty() && harga.isNotEmpty(),
                colors = buttonColorsPrimary()
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = buttonColorsSecondary()
            ) {
                Text("Batal")
            }
        }
    )
}

// Helper functions untuk colors
@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF1E88E5),
    unfocusedBorderColor = Color(0xFFBDBDBD),
    focusedLabelColor = Color(0xFF1E88E5),
    cursorColor = Color(0xFF1E88E5)
)

@Composable
fun buttonColorsPrimary() = androidx.compose.material3.ButtonDefaults.buttonColors(
    containerColor = Color(0xFF1E88E5),
    contentColor = Color.White
)

@Composable
fun buttonColorsSecondary() = androidx.compose.material3.ButtonDefaults.buttonColors(
    containerColor = Color.Transparent,
    contentColor = Color(0xFF1E88E5)
)

// Helper functions
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatCurrency(amount: Double): String {
    return "Rp ${String.format("%,.0f", amount)}"
}

fun parseDate(dateString: String): Long? {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.parse(dateString)?.time
    } catch (e: Exception) {
        null
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminTransportasiPreview() {
    com.tiket.ui.screen.ui.theme.E_TicketPesawat_UTSTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF5F5F5)
        ) {
            AdminTransportasiUi()
        }
    }
}