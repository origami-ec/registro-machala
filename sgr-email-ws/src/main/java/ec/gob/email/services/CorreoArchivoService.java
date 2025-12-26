package ec.gob.email.services;

import ec.gob.email.repository.CorreoArchivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorreoArchivoService {
    @Autowired
    private CorreoArchivoRepository repository;
}
