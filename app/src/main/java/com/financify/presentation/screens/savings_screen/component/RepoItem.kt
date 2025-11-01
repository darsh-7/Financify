package com.financify.presentation.screens.savings_screen.component

import androidx.compose.runtime.Composable

@Composable
fun RepoItem(
//    githubRepoUiModel: GithubReposUiModel,
    onRepoItemClicked: (String, String) -> Unit
) {
//    val imageCrossFadeAnimationDuration = 1000
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 16.dp)
//            .background(
//                color = MaterialTheme.colorScheme.surface,
//                shape = MaterialTheme.shapes.medium
//            )
//            .clickable {
//                onRepoItemClicked(githubRepoUiModel.ownerName, githubRepoUiModel.name)
//            }
//    ) {
//        Image(
//            painter = rememberAsyncImagePainter(
//                ImageRequest.Builder(LocalContext.current)
//                    .data(data = githubRepoUiModel.avatarUrl).apply {
//                        crossfade(imageCrossFadeAnimationDuration)
//                        placeholder(R.drawable.ic_github_placeholser)
//                        error(R.drawable.ic_github_placeholser)
//                    }.build()
//            ),
//            contentDescription = null,
//            modifier = Modifier
//                .padding(top = 12.dp, start = 12.dp)
//                .size(50.dp)
//                .clip(CircleShape)
//        )
//
//        Column(
//            Modifier.padding(12.dp)
//        ) {
//            Row {
//                Text(text = githubRepoUiModel.name, modifier = Modifier.weight(1f))
//                Text(text =  githubRepoUiModel.starsCount)
//                Icon(
//                    painter = painterResource(R.drawable.ic_star),
//                    contentDescription = null,
//                    tint = Color.Yellow,
//                    modifier = Modifier.padding(horizontal = 8.dp)
//                )
//            }
//
//            Text(githubRepoUiModel.ownerName, color = MaterialTheme.colorScheme.onSurface)
//            Text(
//                githubRepoUiModel.description,
//                color = MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier.padding(top = 12.dp),
//                maxLines = 3,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//
//    }
}

