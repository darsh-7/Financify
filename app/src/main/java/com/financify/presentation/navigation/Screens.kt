package com.financify.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.financify.presentation.utils.Constants.Companion.ANALYSIS_SCREEN
import com.financify.presentation.utils.Constants.Companion.HOME_SCREEN
import com.financify.presentation.utils.Constants.Companion.ISSUES_LIST_SCREEN
import com.financify.presentation.utils.Constants.Companion.NAME_ARGUMENT_KEY
import com.financify.presentation.utils.Constants.Companion.OWNER_ARGUMENT_KEY
import com.financify.presentation.utils.Constants.Companion.REPO_DETAILS_SCREEN
import com.financify.presentation.utils.Constants.Companion.REPO_LIST_SCREEN
import com.financify.presentation.utils.Constants.Companion.TEXT_ARGUMENT_KEY
import com.financify.presentation.utils.Constants.Companion.TEXT_RECOGNITION_RESULT_SCREEN
import com.financify.presentation.utils.Constants.Companion.TEXT_RECOGNITION_SCREEN
import java.net.URLEncoder

sealed class Screens(val route: String) {
    data object RepoListScreen: Screens(REPO_LIST_SCREEN)

    data object RepoDetailsScreen: Screens("$REPO_DETAILS_SCREEN/{$OWNER_ARGUMENT_KEY}/{$NAME_ARGUMENT_KEY}"){
        fun passOwnerAndName(owner:String, name:String):String{
            return "$REPO_DETAILS_SCREEN/$owner/$name"
        }
    }

    data object IssuesListScreen: Screens("$ISSUES_LIST_SCREEN/{$OWNER_ARGUMENT_KEY}/{$NAME_ARGUMENT_KEY}"){
        fun passOwnerAndNameIssue(owner:String, name:String):String{
            return "$ISSUES_LIST_SCREEN/$owner/$name"
        }
    }

    data object HomeScreen : Screens(HOME_SCREEN)
    data object AnalysisScreen : Screens(ANALYSIS_SCREEN)


    data object TextRecognitionScreen : Screens(TEXT_RECOGNITION_SCREEN)

    data object TextRecognitionResultScreen : Screens("$TEXT_RECOGNITION_RESULT_SCREEN/{$TEXT_ARGUMENT_KEY}") {
        fun passText(text: String): String {
            return "$TEXT_RECOGNITION_RESULT_SCREEN/${URLEncoder.encode(text, "UTF-8")}"
        }
    }
}

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = Screens.HomeScreen.route
            ),
            BottomNavigationItem(
                label = "Camera",
                icon = Icons.Filled.Star,
                route = Screens.TextRecognitionScreen.route
            ),
            BottomNavigationItem(
                label = "Analysis",
                icon = Icons.Filled.AccountCircle,
                route = Screens.AnalysisScreen.route
            ),
        )
    }
}