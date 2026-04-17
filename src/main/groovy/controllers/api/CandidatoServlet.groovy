package controllers.api

import com.google.gson.Gson
import config.Conexao
import config.ServiceFactory
import groovy.sql.Sql
import service.CandidatoService

import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.BufferedReader
import java.io.IOException

@WebServlet("/api/candidatos")
class CandidatoServlet extends HttpServlet {
    private CandidatoService candidatoService
    private Gson gson = new Gson()

    @Override
    void init() throws ServletException {
        try {
            Sql sql = Conexao.getConn()
            this.candidatoService = ServiceFactory.createCandidatoService(sql)
            if (this.candidatoService == null) {
                throw new ServletException("Falha ao instanciar CandidatoService")
            }
        } catch (Exception e) {
            e.printStackTrace()
            throw new ServletException("Erro na inicializacao do CandidatoServlet: " + e.getMessage())
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        try {
            BufferedReader reader = request.getReader()
            Map dados = gson.fromJson(reader, Map.class)

            if (dados == null) {
                response.status = HttpServletResponse.SC_BAD_REQUEST
                response.writer.print(gson.toJson([erro: "JSON invalido ou vazio"]))
                return
            }

            candidatoService.registrarNovoCandidato(dados)

            response.status = HttpServletResponse.SC_CREATED
            response.writer.print(gson.toJson([mensagem: "Candidato cadastrado com sucesso!"]))
        } catch (Exception e) {
            e.printStackTrace()
            response.status = HttpServletResponse.SC_BAD_REQUEST
            String msgErro = "Falha no cadastro: " + (e.getMessage() ?: e.toString())
            response.writer.print(gson.toJson([erro: msgErro]))
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        try {
            def lista = candidatoService.listarVagasDisponiveis()
            response.status = HttpServletResponse.SC_OK
            response.writer.print(gson.toJson(lista))
        } catch (Exception e) {
            e.printStackTrace()
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            String msgErro = "Erro ao listar: " + e.getMessage()
            response.writer.print(gson.toJson([erro: msgErro]))
        }
    }
}