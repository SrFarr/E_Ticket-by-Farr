package com.tiket.ui.screen.ui.AdminScreen

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiket.local.entity.User
import com.tiket.ui.screen.buttonColorsPrimary
import com.tiket.ui.screen.buttonColorsSecondary
import com.tiket.ui.screen.textFieldColors
import com.tiket.viewmodel.UserVm
import com.tiket.viewmodel.UserVmFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserUi(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onBackToHome: () -> Unit = {}
) {
    val userVm: UserVm = viewModel(
        factory = UserVmFactory(context)
    )

    var showDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val userList by userVm.userList.collectAsState()

    // Load data saat pertama kali dibuka
    LaunchedEffect(Unit) {
        println("Loading data user...")
        userVm.loadAllUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onBackToHome() }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                        Text(
                            "Manajemen User",
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
                    selectedUser = null
                    showDialog = true
                },
                containerColor = Color(0xFF1E88E5),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah User")
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
                SearchSectionUser(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onSearch = {
                        // Implement search logic if needed
                    },
                    onClear = {
                        searchQuery = ""
                        userVm.loadAllUsers()
                        Toast.makeText(context, "Pencarian direset", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (userList.isEmpty()) {
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
                                Icons.Default.Person,
                                contentDescription = "No Data",
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFF1E88E5)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Tidak ada data user",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF666666)
                            )
                            Text(
                                "Klik tombol + untuk menambah user",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                } else {
                    // Filter berdasarkan search
                    val filteredList = if (searchQuery.isNotEmpty()) {
                        userList.filter {
                            it.username.contains(searchQuery, ignoreCase = true) ||
                                    it.email.contains(searchQuery, ignoreCase = true)
                        }
                    } else {
                        userList
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredList) { user ->
                            UserCard(
                                user = user,
                                onEdit = {
                                    selectedUser = user
                                    showDialog = true
                                },
                                onDelete = {
                                    userVm.deleteUser(user)
                                    Toast.makeText(context, "User berhasil dihapus", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }

        // Add/Edit Dialog
        if (showDialog) {
            UserDialog(
                user = selectedUser,
                onDismiss = { showDialog = false },
                onSave = { user ->
                    try {
                        if (selectedUser == null) {
                            userVm.insertUser(user)
                            Toast.makeText(context, "User berhasil ditambah", Toast.LENGTH_SHORT).show()
                        } else {
                            userVm.updateUser(user)
                            Toast.makeText(context, "User berhasil diupdate", Toast.LENGTH_SHORT).show()
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
fun SearchSectionUser(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
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
                "🔍 Pencarian User",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color(0xFF1E88E5)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Cari username atau email...") },
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
fun UserCard(
    user: User,
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
                        imageVector = if (user.isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                        contentDescription = "User Type",
                        modifier = Modifier.size(32.dp),
                        tint = if (user.isAdmin) Color(0xFFF57C00) else Color(0xFF2196F3)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            user.username,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Text(
                            if (user.isAdmin) "Administrator" else "Regular User",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (user.isAdmin) Color(0xFFF57C00) else Color(0xFF666666)
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
                    // Prevent admin from deleting their own account
                    if (user.id != 1) { // Assuming admin has ID 1
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFF44336))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Text("📧 Email", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E88E5))
                Text(user.email, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(8.dp))

                Text("🆔 User ID", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1E88E5))
                Text("ID: ${user.id}", fontWeight = FontWeight.Medium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDialog(
    user: User?,
    onDismiss: () -> Unit,
    onSave: (User) -> Unit
) {
    var username by remember { mutableStateOf(user?.username ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(user?.isAdmin ?: false) }
    var showPassword by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (user == null) "👤 Tambah User" else "✏️ Edit User",
                color = Color(0xFF1E88E5)
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(if (user == null) "Password" else "Password Baru (kosongkan jika tidak diubah)") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Default.Visibility else Icons.Default.Visibility,
                                contentDescription = if (showPassword) "Hide password" else "Show password"
                            )
                        }
                    },
                    singleLine = true,
                    colors = textFieldColors()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAdmin,
                        onCheckedChange = { isAdmin = it }
                    )
                    Text(
                        "Administrator",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                if (user != null) {
                    Text(
                        "⚠️ Kosongkan password jika tidak ingin mengubahnya",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalPassword = if (password.isNotEmpty()) password else user?.password ?: ""
                    val newUser = User(
                        id = user?.id ?: 0,
                        username = username,
                        email = email,
                        password = finalPassword,
                        isAdmin = isAdmin
                    )
                    onSave(newUser)
                },
                enabled = username.isNotEmpty() && email.isNotEmpty() && (user != null || password.isNotEmpty()),
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