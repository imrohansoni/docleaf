package com.imrohansoni.docleaf.features.onboarding.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.imrohansoni.docleaf.R
import com.imrohansoni.docleaf.core.navigation.Screen
import com.imrohansoni.docleaf.core.components.Button
import com.imrohansoni.docleaf.core.components.ButtonType
import kotlinx.coroutines.launch

data class OnboardingPage(
    val image: Int,
    val title: String,
    val description: String
)

val pages = listOf(
    OnboardingPage(
        image = R.drawable.onboarding_scan,
        title = "Scan Anything",
        description = "Scan any document and turn paper into crystal clear PDF"
    ),
    OnboardingPage(
        image = R.drawable.onboarding_edit,
        title = "Edit & Enhance",
        description = "Add text, signature, images and watermarks to your documents"
    ),
    OnboardingPage(
        image = R.drawable.onboarding_files,
        title = "Manager & Share",
        description = "Organise, manage & share your documents"
    ),
    OnboardingPage(
        image = R.drawable.onboarding_pdf_tools,
        title = "PDF Utilities",
        description = "Merge, Split, Compress, Add or remove password with ease"
    ),
)


@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    backstack: NavBackStack<NavKey>
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.weight(1f))
        HorizontalPager(
            state = pagerState
        ) { page ->

            OnboardingPageContent(
                page = pages[page]
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Indicator(
            pageCount = pages.size,
            currentPage = pagerState.currentPage
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                text = "SKIP",
                buttonType = ButtonType.SECONDARY,
                onClick = {
                    backstack.clear()
                    backstack.add(Screen.Main)
                })

            Button(
                modifier = Modifier.weight(1f),
                text = if (pagerState.currentPage == pages.lastIndex) "START" else "NEXT",
                onClick = {
                    if (pagerState.currentPage < pages.lastIndex) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1
                            )
                        }
                    } else {
                        backstack.clear()
                        backstack.add(Screen.Main)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}


@Composable
fun OnboardingPageContent(
    page: OnboardingPage
) {
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box(Modifier.height(50.dp), contentAlignment = Alignment.Center){
            BasicText(
                text = page.title,
                style = TextStyle(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold, FontWeight.Bold, FontStyle.Normal))
                )
            )
        }


        Spacer(modifier = Modifier.height(12.dp))

        BasicText(
            modifier = Modifier.width(240.dp),
            text = page.description,
            style = TextStyle(
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.inter_light,
                        FontWeight.Light,
                        FontStyle.Normal
                    )
                )
            )
        )
    }
}

@Composable
fun Indicator(
    pageCount: Int,
    currentPage: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(
                        if (index == currentPage)
                            10.dp
                        else
                            6.dp
                    )
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage)
                            Color(0xFF75D13D)
                        else
                            Color.DarkGray
                    )
            )
        }
    }
}