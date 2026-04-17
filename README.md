# 🚀 Linketinder - Sistema de Recrutamento Full Stack

O **Linketinder** é um ecossistema de contratação simplificado que une a dinâmica de "Match" à curadoria técnica de talentos. O projeto evoluiu de uma estrutura simples em memória para uma aplicação **Data-Driven** robusta, utilizando persistência em banco de dados relacional e uma arquitetura orientada a objetos de alta qualidade.

---

## 🛠️ Tecnologias e Arquitetura

### **Backend & Persistência**
* **Linguagens:** Groovy (4.0+) / Java JDK 21.
* **Gerenciamento de Dependências:** Gradle 5.1.1.
* **Banco de Dados:** PostgreSQL.
* **Servidor Web:** Apache Tomcat 9.0.
* **Arquitetura:** Padrão DAO (Data Access Object) com Injeção de Dependência.

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
* **CRUD Completo:** Interfaces para Criar, Listar, Atualizar e Deletar qualquer entidade com integridade referencial (**ON DELETE CASCADE**).

---

## 🏛️ Design Patterns & Princípios SOLID

A arquitetura foi refatorada para seguir padrões de mercado, garantindo um código limpo e escalável:

| Padrão | Aplicação no Projeto |
| :--- | :--- |
| **Factory Method** | Centralizado na `ServiceFactory` para instanciar DAOs e Services. |
| **DAO (Data Access Object)** | Isolamento total da lógica SQL em classes especialistas. |
| **Injeção de Dependência** | Dependências injetadas via construtor para facilitar testes e desacoplamento. |
| **MVC / SoC** | Separação rigorosa entre Modelo (DAO), Visão (Servlet/Web) e Controle (Services). |

---

## 📡 Guia de Execução da API (Backend)

Para executar e testar as funcionalidades da API, siga os passos abaixo:

### **1. Configuração do Banco de Dados**
1. Certifique-se de ter o **PostgreSQL** instalado.
2. Crie um banco chamado `linketinder`.
3. Execute os scripts SQL da pasta `/sql` para criar as tabelas e relacionamentos.

### **2. Configuração no IntelliJ (Smart Tomcat)**
1. Vá em **Run > Edit Configurations**.
2. Adicione uma nova configuração de **Smart Tomcat**.
3. Em **Environment Variables**, adicione as credenciais do seu banco:
    * `DB_PASS=sua_senha`
    * `DB_USER=seu_usuario`
4. Clique em **Run** para iniciar o servidor na porta `8080`.

### **3. Testando com Postman**
Para cadastrar um novo candidato, faça uma requisição **POST** para `http://localhost:8080/api/candidatos`:
* **Header:** `Content-Type: application/json`
* **Body (raw JSON):**
```json
{
    "nome": "Sarah",
    "email": "sarah@teste.com",
    "skills": ["Groovy", "Java", "SQL"],
    "sobrenome": "Silva",
    "pais": "Brasil",
    "cep": "12345-678",
    "bio": "Desenvolvedora Backend",
    "cpf": "12345678900",
    "data": "1995-05-15",
    "senha": "123"
}

```
### **🚀 Como Executar o Frontend**
Navegue até a pasta frontend_oficial.

Compile o TypeScript executando tsc.

Abra o arquivo index.html utilizando o Live Server para consumir a API rodando localmente.

Autora: Sarah Silva Lima

Desenvolvido como projeto educacional focado em Engenharia de Software e Clean Code.
