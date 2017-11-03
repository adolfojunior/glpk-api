package com.example.glpk.glpkapi;

import org.gnu.glpk.GLPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GlpkApi {

  @Autowired
  private GlpkService glpkService;

  @GetMapping("/version")
  public String get() {
    return GLPK.glp_version();
  }

  @PostMapping("/solve")
  public String solve() {
    return glpkService.solve();
  }
}
