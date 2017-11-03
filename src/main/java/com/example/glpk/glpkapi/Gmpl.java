package com.example.glpk.glpkapi;

import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.GlpkCallback;
import org.gnu.glpk.GlpkCallbackListener;
import org.gnu.glpk.GlpkTerminal;
import org.gnu.glpk.GlpkTerminalListener;
import org.gnu.glpk.glp_iocp;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_tran;
import org.gnu.glpk.glp_tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gmpl implements GlpkCallbackListener, GlpkTerminalListener {

  private static Logger LOG = LoggerFactory.getLogger(GlpkService.class);

  public Gmpl() {
    GLPK.glp_java_set_numeric_locale("C");
  }

  public void solve() {
    glp_prob lp;
    glp_tran tran;
    glp_iocp iocp;

    String fname;
    int skip = 0;
    int ret;

    // listen to callbacks
    GlpkCallback.addListener(this);

    // listen to terminal output
    GlpkTerminal.addListener(this);

    // create problem
    lp = GLPK.glp_create_prob();

    // allocate workspace
    tran = GLPK.glp_mpl_alloc_wksp();

    // read model
    fname = "sample";
    ret = GLPK.glp_mpl_read_model(tran, fname, skip);
    if (ret != 0) {
      GLPK.glp_mpl_free_wksp(tran);
      GLPK.glp_delete_prob(lp);
      throw new RuntimeException("Model file not valid: " + fname);
    }

    // generate model
    ret = GLPK.glp_mpl_generate(tran, null);
    if (ret != 0) {
      GLPK.glp_mpl_free_wksp(tran);
      GLPK.glp_delete_prob(lp);
      throw new RuntimeException("Cannot generate model: " + fname);
    }

    // build model
    GLPK.glp_mpl_build_prob(tran, lp);

    // set solver parameters
    iocp = new glp_iocp();
    GLPK.glp_init_iocp(iocp);
    iocp.setPresolve(GLPKConstants.GLP_ON);

    // do not listen to output anymore
    GlpkTerminal.removeListener(this);

    // solve model
    ret = GLPK.glp_intopt(lp, iocp);

    // postsolve model
    if (ret == 0) {
      GLPK.glp_mpl_postsolve(tran, lp, GLPKConstants.GLP_MIP);
    }

    // free memory
    GLPK.glp_mpl_free_wksp(tran);
    GLPK.glp_delete_prob(lp);

    // do not listen for callbacks anymore
    GlpkCallback.removeListener(this);
  }

  @Override
  public boolean output(String str) {
    LOG.info(str);
    return false;
  }

  @Override
  public void callback(glp_tree tree) {
    int reason = GLPK.glp_ios_reason(tree);
    if (reason == GLPKConstants.GLP_IBINGO) {
      System.out.println("Better solution found");
    }
  }
}
