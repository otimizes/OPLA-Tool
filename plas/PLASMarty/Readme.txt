

AS PLAS com nome Atual foram criadas por meio da utilização da entrada no formato Papyrus para salvar no formato .smty.
Estas PLAs podem apresentar alguma diferença em relação ao original (todas as diferenças estão citadas na pasta LOgsPLAAtual).
Exemplo de alterações:
    - remoção de variabildiade sem ponto de variação ou vaiante
    - substituição de relacionamento de usage e abstraction por dependency (abstraction e usage são do tipo dependency, mas como o SMarty Modeling atual não contém esses dois relacionamentos, para não perder os relacionamentos foram considerados como dependency)

Em relação ao decoding da saída para o tipo smty, é possível ativar isso utilizando qualquer entrada (papyrus/smty) se alterar a variável toSMarty para true na classe ArchitectureRepresentation/Architeture


A saída contém uma pasta Logs que salva:
    - fitness original da solução;
    - link entre o nome do arquivo e o fitness da solução
    - erros no decoding (caso haja). Um erro grave que impossibilita a abertura da solução por exemplo é interface duplicada


OBS.:
As PLAs ServiceAndSupportSystem e MicrowaveOpenSoftware não funcionam o decoding para o smarty pois existe inconsistência entre as plas e a versão atual da opla, o que ocasiona na perda de dados. No caso do MicrowaveOpenSoftware, o encoding original da opla não consegue carregar corretamente os dados (que nem os estereótipos e subpacotes) e no ServiceAndSupportSystem, ocorre perda de alguns dados pelo encoding e decoding (como relacionamentos não existentes na versão atual da OPLA - possivelmente quem desenhou usou uma versão personalizada da OPLA-Tool). 


OBS.:
As versões agm1, agm2, mm1 e mm2 são versões mais completas e corrigidas da agm e mm, verificando as PLAs originais da Thelma e realizando alterações segundo recomendação da Thelma. As versões atuais usadas da agm e mm são versões incompletas da agm2 e mm2, onde tem perda de elementos como estereótipos.
