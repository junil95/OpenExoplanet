package UpdateTools;

import org.eclipse.egit.github.core.Repository;

import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;

/**
 * Created by dhrumil on 26/11/16.
 */
public class SendPullRequest {
  public static void main(String[] args) {
//    GitHubClient client = new GitHubClient();
//    client.setOAuth2Token("a0e0b081561d3abaeae3bd2536b929d2c2c607d2");
    RepositoryService service = new RepositoryService();
    service.getClient().setOAuth2Token("a0e0b081561d3abaeae3bd2536b929d2c2c607d2");
    Repository repo = new Repository();
    repo.setCloneUrl("https://github.com/DhrumilSoft/Token-Test");
    PullRequestService prs = new PullRequestService();
    //PullRequest pr = new PullRequestService()
    try {
      prs.createPullRequest(repo, );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
