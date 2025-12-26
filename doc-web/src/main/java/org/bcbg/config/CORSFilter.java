/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.config;

import org.bcbg.services.ejbs.MenuCache;
import org.bcbg.session.UserSession;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.util.Constants;

/**
 *
 * @author ANGEL NAVARRO
 */
@WebFilter(filterName = "CORSFilter", urlPatterns = {"*.xhtml"})
public class CORSFilter implements Filter {

    @Inject
    private UserSession session;

    @Inject
    private MenuCache menuCache;

    /**
     * Default constructor.
     */
    public CORSFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @param servletRequest
     * @param servletResponse
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // Authorize (allow) all domains to consume the content
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, UPDATE, HEAD, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
        // response.addHeader("X-Frame-Options", "SAMEORIGIN");
        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        String pfdlgcid = request.getParameter(Constants.DialogFramework.CONVERSATION_PARAM);
        if (pfdlgcid != null) {
            chain.doFilter(request, response);
            return;
        }
        //  allow user to proccede if url is login.xhtml or user logged in or user is accessing any page in //public folder
        String reqURI = request.getRequestURI();
        //System.out.println("uri " + reqURI);
        if (reqURI.contains("login")
                || (session.getName_user() != null)
                || reqURI.contains("javax.faces.resource")
                || reqURI.contains("resource")
                || reqURI.contains("activarUsuario") /*) {*/) {
            try {
                if (reqURI.equals("/") || reqURI.equals("/ventanillaInteligente/") || reqURI.equals("/ventanillaInteligente")) {
                    //response.sendRedirect(request.getContextPath() + "/procesos/dashBoard.xhtml");
                    reqURI = "/procesos/dashBoard";
                    RequestDispatcher dd = request.getRequestDispatcher(reqURI);
                    dd.forward(request, response);
                }
                chain.doFilter(request, response);
            } catch (IOException | ServletException iOException) {
                System.out.println("Error CORSFilter: " + iOException.getLocalizedMessage() + " >> " + iOException.getMessage());
            }
        } else {
            System.out.println("COSfilter reqURI == ventanillaInteligente? " + reqURI.equals("/ventanillaInteligente/") + " reqUri= " + reqURI);
            if (reqURI.equals("/") || reqURI.equals("/ventanillaInteligente/")) {
                System.out.println("entro");
                // response.sendRedirect(request.getContextPath() + "/login");
                reqURI = "/login";
                RequestDispatcher dd = request.getRequestDispatcher(reqURI);
                dd.forward(request, response);
            } else if (reqURI.contains("/rest/")) {
                System.out.println("consulta rest " + reqURI);
                reqURI = reqURI.replace("/ventanillaInteligente", "");
                System.out.println("reqURI " + reqURI);
                RequestDispatcher dd = request.getRequestDispatcher(reqURI);
                dd.forward(request, response);
            } else if (reqURI.contains("admin/manage/recuperarClave")) {
//                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                RequestDispatcher dd = request.getRequestDispatcher(reqURI);
                dd.forward(request, response);
            } else {
                System.out.println("request.getContextPath() " + request.getContextPath());
                reqURI = "/login";
                System.out.println("new reqURI " + reqURI);
                RequestDispatcher dd = request.getRequestDispatcher(reqURI);
                dd.forward(request, response);
            }
//            chain.doFilter(request, response);
        }
    }

    /**
     * @param fConfig
     * @throws javax.servlet.ServletException
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
