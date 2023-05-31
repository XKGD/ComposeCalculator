package fr.xkgd.composecalculator

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.xkgd.composecalculator.ui.calculator.Calculator
import fr.xkgd.composecalculator.ui.theme.ComposeCalculatorTheme
import fr.xkgd.composecalculator.ui.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            ComposeCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel by viewModels<CalculatorViewModel>()
                    val state = viewModel.state.collectAsState()
                    Column(
                        Modifier.width(50.dp)
                    ) {
                        Calculator(
                            state = state.value,
                            onAction = viewModel::onAction
                        )
                    }
                }
            }
        }
    }
}