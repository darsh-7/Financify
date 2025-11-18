package com.financify.presentation.navigation

import com.financify.presentation.utils.Constants.Companion.ISSUES_LIST_SCREEN
import com.financify.presentation.utils.Constants.Companion.NAME_ARGUMENT_KEY
import com.financify.presentation.utils.Constants.Companion.OWNER_ARGUMENT_KEY
import com.financify.presentation.utils.Constants.Companion.REPO_DETAILS_SCREEN
import com.financify.presentation.utils.Constants.Companion.REPO_LIST_SCREEN
import com.financify.presentation.utils.Constants.Companion.TRANSACTION_LIST_SCREEN
import com.financify.presentation.utils.Constants.Companion.USER_ID_ARGUMENT_KEY


sealed class Screens(val route: String) {
    data object RepoListScreen: Screens(REPO_LIST_SCREEN)

    data object TransactionListScreen: Screens("$TRANSACTION_LIST_SCREEN/{$USER_ID_ARGUMENT_KEY}"){
        fun passUserId(userId: String): String {
            return "$TRANSACTION_LIST_SCREEN/$userId"
        }
    }

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
}