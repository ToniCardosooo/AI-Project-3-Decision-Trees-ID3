# Inteligência Artificial - 2022/2023

## Projeto 3 - Árvores de Decisão

### Vista Geral

Este projeto foi desenvolvido no âmbito da Unidade Curricular [Inteligência Artificial](https://sigarra.up.pt/fcup/pt/ucurr_geral.ficha_uc_view?pv_ocorrencia_id=508303), durante o ano letivo 2022/2023, na Faculdade de Ciências da Universidade do Porto, pelo aluno António Cardoso.

O seu objetivo principal é a implementação do algoritmo ID3, de construção de árvores de decisão.

Para tal, foi escolhida a linguagem Java, devido à sua relação com Programação Orientada a Objetos, o facto de ser *strongly-typed*, o seu "*Garbage Collector*", entre outros motivos.

O programa foi desenvolvido e testado em *Ubuntu 20.04.5 LTS* com *javac 11.0.17*.

A pasta `CSV` possui 4 ficheiros `.csv` que podem ser usados para treinar uma árvore de decisão com a implementação encontrada neste repositório.

### Instruções de Compilação e Execução

Para poder executar cada problema é primeiro necessário compilar todos os ficheiros java. Para tal, utilize a seguinte instrução:

`javac *.java`

Após a compilação de todos os ficheiros, é possível, finalmente, executá-los.

Para executar, o utilizador deve escrever no seu terminal / linha de comandos:

`java Program`

Aparecendo o seguinte:

```
Type the training CSV filepath: 
```

O usuário deve escrever um caminho relativo ou absoluto para o ficheiro `.csv` que pretende usar para treinar o modelo de árvore de decisão (ex: `CSV/restaurant.csv`).

De seguida, após o modelo ser treinado, aparece o seguinte prompt:

```
Choose the output format of the Decision Tree model:
1) Project's worksheet format
2) My custom format
Choice: 
```

O user deve inserir `1` se pretende ver um print do modelo com base no formato exigido para a apresentação deste projeto, ou `2` para o print ter um formato feito por mim (a meu ver, mais legivel).

Após o modelo de árvore de decisão ser impresso, o programa pergunta se o user quer realizar algum teste de classificação usando o modelo:

```
Would you like to predict the classification of some examples?
1) Yes
2) No
Choice: 
```

Caso seja escolhido `1`, ou seja, o user pretende realizar um teste de classificação usando o modelo, aparece um último prompt a pedir a localização do ficheiro para teste:

```
Type the testing CSV filepath: 
```

### Importante

É esperado que a primeira coluna de informação em qualquer ficheiro `.csv` seja uma coluna de índices, assim como a primeira linha deve ser o header que identifica o nome de cada coluna.

Caso contrário, o atributo associado à primeira coluna não será considerado para a criação do modelo da árvore de decisão e/ou a interpretação do atributo escolhido num dado nó do modelo de árvore de decisão poderá ser baixa.
