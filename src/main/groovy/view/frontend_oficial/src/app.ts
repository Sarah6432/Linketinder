import type { Candidato } from './models.js';
import { banco } from './storage.js';

declare var Chart: any;

const formCandidato = document.getElementById('formCadastro') as HTMLFormElement;
const formEmpresa = document.getElementById('formCadastroEmpresa') as HTMLFormElement;
const formVaga = document.getElementById('formVaga') as HTMLFormElement;
const formLoginCandidato = document.getElementById('formLoginCandidato') as HTMLFormElement;
const formLoginEmpresa = document.getElementById('formLoginEmpresa') as HTMLFormElement;

if (formCandidato) {
    formCandidato.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const nome = (document.getElementById('nome') as HTMLInputElement).value.trim();
        const cpf = (document.getElementById('cpf') as HTMLInputElement).value.trim();
        const skillsRaw = (document.getElementById('skills') as HTMLInputElement).value.trim();
        const formacao = (document.getElementById('formacao') as HTMLInputElement).value.trim();
        const detalhes = (document.getElementById('detalhes') as HTMLTextAreaElement).value.trim();

        if (!nome || !cpf || !skillsRaw) {
            alert('Por favor, preencha todos os campos obrigatórios.');
            return;
        }

        const novoCandidato: Candidato = {
            id: Date.now(),
            nome,
            email: "contato@linketinder.com",
            skills: skillsRaw.split(',').map(s => s.trim()).filter(s => s !== ""),
            cpf,
            detalhes: detalhes || "Perfil criado",
            formacao: formacao || "Não informada"
        };

        banco.salvarCandidato(novoCandidato);
        alert('Candidato cadastrado!');
        window.location.href = 'login_candidato.html';
    });
}

if (formEmpresa) {
    formEmpresa.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const nome = (document.getElementById('nomeEmpresa') as HTMLInputElement).value.trim();
        const cnpj = (document.getElementById('cnpj') as HTMLInputElement).value.trim();
        const email = (document.getElementById('emailEmpresa') as HTMLInputElement).value.trim();

        if (!nome || !cnpj || !email) {
            alert('Preencha todos os campos da empresa.');
            return;
        }

        const novaEmpresa = {
            id: Date.now(),
            nome,
            cnpj,
            email,
            vagas: []
        };

        const empresas = JSON.parse(localStorage.getItem('empresas') || '[]');
        empresas.push(novaEmpresa);
        localStorage.setItem('empresas', JSON.stringify(empresas));

        alert('Empresa cadastrada com sucesso!');
        window.location.href = 'login_empresa.html';
    });
}

if (formLoginCandidato) {
    formLoginCandidato.addEventListener('submit', (e) => {
        e.preventDefault();
        const cpf = (document.getElementById('loginCpf') as HTMLInputElement).value.trim();
        const user = banco.listarCandidatos().find(u => u.cpf === cpf);

        if (user) {
            sessionStorage.setItem('usuarioLogado', JSON.stringify(user));
            window.location.href = 'perfil.html?visao=candidato';
        } else {
            alert('CPF não encontrado!');
        }
    });
}

if (formLoginEmpresa) {
    formLoginEmpresa.addEventListener('submit', (e) => {
        e.preventDefault();
        const cnpj = (document.getElementById('loginCnpj') as HTMLInputElement).value.trim();
        const empresas = JSON.parse(localStorage.getItem('empresas') || '[]');
        const emp = empresas.find((e: any) => e.cnpj === cnpj);

        if (emp) {
            sessionStorage.setItem('empresaLogada', JSON.stringify(emp));
            window.location.href = 'perfil.html?visao=empresa';
        } else {
            alert('CNPJ não encontrado!');
        }
    });
}

if (formVaga) {
    formVaga.addEventListener('submit', (e) => {
        e.preventDefault();
        const titulo = (document.getElementById('tituloVaga') as HTMLInputElement).value.trim();
        const desc = (document.getElementById('descVaga') as HTMLTextAreaElement).value.trim();
        const skills = (document.getElementById('skillsVaga') as HTMLInputElement).value.trim();

        if (!titulo || !skills) {
            alert('Título e Skills são obrigatórios para a vaga.');
            return;
        }

        const novaVaga = { id: Date.now(), titulo, desc, skills: skills.split(',').map(s => s.trim()) };
        const vagas = JSON.parse(localStorage.getItem('vagas') || '[]');
        vagas.push(novaVaga);
        localStorage.setItem('vagas', JSON.stringify(vagas));

        alert('Vaga publicada!');
        window.location.href = 'perfil.html?visao=empresa';
    });
}

