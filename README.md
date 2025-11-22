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

✔ Criação da interface Desenhavel

<<<<<<< HEAD
## 🛠️ Tecnologias e Padrões de Projeto

O projeto foi desenvolvido utilizando **Java** puro, com foco em Orientação a Objetos.

### Design Patterns Identificados:
* **MVC (Model-View-Controller):** Separação clara entre a lógica de negócio (`Simulador`, `Campo`), a representação visual (`VisualizacaoSimulador`) e o controle de fluxo.
* **Template Method:** A classe abstrata `Animal` define o esqueleto do comportamento (`agir`, `reproduzir`), enquanto as subclasses (`Urso`, `Raposa`) implementam os detalhes específicos (probabilidades, idade máxima).
* **Observer:** Utilizado na interface gráfica para lidar com os eventos dos botões (Listeners).
=======
Foi criada a interface Desenhavel, responsável por definir um contrato mínimo para qualquer forma de visualização da simulação.
A interface possui os métodos:

definirCor(Class<?> classe, Color cor)
>>>>>>> 528392b94d58d013d9f50bc7692ce35e78142643

mostrarStatus(int passo, Campo campo)

<<<<<<< HEAD
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
=======
ehViavel(Campo campo)

reiniciar()

Esses métodos representam tudo que o simulador precisa solicitar a uma view, sem conhecer sua implementação concreta.

✔ VisualizacaoSimulador agora implementa Desenhavel
>>>>>>> 528392b94d58d013d9f50bc7692ce35e78142643

A classe gráfica padrão (VisualizacaoSimulador) passou a:

<<<<<<< HEAD
## 📂 Estrutura do Projeto

* `Principal.java`: Ponto de entrada (`main`).
* `Simulador.java`: Controlador central ("Game Loop"). Gerencia o tempo e as interações.
* `Campo.java`: Representa a grade (grid), armazena os animais e o nível de grama.
* `Animal.java` (Abstrata): Classe base para todas as criaturas.
    * `Urso.java`, `Raposa.java`, `Cobra.java`, `Gavião.java`, `Coelho.java`, `Rato.java`.
* `Clima.java`: Lógica de mudança de tempo.
* `VisualizacaoSimulador.java`: Interface gráfica construída com Java Swing.
=======
implementar a interface Desenhavel

garantir a presença de todos os métodos definidos no contrato

continuar funcionando como a visualização padrão por meio da interface

Essa alteração permite que outras visualizações sejam adicionadas futuramente, como uma visualização textual ou gráfica alternativa, sem modificar o Simulador.
>>>>>>> 528392b94d58d013d9f50bc7692ce35e78142643

✔ Simulador agora depende apenas da interface Desenhavel

<<<<<<< HEAD
## 👥 Autores

* **Base Original:** David J. Barnes e Michael Kölling (livro *Objects First with Java*).
* **Implementação do Ecossistema Estendido:** Grupo 10.
=======
A classe Simulador foi modificada para não conhecer mais diretamente VisualizacaoSimulador.

Principais alterações:

O atributo interno deixou de ser um VisualizacaoSimulador e passou a ser um Desenhavel.

O construtor foi refatorado para aceitar um objeto Desenhavel como parâmetro.

O construtor padrão do simulador instancia a visualização gráfica, mas a armazena como interface.

Todas as chamadas a métodos visuais (mostrarStatus, ehViavel, definirCor, etc.) agora usam apenas o tipo abstrato Desenhavel.

Essa mudança atende ao princípio Programar para interfaces, não implementações, e torna o simulador extensível e mais fácil de manter.

✔ Possibilidade de múltiplas visualizações

Com o desacoplamento implementado, agora é possível criar outras visualizações da simulação sem alterar nenhuma linha de código do simulador.

Exemplo:
VisualizacaoTexto implements Desenhavel 
(Não implementado)
(exibição da simulação no console)

O simulador pode receber qualquer implementação de Desenhavel:

Simulador sim = new Simulador(50, 70, new VisualizacaoTexto());

✔ Benefícios da Refatoração

🔄 Substituição dinâmica da view sem alterar o simulador

📦 Código mais modular e coeso

🧪 Facilita testes automatizados usando uma visualização "fake"

🧩 Permite múltiplas views simultâneas, se necessário

📝 Segue o mesmo padrão ensinado no livro, facilitando alinhamento com o professor
>>>>>>> 528392b94d58d013d9f50bc7692ce35e78142643
