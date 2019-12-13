/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorios_discriminantes;

import dp.Avaliador;
import dp.Const;
import dp.D;
import dp.Pattern;
import evolucionario.INICIALIZAR;
import evolucionario.SSDP;
import evolucionario.SSDPmais;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import sd.SD;

/**
 *
 * @author Tarcisio Lucas
 */
public class RelatorioDiscriminante {
    public static DecimalFormat df = new DecimalFormat("#.##");
    
       
    public static void TXT(
            //Base de dados
            String caminhoBase, String separadorBase, int tipoBase, 
            //Perfil descrição
            String tipoAvaliacao, String[] metricas, String[] rotulos, int ANDouORentreItens,
            //Relatório Caracterísitcas
            boolean relatorioItens, int kItens, 
            //Relatório Grupos
            boolean relatorioGrupos, int k, long seed, String[] filtrarAtributos, String[] filtrarValores, String[][] filtrarAtributosValores, 
            double similaridade,
            int ks
    ) throws FileNotFoundException{
        
        //Configurando arredondamento casas decimais
        df.setRoundingMode(RoundingMode.HALF_UP);
        
        //AND ou OR
        Pattern.ITENS_OPERATOR = ANDouORentreItens;
            
        //Carregando base de dados       
        D.SEPARADOR = separadorBase; //separator database
        Const.random = new Random(seed); //Seed
        D.CarregarArquivo(caminhoBase, tipoBase); //Loading database         
        
        //Gerando arquivo TXT para salvar tudo que seria impresso no console!
        Calendar cal = GregorianCalendar.getInstance();
        String nomeRelatorioTXT = //System.getProperty("user.home") + "/" + "Desktop" + "/"  
                caminhoBase
                + "RID_" + D.nomeBase + "_" + tipoAvaliacao + ((ANDouORentreItens==Const.PATTERN_AND)?"AND_":"OR_") +
                "sim" + similaridade + "k" + k + "ks" + ks + "_" +
                cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)  + 
                "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.getTimeInMillis() +
                ".txt";

        PrintStream out = new PrintStream(new FileOutputStream(nomeRelatorioTXT, true));
        System.setOut(out);
        
        
        
        
        //Informações gerais da base de dados
        System.out.println("################################");
        System.out.println("BASE DE DADOS ##################");
        System.out.println("################################");

        System.out.println("Nome: " + D.nomeBase);
        System.out.println("Total Colunas (atributos): " + D.numeroAtributos);
        System.out.println("Total de Linhas (exemplos/amostras): " + D.numeroExemplos);
        String nomesClasses = "CLASSES: ";
        for(int j = 0; j < D.valoresAlvo.length; j++){
            nomesClasses = nomesClasses + D.valoresAlvo[j];
            if(j < D.valoresAlvo.length-1){
                nomesClasses = nomesClasses + ", ";                    
            }
        }
        System.out.println(nomesClasses);
       
        System.out.println();
        System.out.println("################################");
        System.out.println("INFORMAÇÕES TÉCNICAS ###########");
        System.out.println("################################");
        System.out.println("Métrica de avaliação: " + tipoAvaliacao);
        if(ANDouORentreItens == Const.PATTERN_AND){
            System.out.println("Operador entre características: AND");            
        }else{
            System.out.println("Operador entre características: OR");
        }
        System.out.println("k: " + k + ", ks: " + ks);
        System.out.println("Simularidade: " + similaridade);
        
