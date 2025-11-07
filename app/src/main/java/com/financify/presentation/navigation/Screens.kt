package com.financify.presentation.navigation

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
    
    data object TextRecognitionScreen : Screens(TEXT_RECOGNITION_SCREEN)

    data object TextRecognitionResultScreen : Screens("$TEXT_RECOGNITION_RESULT_SCREEN/{$TEXT_ARGUMENT_KEY}") {
        fun passText(text: String): String {
            return "$TEXT_RECOGNITION_RESULT_SCREEN/${URLEncoder.encode(text, "UTF-8")}"
        }
    }
}