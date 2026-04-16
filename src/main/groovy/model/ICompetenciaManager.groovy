package model

interface ICompetenciaManager {
    void vincularCompetencia(int donoId, String nomeSkill)
    void desvincularCompetencia(int donoId, String nomeSkill)
    void atualizarCompetenciasNoBanco(int donoId, List<String> novasSkills)
}