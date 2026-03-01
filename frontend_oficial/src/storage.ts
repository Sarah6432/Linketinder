import type { Candidato, Empresa } from './models.js';

export const banco = {
    salvarCandidato: (c: Candidato) => {
        const lista = banco.listarCandidatos();
        lista.push(c);
        localStorage.setItem('candidatos', JSON.stringify(lista));
    },
    listarCandidatos: (): Candidato[] => 
        JSON.parse(localStorage.getItem('candidatos') || '[]'),
        
    salvarEmpresa: (e: Empresa) => {
        const lista = banco.listarEmpresas();
        lista.push(e);
        localStorage.setItem('empresas', JSON.stringify(lista));
    },
    listarEmpresas: (): Empresa[] => 
        JSON.parse(localStorage.getItem('empresas') || '[]')
};