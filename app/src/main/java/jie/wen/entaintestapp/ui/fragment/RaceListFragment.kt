package jie.wen.entaintestapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import jie.wen.entaintestapp.R
import jie.wen.entaintestapp.data.Constants.Companion.ONE_MINUTE_IN_SECOND
import jie.wen.entaintestapp.data.Constants.Companion.ONE_SECOND
import jie.wen.entaintestapp.data.enum_data.RaceCategory
import jie.wen.entaintestapp.model.dto.RaceSummaryDTO
import jie.wen.entaintestapp.ui.theme.EntainTestAppTheme
import jie.wen.entaintestapp.viewmodel.RaceListViewModel

@AndroidEntryPoint
class RaceListFragment: Fragment() {
    private lateinit var viewModel: RaceListViewModel

    companion object {
        fun newInstance() = RaceListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[RaceListViewModel::class.java]
        viewModel.fetchData(ONE_SECOND, ONE_MINUTE_IN_SECOND)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                EntainTestAppTheme(
                    content = {
                        FragmentContent(
                            viewModel
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun RaceListFragmentComponent(
    fragmentManager: FragmentManager,
    modifier: Modifier = Modifier,
    fragmentId: Int,
    fragment: Fragment
) {
    AndroidView(
        modifier = modifier.safeDrawingPadding(),
        factory = { context ->
            val frgId = FragmentContainerView(context).apply {
                id = fragmentId
            }

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(fragmentId, fragment, fragment.javaClass.simpleName)
            transaction.addToBackStack(null)
            transaction.commit()
            frgId
        },
        update = { }
    )
}

@Composable
fun FragmentContent(
    viewModel: RaceListViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    Column {
        Column (
            horizontalAlignment = Alignment.Start
        ) {
            FilterButton(RaceCategory.HORSE_RACING, viewModel)
            FilterButton(RaceCategory.HARNESS_RACING, viewModel)
            FilterButton(RaceCategory.GREYHOUND_RACING, viewModel)
            FilterButton(RaceCategory.ALL, viewModel)
        }

        RaceSummaryList(viewModel.uiState.raceList)
    }

    LaunchedEffect(viewModel, lifecycle) {

    }
}

@Composable
fun FilterButton(raceCategory: RaceCategory, viewModel: RaceListViewModel) {
    Button(
        onClick = {
            viewModel.selectedCategory = raceCategory
        },
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(),
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        ),
        interactionSource = remember {
            MutableInteractionSource()
        },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        )
    ) {
        Text(
            text = when(raceCategory) {
                RaceCategory.GREYHOUND_RACING -> stringResource(R.string.greyhound_racing)
                RaceCategory.HARNESS_RACING -> stringResource(R.string.harness_racing)
                RaceCategory.HORSE_RACING -> stringResource(R.string.hourse_racing)
                else -> stringResource(R.string.all)
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun RaceSummaryList(data: List<RaceSummaryDTO>?) {
    LazyColumn(
        Modifier.fillMaxWidth()
    ) {
        data?.let {
            itemsIndexed(data) { index, d ->
                ListRaceSummaryData(d)
                if (index != data.size - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ListRaceSummaryData(raceSummaryDTO: RaceSummaryDTO) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        RowLeft(raceSummaryDTO)
        RowRight(raceSummaryDTO)
    }
}

@Composable
fun RowLeft(raceSummaryDTO: RaceSummaryDTO) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = "${stringResource(R.string.meeting_name)} ${raceSummaryDTO.meetingName ?: ""}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(10.dp)
        )

        Text(
            text = "${stringResource(R.string.race_number)} ${raceSummaryDTO.raceNumber ?: ""}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun RowRight(raceSummaryDTO: RaceSummaryDTO) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = raceSummaryDTO.countDown ?: "",
            textAlign = TextAlign.End,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.align(Alignment.End)
                .fillMaxWidth()
        )
    }
}