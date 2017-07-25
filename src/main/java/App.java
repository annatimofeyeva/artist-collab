import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("users", User.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/users/add-new-user", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/add-new-user.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/users/:userId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String email = request.queryParams("email");
      String skills = request.queryParams("skills");
      String location = request.queryParams("location");
      String password = request.queryParams("password");
      User user = new User(name, password, skills, location, email);
      user.save();
      model.put("user", user);
      model.put("template", "templates/user-details.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/users/:userId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("projects", user.getProjects());
      model.put("template", "templates/user-details.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/users/:userId/add-new-project", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      User user = User.find(Integer.parseInt(request.params(":userId")));
      model.put("user", user);
      model.put("template", "templates/add-new-project.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/projects/:projectId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      User host = User.find(Integer.parseInt(request.queryParams("host_id")));
      String name = request.queryParams("name");
      int hostId = Integer.parseInt(request.queryParams("host_id"));
      String description = request.queryParams("description");
      String location = request.queryParams("location");
      Project project = new Project(name, hostId, description, location);
      project.save();
      Project savedProject = Project.find(project.getId());
      savedProject.addMember(host);
      model.put("project", savedProject);
      model.put("host", host);
      model.put("template", "templates/project-details.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/projects/:projectId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Project project = Project.find(Integer.parseInt(request.params(":projectId")));
      model.put("project", project);
      model.put("template", "templates/project-details.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


  }

}
