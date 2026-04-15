export interface Candidato {
    id: number;
    nome: string; 
    cpf: string;
    email: string;
    detalhes: string;
    formacao: string;
    skills: string[];
}

export interface Empresa {
    id: number;
    nome: string;
    cnpj: string;
    detalhes: string;
    local: string;
    vagas: string[];
}