        //Para cada classe
        for(int i = 0; i < rotulos.length; i++){
            D.GerarDpDn(rotulos[i]);
            
            System.out.println("\n");
            System.out.println("################################");
            System.out.println("INVESTIGANDO: CLASSE = " + rotulos[i]);
            System.out.println("################################");
            double percentualPositivos = 100.0 * (double) D.numeroExemplosPositivo/(double)D.numeroExemplos;
            double percentualNegativos = 100.0 * (double) D.numeroExemplosNegativo/(double)D.numeroExemplos;
            System.out.println("CLASSE = " + rotulos[i] + ": " + D.numeroExemplosPositivo + " linhas (" + df.format(percentualPositivos) + "%)");            
            System.out.println("DEMAIS CLASSES: " + D.numeroExemplosNegativo + " linhas (" + df.format(percentualNegativos) + "%)");
            
            
            //Relatório de itens
            if(relatorioItens){
                System.out.println();
                System.out.println("### TOP-" + kItens + " catacterísticas:");
                Pattern[] itens = INICIALIZAR.D1(tipoAvaliacao);
                Arrays.sort(itens);
                for(int j = 0; j < kItens; j++){
                    System.out.println(itens[j].toString(metricas, false, false, false));
                }
            }
            
            
            //Relatórios de grupos
            Pattern[] pk = null;
            if(relatorioGrupos){
                System.out.println("\n");
                System.out.println("### TOP-" + k + " grupos");
                
                //Executar filtros        
                if(filtrarAtributos != null || filtrarValores != null|| filtrarAtributosValores != null){
                    D.filtrar(filtrarAtributos, filtrarValores, filtrarAtributosValores);
                    String descricao = "";
                    if(filtrarAtributos != null){                        
                        for(int j = 0; j < filtrarAtributos.length; j++){
                            descricao = descricao + filtrarAtributos[j];
                            if(j < filtrarAtributos.length -1){
                                descricao = descricao + ", ";
                            }
                        }
                        System.out.println("Atributos filtrados: {" + descricao + "}");                        
                    }
                    if(filtrarValores != null){
                        descricao = "";
                        for(int j = 0; j < filtrarValores.length; j++){
                            descricao = descricao + filtrarValores[j];
                            if(j < filtrarValores.length -1){
                                descricao = descricao + ", ";
                            }
                        }
                        System.out.println("Valores filtrados: {" + descricao + "}");                        
                    }
                    if(filtrarAtributosValores != null){
                        descricao = "";
                        for(int j = 0; j < filtrarAtributosValores.length; j++){
                            descricao = descricao + filtrarAtributosValores[j][0] + "->" + filtrarAtributosValores[j][1];
                            if(j < filtrarAtributosValores.length -1){
                                descricao = descricao + ", ";
                            }
                        }
                        System.out.println("Características filtradas: {" + descricao + "}");                                               
                    }                    
                }
                               
                //Minerando grupos
                //pk = SSDP.run(k, tipoAvaliacao, -1); //run SSDP
                Pattern.maxSimulares = ks;
                Pattern.medidaSimilaridade = Const.SIMILARIDADE_JACCARD;
                pk = SSDPmais.run(k, tipoAvaliacao, similaridade, -1); //run SSDP+
//                SD sd = new SD();
//                pk = sd.run(0, 2*k, tipoAvaliacao, k); //run SSDP
                
                System.out.println("Cobertura total CLASSE = " + rotulos[i]  + ": " + df.format(Avaliador.coberturaPositivo(pk, k)*100) + "%");
                //Impriminto top-k grupos
                for(int j = 0; j < k; j++){
                    System.out.println(pk[j].toString(metricas, false, false, false));
                }
                
                //Itens utilizados
                
                //Obtendo todos os itens
                HashSet<Integer> allItensPk = new HashSet<Integer>();
                for(int j = 0; j < pk.length; j++){
                    allItensPk.addAll(pk[j].getItens());
                }
                
                //Gerando um Pattern para cada item utilizado e salvando em itensUtilizados
                Pattern[] itensUtilizados = new Pattern[allItensPk.size()];
                Iterator iterator = allItensPk.iterator();
                int indice = 0;
                while(iterator.hasNext()){
                    HashSet<Integer> item = new HashSet<Integer>();
                    item.add( (Integer) iterator.next() );
                    itensUtilizados[indice++] = new Pattern(item, tipoAvaliacao);
                }                
                Arrays.sort(itensUtilizados);
                System.out.println("\n");
                System.out.println("OBS: Características utilizadas nos grupos:");
                for(int j = 0; j < itensUtilizados.length; j++){
                    System.out.println(itensUtilizados[j].toString(metricas, false, false, false));
                }          
            }                
        }       
    }
        
    public static void main(String args[]) throws FileNotFoundException{
                 
        //Base de dados
        String caminho = "C:\\Users\\CoffeeLake_01\\Documents\\NetBeansProjects\\DP4\\pastas\\bases\\"; 
        //String nomeBase = "arXiv_resumoTitulo.csv";
        String nomeBase = "arXiv_resumoTitulo_centralidadeGrau.csv";
        //String nomeBase = "bracis_resumoTitulo_centralidade_grau.csv";
        //String nomeBase = "burczynski-pn-freq-2.CSV";//"chiaretti-pn-freq-2.CSV";
        //String nomeBase = "pns_pessoa_Diabetes10p.csv";
        //String nomeBase = "toy_problem.csv";
        String caminhoBase = caminho + nomeBase;
        String separadorBase = ",";  
        int tipoBase = D.TIPO_CSV;
        
        //Perfil descrição
        //String tipoAvaliacao = Avaliador.METRICA_AVALIACAO_WRACC;
        //String tipoAvaliacao = Avaliador.METRICA_AVALIACAO_WRACC_OVER_SIZE;
        //String[] metricas = null;
        String tipoAvaliacao = Avaliador.METRICA_AVALIACAO_QG;
        
        String[] metricas = {
            Const.METRICA_QUALIDADE,
            Const.METRICA_SIZE,
        //    Const.METRICA_WRACC,
//        //  Const.METRICA_Qg,
//            Const.METRICA_DIFF_SUP,
//            Const.METRICA_LIFT,
//            Const.METRICA_CONF,
//            Const.METRICA_COV,
//            Const.METRICA_CHI_QUAD,
//            Const.METRICA_P_VALUE,
//            Const.METRICA_SUPP_POSITIVO,
//            Const.METRICA_SUPP_NEGATIVO                      
        };
        //String[] rotulos = {"p"};
        String[] rotulos = {"6","80","104","116","134","145","151","153","156","256"};
        //String[] rotulos = {"90","112","118","45","19","36","50","24","37","75"};
        int ANDouORentreItens = Const.PATTERN_OR;
        //int ANDouORentreItens = Const.PATTERN_AND;
        
        
        //Relatório Caracterísitcas
        boolean relatorioItens = false;
        int kItens = 10;
        
        //Relatório Grupos
        boolean relatorioGrupos = true;
        int k = 10;
        int ks = 5;
        double similaridade = 0.1;
        long seed = Const.SEEDS[0];
        String[] filtrarAtributos = null;
        String[] filtrarValores = {"zero", "medio", "baixo"};
        //String[] filtrarValores = {"zero"};
        String[][] filtrarAtributosValores = null;
        //String[][] filtrarAtributosValores = new String[2][2];
        //filtrarAtributosValores[0][0] = "C001";
        //filtrarAtributosValores[0][1] = "<= 5";
        //filtrarAtributosValores[1][0] = "E01805";
        //filtrarAtributosValores[1][1] = "NA";
               
        RelatorioDiscriminante.TXT(caminhoBase, separadorBase, tipoBase, 
                tipoAvaliacao, metricas, rotulos, ANDouORentreItens, 
                relatorioItens, kItens, 
                relatorioGrupos, k, seed, filtrarAtributos, filtrarValores, filtrarAtributosValores,
                similaridade, ks);
    }       
}
