package org.origami.zull.resources;

import org.origami.zull.entity.Usuario;
import org.origami.zull.security.JwtUserDetailsService;
import org.origami.zull.service.JwtDesk;
import org.origami.zull.service.JwtRequest;
import org.origami.zull.service.JwtResponse;
import org.origami.zull.service.UsuarioService;
import org.origami.zull.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
public class JwtAuthenticationResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(jwtTokenUtil.generateToken(userDetails)));
    }


    @RequestMapping(value = "/authenticateDesk", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationTokenDesk(@RequestBody JwtRequest authenticationRequest) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        Usuario u = usuarioService.consultarUsuario(authenticationRequest.getUsername());
        JwtDesk jwtDesk = new JwtDesk(jwtTokenUtil.generateToken(userDetails), u.getId(), u.getUsuarioNombre(), u.getNombreUsuario());

        return ResponseEntity.ok(jwtDesk);
    }

    protected void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}