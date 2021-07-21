package shah.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shah.models.AuthenticationRequest;
import shah.models.AuthenticationResponse;

@RestController
public class HelloResource {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private MyUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;
  
  // 10001
  @RequestMapping({"hello"})
  public String hello() {
    return "<h1>Hello World</h1>";
  }
  // 10007
  @PostMapping({"authenticate"})
  public ResponseEntity<?> createJwtToken(@RequestBody AuthenticationRequest ar) throws Exception {
    try {
      
      authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(ar.getUsername(), ar.getPassword()));
    } catch (BadCredentialsException e) {
      throw new Exception("Incorrect username or password");
    }
    final UserDetails userdetails = userDetailsService.loadUserByUsername(ar.getUsername());
    final String jwt = jwtUtil.generateToken(userdetails);
    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }
}
