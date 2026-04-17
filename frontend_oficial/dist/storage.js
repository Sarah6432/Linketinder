export const banco = {
    salvarCandidato: (c) => {
        const lista = banco.listarCandidatos();
        lista.push(c);
        localStorage.setItem('candidatos', JSON.stringify(lista));
    },
    listarCandidatos: () => JSON.parse(localStorage.getItem('candidatos') || '[]'),
    salvarEmpresa: (e) => {
        const lista = banco.listarEmpresas();
        lista.push(e);
        localStorage.setItem('empresas', JSON.stringify(lista));
    },
    listarEmpresas: () => JSON.parse(localStorage.getItem('empresas') || '[]')
};
//# sourceMappingURL=storage.js.map