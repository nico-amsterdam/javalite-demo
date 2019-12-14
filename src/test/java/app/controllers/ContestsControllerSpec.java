/*
 *  
 */

package app.controllers;

import org.javalite.activeweb.DBControllerSpec;
import app.models.Contest;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 */ 
public class ContestsControllerSpec extends DBControllerSpec {

    Object id;
    String contest_name = "Best artist";

    @Before
    public void before() {
        Contest.deleteAll();
        Contest b = (Contest)Contest.createIt("name", contest_name, "number_of_prizes", "1", "prize1", "free tickets");
        id = b.getId();
        Contest.createIt("name", "Fastest runner", "number_of_prizes", "2", "prize1", "cup", "prize2", "shoes");
        Contest.createIt("name", "Strongest man", "number_of_prizes", "3", "prize1", "free food here", "prize2", "50 bucks", "prize3", "25 bucks");

    }

    @Test
    public void shouldListAllContests() {
        request().get("index"); //<< this is where we execute the controller
        List Contests = (List) assigns().get("contests");
        a(Contests.size()).shouldBeEqual(3);
    }

    @Test
    public void shouldFindOneContestById() {
        request().param("id", id).get("show"); //<< this is where we execute the controller and pass a parameter
        Contest contest = (Contest) assigns().get("contest");
        a(contest.get("name")).shouldBeEqual(contest_name);
    }

    @Test
    public void shouldCreateNewContest() {
        //create a fourth contest
        request().param("name", "Biggest mouse").param("number_of_prizes", "0").param("prize1", "").post("create");
        //get list of contests
        request().get("index");
        List contests = (List) assigns().get("contests");
        a(contests.size()).shouldBeEqual(4);
    }

    @Test
    public void shouldDeleteContestById() {
        Contest b = (Contest) Contest.findAll().get(0);

        request().param("id",  b.getId()).delete("delete");
        
        a(redirected()).shouldBeTrue();
        a(Contest.count()).shouldBeEqual(2);
        a(flash("message")).shouldNotBeNull();
    }

    @Test
    public void shouldShowContestByIdAndVerifyGeneratedHTML() {
        Contest b = (Contest) Contest.findAll().get(0);

        request().param("id",  b.getId()).get("show");

        Contest contest = (Contest) assigns().get("contest");
        a(contest.get("name")).shouldBeEqual(b.get("name"));
        String html = responseContent();
        a(html.contains(b.getString("name"))).shouldBeTrue();
    }


}
