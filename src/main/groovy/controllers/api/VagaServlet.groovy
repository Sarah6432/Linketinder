package controllers.api

import com.google.gson.Gson
import config.Conexao
import config.ServiceFactory
import groovy.sql.Sql
import service.EmpresaService

import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/api/vagas")
class VagaServlet extends HttpServlet{
    private EmpresaService empresaService
    private Gson gson = new Gson()

    @Override
    void init() throws ServletException {
        try{
            Sql sql = Conexao.getConn()
            this.empresaService = ServiceFactory.createEmpresaService(sql)
            if(this.empresaService == null){
                throw new ServletException("Falha ao instanciar CandidatoService")
            }
        }catch (Exception e){
            throw new ServletException("Erro na inicialização do VagaServlet", e.printStackTrace())
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        try {
            BufferedReader reader = request.getReader()
            Map dados = gson.fromJson(reader, Map)

            if(!dados.empresaId || !dados.titulo) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
                response.getWriter().print(gson.toJson([erro: "empresaId e titulo são obrigatórios"]))
                return
            }

            empresaService.anunciarVaga(
                    (int) dados.empresaId,
                    (String) dados.titulo,
                    (String) dados.descricao,
                    (String) dados.local_estado_cidade,
                    (List<String>) dados.requisitos
            )

            response.status = HttpServletResponse.SC_CREATED
            response.getWriter().print(gson.toJson([mesagem: "Vaga anunciada com sucesso"]))

     } catch (Exception e) {
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            response.writer.print(gson.toJson([erro: "Falha ao processar vaga: ${e.printStackTrace()}"]))
        }

    }
}
