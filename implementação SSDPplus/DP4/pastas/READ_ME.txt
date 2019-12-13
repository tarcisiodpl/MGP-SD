args[0]: k
args[1]: m�trica de avalia��o (fitness)
args[2]: n�mero de repeti��es
args[3]: tempo m�ximo em segundos
args[4]: algoritmos

java -jar DP4.jar "k" "m�trica de avalia��o" "n�mero de repeti��es" "tempo m�ximo em segundos" "algoritmos"

Exemplos:
java -jar DP4.jar "20" "Qg" "10" "10" "SSDP"
java -jar DP4.jar "5,10,20" "Qg" "2" "5" "SD,SDrss,SSDP,SSDPplusS10,SSDPplusS50"

RODAR:
java -jar DP4.jar "20" "Qg" "10" "3600" "SSDP,SSDPplusS10,SSDPplusS50,SSDPplusS90"
java -jar DP4.jar "20" "Qg" "1" "3600" "SD,SDrss"

java -jar DP4.jar "20" "WRAcc" "10" "3600" "SSDP,SSDPplusS10,SSDPplusS50,SSDPplusS90"
java -jar DP4.jar "20" "WRAcc" "1" "3600" "SD,SDrss"

OBS: roda para todas as bases cont�das na pasta "bases"
OBS2: n�o salva mais �ndice da simula��o. Perdeu? Lascou!
OBS3: gerar execut�vel com caminhos relativos.

### Algoritmos:
    public final static String ALGORITMO_SSDP = "SSDP";
    public final static String ALGORITMO_SSDPmais = "SSDPplus";
    public final static String ALGORITMO_SSDPmaisS00 = "SSDPplusS00";
    public final static String ALGORITMO_SSDPmaisS10 = "SSDPplusS10";
    public final static String ALGORITMO_SSDPmaisS20 = "SSDPplusS20";
    public final static String ALGORITMO_SSDPmaisS30 = "SSDPplusS30";
    public final static String ALGORITMO_SSDPmaisS40 = "SSDPplusS40";
    public final static String ALGORITMO_SSDPmaisS50 = "SSDPplusS50";
    public final static String ALGORITMO_SSDPmaisS60 = "SSDPplusS60";
    public final static String ALGORITMO_SSDPmaisS70 = "SSDPplusS70";
    public final static String ALGORITMO_SSDPmaisS80 = "SSDPplusS80";    
    public final static String ALGORITMO_SSDPmaisS90 = "SSDPplusS90";
    public final static String ALGORITMO_SSDPmaisS100 = "SSDPplusS100";
    public final static String ALGORITMO_SD = "SD";
    public final static String ALGORITMO_SD_RSS = "SDrss";

### M�trica de avalia��o:
    public static final String METRICA_AVALIACAO_QG = "Qg";
    public static final String METRICA_AVALIACAO_SUB = "Sub";
    public static final String METRICA_AVALIACAO_WRACC = "WRAcc";
    public static final String METRICA_AVALIACAO_WRACC_NORMALIZED = "WRAccN";
    public static final String METRICA_AVALIACAO_WRACC_OVER_SIZE = "WRAccOverSize";
    public static final String METRICA_AVALIACAO_DIFF_SUPP = "DiffSup";


