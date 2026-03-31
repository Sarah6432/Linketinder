🚀 Linketinder - Sistema de Recrutamento (Full Stack MVP)
O Linketinder é um ecossistema de contratação simplificado que une a dinâmica de "Match" à curadoria técnica de talentos. O projeto evoluiu de uma estrutura em memória para uma aplicação Data-Driven, utilizando banco de dados relacional para gerenciar perfis, vagas e interações.

🛠️ Tecnologias e Arquitetura
Backend & Persistência
Linguagem: Groovy (4.0+) / Java JDK 21.
Gradle 5.1.1 para organização do projeto.

Banco de Dados: PostgreSQL.

Arquitetura: Padrão DAO (Data Access Object) para isolamento da lógica de persistência.

Drivers: JDBC para integração Groovy-SQL.

Frontend (Interface Web)
Linguagem: TypeScript (ES6).

Estilização: CSS3 Moderno (Flexbox, Grid, Variáveis).

Gráficos: Chart.js para analytics de competências.

🌟 Novas Funcionalidades (V2.0 - Database Integrated)
Persistência Relacional: Todo o fluxo de Candidatos, Empresas e Vagas agora é armazenado permanentemente no PostgreSQL.

Gerenciamento de Competências (N:N): Sistema de normalização onde competências são entidades únicas vinculadas a múltiplos candidatos e vagas.

Fluxo de Match Real: * Candidatos: Visualizam vagas (apenas descrição e requisitos) e registram interesse.

Empresas: Visualizam candidatos interessados (foco em competências/anonimato) e retribuem o interesse.

Algoritmo de Match: Identificação automática de interesse mútuo via queries SQL de interseção.

CRUD Completo: Interfaces de terminal para Criar, Listar, Atualizar e Deletar (CRUD) qualquer entidade do sistema com integridade referencial (ON DELETE CASCADE).

🏗️ Estrutura do Banco de Dados
O projeto utiliza uma estrutura relacional otimizada:

candidatos / empresas: Entidades principais de usuários.

vagas: Vinculadas a empresas.

competencias: Tabela mestra de habilidades técnicas.

candidato_competencias / vagas_competencias: Tabelas de relacionamento muitos-para-muitos.

curtidas_candidato / curtidas_empresa: Registro de interações para cálculo de Match.

ATUALIZAÇÃO: 
Adicionei o gradle para melhor organização de depêndencias e pastas do projeto!

