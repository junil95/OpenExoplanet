package UpdateTools;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by dhrumil on 26/11/16.
 */
public class CreateOecClone {
  
  public static final String oecClonePath = "Data/oec/open_exoplanet_catalogue-master";
  public static final String gitCloneLink = "https://github.com/DhrumilSoft/open_exoplanet_catalogue.git";
  private static final String branchNameFixed = "OEC-Auto-Merge-Tool";
  private static String branchName = "master";
  public static final String startPoint = "origin/master";
  
  
  /**
   * This will clone the repo in the specified oecClonePath. It will remove previous copies if
   * they exist
   */
  public static void gitCloneRepo() {
    try {
      File f = new File(oecClonePath);
      //delete the directory if it already exists, This will delete recursively
      if (f.exists()) {
        FileUtils.deleteDirectory(f);
      }
      //clone the repository
      Git git = Git.cloneRepository()
              .setURI(gitCloneLink).setDirectory(f).call();
    } catch (GitAPIException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Create a new branch and push it to remote
   */
  public static void createNewBranch() {
    CreateBranchCommand bcc = null;
    CheckoutCommand checkout = null;
    try{
      File f = new File(oecClonePath);
      Repository repo = new FileRepositoryBuilder().readEnvironment().findGitDir(f).build();
      Git git = new Git(repo);
      bcc = git.branchCreate();
      checkout = git.checkout();
    } catch (IOException e){
      e.printStackTrace();
    }
    
    //Generate a unique id for the new branch. Shortened the id, don't require it to be that long
    String uuid = UUID.randomUUID().toString().substring(0, 8);
    branchName = branchNameFixed + uuid;
    try {
      bcc.setName(branchName)
              .setStartPoint(startPoint)
              .setForce(false)
              .call();
    
      checkout.setName(branchName);
      checkout.call();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Add newly generated files and commit the merged changes
   */
  public static void commitChanges() {
    //commit the data
    try {
      File f = new File(oecClonePath);
      Repository repo = new FileRepositoryBuilder().readEnvironment().findGitDir(f).build();
      Git git = new Git(repo);
      git.add().addFilepattern(".").call();
      git.commit()
              .setMessage("Changes created by OEC Auto Merge Tool")
              .call();
    } catch (GitAPIException e){
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Push changes to remote. Remember to only do this after creating a new branch and commiting.
   * Otherwise this will fail because it will try to push the remote branch which won't
   * exist at that point
   *
   * @param token
   * @param name
   */
  public static void pushChanges(String token, String name) {
    try {
      File f = new File(oecClonePath);
      Repository repo = new FileRepositoryBuilder().readEnvironment().findGitDir(f).build();
      
      Git git = new Git(repo);
      PushCommand pushCommand = git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, "")).setRemote("origin");
      pushCommand.setRemote("origin");
      pushCommand.setRefSpecs(new RefSpec(name));
      pushCommand.call();
    } catch (InvalidRemoteException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TransportException e) {
      e.printStackTrace();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Get branch name
   * @return
   */
  public static String getBranchName() {
    return branchName;
  }
  
  public static void main(String[] args) {
    //gitCloneRepo();
    //createNewBranch();
    //commitChanges();
    //pushChanges("a0e0b081561d3abaeae3bd2536b929d2c2c607d2");
    SendPullRequest.createPullRequest("a0e0b081561d3abaeae3bd2536b929d2c2c607d2", "OEC-Auto-Merge-Tool13a99be7");
//    CreateBranchCommand bcc = null;
//    CheckoutCommand checkout = null;
//    Git git = null;
//    String branch = "OEC-Auto-Merge-Tool";
//    File file = new File("Data/Clone");
//
//    try
//    {
//      Repository repo = new FileRepositoryBuilder().readEnvironment().findGitDir(file).build();
//      git = new Git(repo);
//      bcc = git.branchCreate();
//      checkout = git.checkout();
//    } catch (IOException e){
//      e.printStackTrace();
//    }
//
//    try {
//      bcc.setName(branch)
//              .setStartPoint("origin/master")
//              .setForce(false)
//              .call();
//
//      checkout.setName(branch);
//      checkout.call();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//
//    //commit the data
//    try
//
//    {
//      git.add().addFilepattern("*").call();
//      git.commit()
//              .setMessage("Changes created by OEC Auto Merge Tool")
//              .call();
//    } catch (
//            GitAPIException e)
//
//    {
//      e.printStackTrace();
//    }
//
//
//    //For pushing the data
//    try
//
//    {
//      PushCommand pushCommand = git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider("a0e0b081561d3abaeae3bd2536b929d2c2c607d2", "")).setRemote("origin");
//      pushCommand.setRemote("origin");
//      pushCommand.setRefSpecs(new RefSpec("OEC-Auto-Merge-Tool"));
//      pushCommand.call();
//    } catch (
//            GitAPIException e)
//
//    {
//      e.printStackTrace();
//    }
  }
}