package ru.sergey.smarthouse.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.twotone.AccountBox
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material.icons.twotone.Call
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Create
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import me.saket.swipe.rememberSwipeableActionsState
import ru.sergey.smarthouse.base.common.LifeScreen
import ru.sergey.smarthouse.base.common.item_compose.BoxImageLoad
import ru.sergey.smarthouse.base.common.item_compose.TextButtonApp
import ru.sergey.smarthouse.base.common.logD
import ru.sergey.smarthouse.base.extension.setNameNavArguments
import ru.sergey.smarthouse.base.theme.TextApp
import ru.sergey.smarthouse.base.theme.ThemeApp
import ru.sergey.smarthouse.base.theme.DimApp
import ru.sergey.smarthouse.models.api.Camera
import ru.sergey.smarthouse.models.api.Door
import ru.sergey.smarthouse.utils.MOCK_CAMERAS
import ru.sergey.smarthouse.utils.MOCK_DOORS

class MainScreen(
    private val navBuilder: NavGraphBuilder,
    private val navController: NavHostController
) {
    companion object {
        private val routeName: String = MainScreen::class.java.simpleName + "/"
        val route = routeName.setNameNavArguments()

        fun route(host: NavHostController?) {
            host?.navigate(route)
        }
    }


    fun addRoute() = navBuilder.composable(route = route) { entry ->
        val viewModel = MainViewModel()
        LifeScreen(onStart = {
            viewModel.initStartScreen(navController)
        })
        ContentMain(
            listDoors = MOCK_DOORS,
            listCameras = MOCK_CAMERAS
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContentMain(
    listDoors: List<Door>,
    listCameras: List<Camera>,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val stateList by remember { mutableStateOf(ScreenState.values()) }
    var stateScreen by remember { mutableStateOf(ScreenState.DOORS) }
    var topPadding by remember { mutableStateOf(0.dp) }
    val scope = rememberCoroutineScope()
    val pagerState: PagerState = rememberPagerState()

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        stateScreen = ScreenState.getStateByPage(pagerState.currentPage)
    })
    Scaffold(
        topBar = {
            TopScreen(
                scrollBehavior = scrollBehavior,
                stateScreen = stateScreen,
                onChangeStateScreen = { stateScreen = it },
                onChangeHeight = { topPadding = it },
                animateScrollToPage = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        },
    ) { innerPadding ->
        HorizontalPager(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(0.dp),
            verticalAlignment = Alignment.Top,
            state = pagerState,
            pageCount = stateList.size,

            ) { page ->
            when (page) {
                0 -> {
                    ItemCamera(listCameras = listCameras)
                }

                1 -> {
                    ItemDoor(listDoors = listDoors)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    stateScreen: ScreenState,
    onChangeStateScreen: (ScreenState) -> Unit,
    onChangeHeight: (Dp) -> Unit,
    animateScrollToPage: (Int) -> Unit,
) {
    val density = LocalDensity.current
    var columnHeightDp by remember { mutableStateOf(0.dp) }
    var columnWidthDp by remember { mutableStateOf(0.dp) }
    var buttonWidthDp by remember { mutableStateOf(0.dp) }
    var offsetTargetDot by remember { mutableStateOf(0.dp) }
    val offsetDot by animateDpAsState(targetValue = offsetTargetDot, label = "")

    CenterAlignedTopAppBar(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .onGloballyPositioned { coordinates ->
                columnHeightDp = with(density) { coordinates.size.height.toDp() }
                columnWidthDp = with(density) { coordinates.size.width.toDp() }
                onChangeHeight(columnHeightDp)
            }
            .background(ThemeApp.colors.goodContent),
        colors = TopAppBarDefaults.smallTopAppBarColors(),
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(top = dimApp.screenPadding)
            ) {
//                Text(
//                    text = TextApp.titleMainScreen,
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center
//                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ScreenState.values().forEachIndexed { index, item ->
                        TextButtonApp(
                            modifier = Modifier
                                .weight(1f)
                                .onGloballyPositioned {
                                    if (stateScreen == item) {
                                        offsetTargetDot =
                                            with(density) { it.positionInWindow().x.toDp() }
                                    }
                                    buttonWidthDp = with(density) { it.size.width.toDp() }
                                },
                            onClick = {
                                onChangeStateScreen.invoke(item)
                                animateScrollToPage(index)
                            },
                            text = item.getNameState()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .offset(x = offsetDot-10.dp)
                        .width(buttonWidthDp)
                        .height(DimApp.menuItemsHeight)
                        .background(ThemeApp.colors.goodContent)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun ItemDoor(
    listDoors: List<Door>,
) {
    LazyColumn(modifier = Modifier) {
        item {
            Spacer(modifier = Modifier.height(DimApp.screenPadding))
        }
        items(listDoors) { item ->
            var isSnoozed by rememberSaveable { mutableStateOf(false) }
            var isArchived by rememberSaveable { mutableStateOf(false) }
            val snooze = SwipeAction(
                icon = rememberVectorPainter(Icons.TwoTone.Create),
                background = ThemeApp.colors.background,
                onSwipe = { isSnoozed = !isSnoozed },
                isUndo = isSnoozed,
            )
            val archive = SwipeAction(
                icon = rememberVectorPainter(Icons.TwoTone.Star),
                background = ThemeApp.colors.background,
                onSwipe = { isArchived =!isArchived  },
                isUndo = isArchived,
            )

            var state = rememberSwipeableActionsState()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding)
                    .padding(
                        bottom = DimApp.screenPadding
                    )
            ) {
                SwipeableActionsBox(
                    startActions = listOf(),
                    endActions = listOf(snooze, archive),
                    state = state
                ) {
                    Column(modifier = Modifier) {
                        BoxImageLoad(
                            modifier = Modifier.fillMaxWidth(),
                            image = item.snapshot
                        ) {
                            BoxImageLoad(
                                modifier = Modifier.align(Alignment.Center),
                                image = Icon(Icons.Filled.PlayArrow, "Floating action button.")
                            )
                        }
                        Text(
                            modifier = Modifier.padding(DimApp.screenPadding),
                            text = item.name ?: ""
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemCamera(
    listCameras: List<Camera>,
) {
    LazyColumn(modifier = Modifier) {
        item {
            Spacer(modifier = Modifier.height(DimApp.screenPadding))
        }
        items(listCameras) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding)
                    .padding(
                        bottom = DimApp.screenPadding
                    )
            ) {
                Column(modifier = Modifier) {
                    BoxImageLoad(
                        modifier = Modifier.fillMaxWidth(),
                        image = item.snapshot
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(DimApp.screenPadding),
                        text = item.name ?: ""
                    )
                }
            }
        }
    }
}

enum class ScreenState {
    CAMERAS,
    DOORS;

    fun getNameState() = when (this) {
        CAMERAS -> TextApp.textСameras
        DOORS -> TextApp.textDoors
    }

    companion object {
        fun getStateByPage(page: Int) = when (page) {
            0 -> CAMERAS
            else -> DOORS
        }
    }
}

//------------------

@Composable
private fun SwipeableBoxPreview(modifier: Modifier = Modifier) {
    var isSnoozed by rememberSaveable { mutableStateOf(false) }
    var isArchived by rememberSaveable { mutableStateOf(false) }
    var state = rememberSwipeableActionsState()

    val replyAll = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Call),
        background = Color.Perfume,
        onSwipe = { println("Reply swiped") },
        isUndo = false,
    )
    val snooze = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Create),
        background = Color.SeaBuckthorn,
        onSwipe = { isSnoozed  },
        isUndo = isSnoozed,
    )
    val archive = SwipeAction(
        icon = rememberVectorPainter(Icons.TwoTone.Star),
        background = Color.Fern,
        onSwipe = { isArchived },
        isUndo = isArchived,
    )

    SwipeableActionsBox(
        modifier = modifier,
        startActions = listOf(replyAll),
        endActions = listOf(snooze, archive),
        swipeThreshold = 40.dp,
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.surfaceColorAtElevation(40.dp),
        state = state
    ) {
        BatmanIpsumItem(
            isSnoozed = isSnoozed
        )
    }
}

@Composable
private fun BatmanIpsumItem(
    modifier: Modifier = Modifier,
    isSnoozed: Boolean
) {
    Row(
        modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .animateContentSize()
    ) {
        Box(
            Modifier
                .padding(top = 2.dp)
                .size(52.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )

        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "The Batman",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "Fear is a tool. When that light hits the sky, it’s not just a call. It’s a warning. For them.",
                style = MaterialTheme.typography.bodyMedium
            )

            if (isSnoozed) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(Color.SeaBuckthorn.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = "Snoozed until tomorrow",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

val Color.Companion.SeaBuckthorn get() = Color(0xFFF9A825)
val Color.Companion.Fern get() = Color(0xFF66BB6A)
val Color.Companion.Perfume get() = Color(0xFFD0BCFF)

@Preview
@Composable
fun PreviewTest() {
    SwipeableBoxPreview(
        Modifier
            .padding(DimApp.screenPadding)
            .fillMaxWidth()
            .height(DimApp.screenPadding * 5)
    )
}