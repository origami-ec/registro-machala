package org.origami.archivos.config;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Component
public class HttpRequestHandler extends ResourceHttpRequestHandler {

    public final static String ATTR_FILE =
            HttpRequestHandler.class.getName() + ".file";

    @Override
    protected Resource getResource(HttpServletRequest request) {
        final File file = (File) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(file);
    }

}