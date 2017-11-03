package com.example.glpk.glpkapi;

import org.springframework.stereotype.Service;

@Service
public class GlpkService {

  public String solve() {
    new Gmpl().solve();
    return "done";
  }
}
