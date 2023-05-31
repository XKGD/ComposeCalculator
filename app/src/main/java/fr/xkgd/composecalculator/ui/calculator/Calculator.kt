package fr.xkgd.composecalculator.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import fr.xkgd.composecalculator.ui.theme.Orange

@Composable
fun Calculator(
    state: String,
    onAction: (CalculatorAction) -> Unit
) {
    val btnShape = 10
    val padding = 3.dp

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        val (text, calculator) = createRefs()
        val scrollState = rememberScrollState()

        LaunchedEffect(state) {
            scrollState.scrollTo(scrollState.maxValue)
        }

        Box(
            modifier = Modifier
                .constrainAs(text) {
                    bottom.linkTo(calculator.top)
                }
                .fillMaxSize()
                .horizontalScroll(scrollState),
            contentAlignment = Alignment.BottomEnd
        ) {

            Text(
                text = state,
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 70.sp,
                    textAlign = TextAlign.End,
                    lineHeight = 70.sp
                ),
                maxLines = 1
            )
        }

        Box(
            modifier = Modifier.constrainAs(calculator) {
                bottom.linkTo(parent.bottom)
            },
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(padding),
                horizontalArrangement = Arrangement.spacedBy(padding),
                userScrollEnabled = false
            ) {
                item(
                    span = { GridItemSpan(2) }
                ) {
                    CalculatorBtn(
                        text = "AC",
                        bgc = Color.Gray,
                        topStartPercent = btnShape,
                        bottomStartPercent = btnShape,
                        isLarge = true,
                        onClick = {
                            onAction(CalculatorAction.Clear)
                        }
                    )
                }
                item {
                    CalculatorBtn(
                        text = "Del",
                        bgc = Color.Gray,
                        topEndPercent = btnShape,
                        bottomEndPercent = btnShape,
                        onClick = {
                            onAction(CalculatorAction.Delete)
                        }
                    )
                }
                item {
                    CalculatorBtn(
                        text = "÷",
                        bgc = Orange,
                        topStartPercent = btnShape,
                        topEndPercent = btnShape,
                        onClick = {
                            onAction(CalculatorAction.Operation(CalculatorOperation.Divide))
                        }
                    )
                }
                items(3) {item ->
                    val value = item + 7
                    CalculatorBtn(
                        text = value.toString(),
                        topStartPercent = if (value == 7) btnShape else 0,
                        topEndPercent = if (value == 9) btnShape else 0,
                        onClick = {
                            onAction(CalculatorAction.Number(value))
                        }
                    )
                }
                item {
                    CalculatorBtn(text = "x", bgc = Orange, onClick = {
                        onAction(CalculatorAction.Operation(CalculatorOperation.Multiply))
                    })
                }
                items(3) {item ->
                    val value = item + 4
                    CalculatorBtn(text = value.toString(), onClick = {
                        onAction(CalculatorAction.Number(value))
                    })
                }
                item {
                    CalculatorBtn(text = "-", bgc = Orange, onClick = {
                        onAction(CalculatorAction.Operation(CalculatorOperation.Subtract))
                    })
                }
                items(3) {item ->
                    val value = item + 1
                    CalculatorBtn(
                        text = value.toString(),
                        onClick = {
                            onAction(CalculatorAction.Number(value))
                        }
                    )
                }
                item {
                    CalculatorBtn(text = "+", bgc = Orange, onClick = {
                        onAction(CalculatorAction.Operation(CalculatorOperation.Add))
                    })
                }
                item(
                    span = { GridItemSpan(2) }
                ) {
                    CalculatorBtn(
                        text = "0",
                        bottomStartPercent = btnShape,
                        isLarge = true,
                        onClick = {
                            onAction(CalculatorAction.Number(0))
                        }
                    )
                }
                item {
                    CalculatorBtn(text = ".",
                        bottomEndPercent = btnShape,
                        onClick = {
                            onAction(CalculatorAction.Decimal)
                        }
                    )
                }
                item {
                    CalculatorBtn(
                        text = "=",
                        bgc = Orange,
                        bottomStartPercent = btnShape,
                        bottomEndPercent = btnShape,
                        onClick = {
                            onAction(CalculatorAction.Calculate)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorBtn(
    text: String,
    bgc: Color = Color.DarkGray,
    topStartPercent: Int = 0,
    topEndPercent: Int = 0,
    bottomEndPercent: Int = 0,
    bottomStartPercent: Int = 0,
    isLarge: Boolean = false,
    onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStartPercent,
                    topEndPercent,
                    bottomEndPercent,
                    bottomStartPercent
                )
            )
            .background(bgc)
            .aspectRatio(if (isLarge) 2.04f else 1f)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 40.sp
        )
    }
}