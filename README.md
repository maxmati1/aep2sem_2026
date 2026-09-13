# Sistema de Cadastro de Doações de Alimentos

AEP 2026.2, 6S, Engenharia de Software (UniCesumar)
**Entrega 1**

## 1. O problema

ONGs e associações de bairro que arrecadam alimentos costumam controlar as
doações em planilha ou até em papel. Isso causa dois problemas recorrentes:
perde-se o controle de **quanto** foi recebido/já distribuído, e perde-se o
controle de **validade**, o que leva alimentos a vencerem em estoque e
serem descartados (o oposto do que a doação deveria resolver).

**Público:** pequenas ONGs/associações comunitárias que recebem doações de
alimentos e as pessoas em situação de vulnerabilidade alimentar atendidas
por elas.

## 2. ODS

**ODS 2, Fome Zero e Agricultura Sustentável.** A PoC ataca diretamente a
meta de reduzir o desperdício de alimentos e melhorar a gestão de quem
distribui alimento para quem precisa: o sistema cadastra cada doação e
avisa quais itens estão perto de vencer, para que sejam priorizados na
distribuição.

## 3. O que a primeira versão faz

- Cadastrar uma doação (doador, item, categoria, quantidade, unidade,
  data de recebimento, validade).
- Listar todas as doações.
- Buscar uma doação por id.
- Atualizar o status de uma doação (`RECEBIDA` para `DISTRIBUIDA` ou
  `DESCARTADA`).
- Remover uma doação.
- Listar doações que vencem nos próximos N dias (a regra que dá sentido
  ao ODS 2: saber o que priorizar).

Tudo isso roda em cima de **uma única coleção do MongoDB** (`doacoes`),
com objetos homogêneos, como pede o requisito do 1º semestre.

## 4. Tecnologias

- **Java 17** (orientação a objetos)
- **MongoDB**, driver oficial `mongodb-driver-sync`
- **Maven** (build e dependências)
- **JUnit 5** (testes automatizados)
- **JaCoCo** (relatório e verificação de cobertura de testes)

## 5. Estrutura do projeto

```
src/main/java/com/unicesumar/doacoes/
├── Main.java                          # so conecta as pecas (wiring)
├── App.java                           # menu de console (logica testavel)
├── model/Doacao.java                  # objeto de domínio
├── repository/
│   ├── DoacaoRepository.java          # contrato de persistência
│   └── MongoDoacaoRepository.java     # implementação real (MongoDB)
└── service/DoacaoService.java         # regras de negócio (validações, alerta de validade)

src/test/java/com/unicesumar/doacoes/
├── AppTest.java                       # simula uma sessao inteira do menu
├── fakes/FakeDoacaoRepository.java    # repositório em memória, só para teste
├── model/DoacaoTest.java
├── repository/MongoDoacaoRepositoryTest.java  # integracao real com o Mongo
└── service/DoacaoServiceTest.java     # testa as regras de negócio de ponta a ponta
```

## 6. Como rodar

### Pré-requisitos
- JDK 17+
- Maven 3.9+
- MongoDB rodando localmente na porta padrão (`27017`). Pode ser via
  instalação local ou um container:
  ```bash
  docker run -d -p 27017:27017 --name mongo-doacoes mongo:7
  ```

### Executar o sistema
```bash
mvn clean package
java -jar target/aep-doacoes.jar
```
O menu de console vai pedir os dados de cada operação (cadastrar, listar,
buscar, atualizar status, remover, listar próximas da validade).

## 7. Como rodar os testes e ver a cobertura

**Importante: o MongoDB precisa estar rodando antes de `mvn test`** (o
mesmo serviço local usado pra rodar o programa, veja a seção 6). Isso
porque `MongoDoacaoRepositoryTest` é um teste de integração real: ele
salva, busca, atualiza e remove documentos de verdade num banco à parte
(`doacoes_db_test`), e apaga esse banco no final.

```bash
mvn test
```
Isso gera o relatório do JaCoCo em `target/site/jacoco/index.html` (abra
no navegador para ver o percentual por classe) e falha o build se a
cobertura total ficar abaixo de 70%.

**O que é testado:**
- `DoacaoServiceTest` e `DoacaoTest`: regras de negócio e validações,
  usando um repositório fake em memória (`FakeDoacaoRepository`), sem
  depender do Mongo.
- `AppTest`: simula uma sessão inteira no menu de console (cadastrar,
  listar, buscar, atualizar, remover, entradas inválidas) e confere as
  mensagens exibidas.
- `MongoDoacaoRepositoryTest`: testa a integração real com o MongoDB
  (insere, busca, atualiza e remove documentos de verdade).

Só a classe `Main` (que apenas conecta as peças e chama o menu) fica de
fora do cálculo de cobertura, por ser puramente wiring.

## 8. Próximos passos (2ª entrega)

- Separar doadores em uma coleção própria e relacionar com as doações
  (requisito de múltiplas coleções/relacionamento do 2º semestre).
- Endereço do doador ou do ponto de coleta como subdocumento.
- Quadro de tarefas da equipe.
- Documentação técnica completa.
