🔧 Refatoração da Visualização e Desacoplamento do Simulador

Nesta versão do projeto, realizamos uma refatoração estrutural para melhorar o desacoplamento entre a lógica da simulação e o mecanismo de visualização. Essa mudança segue os princípios apresentados no livro Objects First with Java (Barnes & Kölling), especialmente no capítulo que introduz SimulatorView.

✔ Criação da interface Desenhavel

Foi criada a interface Desenhavel, responsável por definir um contrato mínimo para qualquer forma de visualização da simulação.
A interface possui os métodos:

definirCor(Class<?> classe, Color cor)

mostrarStatus(int passo, Campo campo)

ehViavel(Campo campo)

reiniciar()

Esses métodos representam tudo que o simulador precisa solicitar a uma view, sem conhecer sua implementação concreta.

✔ VisualizacaoSimulador agora implementa Desenhavel

A classe gráfica padrão (VisualizacaoSimulador) passou a:

implementar a interface Desenhavel

garantir a presença de todos os métodos definidos no contrato

continuar funcionando como a visualização padrão por meio da interface

Essa alteração permite que outras visualizações sejam adicionadas futuramente, como uma visualização textual ou gráfica alternativa, sem modificar o Simulador.

✔ Simulador agora depende apenas da interface Desenhavel

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
