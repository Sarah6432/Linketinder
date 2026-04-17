# 🚀 Linketinder - Sistema de Recrutamento Full Stack

O **Linketinder** é um ecossistema de contratação simplificado que une a dinâmica de "Match" à curadoria técnica de talentos. O projeto evoluiu de uma estrutura simples em memória para uma aplicação **Data-Driven** robusta, utilizando persistência em banco de dados relacional e uma arquitetura orientada a objetos de alta qualidade.

---

## 🛠️ Tecnologias e Arquitetura

### **Backend & Persistência**
* **Linguagens:** Groovy (4.0+) / Java JDK 21.
* **Gerenciamento de Dependências:** Gradle 5.1.1.
* **Banco de Dados:** PostgreSQL.
* **Drivers:** JDBC para integração Groovy-SQL.
* **Servidor Web:** Apache Tomcat 9 (v2.0).

### **Frontend (Interface Web)**
* **Linguagem:** TypeScript (ES6).
* **Estilização:** CSS3 Moderno (Flexbox, Grid, Variáveis).
* **Gráficos:** Chart.js para analytics de competências.

---

## ✨ Novas Funcionalidades (V2.0 - Database Integrated)

* **Persistência Relacional:** Todo o fluxo de dados agora é armazenado permanentemente no PostgreSQL.
* **Gerenciamento de Competências (N:N):** Sistema de normalização onde competências são entidades únicas vinculadas a múltiplos candidatos e vagas através de tabelas associativas.
* **Fluxo de Match Real:** * **Candidatos:** Visualizam vagas (requisitos/descrição) e registram interesse.
    * **Empresas:** Visualizam interessados de forma anônima (foco em competências) e retribuem o "like".
    * **Algoritmo de Match:** Identificação automática de interesse mútuo via queries SQL de interseção.
* **CRUD Completo:** Interfaces para Criar, Listar, Atualizar e Deletar qualquer entidade com integridade referencial (**ON DELETE CASCADE**).

---

## 📂 Estrutura do Banco de Dados

O projeto utiliza uma modelagem relacional otimizada para garantir a performance:
* `candidatos` / `empresas`: Entidades principais de usuários.
* `vagas`: Vinculadas a empresas via chave estrangeira.
* `competencias`: Tabela mestra de habilidades técnicas.
* `candidato_competencias` / `vagas_competencias`: Tabelas de relacionamento muitos-para-muitos.
* `curtidas_candidato` / `curtidas_empresa`: Registro de interações para cálculo de Match.

---

## 🏛️ Design Patterns & Princípios SOLID

A arquitetura foi refatorada para seguir padrões de mercado, garantindo um código limpo e escalável:

| Padrão | Aplicação no Projeto |
| :--- | :--- |
| **Factory Method** | Centralizado na `ServiceFactory` para instanciar DAOs e Services. |
| **DAO (Data Access Object)** | Isolamento total da lógica SQL em classes especialistas. |
| **Injeção de Dependência** | Dependências injetadas via construtor para facilitar testes e desacoplamento. |
| **Strategy** | Uso de interfaces de persistência permitindo trocar o meio de salvamento facilmente. |
| **MVC / SoC** | Separação rigorosa entre Modelo (DAO), Visão (CLI/Web) e Controle (Services). |

### **Correções Técnicas de Clean Code:**
* **DRY (Don't Repeat Yourself):** Centralização da lógica de exibição no método `exibirPerfil()`.
* **Padrão AAA nos Testes:** Estrutura clara de **Arrange, Act, Assert** para testes unitários.
* **Nomes Intencionais:** Refatoração de métodos para nomes semânticos (ex: `shouldRegisterNewCandidatoCorrectly`).
* **Tratamento de Erros:** Arquitetura de propagação de exceções, evitando falhas silenciosas.

---

## 📡 Endpoints da API (Atualização V2.0)

Com a integração do Tomcat, o sistema agora expõe endpoints RESTful para o Frontend:

* `POST /api/candidatos`: Realiza o cadastro de novos talentos e suas competências.
* `GET /api/candidatos`: Recupera a lista de vagas compatíveis para o perfil logado.
* `POST /api/empresas`: Cadastra organizações no ecossistema.
* `POST /api/vagas`: Permite a criação de novas oportunidades vinculadas a uma empresa.

---

## 🚀 Como Executar

### **1. Configuração do Banco de Dados**
1. Certifique-se de ter o **PostgreSQL** instalado e rodando.
2. Crie um banco de dados chamado `linketinder`.
3. Execute os scripts SQL presentes na pasta `/sql`.

### **2. Executando o Backend**
1. Configure suas credenciais (Usuário e Senha) nas variáveis de ambiente do seu servidor (ou via `Smart Tomcat`).
2. Execute via Gradle:
   ```bash
   ./gradlew run

### **3. Executando o Frontend**
   Navegue até a pasta frontend_oficial.

Compile o TypeScript: tsc.

Abra o arquivo index.html (recomenda-se o uso da extensão Live Server no VS Code).

Autora: Sarah Silva Lima

Desenvolvido como projeto educacional focado em Engenharia de Software e Clean Code.