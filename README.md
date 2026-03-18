🚀 Linketinder - Sistema de Recrutamento (Full Stack MVP)
O Linketinder é um ecossistema de contratação simplificado que une a dinâmica de "Match" à curadoria técnica de talentos. O projeto evoluiu de uma estrutura em memória para uma aplicação Data-Driven, utilizando banco de dados relacional para gerenciar perfis, vagas e interações.

🛠️ Tecnologias e Arquitetura
Backend & Persistência
Linguagem: Groovy (4.0+) / Java JDK 21.

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

🏃 Como Executar
1. Configuração do Banco de Dados
Certifique-se de ter o PostgreSQL instalado.

Crie um banco de dados chamado linketinder.

Execute os scripts SQL presentes na pasta /sql (ou crie as tabelas conforme as definições de classe).

2. Executando o Backend (Console)
Configure suas credenciais na classe Conexao.groovy.

Execute via Gradle:

Bash
./gradlew run
Utilize o menu interativo para navegar entre as áreas de Candidato, Empresa e Gerenciamento.

3. Testando o Frontend (Web)
Navegue até frontend_oficial.

Compile o TypeScript: tsc.

Abra index.html via Live Server.

Autora: Sarah Silva Lima 