function limparCandidatosInvalidos() {
    const candidatos = banco.listarCandidatos();
    const candidatosFiltrados = candidatos.filter(c => 
        c.nome && c.nome.trim() !== "" && 
        c.cpf && c.cpf.trim() !== ""
    );

    if (candidatos.length !== candidatosFiltrados.length) {
        localStorage.setItem('candidatos', JSON.stringify(candidatosFiltrados));
    }
}

function renderizarTelaPerfil() {
    limparCandidatosInvalidos();

    const urlParams = new URLSearchParams(window.location.search);
    const visao = urlParams.get('visao');
    const containerCandidatos = document.getElementById('listaCandidatos');
    const containerVagas = document.getElementById('containerVagas');
    const labelPerfil = document.getElementById('meuPerfil');

    const secaoGrafico = document.querySelector('.chart-section') as HTMLElement;
    const secaoCandidatos = document.querySelector('.lista-anonima') as HTMLElement;
    const secaoVagas = document.querySelector('.lista-vagas') as HTMLElement;

    if (visao === 'candidato') {
        const user = JSON.parse(sessionStorage.getItem('usuarioLogado') || '{}');
        if (labelPerfil) labelPerfil.innerText = `Olá, ${user.nome || 'Candidato'}`;
        if (secaoGrafico) secaoGrafico.style.display = 'none';
        if (secaoCandidatos) secaoCandidatos.style.display = 'none';
        if (secaoVagas) secaoVagas.style.display = 'block';
    } else if (visao === 'empresa') {
        const emp = JSON.parse(sessionStorage.getItem('empresaLogada') || '{}');
        if (labelPerfil) labelPerfil.innerText = `Painel: ${emp.nome || 'Empresa'}`;
        if (secaoVagas) secaoVagas.style.display = 'none';
        if (secaoGrafico) secaoGrafico.style.display = 'block';
        if (secaoCandidatos) secaoCandidatos.style.display = 'block';
    }

    const candidatos = banco.listarCandidatos();
    const vagas = JSON.parse(localStorage.getItem('vagas') || '[]');

    if (containerCandidatos) {
        containerCandidatos.innerHTML = candidatos.length === 0 ? "<p>Nenhum candidato válido.</p>" : 
        candidatos.map(c => `
            <div class="item-anonimo">
                <h3>Candidato Anônimo</h3>
                <p>Skills: ${c.skills.join(', ')}</p>
              <button class="btn" style="margin-top: 10px">Dar Match</button>
            </div>
        `).join('');
    }

    if (containerVagas) {
        containerVagas.innerHTML = vagas.length === 0 ? "<p>Nenhuma vaga cadastrada.</p>" :
        vagas.map((v: any) => `
            <div class="item-vaga">
                <h3>${v.titulo}</h3>
                <p>${v.desc || ''}</p>
                <div class="tags-container">
                    ${v.skills.map((s: string) => `<span class="tag">${s}</span>`).join('')}
                </div>
                <button class="btn btn-outline">Candidatar-se</button>
            </div>
        `).join('');
    }
    
    if (visao === 'empresa') inicializarGrafico(candidatos);
}

function inicializarGrafico(candidatos: any[]) {
    const ctx = document.getElementById('graficoSkills') as HTMLCanvasElement;
    if (!ctx) return;
    const contagem: any = {};
    candidatos.forEach(c => c.skills.forEach((s: string) => {
        const skill = s.trim().toLowerCase();
        if (skill) contagem[skill] = (contagem[skill] || 0) + 1;
    }));
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: Object.keys(contagem).map(s => s.toUpperCase()),
            datasets: [{ label: 'Skills', data: Object.values(contagem), backgroundColor: '#6366f1' }]
        },
        options: {
            scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
        }
    });
}

if (window.location.pathname.includes('perfil.html')) {
    renderizarTelaPerfil();
}