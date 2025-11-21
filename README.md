# 🦁 Simulador de Ecossistema (Predador-Presa)

Este projeto é uma simulação avançada de um ecossistema baseada em agentes, desenvolvida em **Java**. O sistema modela a interação entre diversas espécies de animais, crescimento vegetal, influência climática e obstáculos geográficos.

O projeto é uma evolução robusta do clássico exemplo *"Foxes and Rabbits"* do livro *Objects First with Java* (Barnes & Kölling), implementando novas camadas de complexidade e Padrões de Projeto.

## 📋 Funcionalidades

O simulador vai muito além do modelo básico, introduzindo:

### 1. Cadeia Alimentar Complexa
Diferente do modelo binário (apenas Raposa e Coelho), este ecossistema suporta múltiplas espécies com comportamentos distintos:
* **Urso:** O predador de topo. Caça Raposas, Cobras e Coelhos. Pode **pescar** se estiver perto de um Rio.
* **Gavião:** Predador aéreo que foca na caça de Ratos.
* **Raposa:** Predador intermediário. Caça Coelhos e Ratos.
* **Cobra:** Caça Coelhos e Ratos.
* **Coelho e Rato:** Herbívoros primários.

### 2. Sistema de Vegetação (Grama) 🌱
Os herbívoros não se reproduzem infinitamente. Eles dependem da **Grama** presente no campo.
* A grama cresce gradualmente a cada turno.
* Se os herbívoros comerem tudo, a população decai (Fome), criando um **limite trófico** realista.

### 3. Clima Dinâmico ⛈️☀️
Um sistema climático (`Clima.java`) que altera o estado do ambiente:
* **Estados:** `NORMAL` e `CHUVOSO`.
* **Impacto:** Durante a chuva, a taxa de crescimento da grama aumenta, acelerando a recuperação do ecossistema.

### 4. Geografia e Obstáculos 🗺️
O campo não é apenas uma grade vazia. O simulador carrega um mapa (`mapa.txt`) que contém:
* **Rios (R):** Barreiras naturais (exceto para pesca do Urso).
* **Pedras (P):** Bloqueios de movimento.

### 5. Interface Gráfica (Swing) 🎨
* Visualização em tempo real da grade.
* Legenda de cores para cada espécie.
* Indicador de Clima e Passo atual.
* Controles de **Pausar**, **Continuar** e **Reiniciar** simulação.

---

## 🛠️ Tecnologias e Padrões de Projeto

O projeto foi desenvolvido utilizando **Java** puro, com foco em Orientação a Objetos.

### Design Patterns Identificados:
* **MVC (Model-View-Controller):** Separação clara entre a lógica de negócio (`Simulador`, `Campo`), a representação visual (`VisualizacaoSimulador`) e o controle de fluxo.
* **Template Method:** A classe abstrata `Animal` define o esqueleto do comportamento (`agir`, `reproduzir`), enquanto as subclasses (`Urso`, `Raposa`) implementam os detalhes específicos (probabilidades, idade máxima).
* **Observer:** Utilizado na interface gráfica para lidar com os eventos dos botões (Listeners).

---

## 🚀 Como Executar

### Pré-requisitos
* Java JDK 8 ou superior instalado.

### Passo a Passo
1.  Clone este repositório.
2.  Certifique-se de que o arquivo `mapa.txt` esteja na raiz do projeto.
3.  Compile os arquivos `.java`:
    ```bash
    javac .java
    ```
4.  Execute a classe principal:
    ```bash
    java Principal
    ```

---

## 📂 Estrutura do Projeto

* `Principal.java`: Ponto de entrada (`main`).
* `Simulador.java`: Controlador central ("Game Loop"). Gerencia o tempo e as interações.
* `Campo.java`: Representa a grade (grid), armazena os animais e o nível de grama.
* `Animal.java` (Abstrata): Classe base para todas as criaturas.
    * `Urso.java`, `Raposa.java`, `Cobra.java`, `Gavião.java`, `Coelho.java`, `Rato.java`.
* `Clima.java`: Lógica de mudança de tempo.
* `VisualizacaoSimulador.java`: Interface gráfica construída com Java Swing.

---

## 👥 Autores

* **Base Original:** David J. Barnes e Michael Kölling (livro *Objects First with Java*).
* **Implementação do Ecossistema Estendido:** Grupo 10.