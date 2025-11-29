# 🌿 Simulador de Ecossistema (Predador vs. Presa)

Este projeto é uma simulação complexa de um ecossistema baseada em agentes, desenvolvida em Java. O sistema modela a interação entre diversas espécies de predadores e presas, influenciadas por fatores ambientais como clima, crescimento de vegetação e topografia do terreno.

Desenvolvido como parte da disciplina de Programação Orientada a Objetos pelo **Grupo 10**.

## 🚀 Funcionalidades Principais

* **Cadeia Alimentar Estendida:** Suporte para 6 espécies distintas (Herbívoros e Predadores) com dietas específicas.
* **Sistema Climático Dinâmico:** Ciclos de chuva e tempo normal que afetam diretamente a taxa de regeneração da vegetação.
* **Terreno Personalizável:** Carregamento de mapas via arquivo de texto (`mapa.txt`), permitindo a inclusão de obstáculos como Rios e Pedras.
* **Comportamentos Avançados:**
    * **Ursos** possuem a habilidade de pescar em rios adjacentes.
    * **Herbívoros** consomem vegetação que cresce e amadurece com o tempo.
    * Predadores caçam ativamente baseados em um sistema de dieta.
* **Interface Gráfica Interativa (Swing):** Visualização em tempo real da grade de simulação, contendo:
    * Legenda dinâmica com contagem populacional.
    * Indicador de estado climático.
    * Controles de execução (Pausar/Continuar, Reiniciar).

## 🦁 Espécies e Cadeia Alimentar

As interações biológicas são definidas na classe `Configuracao` e nas implementações individuais de cada animal.

| Espécie | Tipo | Dieta / Fonte de Alimento | Características | Cor |
| :--- | :--- | :--- | :--- | :--- |
| **Rato** | Herbívoro | Vegetação (Grama) | Reprodução alta, vida curta | 🟣 Magenta |
| **Coelho** | Herbívoro | Vegetação (Grama) | Reprodução média | 🟠 Laranja |
| **Raposa** | Predador | Coelho, Rato | Caçador versátil | 🔵 Azul |
| **Cobra** | Predador | Coelho, Rato | Predador rastejante | 🟢 Verde |
| **Gavião** | Predador | Rato | Predador focado em presas pequenas | 🔴 Vermelho |
| **Urso** | Predador | Raposa, Cobra, Coelho + **Peixes** | Pode pescar em Rios (30% chance) | ⚫ Preto |

## 🌧️ Ambiente e Clima

O ambiente de simulação é composto por uma grade de células (`Campo`) onde cada posição pode conter um animal, um obstáculo ou estar vazia.

1.  **Vegetação:** As células vazias contêm grama que cresce gradualmente. Animais herbívoros só podem se alimentar quando a grama atinge o estágio de maturação máxima.
2.  **Clima (Chuva):** O sistema alterna aleatoriamente entre "Normal" e "Chuvoso". Durante o clima chuvoso, a taxa de crescimento da vegetação é acelerada (cresce duas vezes por passo).
3.  **Obstáculos:**
    * **Pedra (`P`):** Bloqueia o movimento e impede o crescimento de grama.
    * **Rio (`R`):** Bloqueia o movimento terrestre, mas serve como fonte de alimento para Ursos.

## 🛠️ Instalação e Execução

### Pré-requisitos
* Java Development Kit (JDK) 8 ou superior.

### Compilando o Projeto
Abra o terminal na pasta raiz do projeto e compile todos os arquivos `.java`:

### Executando a Simulação
Para iniciar o simulador, execute a classe `Principal`:

### 🗺️ Configuração do Mapa (mapa.txt)
O simulador procura por um arquivo chamado `mapa.txt` na raiz do projeto para definir o terreno inicial. Se o arquivo não for encontrado, um campo vazio padrão será criado.

Você pode desenhar o mapa usando os seguintes caracteres:
* R = Rio (Onde ursos podem pescar)
* P = Pedra (Bloqueio Total)
* . ou espaço = Terreno livre (Vegetação)

### 🏗️ Estrutura do Código (MVC)
O projeto foi refatorado para seguir boas práticas de Orientação a Objetos:

* Model (Lógica): Simulador, Campo, Ator, Animal (e subclasses), Vegetacao, Clima.
* View (Interface): VisualizacaoSimulador, Desenhavel.
* Utils/Config: Configuracao, Localizacao, EstatisticasCampo.

Baseado no projeto "Foxes and Rabbits" do livro "Objects First with Java".

```bash
java Principal