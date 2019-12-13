/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolucionario;

import dp.Const;
import dp.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author Tarcisio Lucas
 */
public class SSDPmaisPbest2k {
    public static Pattern[] run(int k, int witdh, String tipoAvaliacao, double similaridade){
                
        Pattern[] Pwitdh = new Pattern[witdh];                
        Pattern[] P = null;
        
        //Inicializa Pwitdh com indivíduos vazios
        for(int i = 0; i < Pwitdh.length;i++){
            Pwitdh[i] = new Pattern(new HashSet<Integer>(), tipoAvaliacao);
        }
        
        //System.out.println("Inicializando população...");
        //Inicializa garantindo que P maior que Pwitdh sempre! em bases pequenas isso nem sempre ocorre
        Pattern[] Paux = INICIALIZAR.D1(tipoAvaliacao);//P recebe população inicial
        if(Paux.length < witdh){
            P = new Pattern[witdh];            
            for(int i = 0; i < witdh; i++){
                if(i < Paux.length){
                    P[i] = Paux[i];
                }else{
                    P[i] = Paux[Const.random.nextInt(Paux.length-1)];
                }                
            }                
        }else{
            P = Paux;
        }      
        
        Arrays.sort(P);
        
        
        //System.arraycopy(P, 0, Pk, 0, k); //Inicializa Pk com os melhores indivíduos da população inicial
        SELECAO.salvandoRelevantesDPmais(Pwitdh, P, similaridade);
                     
//        System.out.println("P0");        
//        System.out.println("Qualidade média k/P: " + Avaliador.avaliarMedia(Pk,k) + "/" + Avaliador.avaliarMedia(P,P.length));
//        System.out.println("Dimensão média k/P: " + Avaliador.avaliarMediaDimensoes(Pk,k) + "/" + Avaliador.avaliarMediaDimensoes(P,P.length));        
//        System.out.println("Cobertura +: " + Avaliador.coberturaPositivo(Pk,k));       
//        Avaliador.imprimirDimensaoQuantidade(Pk, k, 15);
//        Avaliador.imprimirDimensaoQuantidade(P, P.length, 15);

        int numeroGeracoesSemMelhoraPk = 0;
        int indiceGeracoes = 1;
        
        //Laço do AG
        Pattern[] Pnovo = null;
        Pattern[] PAsterisco = null;
        
        int tamanhoPopulacao = P.length;
        
        //System.out.println("Buscas...");
        for(int numeroReinicializacoes = 0; numeroReinicializacoes < 3; numeroReinicializacoes++){//Controle número de reinicializações
            //System.out.println("Reinicialização: " + numeroReinicializacoes);
            if(numeroReinicializacoes > 0){
                P = INICIALIZAR.aleatorio1_D_Pk(tipoAvaliacao, tamanhoPopulacao, Pwitdh);
            }
        
            double mutationTax = 0.4; //Mutação inicia em 0.4. Crossover é sempre 1-mutationTax.
            //System.out.println("============================");
            while(numeroGeracoesSemMelhoraPk < 3){

                if(indiceGeracoes == 1){
                    Pnovo = CRUZAMENTO.ANDduasPopulacoes(P, P, tipoAvaliacao);
                    indiceGeracoes++; 
                }else{
                    Pnovo = CRUZAMENTO.uniforme2Pop(P, mutationTax, tipoAvaliacao);                 
                }                   
                PAsterisco = SELECAO.selecionarMelhores(P, Pnovo); 
                P = PAsterisco;   

                int novosK = SELECAO.salvandoRelevantesDPmais(Pwitdh, PAsterisco, similaridade);//Atualizando Pk e coletando número de indivíduos substituídos
                //System.out.println("Modificações em Pk: " + novosK);
                //Definição automática de mutação de crossover
                if(novosK > 0 && mutationTax > 0.0){//Aumenta cruzamento se Pk estiver evoluindo e se mutação não não for a menos possível.
                    mutationTax -= 0.2;
                }else if(novosK == 0 && mutationTax < 1.0){//Aumenta mutação caso Pk não tenha evoluido e mutação não seja maior que o limite máximo.
                     mutationTax += 0.2;
                }
                //Critério de parada: 3x sem evoluir Pk com taxa de mutação 1.0
                if(novosK == 0 && mutationTax == 1.0){
                    numeroGeracoesSemMelhoraPk++;

                }else{
                    numeroGeracoesSemMelhoraPk = 0;
                }

                //Impriminto resultados
                
                //Avaliador.imprimirRegrasSimilares(Pk,k);
                //Avaliador.imprimirRegras(Pk,k);

    //            System.out.println("P" + indiceGeracoes);        
                //System.out.println("Qualidade média k/P: " + Avaliador.avaliarMedia(Pk,k) + "/" + Avaliador.avaliarMedia(P,P.length));
                //System.out.println("Dimensão média k/P: " + Avaliador.avaliarMediaDimensoes(Pk,k) + "/" + Avaliador.avaliarMediaDimensoes(P,P.length));        
                //System.out.println("Cobertura +: " + Avaliador.coberturaPositivo(Pk,k));
    //            System.out.println("Novos k: " + novosK);

    //            System.out.println("P" + indiceGeracoes);        
    //            System.out.println(Avaliador.avaliarMedia(P,P.length));
    //            System.out.println(Avaliador.avaliarMediaDimensoes(P,P.length));        
    //            Avaliador.imprimirDimensaoQuantidade(P, P.length, 15);         
    //                        
    //            System.out.println("K" + indiceGeracoes);        
    //            System.out.println(Avaliador.avaliarMedia(Pk,k));
    //            System.out.println(Avaliador.avaliarMediaDimensoes(Pk,k));        
    //            Avaliador.imprimirDimensaoQuantidade(Pk, k, 15);
                //Acompanhamento de taxa de mutação e cruzamento
                //System.out.println("Melhorias:" + novosK  + ",M:" + mutationTax + ",C:" + (1.0-mutationTax));                         

            } 
            
            numeroGeracoesSemMelhoraPk = 0;
        }        
        
        Pattern[] Pk = new Pattern[k];
        System.arraycopy(Pwitdh, 0, Pk, 0, Pk.length);
        
        return Pk;
    }
}