Correções para Clean Code: 
Eliminação de Código Duplicado (DRY - Don't Repeat Yourself)
As lógicas de exibição e gerenciamento foram centralizadas. Em vez de cada menu tentar formatar os dados do candidato, passamos a utilizar o método exibirPerfil() da classe, garantindo que qualquer mudança visual seja feita em um único lugar.

Padrão AAA nos Testes (Arrange, Act, Assert)
Refatorei os testes para que cada um tenha uma estrutura clara:

Arrange: Prepara os dados (Cria o objeto).

Act: Executa a ação (Salva ou curti).

Assert: Verifica o resultado (Compara com o esperado).
Isso torna o teste documentação viva do sistema.

Nomes Significativos e Intencionais
Mudei nomes de métodos de teste de InserirCandidatoNovo para shouldRegisterNewCandidatoCorrectly. O nome agora descreve o comportamento esperado, facilitando a leitura por outros desenvolvedores.

Separação de Preocupações (SoC)
O código do LinketinderApp (Interface/CLI) agora foca em interagir com o usuário, enquanto o DAO foca estritamente em SQL. A lógica de "Match" foi movida para onde pertence (regra de negócio), e não jogada dentro de uma query complexa.

Tratamento de Erros Profissional
Saímos do amadorismo de "imprimir o erro na tela e continuar" para uma arquitetura onde o erro é propagado e tratado no main. Isso mantém o estado do programa consistente e evita comportamentos inesperados (Side Effects).

///////////////////////////////
Principais Correções Realizadas - SOLID
1. Separação de Responsabilidades (SRP)
Antes: A lógica de negócio (como decidir quais campos atualizar ou formatar strings) estava misturada com a interface no LinketinderApp ou diretamente no SQL do DAO.

Depois: Criamos a camada de Service (service.CandidatoService, service.EmpresaService). Agora, o App apenas lê dados do teclado e o DAO apenas executa SQL. Toda a "inteligência" do sistema fica protegida nos Services.

2. Estabilidade de Tipos e Construtores (LSP)
Antes: As classes model.Candidato e model.Empresa tinham construtores confusos e redundantes. Ao mudar a hierarquia para uma model.EntidadeBase, corrigimos erros de "missing constructor" que quebravam o sistema.

Depois: O uso de Herança correta permitiu que o sistema tratasse candidatos e empresas como "Pessoas", facilitando a exibição de perfis e garantindo que o construtor usado no DAO fosse o mesmo definido na classe.

3. Sincronização com o Banco de Dados
Antes: Haviam erros de digitação (typos) como data_pascimento (em vez de data_nascimento) e nomes de colunas genéricos como nome em tabelas onde o banco exigia nome_empresa ou nome_vaga.

Depois: Mapeamos cada insert, update e select para bater exatamente com o seu esquema PostgreSQL, eliminando as exceções de PSQLException.

4. Segregação de Interfaces (ISP)
Antes: Os Services dependiam das classes concretas dos DAOs. Se o DAO mudasse, o Service quebrava.

Depois: Criamos interfaces pequenas (model.IReader, model.IWriter, model.ICurtida, model.ICompetenciaManager). Agora, o service.CompetenciaService não sabe o que é um model.CandidatoDAO; ele só sabe que recebeu algo que "sabe vincular competências".
///////////////////////////////////
🏗️ Design Patterns Utilizados
Além da arquitetura base, o projeto aplica padrões que facilitam a manutenção e a escalabilidade do sistema:

1. Factory Method (ServiceFactory)
Utilizado na classe ServiceFactory. Este padrão centraliza a criação de objetos complexos. Em vez de instanciar os serviços manualmente em todo o código, a Factory encapsula a lógica de instanciar os DAOs e passá-los para os serviços.

Vantagem: Se a forma de criar um CandidatoService mudar (ex: precisar de um novo parâmetro), você altera apenas na Factory e não em todo o projeto.

2. Data Access Object - DAO
Presente nas classes CandidatoDAO, EmpresaDAO e VagaDAO. O padrão DAO isola a camada de persistência (Banco de Dados SQL) da lógica de negócio.

Vantagem: O restante do sistema não sabe se os dados vêm de um banco PostgreSQL ou de uma lista na memória; ele apenas solicita os dados para o DAO.

3. Interface Segregation (Segregação de Interfaces)
Visto no uso de interfaces como IReader, IWriter, ICurtida e ICompetenciaManager. O sistema não utiliza uma interface única "gigante", mas sim interfaces pequenas e específicas para cada ação.

Vantagem: Uma classe que só precisa ler dados não é obrigada a implementar métodos de exclusão ou atualização.

4. Dependency Injection (Injeção de Dependência)
Aplicado em todos os Controllers e Services. As dependências (como o Sql ou os DAOs) não são criadas dentro das classes que as usam; elas são "injetadas" através do Construtor.

Vantagem: Facilita testes unitários e torna as classes menos acopladas (mais independentes).

5. Strategy (Estratégia)
Utilizado de forma sutil através das interfaces de persistência. O EmpresaService recebe objetos do tipo IWriter<Empresa>. Ele não sabe qual é a implementação exata (se é um DAO que salva no banco ou um que salva em arquivo), ele apenas executa a "estratégia" de salvar.

Vantagem: Permite trocar o meio de salvamento sem alterar uma linha de código na camada de serviço.

🏃 Como Executar
1. Configuração do Banco de Dados
Certifique-se de ter o PostgreSQL instalado.

Crie um banco de dados chamado linketinder.

Execute os scripts SQL presentes na pasta /sql (ou crie as tabelas conforme as definições de classe).

2. Executando o Backend (Console)
Configure suas credenciais na classe config.Conexao.groovy.

Execute via Gradle:

Bash
./gradlew run
Utilize o menu interativo para navegar entre as áreas de model.Candidato, model.Empresa e Gerenciamento.

3. Testando o Frontend (Web)
Navegue até frontend_oficial.

Compile o TypeScript: tsc.

Abra index.html via Live Server.

Autora: Sarah Silva Lima 
