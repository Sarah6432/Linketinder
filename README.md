Linketinder - Projeto ZG-Hero (MVP)
A proposta é criar um sistema de contratação simplificado, inspirado na dinâmica de "Match" do Tinder e no perfil profissional do LinkedIn. O projeto evoluiu de uma aplicação de console em Groovy para uma interface web moderna.

🚀 Sobre o Projeto
O Dr. Antônio Paçoca identificou que recrutadores perdem talentos por conta de algoritmos tendenciosos. O Linketinder surge para facilitar o encontro entre:
Candidatos: Expõem competências técnicas e descrição pessoal.
Empresas: Listam requisitos desejados para suas vagas.

🛠️ Tecnologias Utilizadas
Backend (Lógica de Negócio)
Linguagem: Groovy (versão 4.0+)

SDK: Java JDK 17 ou superior

Frontend (Interface Web)
Linguagem: TypeScript (compilado para JavaScript ES6)
Estilização: CSS3 Moderno (Flexbox, Grid, Variáveis e Media Queries para Responsividade)
Gráficos: Chart.js para visualização de dados de competências
Persistência: LocalStorage e SessionStorage para simulação de banco de dados no navegador

🏗️ Estrutura do Código
O sistema segue princípios de POO e organização modular:
Interface IPessoa: Define comportamentos básicos.
Herança: Classes Candidato e Empresa herdam de uma base comum, garantindo reutilização.
Validação de Dados: Implementada lógica no frontend para impedir cadastros com campos vazios ou apenas espaços.
Responsividade: Media queries garantem que os cards de login e visualização fiquem centralizados em dispositivos móveis.

💻 Implementação do Frontend
Nesta etapa, a aplicação ganhou uma interface visual completa:
Tela de Login/Cadastro: Fluxos distintos para Candidatos e Empresas com validação de inputs.
Dashboard de Vagas: Cards estendidos e estilizados com foco em legibilidade e interatividade.
Painel da Empresa: Visualização de gráficos de skills dos candidatos utilizando Chart.js.
Sistema de Match: Botão interativo nos cards de candidatos para simular o interesse da empresa.

🏃 Como Executar e Testar
Testando o Frontend (Web)
Navegue até a pasta frontend_oficial.
Certifique-se de que o TypeScript está instalado em seu sistema: npm install -g typescript.
Compile os arquivos .ts para a pasta dist: tsc.
Abra o arquivo index.html em seu navegador (recomendado utilizar a extensão Live Server no VS Code).

Fluxo de Teste:
Realize o cadastro de um novo candidato (campos vazios serão bloqueados).
Faça login como Empresa para visualizar o gráfico de competências dos candidatos cadastrados.
No perfil de empresa, utilize o botão "Dar Match" nos cards de candidatos.

Testando o Backend (Console)
Certifique-se de ter o Groovy instalado: groovy -v.
Execute o programa: groovy Linketinder.groovy.

Escolha a opção 3 (Candidato curte Empresa) e a opção 4 (Empresa curte Candidato) com os mesmos índices para detectar um MATCH no console.
