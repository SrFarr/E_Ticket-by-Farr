package com.tiket.navigation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tiket.local.entity.Transaksi
import com.tiket.local.entity.Transportasi
import com.tiket.ui.screen.*
import com.tiket.ui.screen.ui.UserScreen.*
import com.tiket.viewmodel.*

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val ADMIN_HOME = "admin_home/{userId}/{username}"
    const val USER_HOME = "user_home/{userId}/{username}"
    const val ADMIN_TRANSPORT = "adminTransport/{userId}/{username}"
    const val LIST_PESAWAT = "listPesawat/{userId}"
    const val DETAIL_PESAWAT = "detailPesawat/{pesawatId}/{userId}"
    const val DETAIL_BOOKING = "detailBooking/{transaksiId}/{transportId}/{userId}/{jumlah}/{totalHarga}"
    const val USER_HISTORY = "user_history/{userId}"
    const val LIST_KERETA = "listKereta/{userId}"
    const val DETAIL_KERETA = "detailKereta/{keretaId}/{userId}"
}

@Composable
fun AppNavHost(nc: NavHostController) {
    val context = LocalContext.current

    val authVm: AuthVm = viewModel(factory = AuthVmFactory(context))
    val transportasiVm: TransportasiVm = viewModel(factory = TransportasiVmFactory(context))
    val userVm: UserVm = viewModel(factory = UserVmFactory(context))
    val transaksiVm: TransaksiVm = viewModel(factory = TransaksiVmFactory(context))

    NavHost(
        navController = nc,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginUi(
                vm = authVm,
                onLoginAsAdmin = { user ->
                    val route = "admin_home/${user.id}/${user.username}"
                    nc.navigate(route) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onLoginAsUser = { user ->
                    val route = "user_home/${user.id}/${user.username}"
                    nc.navigate(route) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister = { nc.navigate(Routes.REGISTER) }
            )
        }

        // ---------------- REGISTER ----------------
        composable(Routes.REGISTER) {
            RegisterUi(
                vm = authVm,
                onGoToLogin = {
                    nc.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        // ---------------- ADMIN HOME ----------------
        composable(
            route = Routes.ADMIN_HOME,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val username = backStackEntry.arguments?.getString("username") ?: ""

            AdminHomeUI(
                context = context,
                onTransportasiClick = {
                    nc.navigate("adminTransport/$userId/$username")
                },
                onLogoutClick = {
                    authVm.logout()
                    nc.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ---------------- ADMIN TRANSPORT ----------------
        composable(
            route = Routes.ADMIN_TRANSPORT,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val username = backStackEntry.arguments?.getString("username") ?: ""

            AdminTransportasiUi(
                context = context,
                onBackToHome = {
                    nc.navigate("admin_home/$userId/$username") {
                        popUpTo("admin_home/$userId/$username") { inclusive = true }
                    }
                }
            )
        }


        // ---------------- USER HOME ----------------
        composable(
            route = Routes.USER_HOME,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("username") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val usernameFromArgs = backStackEntry.arguments?.getString("username") ?: ""

            LaunchedEffect(userId) {
                userVm.getUserById(userId)
            }
            val selectedUser by userVm.selectedUser.collectAsState()
            val finalUsername = selectedUser?.username ?: usernameFromArgs

            UserHomeUI(
                id = userId,
                username = finalUsername,
                onGoToPesawat = {
                    nc.navigate("listPesawat/$userId")
                },
                onGoToKereta = { nc.navigate("listKereta/$userId") },
                onLogout = {
                    authVm.logout()
                    nc.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onGoToBooking = {
                    nc.navigate("user_history/$userId")
                }
            )
        }

        composable(
            route = Routes.USER_HISTORY,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            UserHistoryBookingUi(
                userId = userId,
                onBackClick = {
                    // Kembali ke user home dengan data yang sama
                    val username = userVm.selectedUser.value?.username ?: "User"
                    nc.navigate("user_home/$userId/$username") {
                        popUpTo("user_home/$userId/$username") { inclusive = true }
                    }
                }
            )
        }
        // ---------------- LIST PESAWAT ----------------
        composable(
            route = Routes.LIST_PESAWAT,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            UserPesawatUi(
                userId = userId,
                onItemClick = { transportasi ->
                    nc.navigate("detailPesawat/${transportasi.id}/$userId")
                },
                context = context
            )
        }

        // ---------------- DETAIL PESAWAT ----------------
        composable(
            route = Routes.DETAIL_PESAWAT,
            arguments = listOf(
                navArgument("pesawatId") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val pesawatId = backStackEntry.arguments?.getInt("pesawatId") ?: 0
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            LaunchedEffect(pesawatId) {
                transportasiVm.loadAll()
            }
            val transportasiList by transportasiVm.transportasiList.collectAsState()
            val pesawat = transportasiList.find { it.id == pesawatId }

            if (pesawat != null) {
                UserDetailPesawatUi(
                    userId = userId,
                    pesawat = pesawat,
                    onBackClick = { nc.popBackStack() },
                    onBookingSuccess = { transaksiId, jumlah, totalHarga ->
                        nc.navigate("detailBooking/$transaksiId/${pesawat.id}/$userId/$jumlah/$totalHarga")
                    }
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Data pesawat tidak ditemukan")
                    Button(onClick = { nc.popBackStack() }) { Text("Kembali") }
                }
            }
        }

        // ---------------- LIST KERETA ----------------
        composable(Routes.LIST_KERETA,
            arguments = listOf(navArgument("userId"){ type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            UserKeretaUi(
                userId = userId,
                onItemClick = { kereta ->
                    nc.navigate("detailKereta/${kereta.id}/$userId")
                },
                context = context
            )
        }

        composable(Routes.DETAIL_KERETA,
            arguments = listOf(
                navArgument("keretaId"){ type = NavType.IntType },
                navArgument("userId"){ type = NavType.IntType }
            )
        ) { backStackEntry ->
            val keretaId = backStackEntry.arguments?.getInt("keretaId") ?: 0
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            // Tambahkan LaunchedEffect untuk load data
            LaunchedEffect(keretaId) {
                transportasiVm.loadAll()
            }
            val transportasiList by transportasiVm.transportasiList.collectAsState()

            // Pastikan filter jenis "Kereta" dan cari ID yang sesuai
            val kereta = transportasiList.find { it.id == keretaId && it.jenis == "Kereta" }

            if (kereta != null) {
                UserDetailKeretaUi(
                    userId = userId,
                    kereta = kereta,
                    onBackClick = { nc.popBackStack() },
                    onBookingSuccess = { transaksiId, jumlah, totalHarga ->
                        nc.navigate("detailBooking/$transaksiId/${kereta.id}/$userId/$jumlah/$totalHarga")
                    }
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Data kereta tidak ditemukan")
                    Text("ID: $keretaId")
                    Text("Total data: ${transportasiList.size}")
                    Button(onClick = { nc.popBackStack() }) { Text("Kembali") }
                }
            }
        }
        // ---------------- DETAIL BOOKING ----------------

// Dan di NavHost, ganti bagian DETAIL_BOOKING menjadi:
        composable(
            route = Routes.DETAIL_BOOKING,
            arguments = listOf(
                navArgument("transaksiId") { type = NavType.IntType },
                navArgument("transportId") { type = NavType.IntType }, // TRANSPORT ID YANG GENERIC
                navArgument("userId") { type = NavType.IntType },
                navArgument("jumlah") { type = NavType.IntType },
                navArgument("totalHarga") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val transaksiId = backStackEntry.arguments?.getInt("transaksiId") ?: 0
            val transportId = backStackEntry.arguments?.getInt("transportId") ?: 0
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val jumlah = backStackEntry.arguments?.getInt("jumlah") ?: 1
            val totalHarga = backStackEntry.arguments?.getFloat("totalHarga")?.toDouble() ?: 0.0

            LaunchedEffect(userId) { userVm.getUserById(userId) }
            val selectedUser by userVm.selectedUser.collectAsState()

            LaunchedEffect(transportId) { transportasiVm.loadAll() }
            val transportasiList by transportasiVm.transportasiList.collectAsState()
            val transport = transportasiList.find { it.id == transportId }

            if (transport != null) {
                LaunchedEffect(transaksiId) { transaksiVm.loadAll() }
                val transaksiList by transaksiVm.transaksiList.collectAsState()
                var transaksi = transaksiList.find { it.id == transaksiId }

                if (transaksi == null) {
                    transaksi = Transaksi(
                        id = transaksiId,
                        userId = userId,
                        transportasiId = transport.id,
                        jumlah = jumlah,
                        totalHarga = totalHarga,
                        kodeBooking = generateKodeBooking(),
                    )
                    transaksiVm.insert(transaksi)
                }

                DetailBookingUI(
                    userId = userId,
                    transaksi = transaksi,
                    transportasi = transport,
                    onHomeClick = {
                        val username = selectedUser?.username ?: "User"
                        nc.navigate("user_home/$userId/$username") {
                            popUpTo("user_home/$userId/$username") { inclusive = true }
                        }
                    }
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Data booking tidak ditemukan", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { nc.popBackStack() }) { Text("Kembali") }
                }
            }
        }
    }
}
