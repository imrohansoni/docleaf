package com.imrohansoni.docleaf.core.navigation


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.imrohansoni.docleaf.core.components.Icons
import com.imrohansoni.docleaf.features.account.ui.AccountScreen
import com.imrohansoni.docleaf.features.files.ui.FilesScreen
import com.imrohansoni.docleaf.features.home.ui.HomeScreen
import com.imrohansoni.docleaf.features.tools.ui.ToolsScreen
import kotlinx.serialization.Serializable


//enum class NavItem { HOME, FILES, TOOLS, ACCOUNT }
//
//@Composable
//fun BottomNavIcon(
//    modifier: Modifier = Modifier,
//    icon: AppIcon,
//    activeIcon: AppIcon,
//    selected: Boolean = false,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier
//            .fillMaxWidth()
//            .height(50.dp)
//            .clickable(
//                indication = null,
//                interactionSource = remember { MutableInteractionSource() }) {
//                onClick.invoke()
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            icon = if (selected) activeIcon else icon,
//            color = if (selected) null else Color.White,
//            size = 28.dp
//        )
//    }
//}
//
//@Composable
//fun BottomNavigationBar(
//    selectedNavItem: NavItem = NavItem.HOME,
//    onNavItemSelect: (NavItem) -> Unit,
//    onScanSelect: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.Transparent),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        BottomNavIcon(
//            modifier = Modifier.weight(1f),
//            activeIcon = Icons.HomeFill,
//            icon = Icons.Home,
//            selected = selectedNavItem == NavItem.HOME,
//            onClick = {
//                if (selectedNavItem != NavItem.HOME) {
//                    onNavItemSelect.invoke(NavItem.HOME)
//                }
//            }
//        )
//
//        BottomNavIcon(
//            modifier = Modifier.weight(1f),
//            activeIcon = Icons.FolderFill,
//            icon = Icons.Folder,
//            selected = selectedNavItem == NavItem.FILES,
//            onClick = {
//                if (selectedNavItem != NavItem.FILES) {
//                    onNavItemSelect.invoke(NavItem.FILES)
//                }
//            }
//        )
//
//        Box(
//            Modifier
//                .size(60.dp)
//                .clip(CircleShape)
//                .background(Color.Green)
//                .clickable {
//                    onScanSelect.invoke()
//                },
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                icon = Icons.Scan,
//                color = Color.White,
//                size = 32.dp
//            )
//        }
//
//
//        BottomNavIcon(
//            modifier = Modifier.weight(1f),
//            activeIcon = Icons.BoxFill,
//            icon = Icons.Box,
//            selected = selectedNavItem == NavItem.TOOLS,
//            onClick = {
//
//                if (selectedNavItem != NavItem.TOOLS) {
//                    onNavItemSelect.invoke(NavItem.TOOLS)
//                }
//
//            }
//        )
//
//
//        BottomNavIcon(
//            modifier = Modifier.weight(1f),
//            activeIcon = Icons.UserCircleFill,
//            icon = Icons.UserCircle,
//            selected = selectedNavItem == NavItem.ACCOUNT,
//            onClick = {
//                if (selectedNavItem != NavItem.ACCOUNT) {
//                    onNavItemSelect.invoke(NavItem.ACCOUNT)
//                }
//            }
//        )
//    }
//}


@Serializable
sealed class BottomNavigationScreen : NavKey {
    @Serializable
    object Home : BottomNavigationScreen(), NavKey

    @Serializable
    object Files : BottomNavigationScreen(), NavKey

    @Serializable
    object Tools : BottomNavigationScreen(), NavKey

    @Serializable
    object Account : BottomNavigationScreen(), NavKey
}

private fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    backstack: NavBackStack<NavKey>
) {

    val context = LocalContext.current


    val bottomBackstack = rememberNavBackStack(BottomNavigationScreen.Home)

    fun navigateBottom(screen: BottomNavigationScreen) {

        if (bottomBackstack.lastOrNull() == screen)
            return

        bottomBackstack.removeLastOrNull()
        bottomBackstack.add(screen)
    }

//    val selectedNavItem = when (bottomBackstack.lastOrNull()) {
//        BottomNavigationScreen.Home -> NavItem.HOME
//        BottomNavigationScreen.Files -> NavItem.FILES
//        BottomNavigationScreen.Tools -> NavItem.TOOLS
//        BottomNavigationScreen.Account -> NavItem.ACCOUNT
//        else -> NavItem.HOME
//    }

    var hasPermission by remember {
        mutableStateOf(
            hasCameraPermission(context)
        )
    }

    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasPermission = granted
            backstack.add(Screen.Camera)
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            NavDisplay(
                backStack = bottomBackstack,
                entryProvider = { key ->
                    when (key) {
                        BottomNavigationScreen.Home -> {
                            NavEntry(key) {
                                HomeScreen()
                            }
                        }

                        BottomNavigationScreen.Files -> {
                            NavEntry(key) {
                                FilesScreen()
                            }
                        }

                        BottomNavigationScreen.Tools -> {
                            NavEntry(key) {
                                ToolsScreen()
                            }
                        }

                        BottomNavigationScreen.Account -> {
                            NavEntry(key) {
                                AccountScreen()
                            }
                        }

                        else -> throw Exception("Invalid screen")
                    }
                }
            )
        }

//        BottomNavigationBar(
//            selectedNavItem = selectedNavItem,
//            onNavItemSelect = {
//
//                when (it) {
//
//                    NavItem.HOME ->
//                        navigateBottom(BottomNavigationScreen.Home)
//
//                    NavItem.FILES ->
//                        navigateBottom(BottomNavigationScreen.Files)
//
//                    NavItem.TOOLS ->
//                        navigateBottom(BottomNavigationScreen.Tools)
//
//                    NavItem.ACCOUNT ->
//                        navigateBottom(BottomNavigationScreen.Account)
//                }
//            },
//            onScanSelect = {
//                if (!hasPermission) {
//                    launcher.launch(Manifest.permission.CAMERA)
//                } else {
//                    backstack.add(Screen.Camera)
//                }
//            }
//        )

        Spacer(Modifier.height(24.dp))
    }
}

