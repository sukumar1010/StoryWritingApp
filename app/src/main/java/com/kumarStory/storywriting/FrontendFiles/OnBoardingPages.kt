package com.kumarStory.storywriting.FrontendFiles

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kumarStory.storywriting.R
import com.kumarStory.storywriting.SetStatusBarColor
import kotlinx.coroutines.launch


sealed class OnBoardingPage(

    val img : Int,

) {
    object First : OnBoardingPage(
        img = R.drawable.firstscreen,

    )

    object Second : OnBoardingPage(
        img = R.drawable.secondscreen3,

    )
    object Third : OnBoardingPage(
        img = R.drawable.thirdscreen,

        )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingCompose(
    onClick: () -> Unit
){

    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third
    )

    val pageState = rememberPagerState(pageCount = { 3 })
    SetStatusBarColor(color = Color.White, darkIcons = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,

            ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }

        FinishButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            pageState = pageState
        ) {
            onClick()
        }
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    SetStatusBarColor(color = Color.White, darkIcons = true)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = onBoardingPage.img),
            contentDescription = null,
            contentScale = ContentScale.Crop, // Ensures the image covers the entire screen
            modifier = Modifier.fillMaxSize()
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FinishButton(
    modifier: Modifier = Modifier,
    pageState: PagerState,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Show button only on the last page
    if (pageState.currentPage == pageState.pageCount - 1) {
        Button(
            onClick = onClick,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Finish")
        }
    } else {
        // Optional: Add "Next" button to navigate through pages
        Button(
            onClick = {
                coroutineScope.launch {
                    pageState.animateScrollToPage(pageState.currentPage + 1)
                }
            },
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Next")
        }
    }
}


