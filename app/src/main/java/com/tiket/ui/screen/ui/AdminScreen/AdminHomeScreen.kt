package com.tiket.ui.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplaneTicket
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.EmojiTransportation
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiket.viewmodel.TransaksiVm
import com.tiket.viewmodel.TransaksiVmFactory
import com.tiket.viewmodel.TransportasiVm
import com.tiket.viewmodel.TransportasiVmFactory
import com.tiket.viewmodel.UserVm
import com.tiket.viewmodel.UserVmFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeUI(
    context: Context,
    onUsersClick: () -> Unit = {},
    onTransportasiClick: () -> Unit = {},
    onTransaksiClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val transaksiVm: TransaksiVm = viewModel(
        factory = TransaksiVmFactory(context)
    )
    val transportasiVm: TransportasiVm = viewModel(
        factory = TransportasiVmFactory(context)
    )
    val userVm: UserVm = viewModel(
        factory = UserVmFactory(context)
    )

    val transaksiList by transaksiVm.transaksiList.collectAsState()
    val transportasiList by transportasiVm.transportasiList.collectAsState()
    val userList by userVm.userList.collectAsState()

    val transaksiCount = transaksiList.size
    val transportasiCount = transportasiList.size
    val userCount = userList.size

    // Load data saat screen pertama kali dibuka
    LaunchedEffect(Unit) {
        transaksiVm.loadAll()
        transportasiVm.loadAll()
        userVm.loadAllUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dashboard Admin",
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Text(
                        "Overview Sistem",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color(0xFF333333)
                    )
                }

                item {
                    // Row untuk cards statistik
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            title = "Total Users",
                            count = userCount,
                            icon = Icons.Default.Group,
                            iconColor = Color(0xFF4CAF50),
                            backgroundColor = Color(0xFFE8F5E8),
                            onClick = onUsersClick,
                            modifier = Modifier.weight(1f)
                        )

                        StatCard(
                            title = "Transportasi",
                            count = transportasiCount,
                            icon = Icons.Default.Flight,
                            iconColor = Color(0xFF2196F3),
                            backgroundColor = Color(0xFFE3F2FD),
                            onClick = onTransportasiClick,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    StatCard(
                        title = "Total Transaksi",
                        count = transaksiCount,
                        icon = Icons.Default.Receipt,
                        iconColor = Color(0xFF9C27B0),
                        backgroundColor = Color(0xFFF3E5F5),
                        onClick = onTransaksiClick,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Section Recent Activity atau informasi tambahan
                    Text(
                        "Ringkasan",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color(0xFF333333)
                    )

                    // Info cards tambahan
                    InfoCard(
                        title = "Manajemen Users",
                        description = "Kelola data pengguna sistem",
                        icon = Icons.Default.Person,
                        iconColor = Color(0xFF4CAF50),
                        onClick = {
                            onUsersClick()
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoCard(
                        title = "Manajemen Transportasi",
                        description = "Kelola data pesawat atau kereta dan jadwal",
                        icon = Icons.Default.AirplaneTicket,
                        iconColor = Color(0xFF2196F3),
                        onClick = {
                            onTransportasiClick()
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoCard(
                        title = "Laporan Transaksi",
                        description = "Lihat dan kelola semua transaksi",
                        icon = Icons.Default.Receipt,
                        iconColor = Color(0xFF9C27B0),
                        onClick = {
                            onTransaksiClick()
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoCard(
                        title = "Logout",
                        description = "Keluar dari akun admin",
                        icon = Icons.Default.Logout,
                        iconColor = Color(0xFFF44336),
                        onClick = {
                            onLogoutClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666),
                        fontSize = 14.sp
                    )
                    Text(
                        count.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF333333),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                }
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(40.dp),
                    tint = iconColor
                )
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable{
            onClick()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = iconColor
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF333333),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666),
                    fontSize = 12.sp
                )
            }
        }
    }
}
