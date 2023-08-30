package ru.sergey.smarthouse.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.twotone.Create
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import me.saket.swipe.rememberSwipeableActionsState
import ru.sergey.smarthouse.R
import ru.sergey.smarthouse.UseCase
import ru.sergey.smarthouse.base.common.LifeScreen
import ru.sergey.smarthouse.base.common.item_compose.BoxImageLoad
import ru.sergey.smarthouse.base.common.item_compose.DialogApp
import ru.sergey.smarthouse.base.common.item_compose.TextButtonApp
import ru.sergey.smarthouse.base.common.swipe_refresh.BoxSwipeRefresh
import ru.sergey.smarthouse.base.common.swipe_refresh.rememberSwipeRefreshState
import ru.sergey.smarthouse.base.extension.setNameNavArguments
import ru.sergey.smarthouse.base.theme.DimApp
import ru.sergey.smarthouse.base.theme.TextApp
import ru.sergey.smarthouse.base.theme.ThemeApp
import ru.sergey.smarthouse.data.api_client.ApiCamera
import ru.sergey.smarthouse.data.api_client.Client
import ru.sergey.smarthouse.data.db.DbWorker
import ru.sergey.smarthouse.models.api.Camera
import ru.sergey.smarthouse.models.api.Door

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
        val realm = DbWorker().getInstance()
        val case = UseCase(api = ApiCamera(client = Client()), realm = realm)
        val viewModel = MainViewModel(case, realm)

        val doors by viewModel.liveDataDoor.observeAsState()
        val cameras by viewModel.liveDataCamera.observeAsState()
        LifeScreen(onStart = {
            viewModel.initStartScreen(navController)
            viewModel.getDoors()
            viewModel.getCameras()
        })
        ContentMain(
            listDoors = doors ?: listOf(),
            listCameras = cameras ?: listOf(),
            refresh = {
                when (it) {
                    ScreenState.CAMERAS -> viewModel.getCameras()
                    ScreenState.DOORS   -> viewModel.getDoors()
                }
            },
            newValueDoor = viewModel::changeDoor,
            addFavorite = viewModel::changeCamera,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContentMain(
    listDoors: List<Door>,
    listCameras: List<Camera>,
    refresh: (ScreenState) -> Unit,
    newValueDoor: (Door) -> Unit,
    addFavorite: (Camera) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val stateList by remember { mutableStateOf(ScreenState.values()) }
    var stateScreen by remember { mutableStateOf(ScreenState.DOORS) }
    val scope = rememberCoroutineScope()
    val pagerState: PagerState = rememberPagerState()

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    val heightOffset = remember { 100f }
    var dynamicOffsetHeightPx by remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    var buttonWidthDp by remember { mutableStateOf(0.dp) }
    var offsetTargetDot by remember { mutableStateOf(0.dp) }
    val offsetDot by animateDpAsState(targetValue = offsetTargetDot, label = "")

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = dynamicOffsetHeightPx + delta
                dynamicOffsetHeightPx = newOffset.coerceIn(-heightOffset, 0f)
                return Offset.Zero
            }
        }
    }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000L)
            isRefreshing = false
        }
    }

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        stateScreen = ScreenState.getStateByPage(pagerState.currentPage)
        refresh.invoke(stateScreen)
    })

    Scaffold(
        topBar = {
            TopScreen(
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        BoxSwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection),
            indicatorPadding = innerPadding,
            state = swipeRefreshState,
            onRefresh = {
                refresh.invoke(stateScreen)
                isRefreshing = true
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
            ) {
                Surface(shadowElevation = DimApp.baseElevation) {
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
                                    stateScreen = item
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = item.getNameState()
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .offset(x = offsetDot)
                        .width(buttonWidthDp)
                        .height(DimApp.menuItemsHeight)
                        .background(ThemeApp.colors.primary)
                )
                HorizontalPager(
                    contentPadding = PaddingValues(0.dp),
                    verticalAlignment = Alignment.Top,
                    state = pagerState,
                    pageCount = stateList.size,

                    ) { page ->
                    when (page) {
                        0 -> {
                            ItemCamera(listCameras = listCameras, addFavorite = addFavorite)
                        }

                        1 -> {
                            ItemDoor(listDoors = listDoors, newValue = newValueDoor,
                                addFavorite = newValueDoor, )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreen(
    scrollBehavior: TopAppBarScrollBehavior,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(ThemeApp.colors.goodContent),
        colors = TopAppBarDefaults.smallTopAppBarColors(),
        title = {
            Text(
                text = TextApp.titleMainScreen,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun ItemDoor(
    listDoors: List<Door>,
    newValue: (Door) -> Unit,
    addFavorite: (Door) -> Unit,
) {
    var clickItem by remember { mutableStateOf<Door?>(null) }
    val isViewDialog by remember(clickItem) { mutableStateOf(clickItem != null) }

    if (isViewDialog)
        DialogApp(
            oldValue = clickItem?.name ?: "",
            newValue = { new ->
                clickItem?.let { newValue.invoke(it.copy(name = new)) }
            },
            onDismiss = { clickItem = null })

    LazyColumn(modifier = Modifier) {
        item {
            Spacer(modifier = Modifier.height(DimApp.screenPadding))
        }
        items(listDoors) { item ->
            var isFavorite by rememberSaveable { mutableStateOf(false) }
            var isEdit by rememberSaveable { mutableStateOf(false) }
            val snooze = SwipeAction(
                icon = rememberVectorPainter(Icons.TwoTone.Star),
                background = ThemeApp.colors.background,
                onSwipe = {
                    isFavorite = !isFavorite
                    addFavorite.invoke(item.copy(favorites = !(item.favorites ?: false)))
                },
                isUndo = isFavorite,
            )
            val archive = SwipeAction(
                icon = rememberVectorPainter(Icons.TwoTone.Create),
                background = ThemeApp.colors.background,
                onSwipe = {
                    isEdit = !isEdit
                    clickItem = item
                },
                isUndo = isEdit,
            )

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
                    state = rememberSwipeableActionsState()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        BoxImageLoad(
                            modifier = Modifier.fillMaxWidth(),
                            image = item.snapshot
                        ) {
                            if (item.snapshot != null) {
                                PlayButton()
                            }
                        }
                        Text(
                            modifier = Modifier
                                .padding(DimApp.screenPadding)
                                .fillMaxWidth(),
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
    addFavorite: (Camera) -> Unit
) {


    LazyColumn(modifier = Modifier) {
        item {
            Spacer(modifier = Modifier.height(DimApp.screenPadding))
        }
        items(listCameras) { item ->
            var isFavorite by rememberSaveable { mutableStateOf(false) }
            val snooze = SwipeAction(
                icon = rememberVectorPainter(Icons.TwoTone.Star),
                background = ThemeApp.colors.background,
                onSwipe = {
                    isFavorite = !isFavorite
                    addFavorite.invoke(item.copy(favorites = !(item.favorites ?: false)))
                },
                isUndo = isFavorite,
            )
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
                    endActions = listOf(snooze),
                    state = rememberSwipeableActionsState()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        BoxImageLoad(
                            modifier = Modifier.fillMaxWidth(),
                            image = item.snapshot
                        ) {
                            PlayButton()
                            if (item.snapshot != null) {
                                if (item.rec == true)
                                    Rec()
                                if (item.favorites == true)
                                    Favorite()
                            }
                        }
                        Text(
                            modifier = Modifier
                                .padding(DimApp.screenPadding)
                                .fillMaxWidth(),
                            text = item.name ?: ""
                        )
                    }
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
        DOORS   -> TextApp.textDoors
    }

    companion object {
        fun getStateByPage(page: Int) = when (page) {
            0 -> CAMERAS
            else -> DOORS
        }
    }
}


@Composable
fun BoxScope.PlayButton() {
    BoxImageLoad(
        image = Icon(
            Icons.Filled.PlayArrow,
            "",
            modifier = Modifier
                .size(
                    DimApp.iconSizeTouchStandard
                )
                .align(Alignment.Center)
        ),
    )

}

@Composable
fun BoxScope.Rec() {
    val painter =
        rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current).apply {
            size(Size.ORIGINAL)
            data(R.drawable.ic_rec)
            crossfade(true)
        }.build())

    BoxImageLoad(
        image = Icon(
            painter,
            "",
            modifier = Modifier
                .padding(top = DimApp.screenPadding, start = DimApp.screenPadding)
                .size(
                    DimApp.iconSizeStandard
                )
                .align(Alignment.TopStart),
            tint = ThemeApp.colors.attentionContent
        ),
    )
}

@Composable
fun BoxScope.Favorite() {
    val painter =
        rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current).apply {
            size(Size.ORIGINAL)
            data(R.drawable.ic_star)
            crossfade(true)
        }.build())

    BoxImageLoad(
        image = Icon(
            painter,
            "",
            modifier = Modifier
                .padding(top = DimApp.screenPadding, end = DimApp.screenPadding)
                .size(
                    DimApp.iconSizeStandard
                )
                .align(Alignment.TopEnd),
            tint = Color.Yellow
        ),
    )
}
