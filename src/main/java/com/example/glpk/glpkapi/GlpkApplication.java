package com.example.glpk.glpkapi;

import org.gnu.glpk.GLPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GlpkApplication {

  private static Logger LOG = LoggerFactory.getLogger(GlpkApplication.class);

  public static void main(String[] args) {
    loadGlpk();
    runApplication(args);
  }

  private static void runApplication(String[] args) {
    SpringApplication.run(GlpkApplication.class, args);
  }

  private static void loadGlpk() {
    try {
      LOG.info("GLPK Version: {}", GLPK.glp_version());
    } catch (Throwable e) {
      LOG.error("Failed to load GLPK", e);
      throw new IllegalStateException(e);
    }
  }
}
