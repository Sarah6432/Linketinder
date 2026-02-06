# Linketinder - Projeto ZG-Hero (MVP)

A proposta é criar um sistema de contratação simplificado, inspirado na dinâmica de "Match" do Tinder e no perfil profissional do LinkedIn, utilizando a linguagem **Groovy**.

## 🚀 Sobre o Projeto
O Dr. Antônio Paçoca identificou que recrutadores perdem talentos por conta de algoritmos tendenciosos. O **Linketinder** surge para facilitar o encontro entre:
- **Candidatos:** Que expõem suas competências técnicas e descrição pessoal.
- **Empresas:** Que listam os requisitos desejados para suas vagas.

## 🛠️ Tecnologias Utilizadas
- **Linguagem:** Groovy (versão 4.0+)
- **Paradigma:** Orientação a Objetos (POO)
- **SDK:** Java JDK 17 ou superior

## 🏗️ Estrutura do Código
O sistema foi estruturado seguindo princípios de POO:
- **Interface `IPessoa`:** Define os comportamentos básicos.
- **Classe Abstrata `Pessoa`:** Reúne atributos comuns (Nome, E-mail, CEP, Competências, etc).
- **Herança:** As classes `Candidato` e `Empresa` herdam de `Pessoa`, garantindo a reutilização de código e organização.
- **Estrutura de Dados:** Utilização de `List` (ArrayList) para armazenamento em memória dos perfis pré-cadastrados.

## 🏃 Como Executar o Projeto
1. **Pré-requisitos:** Certifique-se de ter o Groovy instalado em sua máquina.
   - Você pode verificar usando o comando: `groovy -v`
3. **Clone o repositório:**
   ```bash
  - git clone [https://github.com/Sarah6432/Linketinder.git](https://github.com/Sarah6432/Linketinder.git)
   
