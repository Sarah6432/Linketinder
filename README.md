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
2. **Clone o repositório:**
   ```bash
 ## 🚀 Atualização: Implementação do Sistema de Match

Nesta nova etapa do projeto Linketinder, foi implementada a funcionalidade principal da aplicação: o sistema de curtidas e a detecção de interesse mútuo entre candidatos e empresas.

### 🛠️ O que há de novo:

* **Lógica de Interação Intermediária:** Criada a classe `Curtida`, que funciona como uma ponte entre os objetos `Candidato` e `Empresa`. Ela armazena o estado do interesse de cada parte (booleano).
* **Evento de Match:** O sistema agora detecta automaticamente quando um candidato curtiu uma empresa **e** essa mesma empresa curtiu o candidato de volta. 
* **Anonimato vs. Identificação:** Seguindo o conceito do Dr. Antônio Paçoca, o sistema permite que as partes interajam com base em competências, disparando uma notificação visual de "Match" quando a conexão é estabelecida.
* **Menu Interativo Expandido:** O terminal agora conta com opções de simulação (3 e 4) para testar os fluxos de curtida e validar a regra de negócio.

### 🧪 Como Testar o Match:
1. Execute o programa: `groovy Linketinder.groovy`.
2. Escolha a **opção 3** (Candidato curte Empresa), informe o índice do candidato (ex: 0) e o índice da empresa (ex: 4).
3. Escolha a **opção 4** (Empresa curte Candidato), informe o índice da empresa (ex: 4) e o índice do candidato (ex: 0).
4. O console exibirá o alerta de **MATCH DETECTADO!**.  
  - git clone [https://github.com/Sarah6432/Linketinder.git](https://github.com/Sarah6432/Linketinder.git)
   
