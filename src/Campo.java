import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa o campo onde os animais vivem.
 * O campo é uma grade retangular de localizações.
 * Cada localização pode conter um único animal ou obstáculo.
 * 
 * --- NOVIDADE: Gerenciamento de Grama ---
 * Agora o campo também gerencia o crescimento da grama,
 * que pode ser comida por alguns animais.
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
    
    private int[][] nivelGrama;

    private static final int MAX_CRESCIMENTO_GRAMA = 10; 

    private static final int VALOR_NUTRICIONAL_GRAMA = 5; 

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
        nivelGrama = new int[profundidade][largura];
        
        for(int i=0; i < profundidade; i++) {
            for(int j=0; j < largura; j++) {
                nivelGrama[i][j] = MAX_CRESCIMENTO_GRAMA;
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
                
                if(!ehObstaculo && nivelGrama[i][j] < MAX_CRESCIMENTO_GRAMA) {
                    nivelGrama[i][j]++;
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
        
        if(nivelGrama[lin][col] >= MAX_CRESCIMENTO_GRAMA) {
            nivelGrama[lin][col] = 0;
            return VALOR_NUTRICIONAL_GRAMA;
        }
        return 0;
    }
    
    /**
     * Verifica se a grama está verde (madura) nesta posição.
     */
    public boolean temGramaMadura(int linha, int coluna) {
        return nivelGrama[linha][coluna] >= MAX_CRESCIMENTO_GRAMA;
    }
    
    /**
     * Copia o estado da grama de outro campo para este.
     * Essencial porque o simulador troca os campos a cada passo.
     */
    public void copiarGramaDe(Campo outroCampo) {
        for(int i=0; i < profundidade; i++) {
            for(int j=0; j < largura; j++) {
                this.nivelGrama[i][j] = outroCampo.nivelGrama[i][j];
            }
        }
    }
    
    public void limpar()
    {
        for(int linha = 0; linha < profundidade; linha++) {
            for(int coluna = 0; coluna < largura; coluna++) {
                campo[linha][coluna] = null;
            }
        }
    }
    
    public void colocar(Object animal, int linha, int coluna)
    {
        colocar(animal, new Localizacao(linha, coluna));
    }
    
    public void colocar(Object animal, Localizacao localizacao)
    {
        campo[localizacao.getLinha()][localizacao.getColuna()] = animal;
    }
    
    public Object getObjetoEm(Localizacao localizacao)
    {
        return getObjetoEm(localizacao.getLinha(), localizacao.getColuna());
    }
    
    public Object getObjetoEm(int linha, int coluna)
    {
        return campo[linha][coluna];
    }
    
    public Localizacao localizacaoAdjacenteLivre(Localizacao localizacao)
    {
        Iterator adjacentes = localizacoesAdjacentes(localizacao);
        while(adjacentes.hasNext()) {
            Localizacao proxima = (Localizacao) adjacentes.next();
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

    public Iterator localizacoesAdjacentes(Localizacao localizacao)
    {
        int linha = localizacao.getLinha();
        int coluna = localizacao.getColuna();
        LinkedList locais = new LinkedList();
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

    public int getProfundidade()
    {
        return profundidade;
    }
    
    public int getLargura()
    {
        return largura;
    }
}