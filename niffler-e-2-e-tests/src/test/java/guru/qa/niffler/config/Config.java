package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();
  String spendJdbcUrl();
  String spendUrl();
  String githubUrl();
  String userDataUrl();
  String gatewayUrl();
  String authJdbcUrl();
}
