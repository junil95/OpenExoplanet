package UpdateTools;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
/**
 * Created by dhrumil on 26/11/16.
 */
public class SendPullRequest {
  /**
   * Owner of the repository
   */
  public static final String repoOwner = "DhrumilSoft";
  
  /**
   * Repo name
   */
  public static final String repoName = "open_exoplanet_catalogue";
  
  public static final String mainBranch = "master";
  
  /**
   * Create pull request to merge branch into master
   * @param token
   */
  public static void createPullRequest(String token, String branchName){
    try {
      RepositoryService service = new RepositoryService();
      service.getClient().setOAuth2Token(token);
      Repository r = service.getRepository(repoOwner, repoName);
      PullRequest pr = new PullRequest();
      pr.setBase(new PullRequestMarker().setLabel(mainBranch));
      pr.setHead(new PullRequestMarker().setLabel(branchName));
      pr.setTitle("OEC Auto Merge Tool Pull Request");
      pr.setBody("Automatic merge of new data found in Exoplanet EU and NASA Archive catalogues to" +
              " OEC");
      PullRequestService prs = new PullRequestService();
      prs.getClient().setOAuth2Token(token);
      prs.createPullRequest(r, pr);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  
}
