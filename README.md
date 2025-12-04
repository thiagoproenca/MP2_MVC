# Miniprojeto Web – Sistema de Reservas

Projeto da disciplina de Desenvolvimento Web.  
Usa padrão MVC com **Servlets, JSP e MySQL**, organizado em projeto **Maven**.

## Como rodar
1. Instalar Maven.
2. No terminal, dentro do projeto: `mvn tomcat7:run`
3. Acessar no navegador: `http://localhost:8080/reservas`

## Estrutura básica
src/main/java/com/restaurante/reservas/
- controllers/ -> Servlets
- dao/ -> Acesso ao banco
- model/ -> Classes do sistema

src/main/webapp/
- pages/ -> JSPs
- WEB-INF/ -> web.xml

## Convenção simples
- Servlets ficam em **controllers**
- DAO em **dao**
- Modelos em **model**
- Páginas JSP vão em **pages**

## Observação
As pastas já estão no repositório (com .gitkeep) para facilitar o desenvolvimento do grupo.