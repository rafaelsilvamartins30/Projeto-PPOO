import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa o campo onde os animais vivem.
 * O campo é uma grade retangular de localizações.
 * Cada localização pode conter um único animal ou obstáculo.
 * As plantas (grama) também crescem no campo, mas não ocupam espaço
 * 
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-23
 */
public class Campo
{

    private static final Random aleatorio = new Random();
    
    private int profundidade;
    
    private int largura;

    private Object[][] campo;
    
    private Vegetacao[][] vegetacao;

    /**
     * Cria um campo de tamanho especificado.
     * @param profundidade A profundidade do campo.
     * @param largura A largura do campo.
     */
    public Campo(int profundidade, int largura)
    {
        this.profundidade = profundidade;
        this.largura = largura;
        campo = new Object[profundidade][largura];
        vegetacao = new Vegetacao[profundidade][largura];
        
        for(int i=0; i < profundidade; i++) {
            for(int j=0; j < largura; j++) {
                vegetacao[i][j] = new Vegetacao();
            }
        }
    }
    
    /**
     * Faz a grama crescer em todo o campo, exceto onde há obstáculos.
     */
    public void crescerGrama() {
        for(int i=0; i < profundidade; i++) {
            for(int j=0; j < largura; j++) {
                Object obj = getObjetoEm(i, j);
                boolean ehObstaculo = (obj instanceof Obstaculo);
                
                if(!ehObstaculo) {
                    vegetacao[i][j].crescer();
                }
            }
        }
    }
    
    /**
     * Tenta comer a grama na posição especificada.
     * @return O valor nutricional se conseguiu comer, ou 0 se não tinha grama.
     */
    public int comerGrama(Localizacao localizacao) {
        int lin = localizacao.getLinha();
        int col = localizacao.getColuna();
        
        return vegetacao[lin][col].vegetacaoEhComida();
    }
    
    /**
     * Verifica se a grama está verde (madura) nesta posição.
     */
    public boolean temGramaMadura(int linha, int coluna) {
        return vegetacao[linha][coluna].estaMadura();
    }
    
    /**
     * Copia o estado da grama de outro campo para este.
     * Essencial porque o simulador troca os campos a cada passo.
     */
    public void copiarGramaDe(Campo outroCampo) {
        for(int i=0; i < profundidade; i++) {
            for(int j=0; j < largura; j++) {
                int nivelOutro = outroCampo.vegetacao[i][j].getNivelCrescimento();
                this.vegetacao[i][j].setNivelCrescimento(nivelOutro);
            }
        }
    }
    
    /**
     * Limpa o campo, removendo todos os animais e obstáculos.
     */
    public void limpar()
    {
        for(int linha = 0; linha < profundidade; linha++) {
            for(int coluna = 0; coluna < largura; coluna++) {
                campo[linha][coluna] = null;
            }
        }
    }
    
    /**
     * Coloca um animal ou obstáculo em uma localização específica.
     * @param animal O animal ou obstáculo a ser colocado.
     * @param linha A coordenada vertical da localização.
     * @param coluna A coordenada horizontal da localização.
     */
    public void colocar(Object animal, int linha, int coluna)
    {
        colocar(animal, new Localizacao(linha, coluna));
    }
    
    /**
     * Coloca um animal ou obstáculo em uma localização específica.
     * @param animal O animal ou obstáculo a ser colocado.
     * @param localizacao A localização onde colocar o animal ou obstáculo.
     */
    public void colocar(Object animal, Localizacao localizacao)
    {
        campo[localizacao.getLinha()][localizacao.getColuna()] = animal;
    }
    
    /**
     * Retorna o objeto (animal ou obstáculo) em uma localização específica.
     * @param localizacao A localização a ser verificada.
     * @return O objeto na localização, ou null se estiver vazia.
     */
    public Object getObjetoEm(Localizacao localizacao)
    {
        return getObjetoEm(localizacao.getLinha(), localizacao.getColuna());
    }
    
    /**
     * Retorna o objeto (animal ou obstáculo) em uma localização específica.
     * @param linha A coordenada vertical da localização.
     * @param coluna A coordenada horizontal da localização.
     * @return O objeto na localização, ou null se estiver vazia.
     */
    public Object getObjetoEm(int linha, int coluna)
    {
        return campo[linha][coluna];
    }
    
    /**
     * Encontra uma localização adjacente livre.
     * @param localizacao A localização central.
     * @return Uma localização adjacente livre, ou null se não houver nenhuma.
     */
    public Localizacao localizacaoAdjacenteLivre(Localizacao localizacao)
    {
        Iterator<Localizacao> adjacentes = localizacoesAdjacentes(localizacao);
        while(adjacentes.hasNext()) {
            Localizacao proxima = adjacentes.next();
            if(campo[proxima.getLinha()][proxima.getColuna()] == null) {
                return proxima;
            }
        }
        if(campo[localizacao.getLinha()][localizacao.getColuna()] == null) {
            return localizacao;
        } 
        else {
            return null;
        }
    }

    /**
     * Retorna um iterador sobre as localizações adjacentes a uma dada localização.
     * As localizações são retornadas em ordem aleatória.
     * @param localizacao A localização central.
     * @return Um iterador sobre as localizações adjacentes.
     */
    public Iterator<Localizacao> localizacoesAdjacentes(Localizacao localizacao)
    {
        int linha = localizacao.getLinha();
        int coluna = localizacao.getColuna();
        LinkedList<Localizacao> locais = new LinkedList<Localizacao>();
        for(int deslocLinha = -1; deslocLinha <= 1; deslocLinha++) {
            int proxLinha = linha + deslocLinha;
            if(proxLinha >= 0 && proxLinha < profundidade) {
                for(int deslocCol = -1; deslocCol <= 1; deslocCol++) {
                    int proxColuna = coluna + deslocCol;
                    if(proxColuna >= 0 && proxColuna < largura && (deslocLinha != 0 || deslocCol != 0)) {
                        locais.add(new Localizacao(proxLinha, proxColuna));
                    }
                }
            }
        }
        Collections.shuffle(locais, aleatorio);
        return locais.iterator();
    }

    /**
     * Retorna a profundidade do campo.
     * @return A profundidade.
     */
    public int getProfundidade()
    {
        return profundidade;
    }
    
    /**
     * Retorna a largura do campo.
     * @return A largura.
     */
    public int getLargura()
    {
        return largura;
    }
}