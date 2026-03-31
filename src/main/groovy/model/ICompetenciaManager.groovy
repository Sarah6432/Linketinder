package model

interface ICompetenciaManager {
    void vincular(int entidadeId, String nomeSkill)
    void desvincular(int id, String nomeSkill)
}