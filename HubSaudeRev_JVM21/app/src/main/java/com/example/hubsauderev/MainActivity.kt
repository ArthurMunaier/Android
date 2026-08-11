package com.example.hubsauderev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// Enum que representa as telas disponíveis no aplicativo.
// Usamos esse enum para controlar a navegação sem precisar
// criar várias Activities.
enum class TelaSaude {
    MENU,
    CALCULAR_IMC,
    META_AGUA
}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // Estado que controla qual tela está sendo exibida.
            // O remember mantém o valor enquanto a composição estiver ativa.
            // O mutableStateOf faz a interface ser atualizada quando o valor mudar.
            var telaAtual by remember {
                mutableStateOf(TelaSaude.MENU)
            }

            // A navegação é feita através do estado telaAtual.
            // Não precisamos utilizar XML ou abrir uma nova Activity.
            when (telaAtual) {

                TelaSaude.MENU -> {
                    TelaMenu(
                        onNavegar = { destino ->

                            // Recebe a tela escolhida pelo usuário
                            // e atualiza o estado da navegação.
                            telaAtual = destino
                        }
                    )
                }

                TelaSaude.CALCULAR_IMC -> {
                    TelaIMC(
                        onVoltar = {

                            // Altera o estado para voltar ao menu principal.
                            telaAtual = TelaSaude.MENU
                        }
                    )
                }

                TelaSaude.META_AGUA -> {
                    TelaAgua(
                        onVoltar = {

                            // Altera o estado para voltar ao menu principal.
                            telaAtual = TelaSaude.MENU
                        }
                    )
                }
            }
        }
    }
}


// ---------------------------------------------------------
// TELA MENU
// ---------------------------------------------------------

@Composable
fun TelaMenu(
    // Callback responsável por avisar qual tela o usuário deseja acessar.
    onNavegar: (TelaSaude) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Hub de Saúde",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(40.dp))


        // Botão responsável por abrir a tela de cálculo do IMC.
        Button(
            onClick = {

                // Envia para a MainActivity a tela que deve ser aberta.
                onNavegar(TelaSaude.CALCULAR_IMC)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular IMC")
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Botão responsável por abrir a tela de meta de água.
        Button(
            onClick = {

                // Envia para a MainActivity a tela de água.
                onNavegar(TelaSaude.META_AGUA)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Meta de Água")
        }
    }
}


// ---------------------------------------------------------
// TELA IMC
// ---------------------------------------------------------

@Composable
fun TelaIMC(
    // Callback utilizado para retornar ao menu principal.
    onVoltar: () -> Unit
) {

    // Estado que armazena o peso digitado pelo usuário.
    // O valor inicial é uma String vazia.
    var peso by remember {
        mutableStateOf("")
    }

    // Estado que armazena a altura digitada pelo usuário.
    var altura by remember {
        mutableStateOf("")
    }

    // Estado que armazena o resultado do cálculo do IMC.
    // O valor inicial é 0.0.
    var resultado by remember {
        mutableStateOf(0.0)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Calculadora de IMC",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(24.dp))


        // Campo onde o usuário informa o peso em Kg.
        OutlinedTextField(
            value = peso,

            // Atualiza o estado peso sempre que o usuário digitar.
            onValueChange = {
                peso = it
            },

            label = {
                Text("Peso em Kg")
            },

            modifier = Modifier.fillMaxWidth(),

            // Abre o teclado numérico no celular.
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(modifier = Modifier.height(16.dp))


        // Campo onde o usuário informa a altura em metros.
        OutlinedTextField(
            value = altura,

            // Atualiza o estado altura sempre que o usuário digitar.
            onValueChange = {
                altura = it
            },

            label = {
                Text("Altura em Metros")
            },

            modifier = Modifier.fillMaxWidth(),

            // Abre o teclado numérico no celular.
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(modifier = Modifier.height(24.dp))


        // Botão responsável por realizar o cálculo do IMC.
        Button(
            onClick = {

                // toDoubleOrNull() tenta converter o texto para Double.
                // Caso o campo esteja vazio ou tenha um valor inválido,
                // o ?: 0.0 utiliza o valor 0.0 para evitar erro.
                val pesoNumerico = peso.toDoubleOrNull() ?: 0.0
                val alturaNumerica = altura.toDoubleOrNull() ?: 0.0


                // Fórmula do IMC:
                // IMC = Peso / (Altura * Altura)
                //
                // Também verificamos se a altura é maior que zero
                // para evitar uma divisão por zero.
                if (alturaNumerica > 0) {

                    resultado =
                        pesoNumerico / (alturaNumerica * alturaNumerica)

                } else {

                    resultado = 0.0
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular IMC")
        }

        Spacer(modifier = Modifier.height(24.dp))


        // %.2f faz o resultado aparecer com duas casas decimais.
        Text(
            text = "IMC: %.2f".format(resultado),
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))


        // Botão utilizado para voltar para o menu principal.
        Button(
            onClick = {

                // Executa o callback de voltar.
                onVoltar()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar")
        }
    }
}


// ---------------------------------------------------------
// TELA ÁGUA
// ---------------------------------------------------------

@Composable
fun TelaAgua(
    // Callback utilizado para retornar ao menu principal.
    onVoltar: () -> Unit
) {

    // Estado que controla a quantidade de copos consumidos.
    // O contador começa em 0.
    var copos by remember {
        mutableStateOf(0)
    }


    // Cada copo possui 250 ml.
    // Multiplicamos a quantidade de copos por 250
    // para descobrir o total de água ingerido.
    val totalMl = copos * 250


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Meta de Água",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(32.dp))


        // Mostra a quantidade de copos consumidos.
        Text(
            text = "Copos consumidos: $copos",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))


        // Mostra o total de água ingerido em ml.
        Text(
            text = "Total ingerido: ${totalMl} ml",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))


        // Botão utilizado para adicionar um copo de água.
        Button(
            onClick = {

                // A cada clique o contador aumenta em 1.
                copos++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Beber 1 Copo")
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Botão utilizado para voltar ao menu principal.
        Button(
            onClick = {

                // Executa o callback de voltar.
                onVoltar()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar")
        }
    }
}


// ---------------------------------------------------------
// PREVIEWS
// ---------------------------------------------------------

// O @Preview permite visualizar a tela no Android Studio
// sem precisar executar o aplicativo no emulador.

@Preview(showBackground = true)
@Composable
fun PreviewTelaMenu() {
    TelaMenu(onNavegar = {})
}


@Preview(showBackground = true)
@Composable
fun PreviewTelaIMC() {
    TelaIMC(onVoltar = {})
}


@Preview(showBackground = true)
@Composable
fun PreviewTelaAgua() {
    TelaAgua(onVoltar = {})
}