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

@WebServlet("/api/empresas")
class EmpresaServlet extends HttpServlet{
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
            throw new ServletException("Erro na inicialização do EmpresaServlet", e.printStackTrace())
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

            empresaService.registrarEmpresa(dados)

            response.status = HttpServletResponse.SC_CREATED
            response.writer.print(gson.toJson([mesagem: "Empresa cadastrada com sucesso!"]))
        } catch (Exception e) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            response.writer.print(gson.toJson([erro: "Falha no cadastro: ${e.printStackTrace()}"]))
        }

    }
}
