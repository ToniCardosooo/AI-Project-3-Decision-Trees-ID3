# Inteligência Artificial - 2022/2023

## Projeto 3 - Árvores de Decisão

### Vista Geral

Este projeto foi desenvolvido no âmbito da Unidade Curricular [Inteligência Artificial](https://sigarra.up.pt/fcup/pt/ucurr_geral.ficha_uc_view?pv_ocorrencia_id=508303), durante o ano letivo 2022/2023, na Faculdade de Ciências da Universidade do Porto, pelo aluno António Cardoso.

O seu principal objetivo é a implementação do algoritmo ID3, de construção de árvores de decisão.

Para tal, foi escolhida a linguagem Java, devido à sua relação com Programação Orientada a Objetos, o facto de ser *strongly-typed*, o seu "*Garbage Collector*", entre outros motivos.

O programa foi desenvolvido e testado em *Ubuntu 20.04.5 LTS* com *javac 11.0.17*.

A pasta `CSV` possui 4 ficheiros `.csv` que podem ser usados para treinar uma árvore de decisão com a implementação encontrada neste repositório.

### Instruções de Compilação e Execução

Para poder executar cada problema é primeiro necessário compilar todos os ficheiros java. Para tal, utilize a seguinte instrução:

`javac *.java`

Após a compilação de todos os ficheiros, é possível, finalmente, executá-los.

Para executar, o utilizador deve escrever no seu terminal / linha de comandos:

`java Program <filepath to training CSV> (optional)<filepath to testing CSV>`

Se não for passado nenhum caminho relativo para um ficheiro `.csv`, o programa irá pedir para ser executado com a sintaxe acima definida.

Caso seja passado apenas o caminho para um ficheiro `.csv`, o programa irá criar uma árvore de decisão treinada com base nos exemplos dentro do ficheiro, e irá imprimir esta.

Paraa além disso, se for passado um segundo ficheiro `.csv`, o programa também irá tentar prever a classe dos exemplos neste ficheiro, usando a árvore de decisão criada a partir da aprendizagem com o primeiro ficheiro, imprimindo os resultados.

### Importante

É esperado que a primeira coluna de informação em qualquer ficheiro `.csv` seja uma coluna de índices.

Caso contrário, o atributo associado à primeira coluna não será considerado para a criação do modelo da árvore de decisão.
