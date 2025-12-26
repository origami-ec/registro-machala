package org.origami.zull.conf;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.origami.zull.entity.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class TransactionFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TransactionFilter.class);
    //@Autowired
    //private KafkaLogServices kafkaLogServices;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    private String msg = "";

    @Override
    public void doFilter(ServletRequest request, javax.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        HttpServletResponse res = (HttpServletResponse) response;

        // logger.info("Headers : {}", "Authorization" + "=" +
        // req.getHeader("Authorization"));
        // logger.info(String.format("%s request to %s", req.getMethod(),
        // req.getRequestURL().toString()));

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Map<String, String> headers = Collections.list(httpRequest.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, httpRequest::getHeader));

        logger.info("headers {}", headers.toString());

        msg = "Inicio de transaccion para  : {" + req.getRequestURI() + "} \n  Log Request  {" + req.getMethod() + "} {"
                + req.getRequestURI() + "}";

        String ipUserSession = headers.get("ip") != null ? headers.get("ip") : req.getRemoteAddr();
        String macClient = headers.get("mac") != null ? headers.get("mac") : "";
        String osClient = headers.get("os") != null ? headers.get("os") : "";
        String user = headers.get("user") != null ? headers.get("user") : "";

        //kafkaLogServices.sendMessageLogFile(msg);
        /*
         * Authentication authentication =
         * SecurityContextHolder.getContext().getAuthentication(); String
         * currentPrincipalName = authentication.getName();
         * logger.info("currentPrincipalName: {} ", currentPrincipalName);
         */

        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // User user = (User) auth.getPrincipal();
        msg = "Confirmacion de transaccion para  : {" + req.getRequestURI() + "} \n Log Response  {"
                + res.getContentType() + "}";
        chain.doFilter(request, response);
        logger.info("{} ", msg);
        //kafkaLogServices.sendMessageLogFile(msg);
        guardarLog(req.getMethod(), req.getRequestURL().toString(), ipUserSession, res.getContentType(), macClient, user, osClient);
    }

    @Async
    void guardarLog(String accion, String url, String ip, String response, String mac, String userApp, String osClient) {
        String app = "";
        Logs logs = new Logs();
        if (url.contains("/servicios/")) {
            app = "OrigamiGTWS";
        } else if (url.contains("/servicios-firma-ec/")) {
            app = "OrigamiGTFirmaEC";
        } else if (url.contains("/servicios-mail/")) {
            app = "OrigamiGTCorreo";
        } else if (url.contains("/servicios-media/")) {
            app = "OrigamiGTArchivos";
        } else if (url.contains("/servicios-logs/")) {
            app = "OrigamiGTLogs";
        } else if (url.contains("authenticate")) {
            app = "OrigamiGTZull";
        } else if (url.contains("/servicios-docs/")) {
            app = "OrigamiGTDocs";
        }
        logs.setApp(app);
        logs.setDescripcion("Logs WS OrigamiGT");
        logs.setIp(ip);
        logs.setResponse(response);
        logs.setUsuario("servicios-ws");
        logs.setAccion(accion);
        logs.setFecha(new Date());
        logs.setRequest(url);
        logs.setMac(mac);
        logs.setOsClient(osClient);
        logs.setUsuarioApp(userApp);
        //kafkaLogServices.sendMessageLogAsgard(new Gson().toJson(logs));
    }
}
